package com.mayank.musichub.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Title is required")
@Size(max = 100, message = "Title must be under 100 characters")
private String title;
    
    @NotBlank(message = "Artist is required")
private String artist;

    @NotBlank(message = "Album is required")
private String album;

    private String imageUrl;

    @NotBlank(message = "Genre is required")
private String genre;

   @NotBlank(message = "Language is required")
private String language;

   @NotNull(message = "Rating is required")
@Min(value = 1, message = "Minimum rating is 1")
@Max(value = 5, message = "Maximum rating is 5")
private Integer rating;

    private String duration;

   @NotNull(message = "Release year is required")
@Min(value = 1900, message = "Invalid year")
@Max(value = 2100, message = "Invalid year")
private Integer releaseYear;

    private String youtubeUrl;

    
    @Column(columnDefinition = "TEXT")
    private String spotifyUrl;

    private Boolean favorite = false;
    
    
    @Column(columnDefinition = "TEXT")
private String lyrics;

    public Song() {
    }

    public Song(Integer id, String title, String artist , String album) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getAlbum() {
    return album;
}

public void setAlbum(String album) {
    this.album = album;
}
public String getImageUrl() {
    return imageUrl;
}

public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
}
public String getGenre() {
    return genre;
}

public void setGenre(String genre) {
    this.genre = genre;
}
public String getLanguage() {
    return language;
}

public void setLanguage(String language) {
    this.language = language;
}
public Integer getRating() {
    return rating;
}
public void setRating(Integer rating) {
    this.rating = rating;
}
public String getDuration() {
    return duration;
}
public void setDuration(String duration) {
    this.duration = duration;
}
public Integer getReleaseYear() {
    return releaseYear;
}
public void setReleaseYear(Integer releaseYear) {
    this.releaseYear = releaseYear;
}
public String getYoutubeUrl() {
    return youtubeUrl;
}
public void setYoutubeUrl(String youtubeUrl) {
    this.youtubeUrl = youtubeUrl;
}

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }
}