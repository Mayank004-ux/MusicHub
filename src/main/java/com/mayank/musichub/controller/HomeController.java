package com.mayank.musichub.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import com.mayank.musichub.model.Song;
import com.mayank.musichub.service.AudioStorageService;
import com.mayank.musichub.service.SongService;
import com.mayank.musichub.util.UrlValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private SongService songService;

    @Autowired
    private AudioStorageService audioStorageService;

    @GetMapping("/")
public String home(

        @RequestParam(defaultValue = "0") int page,
         @RequestParam(required = false) String keyword,
        Model model) {

    Page<Song> songPage;

if (keyword != null && !keyword.trim().isEmpty()) {

    songPage = songService.searchSongsByPage(keyword, page);

} else {

    songPage = songService.getSongsByPage(page);

}

    model.addAttribute("songs", songPage.getContent());

    model.addAttribute("currentPage", page);

    model.addAttribute("totalPages", songPage.getTotalPages());

    model.addAttribute("keyword", keyword);

    // Dashboard Statistics
    model.addAttribute("totalSongs", songService.getTotalSongs());

    model.addAttribute("favoriteSongs", songService.getFavoriteSongsCount());

    model.addAttribute("totalArtists", songService.getTotalArtists());

    model.addAttribute("totalGenres", songService.getTotalGenres());

    model.addAttribute("totalAlbums", songService.getTotalAlbums());

    model.addAttribute("averageRating", songService.getAverageRating());

    model.addAttribute("activePage", "home");

    model.addAttribute("totalSongs",songService.getTotalSongCount());

    model.addAttribute( "recentSongs",songService.getRecentSongs());

    return "index";
}
    @GetMapping("/song/{id}")
public String songDetails(@PathVariable Integer id,
                          Model model) {

    Song song = songService.getSongById(id);

    model.addAttribute("song", song);

    model.addAttribute( "recommendedSongs",songService.getRecommendedSongs(id));

    return "song-details";
}
@GetMapping("/song/edit/{id}")
public String editSongPage(@PathVariable Integer id,
                           Model model) {

    Song song = songService.getSongById(id);

    model.addAttribute("song", song);

    return "edit-song";
}
@PostMapping("/song/update/{id}")
public String updateSong(@PathVariable Integer id,
                         @Valid @ModelAttribute("song") Song song,
                         BindingResult result,
                         @RequestParam(value = "audioFile", required = false) MultipartFile audioFile,
                         @RequestParam(value = "removeAudio", defaultValue = "false") boolean removeAudio,
                         Model model) {

    // Validate official streaming and preview URLs
    if (song.getSpotifyUrl() != null && !song.getSpotifyUrl().isBlank()
            && !UrlValidationUtil.isSpotifyUrl(song.getSpotifyUrl())) {
        result.rejectValue("spotifyUrl", "error.song", "Invalid Spotify URL. Must be an official Spotify HTTPS link (e.g., https://open.spotify.com/...)");
    }
    if (song.getYoutubeUrl() != null && !song.getYoutubeUrl().isBlank()
            && !UrlValidationUtil.isYouTubeUrl(song.getYoutubeUrl())) {
        result.rejectValue("youtubeUrl", "error.song", "Invalid YouTube URL. Must be an official YouTube HTTPS link (e.g., https://www.youtube.com/...)");
    }
    if (song.getAudioUrl() != null && !song.getAudioUrl().isBlank()
            && !UrlValidationUtil.isValidPreviewUrl(song.getAudioUrl())) {
        result.rejectValue("audioUrl", "error.song", "Invalid preview URL. Must be a secure HTTPS link.");
    }

    if (result.hasErrors()) {
        Song original = songService.getSongById(id);
        if (original != null) {
            song.setAudioFileName(original.getAudioFileName());
            song.setAudioStorageKey(original.getAudioStorageKey());
            song.setAudioFileSize(original.getAudioFileSize());
        }
        model.addAttribute("song", song);
        return "edit-song";
    }

    try {
        songService.updateSongWithAudio(id, song, audioFile, removeAudio);
    } catch (IllegalArgumentException e) {
        Song original = songService.getSongById(id);
        if (original != null) {
            song.setAudioFileName(original.getAudioFileName());
            song.setAudioStorageKey(original.getAudioStorageKey());
            song.setAudioFileSize(original.getAudioFileSize());
        }
        model.addAttribute("uploadError", e.getMessage());
        model.addAttribute("song", song);
        return "edit-song";
    } catch (Exception e) {
        model.addAttribute("uploadError", "Failed to update audio file: " + e.getMessage());
        model.addAttribute("song", song);
        return "edit-song";
    }

    return "redirect:/song/" + id;
}

