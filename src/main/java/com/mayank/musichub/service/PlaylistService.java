package com.mayank.musichub.service;

import com.mayank.musichub.model.Playlist;
import com.mayank.musichub.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mayank.musichub.repository.SongRepository;
import com.mayank.musichub.model.Song;
import java.util.ArrayList;

import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }
    public List<Playlist> searchPlaylists(String keyword) {

    return playlistRepository.findByNameContainingIgnoreCase(keyword);

}

    public Playlist savePlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }
    public Playlist updatePlaylist(Long id, Playlist updatedPlaylist) {

    Playlist playlist = getPlaylistById(id);

    playlist.setName(updatedPlaylist.getName());

    return playlistRepository.save(playlist);
}

    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }
    public void addSongsToPlaylist(Long playlistId, List<Integer> songIds) {

    Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow();

    List<Song> songs = new ArrayList<>();

    for (Integer id : songIds) {

        Song song = songRepository.findById(id)
                .orElseThrow();

        songs.add(song);

    }

    playlist.setSongs(songs);

    playlistRepository.save(playlist);

}
public void removeSongFromPlaylist(Long playlistId, Integer songId) {

    Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow();

    Song song = songRepository.findById(songId)
            .orElseThrow();

    playlist.getSongs().remove(song);

    playlistRepository.save(playlist);

}
public void addSingleSongToPlaylist(Long playlistId, Integer songId) {

    Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow();

    Song song = songRepository.findById(songId)
            .orElseThrow();

    if (!playlist.getSongs().contains(song)) {

        playlist.getSongs().add(song);

        playlistRepository.save(playlist);

    }

}
public List<Playlist> sortAscending() {

    return playlistRepository.findAllByOrderByNameAsc();

}

public List<Playlist> sortDescending() {

    return playlistRepository.findAllByOrderByNameDesc();

}
}