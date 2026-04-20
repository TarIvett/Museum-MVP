package org.museum_app.museum.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.museum_app.museum.presenter.ArtistPresenter;

import java.net.URL;
import java.util.ResourceBundle;

public class ArtistFormGUI extends MuseumGUI implements Initializable {
    @FXML public TextField tfNationality;
    @FXML public TextField tfBirthPlace;
    @FXML public TextField tfBirthDate;
    @FXML public TextField tfName;
    @FXML public TextField tfPhoto;
    @FXML public ImageView imgPreview;
    @FXML public Label lblPageTitle;
    private String oldName;

    private ArtistPresenter artistPresenter;

    private Integer currentArtistId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artistPresenter = new ArtistPresenter(this);
    }

    public void loadArtistForEdit(int id) {
        this.currentArtistId = id;
        lblPageTitle.setText("Edit Artist");
        artistPresenter.prepareArtistForEdit(id);
    }

    @Override
    public void refreshImagePreview() {
        onPreviewImage();
    }

    public void onPreviewImage() {
        String url = tfPhoto.getText();
        if (url != null && !url.trim().isEmpty()) {
            Image img = safeLoadImage(url);
            if (img != null) {
                imgPreview.setImage(img);
            }
        }
    }

    @FXML
    private void onBrowseImage() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        java.io.File selectedFile = fileChooser.showOpenDialog(lblPageTitle.getScene().getWindow());

        if (selectedFile != null) {
            artistPresenter.copyImageFile(selectedFile);
        }
    }

    @FXML
    public void onSave() {
        artistPresenter.saveArtist(currentArtistId);
    }

    @FXML
    public void onCancel() {
        artistPresenter.onBackFromArtist();
    }


    public void navigateToArtistList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artist_list.fxml"));
            Parent root = loader.load();
            tfName.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public String getArtistName() { return tfName.getText(); }
    @Override public void setArtistName(String name) { this.tfName.setText(name); }

    @Override public String getOldArtistName() { return oldName; }
    @Override public void setOldArtistName(String name) { this.oldName = name; }

    @Override public String getArtistBirthDate() { return tfBirthDate.getText(); }
    @Override public void setArtistBirthDate(String date) { this.tfBirthDate.setText(date); }

    @Override public String getArtistBirthPlace() { return tfBirthPlace.getText(); }
    @Override public void setArtistBirthPlace(String place) { this.tfBirthPlace.setText(place); }

    @Override public String getArtistNationality() { return tfNationality.getText(); }
    @Override public void setArtistNationality(String nationality) { this.tfNationality.setText(nationality); }

    @Override public String getArtistPhoto() { return tfPhoto.getText(); }
    @Override public void setArtistPhoto(String photo) { this.tfPhoto.setText(photo); }

    @Override
    public void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

}