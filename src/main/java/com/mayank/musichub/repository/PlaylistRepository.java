package com.mayank.musichub.repository;

import com.mayank.musichub.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

 List<Playlist> findByNameContainingIgnoreCase(String keyword);
 List<Playlist> findAllByOrderByNameAsc();

List<Playlist> findAllByOrderByNameDesc();

}