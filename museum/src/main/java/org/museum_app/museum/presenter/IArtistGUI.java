package org.museum_app.museum.presenter;

import java.util.List;

public interface IArtistGUI extends IMuseumGUI {
    //getter + setter for the artist ID
    public int getArtistId();
    public void setArtistId(int artistId);

    //For searching artist based on name
    //getter + setter for the artist name
    public String getArtistName();
    public void setArtistName(String artistName);

    //getter + setter for the artist birthdate
    public String getArtistBirthDate();
    public void setArtistBirthDate(String artistBirthDate);

    //getter + setter for the artist birthplace
    public String getArtistBirthPlace();
    public void setArtistBirthPlace(String artistBirthPlace);

    //getter + setter for the artist nationality
    public String getArtistNationality();
    public void setArtistNationality(String artistNationality);

    //getter + setter for the artist photo
    public String getArtistPhoto();
    public void setArtistPhoto(String artistPhoto);

    //others
    //getter for the old name of the artist to be updated
    public String getOldArtistName();
    public void setOldArtistName(String oldArtistName);

    void navigateToArtistDetail(int id);
    void navigateToArtistForm(Integer id);

    void setArtists(List<String[]> artists);
    void setArtworks(List<String[]> artworkRows);

    void refreshImagePreview();

    void navigateToArtistList();

    void navigateToArtworkDetail(int artworkId);

}
