package org.museum_app.museum.presenter;

public interface IMuseumGUI {
    // Artist column indexes
    int ARTIST_ID         = 0;
    int ARTIST_NAME       = 1;
    int ARTIST_BIRTHDATE  = 2;
    int ARTIST_BIRTHPLACE = 3;
    int ARTIST_NATIONALITY= 4;
    int ARTIST_PHOTO      = 5;

    // Artwork column indexes
    int ARTWORK_ID          = 0;
    int ARTWORK_TITLE       = 1;
    int ARTWORK_ARTIST_NAME = 2;
    int ARTWORK_DESCRIPTION = 3;
    int ARTWORK_YEAR        = 4;
    int ARTWORK_GENRE       = 5;
    int ARTWORK_PHOTO1      = 6;
    int ARTWORK_PHOTO2      = 7;
    int ARTWORK_PHOTO3      = 8;
    int ARTWORK_ARTIST_PHOTO = 9;

    public void navigateToHome();
    public void showMessage(String message);
}