@PostMapping("/song/delete/{id}")
public String deleteSong(@PathVariable Integer id) {

    songService.deleteSong(id);

    return "redirect:/";
}

@GetMapping("/song/add")
public String addSongPage(Model model){

    model.addAttribute("song", new Song());
    model.addAttribute("activePage", "add-song");

    return "add-song";

}

@PostMapping("/song/save")
public String saveSong(@Valid @ModelAttribute("song") Song song,
                       BindingResult result,
                       @RequestParam(value = "audioFile", required = false) MultipartFile audioFile,
                       Model model) {

    // Validate official streaming and preview URLs
    if (song.getSpotifyUrl() != null && !song.getSpotifyUrl().isBlank()
            && !UrlValidationUtil.isSpotifyUrl(song.getSpotifyUrl())) {
        result.rejectValue("spotifyUrl", "error.song", "Invalid Spotify URL. Must be an official Spotify HTTPS link (e.g., https://open.spotify.com/...)");
    }
    if (song.getYoutubeUrl() != null && !song.getYoutubeUrl().isBlank()
            && !UrlValidationUtil.isYouTubeUrl(song.getYoutubeUrl())) {
        result.rejectValue("youtubeUrl", "error.song", "Invalid YouTube URL. Must be an official YouTube HTTPS link (e.g., https://www.youtube.com/...)");
    }
    if (song.getAudioUrl() != null && !song.getAudioUrl().isBlank()
            && !UrlValidationUtil.isValidPreviewUrl(song.getAudioUrl())) {
        result.rejectValue("audioUrl", "error.song", "Invalid preview URL. Must be a secure HTTPS link.");
    }

    if (result.hasErrors()) {
        model.addAttribute("activePage", "add-song");
        return "add-song";
    }

    try {
        songService.saveSongWithAudio(song, audioFile);
    } catch (IllegalArgumentException e) {
        model.addAttribute("uploadError", e.getMessage());
        model.addAttribute("activePage", "add-song");
        return "add-song";
    } catch (Exception e) {
        model.addAttribute("uploadError", "Failed to save song audio: " + e.getMessage());
        model.addAttribute("activePage", "add-song");
        return "add-song";
    }

    return "redirect:/";
}

