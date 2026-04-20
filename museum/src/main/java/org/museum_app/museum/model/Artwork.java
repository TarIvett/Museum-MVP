package org.museum_app.museum.model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "artwork")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArtwork")
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "idArtist")
    private Artist artist;

    @Column(name = "description")
    private String description;

    @Column(name = "creationYear")
    private Date creationYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private ArtworkGenreList genre;

    @Column(name = "photo1")
    private String photo1;

    @Column(name = "photo2")
    private String photo2;

    @Column(name = "photo3")
    private String photo3;

    public Artwork() {}

    public Artwork(String title, Artist artist, String description, Date creationYear, ArtworkGenreList genre, String photo1, String photo2, String photo3) {
        this.title = title;
        this.artist = artist;
        this.description = description;
        this.creationYear = creationYear;
        this.genre = genre;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
    }

    public Artwork(Integer id, String title, Artist artist, String description, Date creationYear, ArtworkGenreList genre, String photo1, String photo2, String photo3) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.description = description;
        this.creationYear = creationYear;
        this.genre = genre;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(Date creationYear) {
        this.creationYear = creationYear;
    }

    public ArtworkGenreList getGenre() {
        return genre;
    }

    public void setGenre(ArtworkGenreList genre) {
        this.genre = genre;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    @Override
    public String toString() {
        return "Artwork{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist=" + artist +
                ", description='" + description + '\'' +
                ", creationYear=" + creationYear +
                ", genre=" + genre +
                ", photo1='" + photo1 + '\'' +
                ", photo2='" + photo2 + '\'' +
                ", photo3='" + photo3 + '\'' +
                '}';
    }
}
