package com.mayank.musichub.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mayank.musichub.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

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

Page<Song> findAll(Pageable pageable);
}