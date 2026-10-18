package com.mayank.musichub.controller;

import com.mayank.musichub.model.Song;
import com.mayank.musichub.service.SongService;

import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.mayank.musichub.service.AudioStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private AudioStorageService audioStorageService;

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }
    @GetMapping("/{id}")
public Song getSongById(@PathVariable Integer id) {
    return songService.getSongById(id);
}

    @PostMapping
    public Song addSong(@Valid @RequestBody Song song) {
        return songService.saveSong(song);
    }
    @PutMapping("/{id}")
public Song updateSong(@PathVariable Integer id,
                       @RequestBody Song song) {

    return songService.updateSong(id, song);
}
@DeleteMapping("/{id}")
public String deleteSong(@PathVariable Integer id) {

    songService.deleteSong(id);

    return "Song deleted successfully!";
}
@GetMapping("/artist/{artist}")
public List<Song> getSongsByArtist(@PathVariable String artist) {
    return songService.getSongsByArtist(artist);
}
@GetMapping("/title/{title}")
public List<Song> getSongsByTitle(@PathVariable String title) {
    return songService.getSongsByTitle(title);
}
@GetMapping("/album/{album}")
public List<Song> getSongsByAlbum(@PathVariable String album) {
    return songService.getSongsByAlbum(album);
}
@GetMapping("/search/{title}/{artist}")
public List<Song> getSongsByTitleAndArtist(
        @PathVariable String title,
        @PathVariable String artist) {

    return songService.getSongsByTitleAndArtist(title, artist);
}
@GetMapping("/search/artist/{artist}")
public List<Song> searchArtist(@PathVariable String artist) {
    return songService.searchArtist(artist);
}
@GetMapping("/search/songs/{keyword}")
public List<Song> searchSongs(@PathVariable String keyword) {
    return songService.searchSongs(keyword);
}

@GetMapping("/{id}/download")
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

@GetMapping("/{id}/stream")
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

}