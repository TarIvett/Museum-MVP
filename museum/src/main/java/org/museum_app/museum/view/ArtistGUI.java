package org.museum_app.museum.view;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.museum_app.museum.presenter.ArtistPresenter;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArtistGUI extends MuseumGUI implements Initializable {
    private ArtistPresenter artistPresenter;

    @FXML public ImageView imgArtist;
    @FXML public Label lblArtistName;
    @FXML public Label lblBirthDate;
    @FXML public Label lblBirthPlace;
    @FXML public Label lblNationality;
    @FXML public VBox vBoxArtworks;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artistPresenter = new ArtistPresenter(this);
    }

    public void receiveIdAndLoad(int id) {
        setArtistId(id);
        artistPresenter.loadSingleArtist(id);
    }

    @Override
    public void setArtistName(String name) {
        super.setArtistName(name);
        if (lblArtistName != null) lblArtistName.setText(name);
    }

    @Override
    public void setArtistBirthDate(String date) {
        if (lblBirthDate != null) lblBirthDate.setText(date);
    }

    @Override
    public void setArtistBirthPlace(String place) {
        super.setArtistBirthPlace(place);
        if (lblBirthPlace != null) lblBirthPlace.setText(place);
    }

    @Override
    public void setArtistNationality(String nationality) {
        super.setArtistNationality(nationality);
        if (lblNationality != null) lblNationality.setText(nationality);
    }

    @Override
    public void setArtistPhoto(String photo) {
        super.setArtistPhoto(photo);
        Image image = safeLoadImage(photo);
        if (image != null && imgArtist != null) {
            imgArtist.setImage(image);
        }
    }

    @Override
    public void setArtworks(List<String[]> artworks) {
        vBoxArtworks.getChildren().clear();

        for (String[] row : artworks) {
            try {
                URL fxmlLocation = getClass().getResource("/org/museum_app/museum/fxml/artworkListCard.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent card = loader.load();

                Label title = (Label) card.lookup("#titleLabel");
                Label artist = (Label) card.lookup("#artistLabel");
                ImageView img = (ImageView) card.lookup("#artworkImage");

                if (title != null) title.setText(row[ARTWORK_TITLE]);
                if (artist != null) artist.setText(row[ARTWORK_ARTIST_NAME]);

                Image image = safeLoadImage(row[ARTWORK_PHOTO1]); // I changed this to ARTWORK_PHOTO1, as ARTIST_PHOTO index was likely grabbing the wrong column!
                if (image != null && img != null) {
                    img.setImage(image);
                }

                card.setCursor(javafx.scene.Cursor.HAND);
                card.setOnMouseClicked(e -> {
                    int clickedArtworkId = Integer.parseInt(row[ARTWORK_ID]);
                    artistPresenter.onArtworkClicked(clickedArtworkId);
                });

                vBoxArtworks.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void navigateToArtworkDetail(int artworkId) {
        try {
            FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artwork.fxml"));
            Parent root = detailLoader.load();

            ArtworkGUI detailController = detailLoader.getController();
            detailController.receiveIdAndLoad(artworkId);

            vBoxArtworks.getScene().setRoot(root);

        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Could not open artwork details.");
        }
    }

    @FXML
    private void onBack() {
        artistPresenter.onBackFromArtist();
    }

    @Override
    public void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }
}