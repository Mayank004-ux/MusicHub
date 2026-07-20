package com.mayank.musichub.service;

import com.mayank.musichub.model.Song;
import com.mayank.musichub.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }
    public List<Song> getSongsByArtist(String artist) {
    return songRepository.findByArtist(artist);
}
    public Song getSongById(Integer id) {
    return songRepository.findById(id).orElse(null);
}
public Song updateSong(Integer id, Song updatedSong) {

    Song existingSong = songRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Song not found"));

    existingSong.setTitle(updatedSong.getTitle());
    existingSong.setArtist(updatedSong.getArtist());
    existingSong.setAlbum(updatedSong.getAlbum());
    existingSong.setImageUrl(updatedSong.getImageUrl());
    existingSong.setGenre(updatedSong.getGenre());
    existingSong.setLanguage(updatedSong.getLanguage());
    existingSong.setRating(updatedSong.getRating());
    existingSong.setDuration(updatedSong.getDuration());
    existingSong.setReleaseYear(updatedSong.getReleaseYear());
    existingSong.setYoutubeUrl(updatedSong.getYoutubeUrl());
    existingSong.setFavorite(updatedSong.getFavorite());
    existingSong.setLyrics(updatedSong.getLyrics());
    existingSong.setSpotifyUrl(updatedSong.getSpotifyUrl());

    songRepository.save(existingSong);

    return songRepository.save(existingSong);
}
public void deleteSong(Integer id) {
     Song existingSong = songRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Song not found"));

    songRepository.delete(existingSong);
}
public List<Song> getSongsByTitle(String title) {
    return songRepository.findByTitle(title);
}
public List<Song> getSongsByAlbum(String album) {
    return songRepository.findByAlbum(album);
}
public List<Song> getSongsByTitleAndArtist(String title, String artist) {
    return songRepository.findByTitleAndArtist(title, artist);
}
public List<Song> searchArtist(String artist) {
    return songRepository.findByArtistContainingIgnoreCase(artist);
}
public List<Song> searchSongs(String keyword) {

    return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(
                    keyword,
                    keyword
            );

}
public List<Song> getFavoriteSongs() {

    return songRepository.findByFavoriteTrue();

}
public List<Song> getSongsByGenre(String genre) {

    return songRepository.findByGenreIgnoreCase(genre);

}
public List<Song> getSongsByArtistName(String artist) {

    return songRepository.findByArtistIgnoreCase(artist);

}
public List<Song> getSongsByRating() {

    return songRepository.findAllByOrderByRatingDesc();

}

public List<Song> getSongsByReleaseYear() {

    return songRepository.findAllByOrderByReleaseYearDesc();

}

public List<Song> getSongsByTitleAsc() {

    return songRepository.findAllByOrderByTitleAsc();

}

public List<Song> getSongsByTitleDesc() {

    return songRepository.findAllByOrderByTitleDesc();

}
public Page<Song> getSongsByPage(int page) {
    Pageable pageable = PageRequest.of(page , 4);
    return songRepository.findAll(pageable);
}
public List<Song> getTopRatedSongs() {
    return songRepository.findByRatingGreaterThanEqual(4);
}
public long getTotalSongs() {
    return songRepository.count();
}

public long getFavoriteSongsCount() {
    return songRepository.countByFavoriteTrue();
}

public long getTotalArtists() {
    return songRepository.countDistinctArtists();
}

public long getTotalGenres() {
    return songRepository.countDistinctGenres();
}
public long getTotalAlbums() {
    return songRepository.countDistinctAlbums();
}
public Double getAverageRating() {

    Double average = songRepository.getAverageRating();

    if (average == null) {
        return 0.0;
    }

    return Math.round(average * 10.0) / 10.0;

}
}
