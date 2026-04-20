package org.museum_app.museum.model.repository;

import org.museum_app.museum.model.Artist;
import jakarta.persistence.EntityManager;

import java.util.List;

//CRUD operations
//CREATE, READ, UPDATE, DELETE

public class ArtistRepository {
    //CREATE
    public Artist save(Artist artist) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            Artist result = em.merge(artist);
            em.getTransaction().commit();
            return result;
        } finally { em.close(); }
    }

    //READ
    public List<Artist> findAll() {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Artist a", Artist.class)
                    .getResultList();
        } finally { em.close(); }
    }

    //UPDATE
    public Artist update(String oldName, Artist updatedArtist) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            List<Artist> found = em.createQuery(
                            "SELECT a FROM Artist a WHERE a.name = :name", Artist.class)
                    .setParameter("name", oldName)
                    .getResultList();
            if (found.isEmpty()) return null;

            Artist existing = found.getFirst();
            existing.setName(updatedArtist.getName());
            existing.setBirthDate(updatedArtist.getBirthDate());
            existing.setBirthPlace(updatedArtist.getBirthPlace());
            existing.setNationality(updatedArtist.getNationality());
            existing.setPhoto(updatedArtist.getPhoto());

            Artist result = em.merge(existing);
            em.getTransaction().commit();
            return result;
        } finally { em.close(); }
    }

    //DELETE
    public void delete(Artist artist) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            Artist managed = em.merge(artist);
            em.remove(managed);
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    //FIND BASED ON ID
    public Artist findById(int id) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.find(Artist.class, id);
        } finally { em.close(); }
    }

    //FIND BASED ON NAME
    public List<Artist> findByName(String name) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artist a WHERE a.name = :name", Artist.class)
                    .setParameter("name", name)
                    .getResultList();
        } finally { em.close(); }
    }

    //SEARCH BASED ON NAME OR PARTIAL NAME
    public List<Artist> searchByName(String name) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artist a WHERE LOWER(a.name) LIKE LOWER(:name)", Artist.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }


}