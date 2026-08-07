package com.mayank.musichub.controller;

import com.mayank.musichub.model.Song;
import com.mayank.musichub.service.SongService;

import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

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

}