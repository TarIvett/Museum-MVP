package org.museum_app.museum.view;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.museum_app.museum.presenter.ArtworkPresenter;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArtworksAllGUI extends MuseumGUI implements Initializable {
    private ArtworkPresenter artworkPresenter;

    @FXML private VBox vboxArtworkList;
    @FXML private TextField tfSearch;
    @FXML private ComboBox<String> cbArtist;
    @FXML private ComboBox<String> cbGenre;

    private Integer selectedArtworkId = null;
    private javafx.scene.Node selectedCardNode = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artworkPresenter = new ArtworkPresenter(this);

        cbArtist.getItems().add("All Artists");
        cbArtist.getItems().addAll(artworkPresenter.getAllArtistNames());
        cbArtist.setValue("All Artists");

        cbGenre.getItems().addAll("All Genres",
                "HISTORICAL",
                "PORTRAITURE",
                "LANDSCAPE",
                "PHOTOGRAPHY",
                "STILL_LIFE",
                "ABSTRACT",
                "WILDLIFE",
                "SCULPTURE",
                "SELF_PAINTING",
                "OTHER");
        cbGenre.setValue("All Genres");

        javafx.application.Platform.runLater(() -> {
            artworkPresenter.loadAllArtworksOrd();
        });

        javafx.beans.value.ChangeListener<String> filterListener = (obs, oldVal, newVal) -> triggerFilters();

        if (tfSearch != null) tfSearch.textProperty().addListener(filterListener);
        if (cbArtist != null) cbArtist.valueProperty().addListener(filterListener);
        if (cbGenre != null) cbGenre.valueProperty().addListener(filterListener);
    }

    private void triggerFilters() {
        String searchTitle = tfSearch.getText();
        String selectedArtist = cbArtist.getValue();
        String selectedGenre = cbGenre.getValue();

        artworkPresenter.applyFilters(searchTitle, selectedArtist, selectedGenre);
    }

    @Override
    public void setArtworks(List<String[]> artworks) {
        vboxArtworkList.getChildren().clear();

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

                Image image = safeLoadImage(row[ARTWORK_PHOTO1]);
                if (image != null) {
                    img.setImage(image);
                }

                card.setCursor(javafx.scene.Cursor.HAND);
                card.setOnMouseClicked(e -> {
                    // DOUBLE CLICK: Strict MVP - View just tells Presenter it happened
                    if (e.getClickCount() == 2) {
                        int clickedId = Integer.parseInt(row[ARTWORK_ID]);
                        artworkPresenter.onArtworkDoubleClicked(clickedId);
                    }
                    // SINGLE CLICK: View manages its own visual highlight state
                    else if (e.getClickCount() == 1) {
                        if (selectedCardNode != null) {
                            selectedCardNode.setStyle("-fx-background-color: transparent;");
                        }
                        selectedArtworkId = Integer.parseInt(row[ARTWORK_ID]);
                        selectedCardNode = card;
                        selectedCardNode.setStyle("-fx-background-color: #af945e;");
                    }
                });
                vboxArtworkList.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAdd() {
        artworkPresenter.onAddArtworkClicked();
    }

    @FXML
    private void onEdit() {
        artworkPresenter.onEditArtworkClicked(selectedArtworkId);
    }

    @FXML
    private void onDelete() {
        if (selectedArtworkId == null) {
            showMessage("Please click an artwork first to select it for deletion.");
            return;
        }

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete this artwork?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                artworkPresenter.deleteArtwork(selectedArtworkId);
                selectedArtworkId = null;
                selectedCardNode = null;
            }
        });
    }

    @FXML
    private void onBack() {
        artworkPresenter.onBackFromArtworkList();
    }

    public void navigateToArtworkDetail(int id) {
        try {
            FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artwork.fxml"));
            Parent root = detailLoader.load();
            ArtworkGUI detailController = detailLoader.getController();
            detailController.receiveIdAndLoad(id);
            vboxArtworkList.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Could not open artwork details.");
        }
    }

    public void navigateToArtworkForm(Integer id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artwork_form.fxml"));
            Parent root = loader.load();

            if (id != null) {
                ArtworkFormGUI formController = loader.getController();
                formController.loadArtworkForEdit(id);
            }

            vboxArtworkList.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Could not open Add/Edit form.");
        }
    }

    @Override
    public void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }
}