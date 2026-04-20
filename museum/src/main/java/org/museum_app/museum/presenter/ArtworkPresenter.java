package org.museum_app.museum.presenter;

import org.museum_app.museum.model.Artist;
import org.museum_app.museum.model.Artwork;
import org.museum_app.museum.model.ArtworkGenreList;
import org.museum_app.museum.model.repository.ArtistRepository;
import org.museum_app.museum.model.repository.ArtworkRepository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ArtworkPresenter {
    private final IArtworkGUI artworkGUI;
    private final ArtworkRepository artworkRepository = new ArtworkRepository();
    private final ArtistRepository artistRepository = new ArtistRepository();

    public ArtworkPresenter(IArtworkGUI artworkGUI) {
        this.artworkGUI = artworkGUI;
    }

    // Handles both INSERT and UPDATE logic
    public void saveArtwork(Integer currentId) {
        try {
            String title = artworkGUI.getArtworkTitle();
            String artistName = artworkGUI.getArtworkArtistName();
            String genre = artworkGUI.getArtworkGenre();
            String yearStr = artworkGUI.getArtworkCreationYear();

            if (title == null || title.trim().isEmpty() || artistName == null || genre == null) {
                artworkGUI.showMessage("Please fill out the Title, Artist, and Genre fields.");
                return;
            }

            List<Artist> artists = artistRepository.findByName(artistName);
            if (artists.isEmpty()) {
                artworkGUI.showMessage("Artist not found");
                return;
            }

            Artist artist = artists.getFirst();

            Date creationDate = null;
            if (yearStr != null && !yearStr.trim().isEmpty()) {
                String formattedYear = yearStr.trim();
                if (formattedYear.length() == 4) {
                    creationDate = Date.valueOf(formattedYear + "-01-01");
                } else {
                    creationDate = Date.valueOf(formattedYear);
                }
            }

            Artwork artwork = new Artwork(
                    title,
                    artist,
                    artworkGUI.getArtworkDescription(),
                    creationDate,
                    ArtworkGenreList.valueOf(genre),
                    artworkGUI.getArtworkPhoto1(),
                    artworkGUI.getArtworkPhoto2(),
                    artworkGUI.getArtworkPhoto3()
            );

            Artwork result;
            if (currentId == null) {
                result = artworkRepository.save(artwork);
            } else {
                result = artworkRepository.update(artworkGUI.getOldArtworkTitle(), artist.getId(), artwork);
            }

            if (result == null) {
                artworkGUI.showMessage("Artwork not saved");
                return;
            }

            artworkGUI.navigateToArtworkList();

        } catch (IllegalArgumentException e) {
            artworkGUI.showMessage("Invalid Year format. Please enter a 4-digit year (e.g., 1889).");
        } catch (Exception e) {
            artworkGUI.showMessage(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //all artworks as in READ
    public void loadAllArtworks() {
        try {
            List<String[]> rows = artworkRepository.findAll()
                    .stream()
                    .map(this::artworkToRow)
                    .collect(Collectors.toList());
            artworkGUI.setArtworks(rows);
        } catch (Exception e) {
            artworkGUI.showMessage(e.getMessage());
        }
    }

    //all artworks alphabetically ordered by artist name
    public void loadAllArtworksOrd() {
        try {
            List<String[]> rows = artworkRepository.findAllSortedByArtist()
                    .stream()
                    .map(this::artworkToRow)
                    .collect(Collectors.toList());
            artworkGUI.setArtworks(rows);
        } catch (Exception e) {
            artworkGUI.showMessage(e.getMessage());
        }
    }

    //delete an Artwork as in DELETE
    public void deleteArtwork(int artworkId) {
        try {
            Artwork artwork = artworkRepository.findById(artworkId);
            if (artwork == null) {
                artworkGUI.showMessage("Artwork not found");
                return;
            }
            artworkRepository.delete(artwork);
            loadAllArtworksOrd();
        } catch (Exception e) {
            artworkGUI.showMessage(e.getMessage());
        }
    }

    //filter artworks based on search input, selected artist and genre
    public void applyFilters(String title, String artist, String genre) {
        try {
            List<Artwork> filtered = artworkRepository.searchAndFilter(title, artist, genre);

            List<String[]> rows = filtered.stream()
                    .map(this::artworkToRow)
                    .collect(java.util.stream.Collectors.toList());

            if (rows.isEmpty()) {
                artworkGUI.setArtworks(new java.util.ArrayList<>());
            } else {
                artworkGUI.setArtworks(rows);
            }
        } catch (Exception e) {
            artworkGUI.showMessage("Filter error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //helper to be able to show all artist names
    public List<String> getAllArtistNames() {
        try {
            return artistRepository.findAll().stream()
                    .map(Artist::getName)
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    //transform artwork object to a list of strings for the gui (Used for List/Table views)
    private String[] artworkToRow(Artwork artwork) {
        return new String[] {
                String.valueOf(artwork.getId()),
                artwork.getTitle(),
                artwork.getArtist().getName(),
                artwork.getDescription(),
                artwork.getCreationYear() != null ? artwork.getCreationYear().toString() : "",
                artwork.getGenre() != null ? artwork.getGenre().toString() : "",
                artwork.getPhoto1() != null ? artwork.getPhoto1() : "",
                artwork.getPhoto2() != null ? artwork.getPhoto2() : "",
                artwork.getPhoto3() != null ? artwork.getPhoto3() : "",
                artwork.getArtist().getPhoto() != null ? artwork.getArtist().getPhoto() : ""
        };
    }

    // triggered when an artwork card is double-clicked
    public void loadSingleArtwork(int id) {
        try {
            Artwork found = artworkRepository.findById(id);

            if (found == null) {
                artworkGUI.showMessage("Artwork not found.");
                return;
            }

            String formattedGenre = "GENRE - ";
            if (found.getGenre() != null) {
                formattedGenre += found.getGenre().toString().substring(0, 1).toUpperCase()
                        + found.getGenre().toString().substring(1).toLowerCase();
            }

            String formattedYear = "CREATION YEAR - ";
            if (found.getCreationYear() != null) {
                formattedYear += found.getCreationYear().toString().substring(0, 4);
            }

            artworkGUI.setArtworkTitle(found.getTitle());
            artworkGUI.setArtworkGenre(formattedGenre);
            artworkGUI.setArtworkCreationYear(formattedYear);
            artworkGUI.setArtworkArtistName(found.getArtist().getName());
            artworkGUI.setArtworkDescription(found.getDescription());
            artworkGUI.setArtworkPhoto1(found.getPhoto1());

            if (found.getArtist().getPhoto() != null) {
                artworkGUI.setArtworkArtistPhotoDisplay(found.getArtist().getPhoto());
            }
        } catch (Exception e) {
            artworkGUI.showMessage(e.getMessage());
        }
    }

    // for edit form - Pushes data to the form (Strict MVP replacement for getArtworkById)
    public void prepareArtworkForEdit(int id) {
        try {
            Artwork found = artworkRepository.findById(id);

            if (found == null) {
                artworkGUI.showMessage("Could not load artwork data.");
                return;
            }

            artworkGUI.setOldArtworkTitle(found.getTitle());
            artworkGUI.setArtworkTitle(found.getTitle());
            artworkGUI.setArtworkArtistName(found.getArtist().getName());
            artworkGUI.setArtworkDescription(found.getDescription());

            if(found.getCreationYear() != null) {
                artworkGUI.setArtworkCreationYear(found.getCreationYear().toString());
            }

            if(found.getGenre() != null){
                artworkGUI.setArtworkGenre(found.getGenre().toString());
            }

            artworkGUI.setArtworkPhoto1(found.getPhoto1());
            artworkGUI.setArtworkPhoto2(found.getPhoto2());
            artworkGUI.setArtworkPhoto3(found.getPhoto3());

            artworkGUI.refreshImagePreview();

        } catch (Exception e) {
            artworkGUI.showMessage(e.getMessage());
        }
    }

    // for showing the image for the artwork and copying it to local directory
    public void copyImageFile(java.io.File selectedFile, int photoIndexId) {
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

            if (photoIndexId == 1) {
                artworkGUI.setArtworkPhoto1(relativePath);
            } else if (photoIndexId == 2) {
                artworkGUI.setArtworkPhoto2(relativePath);
            } else if (photoIndexId == 3) {
                artworkGUI.setArtworkPhoto3(relativePath);
            }

            artworkGUI.refreshImagePreview();

        } catch (Exception e) {
            e.printStackTrace();
            artworkGUI.showMessage("Failed to copy image: " + e.getMessage());
        }
    }

    // triggered when "See all" is clicked on home screen
    public void onSeeAllArtworksClicked() {
        artworkGUI.navigateToArtworkList();
    }

    // triggered when back button is clicked on artwork detail
    public void onBackFromArtwork() {
        artworkGUI.navigateToArtworkList();
    }

    // Triggered by ArtworksAllGUI when a card is double-clicked
    public void onArtworkDoubleClicked(int id) {
        artworkGUI.navigateToArtworkDetail(id);
    }

    // Triggered by ArtworksAllGUI when + button clicked
    public void onAddArtworkClicked() {
        artworkGUI.navigateToArtworkForm(null);
    }

    // Triggered by ArtworksAllGUI when edit button clicked
    public void onEditArtworkClicked(Integer id) {
        if (id == null) {
            artworkGUI.showMessage("Please select an artwork first to edit.");
            return;
        }
        artworkGUI.navigateToArtworkForm(id);
    }

    // triggered when back button is clicked on artwork list
    public void onBackFromArtworkList() {
        artworkGUI.navigateToHome();
    }
}