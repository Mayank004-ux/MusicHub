package com.mayank.musichub.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs = new ArrayList<>();

    public Playlist() {
    }

    public Playlist(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    public double getAverageRating() {

    if (songs == null || songs.isEmpty()) {
        return 0;
    }

    double total = 0;
    int count = 0;

    for (Song song : songs) {

        if (song.getRating() != null) {

            total += song.getRating();
            count++;

        }

    }

    if (count == 0) {
        return 0;
    }

    return total / count;

}
public String getTotalDuration() {

    if (songs == null || songs.isEmpty()) {
        return "0 min";
    }

    int totalSeconds = 0;

    for (Song song : songs) {

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
}