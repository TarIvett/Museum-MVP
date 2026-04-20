package org.museum_app.museum.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.museum_app.museum.presenter.ArtworkPresenter;

import java.net.URL;
import java.util.ResourceBundle;

public class ArtworkFormGUI extends MuseumGUI implements Initializable {

    @FXML private Label lblPageTitle;
    @FXML private TextField tfTitle;
    @FXML private TextField tfPhoto1;
    @FXML private TextField tfPhoto2;
    @FXML private TextField tfPhoto3;
    @FXML private ComboBox<String> cbArtist;
    @FXML private ComboBox<String> cbGenre;
    @FXML public TextField tfYear;
    @FXML public TextArea taDescription;
    @FXML private ImageView img1Preview;
    @FXML private ImageView img2Preview;
    @FXML private ImageView img3Preview;

    private String oldTitle;
    private ArtworkPresenter artworkPresenter;
    private Integer currentArtworkId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artworkPresenter = new ArtworkPresenter(this);
        cbArtist.getItems().addAll(artworkPresenter.getAllArtistNames());

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
    }

    public void loadArtworkForEdit(int id) {
        this.currentArtworkId = id;
        lblPageTitle.setText("Edit Artwork");
        artworkPresenter.prepareArtworkForEdit(id);
    }

    @Override
    public void refreshImagePreview() {
        onPreviewImage();
    }

    @FXML
    public void onPreviewImage() {
        String url1 = tfPhoto1.getText();
        String url2 = tfPhoto2.getText();
        String url3 = tfPhoto3.getText();

        if (url1 != null && !url1.trim().isEmpty()) {
            Image img1 = safeLoadImage(url1);
            if (img1 != null) img1Preview.setImage(img1);
        }

        if (url2 != null && !url2.trim().isEmpty()) {
            Image img2 = safeLoadImage(url2);
            if (img2 != null) img2Preview.setImage(img2);
        }

        if (url3 != null && !url3.trim().isEmpty()) {
            Image img3 = safeLoadImage(url3);
            if (img3 != null) img3Preview.setImage(img3);
        }
    }

    @FXML
    private void onBrowseImage(int photoIndexId) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        java.io.File selectedFile = fileChooser.showOpenDialog(lblPageTitle.getScene().getWindow());

        if (selectedFile != null) {
            artworkPresenter.copyImageFile(selectedFile, photoIndexId);
        }
    }

    @FXML
    private void onBrowseImage1() { onBrowseImage(1); }

    @FXML
    private void onBrowseImage2() { onBrowseImage(2); }

    @FXML
    private void onBrowseImage3() { onBrowseImage(3); }

    @FXML
    private void onSave() {
        artworkPresenter.saveArtwork(currentArtworkId);
    }

    @FXML
    private void onCancel() {
        artworkPresenter.onBackFromArtwork();
    }

    @Override
    public void navigateToArtworkList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artwork_list.fxml"));
            Parent root = loader.load();
            tfTitle.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public String getArtworkTitle() { return tfTitle.getText(); }
    @Override public void setArtworkTitle(String title) { this.tfTitle.setText(title); }

    @Override public String getOldArtworkTitle() { return oldTitle; }
    @Override public void setOldArtworkTitle(String title) { this.oldTitle = title; }

    @Override public String getArtworkArtistName() { return cbArtist.getValue(); }
    @Override public void setArtworkArtistName(String name) { this.cbArtist.setValue(name); }

    @Override public String getArtworkDescription() { return taDescription.getText(); }
    @Override public void setArtworkDescription(String desc) { this.taDescription.setText(desc); }

    @Override public String getArtworkCreationYear() { return tfYear.getText(); }

    @Override public void setArtworkCreationYear(String yearStr) {
        if (yearStr != null && yearStr.length() >= 4) {
            this.tfYear.setText(yearStr.substring(0, 4));
        } else if (yearStr != null) {
            this.tfYear.setText(yearStr);
        } else {
            this.tfYear.setText("");
        }
    }

    @Override public String getArtworkGenre() { return cbGenre.getValue(); }
    @Override public void setArtworkGenre(String genre) { this.cbGenre.setValue(genre); }

    @Override public String getArtworkPhoto1() { return tfPhoto1.getText(); }
    @Override public void setArtworkPhoto1(String photo) { this.tfPhoto1.setText(photo); }

    @Override public String getArtworkPhoto2() { return tfPhoto2.getText(); }
    @Override public void setArtworkPhoto2(String photo) { this.tfPhoto2.setText(photo); }

    @Override public String getArtworkPhoto3() { return tfPhoto3.getText(); }
    @Override public void setArtworkPhoto3(String photo) { this.tfPhoto3.setText(photo); }

    @Override
    public void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }
}