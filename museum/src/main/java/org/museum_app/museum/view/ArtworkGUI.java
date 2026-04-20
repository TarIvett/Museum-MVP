package org.museum_app.museum.view;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.museum_app.museum.presenter.ArtworkPresenter;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class ArtworkGUI extends MuseumGUI implements Initializable {
    private ArtworkPresenter artworkPresenter;

    @FXML public ImageView imgArtwork;
    @FXML public Label lblTitle;
    @FXML public ImageView imgArtist;
    @FXML public Label lblGenre;
    @FXML public Label lblCreationYear;
    @FXML public Label lblArtistName;
    @FXML public Label lblDescription;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artworkPresenter = new ArtworkPresenter(this);
    }

    public void receiveIdAndLoad(int id) {
        artworkPresenter.loadSingleArtwork(id);
    }


    @Override
    public void setArtworkTitle(String title) {
        if (lblTitle != null) lblTitle.setText(title);
    }

    @Override
    public void setArtworkGenre(String genre) {
        if (lblGenre != null) lblGenre.setText(genre);
    }

    @Override
    public void setArtworkCreationYear(String year) {
        if (lblCreationYear != null) lblCreationYear.setText(year);
    }

    @Override
    public void setArtworkArtistName(String name) {
        if (lblArtistName != null) lblArtistName.setText(name);
    }

    @Override
    public void setArtworkDescription(String desc) {
        if (lblDescription != null) lblDescription.setText(desc);
    }

    @Override
    public void setArtworkPhoto1(String photo) {
        Image image = safeLoadImage(photo);
        if (image != null && imgArtwork != null) {
            imgArtwork.setImage(image);
        }
    }

    public void setArtworkArtistPhotoDisplay(String photo) {
        Image image = safeLoadImage(photo);
        if (image != null && imgArtist != null) {
            imgArtist.setImage(image);
        }
    }

    @FXML
    private void onBack() {
        artworkPresenter.onBackFromArtwork();
    }
}