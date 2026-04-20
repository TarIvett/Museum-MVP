package org.museum_app.museum.presenter;

import org.museum_app.museum.model.Artist;
import org.museum_app.museum.model.Artwork;
import org.museum_app.museum.model.repository.ArtistRepository;
import org.museum_app.museum.model.repository.ArtworkRepository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistPresenter {
    private final IArtistGUI artistGUI;
    private final ArtistRepository artistRepository = new ArtistRepository();
    private final ArtworkRepository artworkRepository = new ArtworkRepository();

    public ArtistPresenter(IArtistGUI artistGUI) {
        this.artistGUI = artistGUI;
    }

    //all artists as in READ
    public void loadAllArtists() {
        try {
            List<String[]> rows = artistRepository.findAll()
                    .stream()
                    .map(this::artistToRow)
                    .collect(Collectors.toList());
            artistGUI.setArtists(rows);
        } catch (Exception e) {
            artistGUI.showMessage(e.getMessage());
        }
    }

    //delete an Artist as in DELETE
    public void deleteArtist(int artistId) {
        try {
            Artist artist = artistRepository.findById(artistId);
            if (artist == null) {
                artistGUI.showMessage("Artist not found");
                return;
            }
            artistRepository.delete(artist);
            loadAllArtists();
        } catch (Exception e) {
            artistGUI.showMessage(e.getMessage());
        }
    }

    //search artist by name
    public void searchArtistByName(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                loadAllArtists();
                return;
            }

            List<String[]> rows = artistRepository
                    .searchByName(keyword)
                    .stream()
                    .map(this::artistToRow)
                    .collect(Collectors.toList());

            if (rows.isEmpty()) {
                artistGUI.setArtists(new java.util.ArrayList<>());
            } else {
                artistGUI.setArtists(rows);
            }

        } catch (Exception e) {
            artistGUI.showMessage("Search error: " + e.getMessage());
        }
    }

    //transform artist object to a list of strings for the gui (Only used for lists now)
    private String[] artistToRow(Artist artist) {
        return new String[] {
                String.valueOf(artist.getId()),
                artist.getName(),
                artist.getBirthDate().toString(),
                artist.getBirthPlace(),
                artist.getNationality(),
                artist.getPhoto()
        };
    }

    // triggered when an artist card is clicked
    public void loadSingleArtist(int id) {
        try {
            Artist found = artistRepository.findById(id);
            if (found == null) {
                artistGUI.showMessage("Artist not found.");
                return;
            }

            artistGUI.setArtistName(found.getName());
            artistGUI.setArtistBirthDate(found.getBirthDate().toString());
            artistGUI.setArtistBirthPlace(found.getBirthPlace());
            artistGUI.setArtistNationality(found.getNationality());
            artistGUI.setArtistPhoto(found.getPhoto());

            List<Artwork> artistArtworks = artworkRepository.findByArtistId(id);
            List<String[]> artworkRows = new java.util.ArrayList<>();

            for (Artwork a : artistArtworks) {
                String[] row = new String[10];

                row[IMuseumGUI.ARTWORK_ID] = String.valueOf(a.getId());
                row[IMuseumGUI.ARTWORK_TITLE] = a.getTitle();
                row[IMuseumGUI.ARTWORK_ARTIST_NAME] = found.getName();
                row[IMuseumGUI.ARTWORK_PHOTO1] = a.getPhoto1() != null ? a.getPhoto1() : "";

                artworkRows.add(row);
            }

            artistGUI.setArtworks(artworkRows);
        } catch (Exception e) {
            artistGUI.showMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    // both INSERT (Create) and UPDATE logic
    public void saveArtist(Integer currentId) {
        String name = artistGUI.getArtistName();
        String birthDateStr = artistGUI.getArtistBirthDate();

        if (name == null || name.trim().isEmpty()) {
            artistGUI.showMessage("Name is required.");
            return;
        }

        try {
            Date sqlDate = Date.valueOf(birthDateStr);
            Artist artist = new Artist(name, sqlDate, artistGUI.getArtistBirthPlace(),
                    artistGUI.getArtistNationality(), artistGUI.getArtistPhoto());

            Artist result;
            if (currentId == null) {
                result = artistRepository.save(artist);
            } else {
                result = artistRepository.update(artistGUI.getOldArtistName(), artist);
            }

            if (result != null) {
                artistGUI.navigateToArtistList();
            }
        } catch (Exception e) {
            artistGUI.showMessage("Error: " + e.getMessage());
        }
    }

    // for edit form, loading the data for the selected artist
    public void prepareArtistForEdit(int id) {
        try {
            Artist found = artistRepository.findById(id);

            if (found == null) {
                artistGUI.showMessage("Could not load artist data.");
                return;
            }

            artistGUI.setOldArtistName(found.getName());
            artistGUI.setArtistName(found.getName());
            artistGUI.setArtistBirthDate(found.getBirthDate().toString());
            artistGUI.setArtistBirthPlace(found.getBirthPlace());
            artistGUI.setArtistNationality(found.getNationality());
            artistGUI.setArtistPhoto(found.getPhoto());

            artistGUI.refreshImagePreview();

        } catch (Exception e) {
            artistGUI.showMessage(e.getMessage());
        }
    }

    // for showing the image for the artist and copying it to local directory
    public void copyImageFile(java.io.File selectedFile) {
        if (selectedFile == null) return;

        try {
            java.io.File destDir = new java.io.File("uploaded_images");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            java.io.File copiedFile = new java.io.File(destDir, selectedFile.getName());

            java.nio.file.Files.copy(
                    selectedFile.toPath(),
                    copiedFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            String relativePath = "uploaded_images/" + selectedFile.getName();
            artistGUI.setArtistPhoto(relativePath);
            artistGUI.refreshImagePreview();

        } catch (Exception e) {
            e.printStackTrace();
            artistGUI.showMessage("Failed to copy image: " + e.getMessage());
        }
    }

    // Triggered when "See all" is clicked on home screen
    public void onSeeAllArtistsClicked() {
        artistGUI.navigateToArtistList();
    }

    // Triggered when back button is clicked on artist detail or form
    public void onBackFromArtist() {
        artistGUI.navigateToArtistList();
    }

    // Triggered by ArtistsAllGUI when Add is clicked
    public void onAddArtistClicked() {
        artistGUI.navigateToArtistForm(null);
    }

    // Triggered by ArtistsAllGUI when Edit is clicked
    public void onEditArtistClicked(Integer id) {
        if (id == null) {
            artistGUI.showMessage("Please select an artist first to edit.");
            return;
        }
        artistGUI.navigateToArtistForm(id);
    }

    // Triggered by ArtistsAllGUI when a card is double-clicked
    public void onArtistDoubleClicked(int id) {
        artistGUI.navigateToArtistDetail(id);
    }

    // Triggered by ArtistsGUI when an artwork card is clicked
    public void onArtworkClicked(int artworkId) {
        artistGUI.navigateToArtworkDetail(artworkId);
    }

    // Triggered when back button is clicked on artist list
    public void onBackFromArtistList() {
        artistGUI.navigateToHome();
    }
}