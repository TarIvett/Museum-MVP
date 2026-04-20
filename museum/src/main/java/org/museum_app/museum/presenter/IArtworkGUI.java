package org.museum_app.museum.presenter;

import java.util.List;

public interface IArtworkGUI extends IMuseumGUI{
    //getter + setter for the artwork ID
    public int getArtworkId();
    public void setArtworkId(int artworkId);

    //getter + setter for the artwork title
    public String getArtworkTitle();
    public void setArtworkTitle(String artworkTitle);

    //getter + setter for the artwork artist
    public String getArtworkArtistName();
    public void setArtworkArtistName(String artworkArtistName);

    //getter + setter for the artwork description
    public String getArtworkDescription();
    public void setArtworkDescription(String artworkDescription);

    //getter + setter for the artwork creation year
    public String getArtworkCreationYear();
    public void setArtworkCreationYear(String artworkCreationYear);

    //getter + setter for the artwork genre
    public String getArtworkGenre();
    public void setArtworkGenre(String artworkGenre);

    //getter + setter for the artwork photo1
    public String getArtworkPhoto1();
    public void setArtworkPhoto1(String artworkPhoto1);

    //getter + setter for the artwork photo2
    public String getArtworkPhoto2();
    public void setArtworkPhoto2(String artworkPhoto2);

    //getter + setter for the artwork photo3
    public String getArtworkPhoto3();
    public void setArtworkPhoto3(String artworkPhoto3);

    //others
    //getter for the old title of the artwork to be updated
    public String getOldArtworkTitle();
    public void setOldArtworkTitle(String oldArtworkTitle);

    public void setArtworks(List<String[]> artworks);

    void refreshImagePreview();

    void navigateToArtworkList();

    void navigateToArtworkDetail(int id);

    void navigateToArtworkForm(Integer id);

    void setArtworkArtistPhotoDisplay(String photo);
}