@GetMapping("/song/{id}/download")
public ResponseEntity<Resource> downloadSongAudio(@PathVariable Integer id) {
    Song song = songService.getSongById(id);
    if (song == null) {
        return ResponseEntity.notFound().build();
    }
    if (!song.hasAudioFile()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    try {
        Resource resource = audioStorageService.loadAudioAsResource(song.getAudioStorageKey());
        String safeFilename = audioStorageService.generateSafeDownloadFilename(song);

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (song.getAudioContentType() != null) {
            try {
                mediaType = MediaType.parseMediaType(song.getAudioContentType());
            } catch (Exception ignored) {
            }
        }

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(safeFilename, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

@GetMapping("/song/{id}/stream")
public ResponseEntity<Resource> streamSongAudio(@PathVariable Integer id) {
    Song song = songService.getSongById(id);
    if (song == null || !song.hasAudioFile()) {
        return ResponseEntity.notFound().build();
    }

    try {
        Resource resource = audioStorageService.loadAudioAsResource(song.getAudioStorageKey());

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (song.getAudioContentType() != null) {
            try {
                mediaType = MediaType.parseMediaType(song.getAudioContentType());
            } catch (Exception ignored) {
            }
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
@GetMapping("/favorites")
public String showFavoriteSongs(
        @RequestParam(required = false) String keyword,
        Model model) {

    List<Song> songs;
    if (keyword != null && !keyword.trim().isEmpty()) {
        songs = songService.searchFavoriteSongs(keyword.trim());
    } else {
        songs = songService.getFavoriteSongs();
    }

    model.addAttribute("songs", songs);
    model.addAttribute("keyword", keyword != null ? keyword.trim() : null);
    model.addAttribute("favoriteCount", songService.getFavoriteSongCount());
    model.addAttribute("averageRating", songService.getFavoriteAverageRating());
    model.addAttribute("favoriteDuration", songService.getFavoriteTotalDuration());
    model.addAttribute("languageStats", songService.getFavoriteLanguageStats());
    model.addAttribute("activePage", "favorites");

    return "favorites";
}
@GetMapping("/genre/{genre}")
public String showSongsByGenre(@PathVariable String genre, Model model) {

    model.addAttribute("songs", songService.getSongsByGenre(genre));

    model.addAttribute("genre", genre);

    return "genre";

}
@GetMapping("/artist/{artist}")
public String showSongsByArtist(@PathVariable String artist, Model model) {

    model.addAttribute("songs", songService.getSongsByArtistName(artist));

    model.addAttribute("artist", artist);

    return "artist";

}
@GetMapping("/sort")
public String sortSongs(
        @RequestParam(required = false) String type,
        Model model) {

    if ("rating".equals(type)) {

        model.addAttribute("songs", songService.getSongsByRating());

    } else if ("year".equals(type)) {

        model.addAttribute("songs", songService.getSongsByReleaseYear());

    } else if ("titleAsc".equals(type)) {

        model.addAttribute("songs", songService.getSongsByTitleAsc());

    } else if ("titleDesc".equals(type)) {

        model.addAttribute("songs", songService.getSongsByTitleDesc());

    } else {

        model.addAttribute("songs", songService.getAllSongs());

    }

    model.addAttribute("sortType", type);

    return "sort";
}
@GetMapping("/sort/rating")
public String sortByRating(Model model) {

    model.addAttribute("songs", songService.getSongsByRating());

    model.addAttribute("sortType", "⭐ Rating (Highest First)");

    return "sort";

}
@GetMapping("/sort/year")
public String sortByYear(Model model) {

    model.addAttribute("songs", songService.getSongsByReleaseYear());

    model.addAttribute("sortType", "📅 Release Year (Newest First)");

    return "sort";

}
@GetMapping("/sort/title-asc")
public String sortByTitleAsc(Model model) {

    model.addAttribute("songs", songService.getSongsByTitleAsc());

    model.addAttribute("sortType", "🔤 Title (A-Z)");

    return "sort";

}
@GetMapping("/sort/title-desc")
public String sortByTitleDesc(Model model) {

    model.addAttribute("songs", songService.getSongsByTitleDesc());

    model.addAttribute("sortType", "🔤 Title (Z-A)");

    return "sort";

}
@GetMapping("/top-rated")
public String topRatedSongs(
        @RequestParam(required = false) String keyword,
        Model model) {

    List<Song> songs;
    if (keyword != null && !keyword.trim().isEmpty()) {
        songs = songService.searchTopRatedSongs(keyword.trim());
    } else {
        songs = songService.getTopRatedSongs();
    }

    model.addAttribute("songs", songs);
    model.addAttribute("keyword", keyword != null ? keyword.trim() : null);
    model.addAttribute("activePage", "top-rated");

    return "top-rated";
}
@GetMapping("/add-song")
public String addSongForm(Model model) {

    model.addAttribute("song", new Song());

    model.addAttribute("activePage", "add-song");

    return "add-song";
}
@GetMapping("/analytics")
public String analytics(Model model) {

    model.addAttribute("activePage", "analytics");

    model.addAttribute("totalSongs", songService.getTotalSongCount());

    model.addAttribute("favoriteSongs", songService.getFavoriteSongCount());

    model.addAttribute("totalArtists", songService.getTotalArtists());

    model.addAttribute("totalGenres", songService.getTotalGenres());

    model.addAttribute("totalAlbums", songService.getTotalAlbums());

    model.addAttribute("averageRating", songService.getAverageRating());
    
    model.addAttribute("recentSongs", songService.getRecentSongs());

    model.addAttribute("favoriteAverageRating", songService.getFavoriteAverageRating());

model.addAttribute("favoriteDuration",songService.getFavoriteTotalDuration());

model.addAttribute( "languageStats",songService.getFavoriteLanguageStats());

model.addAttribute("genreStatistics",songService.getGenreStatistics());

model.addAttribute( "artistStatistics",songService.getArtistStatistics());

model.addAttribute( "highestRatedSong",songService.getHighestRatedSong());

model.addAttribute("latestSong",songService.getLatestSong());

model.addAttribute("favoritePercentage",String.format("%.1f",songService.getFavoritePercentage()));

model.addAttribute("popularGenre",songService.getMostPopularGenre());

    return "analytics";

}
}