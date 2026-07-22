package com.mayank.musichub.controller;

import com.mayank.musichub.model.Playlist;
import com.mayank.musichub.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.mayank.musichub.service.SongService;
import com.mayank.musichub.model.Song;
import java.util.List;

@Controller
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    @GetMapping("/playlists")
    public String playlists(Model model) {

        model.addAttribute("playlists",
                playlistService.getAllPlaylists());

        return "playlists";
    }

    @GetMapping("/playlist/add")
    public String addPlaylistForm(Model model) {

        model.addAttribute("playlist",
                new Playlist());

        return "add-playlist";
    }

    @PostMapping("/playlist/save")
    public String savePlaylist(@ModelAttribute Playlist playlist) {

        playlistService.savePlaylist(playlist);

        return "redirect:/playlists";
    }
@GetMapping("/playlist/{id}")
public String viewPlaylist(@PathVariable Long id,
                           Model model) {

    Playlist playlist = playlistService.getPlaylistById(id);

    model.addAttribute("playlist", playlist);

    return "playlist-details";
}
@GetMapping("/playlist/{id}/add-songs")
public String addSongsPage(@PathVariable Long id,
                           Model model) {

    Playlist playlist = playlistService.getPlaylistById(id);

    model.addAttribute("playlist", playlist);

    model.addAttribute("songs",
            songService.getAllSongs());

    return "add-songs-to-playlist";
}
@PostMapping("/playlist/{id}/add-songs")
public String addSongsToPlaylist(

        @PathVariable Long id,

        @RequestParam("songIds") List<Integer> songIds) {

    playlistService.addSongsToPlaylist(id, songIds);

    return "redirect:/playlist/" + id;
}
@PostMapping("/playlist/delete/{id}")
public String deletePlaylist(@PathVariable Long id) {

    playlistService.deletePlaylist(id);

    return "redirect:/playlists";
}
@GetMapping("/playlist/edit/{id}")
public String editPlaylist(@PathVariable Long id,
                           Model model) {

    Playlist playlist = playlistService.getPlaylistById(id);

    model.addAttribute("playlist", playlist);

    return "edit-playlist";
}
@PostMapping("/playlist/update/{id}")
public String updatePlaylist(@PathVariable Long id,
                             @ModelAttribute Playlist playlist) {

    playlistService.updatePlaylist(id, playlist);

    return "redirect:/playlists";
}
}