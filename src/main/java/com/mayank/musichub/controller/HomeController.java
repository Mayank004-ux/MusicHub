package com.mayank.musichub.controller;

import com.mayank.musichub.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mayank.musichub.model.Song;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {

    @Autowired
    private SongService songService;

    @GetMapping("/")
public String home(

        @RequestParam(defaultValue = "0") int page,

        Model model) {

    Page<Song> songPage = songService.getSongsByPage(page);

    model.addAttribute("songs", songPage.getContent());

    model.addAttribute("currentPage", page);

    model.addAttribute("totalPages", songPage.getTotalPages());

    // Dashboard Statistics
    model.addAttribute("totalSongs", songService.getTotalSongs());

    model.addAttribute("favoriteSongs", songService.getFavoriteSongsCount());

    model.addAttribute("totalArtists", songService.getTotalArtists());

    model.addAttribute("totalGenres", songService.getTotalGenres());

    model.addAttribute("totalAlbums", songService.getTotalAlbums());

    model.addAttribute("averageRating", songService.getAverageRating());

    model.addAttribute("activePage", "home");

    model.addAttribute("totalSongs",songService.getTotalSongCount());

    model.addAttribute( "recentSongs",songService.getRecentSongs());

    return "index";
}
    @GetMapping("/song/{id}")
public String songDetails(@PathVariable Integer id,
                          Model model) {

    Song song = songService.getSongById(id);

    model.addAttribute("song", song);

    return "song-details";
}
@GetMapping("/song/edit/{id}")
public String editSongPage(@PathVariable Integer id,
                           Model model) {

    Song song = songService.getSongById(id);

    model.addAttribute("song", song);

    return "edit-song";
}
@PostMapping("/song/update/{id}")
public String updateSong(@PathVariable Integer id,
                         @ModelAttribute Song song) {

    songService.updateSong(id, song);

    return "redirect:/";
}
@PostMapping("/song/delete/{id}")
public String deleteSong(@PathVariable Integer id) {

    songService.deleteSong(id);

    return "redirect:/";
}
@GetMapping("/song/add")
public String addSongPage(Model model){

    model.addAttribute("song", new Song());

    return "add-song";

}
@PostMapping("/song/save")
public String saveSong(@ModelAttribute Song song){

    songService.saveSong(song);

    return "redirect:/";

}
@GetMapping("/favorites")
public String showFavoriteSongs(Model model) {

    model.addAttribute("songs", songService.getFavoriteSongs());
   model.addAttribute( "favoriteCount", songService.getFavoriteSongCount());
   model.addAttribute( "averageRating", songService.getFavoriteAverageRating());
   model.addAttribute("favoriteDuration",songService.getFavoriteTotalDuration());
   model.addAttribute("languageStats",songService.getFavoriteLanguageStats());

    return "favorites";

}
@GetMapping("/genre/{genre}")
public String showSongsByGenre(@PathVariable String genre, Model model) {

    model.addAttribute("songs", songService.getSongsByGenre(genre));

    model.addAttribute("genre", genre);

    return "genre";

}
@GetMapping("/artist/{artist}")
public String showSongsByArtist(@PathVariable String artist, Model model) {

    model.addAttribute("songs", songService.getSongsByArtistName(artist));

    model.addAttribute("artist", artist);

    return "artist";

}
@GetMapping("/sort")
public String sortSongs(
        @RequestParam(required = false) String type,
        Model model) {

    if ("rating".equals(type)) {

        model.addAttribute("songs", songService.getSongsByRating());

    } else if ("year".equals(type)) {

        model.addAttribute("songs", songService.getSongsByReleaseYear());

    } else if ("titleAsc".equals(type)) {

        model.addAttribute("songs", songService.getSongsByTitleAsc());

    } else if ("titleDesc".equals(type)) {

        model.addAttribute("songs", songService.getSongsByTitleDesc());

    } else {

        model.addAttribute("songs", songService.getAllSongs());

    }

    model.addAttribute("sortType", type);

    return "sort";
}
@GetMapping("/sort/rating")
public String sortByRating(Model model) {

    model.addAttribute("songs", songService.getSongsByRating());

    model.addAttribute("sortType", "⭐ Rating (Highest First)");

    return "sort";

}
@GetMapping("/sort/year")
public String sortByYear(Model model) {

    model.addAttribute("songs", songService.getSongsByReleaseYear());

    model.addAttribute("sortType", "📅 Release Year (Newest First)");

    return "sort";

}
@GetMapping("/sort/title-asc")
public String sortByTitleAsc(Model model) {

    model.addAttribute("songs", songService.getSongsByTitleAsc());

    model.addAttribute("sortType", "🔤 Title (A-Z)");

    return "sort";

}
@GetMapping("/sort/title-desc")
public String sortByTitleDesc(Model model) {

    model.addAttribute("songs", songService.getSongsByTitleDesc());

    model.addAttribute("sortType", "🔤 Title (Z-A)");

    return "sort";

}
@GetMapping("/top-rated")
public String topRatedSongs(Model model) {

    model.addAttribute("songs", songService.getTopRatedSongs());

    model.addAttribute("activePage", "top-rated");

    return "top-rated";
}
@GetMapping("/add-song")
public String addSongForm(Model model) {

    model.addAttribute("song", new Song());

    model.addAttribute("activePage", "add-song");

    return "add-song";
}
@GetMapping("/analytics")
public String analytics(Model model) {

    model.addAttribute("activePage", "analytics");

    model.addAttribute("totalSongs", songService.getTotalSongCount());

    model.addAttribute("favoriteSongs", songService.getFavoriteSongCount());

    model.addAttribute("totalArtists", songService.getTotalArtists());

    model.addAttribute("totalGenres", songService.getTotalGenres());

    model.addAttribute("totalAlbums", songService.getTotalAlbums());

    model.addAttribute("averageRating", songService.getAverageRating());
    
    model.addAttribute("recentSongs", songService.getRecentSongs());

    model.addAttribute("favoriteAverageRating", songService.getFavoriteAverageRating());

model.addAttribute("favoriteDuration",songService.getFavoriteTotalDuration());

model.addAttribute( "languageStats",songService.getFavoriteLanguageStats());

    return "analytics";

}
}