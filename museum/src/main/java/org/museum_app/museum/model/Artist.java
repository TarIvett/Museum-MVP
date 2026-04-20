package org.museum_app.museum.model;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArtist")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "birthPlace")
    private String birthPlace;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "photo")
    private String photo;

    public Artist() {}

    public Artist(String name, Date birthDate, String birthPlace, String nationality, String photo) {
        this.name = name;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.nationality = nationality;
        this.photo = photo;
    }

    public Artist(Integer id, String name, Date birthDate, String birthPlace, String nationality, String photo) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.nationality = nationality;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", birthPlace='" + birthPlace + '\'' +
                ", nationality='" + nationality + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}

