package org.museum_app.museum.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.museum_app.museum.presenter.IArtistGUI;
import org.museum_app.museum.presenter.IArtworkGUI;
import org.museum_app.museum.presenter.IMuseumGUI;

import java.io.File;
import java.net.URL;
import java.util.List;

public abstract class MuseumGUI implements IMuseumGUI, IArtistGUI, IArtworkGUI {

    protected static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    @Override
    public void showMessage(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    // IArtistGUI empty methods
    @Override public void setArtistId(int id){}
    @Override public int getArtistId(){return 0;}

    @Override public void setArtistName(String name) {}
    @Override public String getArtistName() { return null; }

    @Override public void setOldArtistName(String name) {}
    @Override public String getOldArtistName() { return null; }

    @Override public void setArtistBirthDate(String date) {}
    @Override public String getArtistBirthDate() { return null; }

    @Override public void setArtistBirthPlace(String place) {}
    @Override public String getArtistBirthPlace() { return null; }

    @Override public void setArtistNationality(String nationality) {}
    @Override public String getArtistNationality() { return null; }

    @Override public void setArtistPhoto(String photo) {}
    @Override public String getArtistPhoto() { return null; }

    @Override public void setArtists(List<String[]> artists) {}


    // IArtworkGUI empty methods
    @Override public void setArtworkId(int id){}
    @Override public int getArtworkId(){return 0;}

    @Override public void setArtworkTitle(String title) {}
    @Override public String getArtworkTitle() { return null; }

    @Override public void setOldArtworkTitle(String title) {}
    @Override public String getOldArtworkTitle() { return null; }

    @Override public void setArtworkArtistName(String name) {}
    @Override public String getArtworkArtistName() { return null; }

    @Override public void setArtworkDescription(String desc) {}
    @Override public String getArtworkDescription() { return null; }

    @Override public void setArtworkCreationYear(String year) {}
    @Override public String getArtworkCreationYear() { return null; }

    @Override public void setArtworkGenre(String genre) {}
    @Override public String getArtworkGenre() { return null; }

    @Override public void setArtworkPhoto1(String photo) {}
    @Override public String getArtworkPhoto1() { return null; }

    @Override public void setArtworkPhoto2(String photo) {}
    @Override public String getArtworkPhoto2() { return null; }

    @Override public void setArtworkPhoto3(String photo) {}
    @Override public String getArtworkPhoto3() { return null; }

    @Override public void setArtworkArtistPhotoDisplay(String photo) {}
    @Override public void setArtworks(List<String[]> artworks) {}

    protected void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            if (primaryStage != null && primaryStage.getScene() != null) {
                primaryStage.getScene().setRoot(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Failed to load screen: " + fxmlPath);
        }
    }

    @Override
    public void navigateToHome() {
        switchScene("/org/museum_app/museum/fxml/home.fxml");
    }

    @Override
    public void navigateToArtistList() {
        switchScene("/org/museum_app/museum/fxml/artist_list.fxml");
    }

    @Override
    public void navigateToArtworkList() {
        switchScene("/org/museum_app/museum/fxml/artwork_list.fxml");
    }

    @Override
    public void navigateToArtistDetail(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artist.fxml"));
            Parent root = loader.load();

            ArtistGUI controller = loader.getController();
            controller.receiveIdAndLoad(id);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void navigateToArtistForm(Integer id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artist_form.fxml"));
            Parent root = loader.load();

            if (id != null) {
                ArtistFormGUI controller = loader.getController();
                controller.loadArtistForEdit(id);
            }

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void navigateToArtworkDetail(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artwork.fxml"));
            Parent root = loader.load();

            ArtworkGUI controller = loader.getController();
            controller.receiveIdAndLoad(id);

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void navigateToArtworkForm(Integer id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/museum_app/museum/fxml/artwork_form.fxml"));
            Parent root = loader.load();

            if (id != null) {
                ArtworkFormGUI controller = loader.getController();
                controller.loadArtworkForEdit(id);
            }

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Shared utility for images
    protected Image safeLoadImage(String path) {
        if (path == null || path.trim().isEmpty()) return null;

        try {
            if (path.startsWith("http://") || path.startsWith("https://")) {
                return new Image(path, 360, 0, true, true, true);
            }

            File file = new File(path);
            if (file.exists()) {
                return new Image(file.toURI().toString(), 360, 0, true, true, true);
            }

            String resourcePath = path.startsWith("/") ? path : "/" + path;
            URL resource = getClass().getResource(resourcePath);
            if (resource != null) {
                return new Image(resource.toExternalForm(), 360, 0, true, true, true);
            }

        } catch (Exception e) {
            System.err.println("Error while trying to upload: " + path);
        }
        return null;
    }

    @Override public void refreshImagePreview() {}
}