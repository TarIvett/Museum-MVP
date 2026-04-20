package org.museum_app.museum.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.museum_app.museum.presenter.ArtistPresenter;
import org.museum_app.museum.presenter.ArtworkPresenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeGUI extends MuseumGUI implements Initializable {
    private ArtistPresenter artistPresenter;
    private ArtworkPresenter artworkPresenter;

    @FXML private HBox artworkContainer;
    @FXML private HBox artistContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artistPresenter  = new ArtistPresenter(this);
        artworkPresenter = new ArtworkPresenter(this);

        javafx.application.Platform.runLater(() -> {
            artworkPresenter.loadAllArtworks();
            artistPresenter.loadAllArtists();
        });
    }

    @FXML
    public void onSeeAllArtworks() {
        artworkPresenter.onSeeAllArtworksClicked();
    }

    @FXML
    public void onSeeAllArtists() {
        artistPresenter.onSeeAllArtistsClicked();
    }

    @Override
    public void setArtworks(List<String[]> artworksList) {
        if (artworkContainer == null) return;
        artworkContainer.getChildren().clear();

        int limit = Math.min(artworksList.size(), 6);

        for (int i = 0; i < limit; i++) {
            String[] row = artworksList.get(i);
            try {
                URL fxmlLocation = getClass().getResource("/org/museum_app/museum/fxml/artworkHomeCard.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent card = loader.load();

                Label title = (Label) card.lookup("#titleLabel");
                Label artist = (Label) card.lookup("#artistLabel");
                ImageView img = (ImageView) card.lookup("#artworkImage");

                if (title != null) title.setText(row[ARTWORK_TITLE]);
                if (artist != null) artist.setText(row[ARTWORK_ARTIST_NAME]);

                Image image = safeLoadImage(row[ARTWORK_PHOTO1]);
                if (image != null && img != null) {
                    img.setImage(image);
                }

                card.setCursor(javafx.scene.Cursor.HAND);
                card.setOnMouseClicked(e -> {
                    int clickedId = Integer.parseInt(row[ARTWORK_ID]);
                    artworkPresenter.onArtworkDoubleClicked(clickedId);
                });

                artworkContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setArtists(List<String[]> artistsList) {
        if (artistContainer == null) return;
        artistContainer.getChildren().clear();

        int limit = Math.min(artistsList.size(), 4);

        for (int i = 0; i < limit; i++) {
            String[] row = artistsList.get(i);
            try {
                URL fxmlLocation = getClass().getResource("/org/museum_app/museum/fxml/artistListCard.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent card = loader.load();

                Label name = (Label) card.lookup("#nameLabel");
                ImageView img = (ImageView) card.lookup("#artistImage");

                if (name != null) name.setText(row[ARTIST_NAME]);

                Image image = safeLoadImage(row[ARTIST_PHOTO]);
                if (image != null && img != null) {
                    img.setImage(image);
                }

                card.setCursor(javafx.scene.Cursor.HAND);
                card.setOnMouseClicked(e -> {
                    int clickedId = Integer.parseInt(row[ARTIST_ID]);
                    artistPresenter.onArtistDoubleClicked(clickedId);
                });

                artistContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void navigateToArtistList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artist_list.fxml"));
            Parent root = loader.load();
            artistContainer.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void navigateToArtistDetail(int id) {
        try {
            FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artist.fxml"));
            Parent root = detailLoader.load();
            ArtistGUI detailController = detailLoader.getController();
            detailController.receiveIdAndLoad(id);
            artistContainer.getScene().setRoot(root);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @Override
    public void navigateToArtworkDetail(int id) {
        try {
            FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artwork.fxml"));
            Parent root = detailLoader.load();
            ArtworkGUI detailController = detailLoader.getController();
            detailController.receiveIdAndLoad(id);
            artworkContainer.getScene().setRoot(root);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @Override public void navigateToArtistForm(Integer id) {}
    @Override public void navigateToArtworkForm(Integer id) {}
}