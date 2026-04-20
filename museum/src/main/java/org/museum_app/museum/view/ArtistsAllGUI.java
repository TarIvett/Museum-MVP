package org.museum_app.museum.view;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.museum_app.museum.presenter.ArtistPresenter;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArtistsAllGUI extends MuseumGUI implements Initializable {
    private ArtistPresenter artistPresenter;

    @FXML private FlowPane flowPaneArtists;
    @FXML private TextField tfSearch;

    private javafx.scene.Node selectedCardNode = null;
    private Integer selectedArtistId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artistPresenter = new ArtistPresenter(this);

        javafx.application.Platform.runLater(() -> {
            artistPresenter.loadAllArtists();
        });

        if (tfSearch != null) {
            tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                artistPresenter.searchArtistByName(newValue);
            });
        }
    }

    @Override
    public void setArtists(List<String[]> artists) {
        flowPaneArtists.getChildren().clear();

        for (String[] row : artists) {
            try {
                URL fxmlLocation = getClass().getResource("/org/museum_app/museum/fxml/artistListCard.fxml");
                if (fxmlLocation == null) {
                    System.err.println("Artist card FXML not found!");
                    continue;
                }

                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent cardNode = loader.load();

                Label nameLabel = (Label) cardNode.lookup("#nameLabel");
                ImageView artistImage = (ImageView) cardNode.lookup("#artistImage");

                if (nameLabel != null) {
                    nameLabel.setText(row[ARTIST_NAME]);
                }

                if (artistImage != null) {
                    Image image = safeLoadImage(row[ARTIST_PHOTO]);
                    if (image != null) {
                        artistImage.setImage(image);
                    }
                }

                cardNode.setCursor(javafx.scene.Cursor.HAND);
                cardNode.setOnMouseClicked(e -> {
                    // DOUBLE CLICK: View delegates strictly to Presenter
                    if (e.getClickCount() == 2) {
                        int clickedId = Integer.parseInt(row[ARTIST_ID]);
                        artistPresenter.onArtistDoubleClicked(clickedId);
                    }
                    // SINGLE CLICK: View handles its own visual state (highlighting)
                    else if (e.getClickCount() == 1) {
                        if (selectedCardNode != null) {
                            selectedCardNode.getStyleClass().remove("selected-card");
                        }
                        selectedArtistId = Integer.parseInt(row[ARTIST_ID]);
                        selectedCardNode = cardNode;
                        selectedCardNode.getStyleClass().add("selected-card");
                    }
                });

                flowPaneArtists.getChildren().add(cardNode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onAdd() {
        artistPresenter.onAddArtistClicked();
    }

    @FXML
    public void onEdit() {
        artistPresenter.onEditArtistClicked(selectedArtistId);
    }

    @FXML
    public void onDelete() {
        if (selectedArtistId == null) {
            showMessage("Please click an artist first to select it for deletion.");
            return;
        }

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete this artist?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                artistPresenter.deleteArtist(selectedArtistId);
                selectedArtistId = null;
                selectedCardNode = null;
            }
        });
    }

    @FXML
    private void onBack() {
        artistPresenter.onBackFromArtistList();
    }

    @Override
    public void navigateToArtistDetail(int id) {
        try {
            FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artist.fxml"));
            Parent root = detailLoader.load();
            ArtistGUI detailController = detailLoader.getController();
            detailController.receiveIdAndLoad(id);
            flowPaneArtists.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Could not open artist details.");
        }
    }

    @Override
    public void navigateToArtistForm(Integer id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artist_form.fxml"));
            Parent root = loader.load();

            if (id != null) {
                ArtistFormGUI formController = loader.getController();
                formController.loadArtistForEdit(id);
            }

            flowPaneArtists.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Could not open form.");
        }
    }

    @Override
    public void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }
}