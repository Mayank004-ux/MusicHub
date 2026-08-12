package com.mayank.musichub.service;

import com.mayank.musichub.model.Song;
import com.mayank.musichub.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
public int getFavoriteSongCount() {

    return songRepository.findByFavoriteTrue().size();

}
public double getFavoriteAverageRating() {

    List<Song> favorites = songRepository.findByFavoriteTrue();

    if (favorites.isEmpty()) {
        return 0;
    }

    double total = 0;
    int count = 0;

    for (Song song : favorites) {

        if (song.getRating() != null) {

            total += song.getRating();
            count++;

        }

    }

    if (count == 0) {
        return 0;
    }

    return Math.round((total / count) * 100.0) / 100.0;

}
public String getFavoriteTotalDuration() {

    List<Song> favorites = songRepository.findByFavoriteTrue();

    int totalSeconds = 0;

    for (Song song : favorites) {

        if (song.getDuration() != null &&
                song.getDuration().contains(":")) {

            String[] parts = song.getDuration().split(":");

            totalSeconds += Integer.parseInt(parts[0]) * 60;
            totalSeconds += Integer.parseInt(parts[1]);

        }

    }

    int minutes = totalSeconds / 60;
    int seconds = totalSeconds % 60;

    return minutes + " min " + seconds + " sec";

}
public Map<String, Integer> getFavoriteLanguageStats() {

    List<Song> favorites = songRepository.findByFavoriteTrue();

    Map<String, Integer> stats = new HashMap<>();

    for (Song song : favorites) {

        if (song.getLanguage() != null &&
            !song.getLanguage().isBlank()) {

            stats.put(
                song.getLanguage(),
                stats.getOrDefault(song.getLanguage(), 0) + 1
            );

        }

    }

    return stats;

}
public int getTotalSongCount() {

    return songRepository.findAll().size();

}
public List<Song> getRecentSongs() {

    return songRepository.findTop5ByOrderByIdDesc();

}
public Map<String, Long> getGenreStatistics() {

    List<Object[]> results = songRepository.getGenreStatistics();

    Map<String, Long> stats = new LinkedHashMap<>();

    for (Object[] row : results) {

        stats.put(
                (String) row[0],
                (Long) row[1]
        );

    }

    return stats;
}
public Map<String, Long> getArtistStatistics() {

    List<Object[]> results = songRepository.getArtistStatistics();

    Map<String, Long> stats = new LinkedHashMap<>();

    for (Object[] row : results) {

        stats.put(
                (String) row[0],
                (Long) row[1]
        );

    }

    return stats;
}
public Song getHighestRatedSong() {
    return songRepository.findTopByOrderByRatingDesc();
}

public Song getLatestSong() {
    return songRepository.findTopByOrderByReleaseYearDesc();
}

public double getFavoritePercentage() {

    long total = songRepository.count();

    if(total == 0){
        return 0;
    }

    return (songRepository.countByFavoriteTrue() * 100.0) / total;
}
public String getMostPopularGenre() {

    Map<String, Long> genres = getGenreStatistics();

    if(genres.isEmpty()){
        return "N/A";
    }

    return genres.keySet().iterator().next();
}
public List<Song> getRecommendedSongs(Integer id) {

    Song song = getSongById(id);

    if (song == null) {
        return List.of();
    }

    return songRepository.findTop4ByGenreIgnoreCaseAndIdNot(
            song.getGenre(),
            id
    );

}
public Page<Song> searchSongsByPage(String keyword, int page) {

    Pageable pageable = PageRequest.of(page, 4);

    return songRepository
            .findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCase(
                    keyword,
                    keyword,
                    keyword,
                    pageable);
}
}
