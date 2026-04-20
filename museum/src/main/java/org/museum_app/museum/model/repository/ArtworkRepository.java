package org.museum_app.museum.model.repository;

import org.museum_app.museum.model.Artist;
import org.museum_app.museum.model.Artwork;
import org.museum_app.museum.model.ArtworkGenreList;
import jakarta.persistence.EntityManager;

import java.util.List;

//CRUD operations
//CREATE, READ, UPDATE, DELETE

public class ArtworkRepository {
    //CREATE
    public Artwork save(Artwork artwork) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            Artwork result = em.merge(artwork);
            em.getTransaction().commit();
            return result;
        } finally { em.close(); }
    }

    //READ
    public List<Artwork> findAll() {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Artwork a", Artwork.class)
                    .getResultList();
        } finally { em.close(); }
    }

    //UPDATE
    public Artwork update(String oldTitle, int artistId, Artwork updatedArtwork) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            List<Artwork> found = em.createQuery(
                            "SELECT a FROM Artwork a WHERE a.title = :title AND a.artist.id = :artistId",
                            Artwork.class)
                    .setParameter("title", oldTitle)
                    .setParameter("artistId", artistId)
                    .getResultList();
            if (found.isEmpty()) return null;

            Artwork existing = found.getFirst();
            existing.setTitle(updatedArtwork.getTitle());
            existing.setDescription(updatedArtwork.getDescription());
            existing.setArtist(updatedArtwork.getArtist());
            existing.setCreationYear(updatedArtwork.getCreationYear());
            existing.setGenre(updatedArtwork.getGenre());
            existing.setPhoto1(updatedArtwork.getPhoto1());
            existing.setPhoto2(updatedArtwork.getPhoto2());
            existing.setPhoto3(updatedArtwork.getPhoto3());

            Artwork result = em.merge(existing);
            em.getTransaction().commit();
            return result;
        } finally { em.close(); }
    }

    //DELETE
    public void delete(Artwork artwork) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            em.getTransaction().begin();
            Artwork managed = em.merge(artwork);
            em.remove(managed);
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    //FIND BY ID
    public Artwork findById(int id) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.find(Artwork.class, id);
        } finally { em.close(); }
    }

    //FIND BY TITLE
    public List<Artwork> findByTitle(String title) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artwork a WHERE a.title = :title",
                            Artwork.class)
                    .setParameter("title", title)
                    .getResultList();
        } finally { em.close(); }
    }

    //SEARCH BASED ON TITLE OR PARTIAL TITLE
    public List<Artwork> searchByTitle(String title) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artwork a WHERE LOWER(a.title) LIKE LOWER(:title)", Artwork.class)
                    .setParameter("title", "%" + title + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    //FIND ALL ARTWORKS FOR ONE ARTIST ID
    public List<Artwork> findByArtistId(int artistId) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artwork a WHERE a.artist.id = :artistId",
                            Artwork.class)
                    .setParameter("artistId", artistId)
                    .getResultList();
        } finally { em.close(); }
    }

    //FIND ALL ARTWORKS FOR A SPECIFIC GENRE
    public List<Artwork> findByGenre(ArtworkGenreList genre) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artwork a WHERE a.genre = :genre",
                            Artwork.class)
                    .setParameter("genre", genre)
                    .getResultList();
        } finally { em.close(); }
    }

    //FIND ALL ARTWORKS BY AN ARTIST AND WITH A SPECIFIC GENRE
    public List<Artwork> findByArtistIdAndGenre(int artistId, ArtworkGenreList genre) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artwork a WHERE a.artist.id = :artistId AND a.genre = :genre",
                            Artwork.class)
                    .setParameter("artistId", artistId)
                    .setParameter("genre", genre)
                    .getResultList();
        } finally { em.close(); }
    }

    //FIND ALL ARTWORKS BY AN ARTIST AND A SPECIFIC TITLE
    public List<Artwork> findByTitleAndArtistId(String title, int artistId) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artwork a WHERE a.title = :title AND a.artist.id = :artistId",
                            Artwork.class)
                    .setParameter("title", title)
                    .setParameter("artistId", artistId)
                    .getResultList();
        } finally { em.close(); }
    }

    //FILTER OUT BASED ON SEARCH INPUT, SPECIFIED GENRE AND ARTIST
    public List<Artwork> searchAndFilter(String title, String artistName, String genreStr) {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder("SELECT a FROM Artwork a WHERE 1=1");

            if (title != null && !title.trim().isEmpty()) {
                queryStr.append(" AND LOWER(a.title) LIKE LOWER(:title)");
            }
            if (artistName != null && !artistName.equals("All Artists")) {
                queryStr.append(" AND a.artist.name = :artistName");
            }
            if (genreStr != null && !genreStr.equals("All Genres")) {
                queryStr.append(" AND a.genre = :genre");
            }

            var query = em.createQuery(queryStr.toString(), Artwork.class);

            if (title != null && !title.trim().isEmpty()) {
                query.setParameter("title", "%" + title + "%");
            }
            if (artistName != null && !artistName.equals("All Artists")) {
                query.setParameter("artistName", artistName);
            }
            if (genreStr != null && !genreStr.equals("All Genres")) {
                query.setParameter("genre", ArtworkGenreList.valueOf(genreStr.toUpperCase()));
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //SORT ALL ARTWORK BASED ON ARTIST
    public List<Artwork> findAllSortedByArtist() {
        EntityManager em = DatabaseManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Artwork a ORDER BY a.artist.name ASC",
                            Artwork.class)
                    .getResultList();
        } finally { em.close(); }
    }

}