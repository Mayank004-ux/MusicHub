package com.mayank.musichub.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import com.mayank.musichub.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {

    List<Song> findByArtist(String artist);
    List<Song> findByTitle(String title);
    List<Song> findByAlbum(String album);
    List<Song> findByTitleAndArtist(String title, String artist);
    List<Song> findByArtistContainingIgnoreCase(String artist);
    List<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(
        String title,
        String artist
);
List<Song> findByFavoriteTrue();
List<Song> findByGenreIgnoreCase(String genre);
List<Song> findByArtistIgnoreCase(String artist);
List<Song> findAllByOrderByRatingDesc();

List<Song> findAllByOrderByReleaseYearDesc();

List<Song> findAllByOrderByTitleAsc();

List<Song> findAllByOrderByTitleDesc();

List<Song> findTop5ByOrderByIdDesc();

Page<Song> findAll(Pageable pageable);

List<Song> findByRatingGreaterThanEqual(Integer rating);

long countByFavoriteTrue();

@Query("SELECT COUNT(DISTINCT s.artist) FROM Song s")
long countDistinctArtists();

@Query("SELECT COUNT(DISTINCT s.genre) FROM Song s")
long countDistinctGenres();

@Query("SELECT COUNT(DISTINCT s.album) FROM Song s")
long countDistinctAlbums();

@Query("SELECT AVG(s.rating) FROM Song s")
Double getAverageRating();
Song findTopByOrderByRatingDesc();

Song findTopByOrderByReleaseYearDesc();

@Query("""
SELECT s.genre, COUNT(s)
FROM Song s
GROUP BY s.genre
ORDER BY COUNT(s) DESC
""")
List<Object[]> getGenreStatistics();

@Query("""
SELECT s.artist, COUNT(s)
FROM Song s
GROUP BY s.artist
ORDER BY COUNT(s) DESC
""")
List<Object[]> getArtistStatistics();

List<Song> findTop4ByGenreIgnoreCaseAndIdNot(String genre, Integer id);

Page<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCase(
        String title,
        String artist,
         String album,
        Pageable pageable
);

}