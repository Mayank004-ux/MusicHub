package com.mayank.musichub;

import com.mayank.musichub.model.Song;
import com.mayank.musichub.repository.SongRepository;
import com.mayank.musichub.service.AudioStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MusicHubAudioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AudioStorageService audioStorageService;

    private byte[] createMockMp3Bytes() {
        byte[] bytes = new byte[64];
        bytes[0] = 0x49; // 'I'
        bytes[1] = 0x44; // 'D'
        bytes[2] = 0x33; // '3'
        bytes[3] = 0x03;
        return bytes;
    }

    @Test
    void testExistingSongsArePreserved() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("songs"));

        mockMvc.perform(get("/songs"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDownloadNonExistentSongReturns404() throws Exception {
        mockMvc.perform(get("/song/999999/download"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/songs/999999/download"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDownloadSongWithoutAudioReturns404() throws Exception {
        // Create song without audio file
        Song song = new Song();
        song.setTitle("Acoustic Demo");
        song.setArtist("Indie Artist");
        song.setAlbum("Unplugged");
        song.setGenre("Folk");
        song.setLanguage("English");
        song.setRating(4);
        song.setReleaseYear(2023);
        Song saved = songRepository.save(song);

        try {
            mockMvc.perform(get("/song/" + saved.getId() + "/download"))
                    .andExpect(status().isNotFound());

            mockMvc.perform(get("/songs/" + saved.getId() + "/download"))
                    .andExpect(status().isNotFound());
        } finally {
            songRepository.delete(saved);
        }
    }

    @Test
    void testUploadAudioAndDownloadSuccess() throws Exception {
        MockMultipartFile audioFile = new MockMultipartFile(
                "audioFile",
                "test_track.mp3",
                "audio/mpeg",
                createMockMp3Bytes()
        );

        // Upload new song with audio file via multipart form
        mockMvc.perform(multipart("/song/save")
                        .file(audioFile)
                        .param("title", "Integration Test Song")
                        .param("artist", "Test Artist")
                        .param("album", "Test Album")
                        .param("genre", "Rock")
                        .param("language", "English")
                        .param("rating", "5")
                        .param("releaseYear", "2024")
                        .param("duration", "3:30")
                        .param("spotifyUrl", "https://open.spotify.com/track/4cOdK2wGLETKBW3PvgPWqT")
                        .param("youtubeUrl", "https://www.youtube.com/watch?v=kJQP7kiw5Fk"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Retrieve the created song
        Song created = songRepository.findByTitle("Integration Test Song").stream()
                .findFirst()
                .orElse(null);

        assertNotNull(created, "Created song should exist in repository");
        assertTrue(created.hasAudioFile(), "Song should have audioStorageKey populated");
        assertEquals("test_track.mp3", created.getAudioFileName());

        try {
            // Verify Song Details page renders download button
            mockMvc.perform(get("/song/" + created.getId()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("song-details"))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("Download (")));

            // Test Download endpoint
            mockMvc.perform(get("/song/" + created.getId() + "/download"))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, org.hamcrest.Matchers.containsString("attachment")))
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, org.hamcrest.Matchers.containsString("Integration")))
                    .andExpect(content().contentTypeCompatibleWith("audio/mpeg"));

            // Test Stream endpoint
            mockMvc.perform(get("/song/" + created.getId() + "/stream"))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline"))
                    .andExpect(content().contentTypeCompatibleWith("audio/mpeg"));

            // Test Updating metadata retains existing audio file
            mockMvc.perform(multipart("/song/update/" + created.getId())
                            .param("title", "Integration Test Song Updated")
                            .param("artist", "Test Artist")
                            .param("album", "Test Album")
                            .param("genre", "Rock")
                            .param("language", "English")
                            .param("rating", "5")
                            .param("releaseYear", "2024"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/song/" + created.getId()));

            Song updated = songRepository.findById(created.getId()).orElseThrow();
            assertEquals("Integration Test Song Updated", updated.getTitle());
            assertTrue(updated.hasAudioFile(), "Audio file should be retained when no new file is uploaded");
            assertEquals(created.getAudioStorageKey(), updated.getAudioStorageKey());

        } finally {
            // Clean up
            songRepository.findById(created.getId()).ifPresent(s -> {
                if (s.getAudioStorageKey() != null) {
                    audioStorageService.deleteAudioFile(s.getAudioStorageKey());
                }
                songRepository.delete(s);
            });
        }
    }

    @Test
    void testRejectInvalidStreamingUrls() throws Exception {
        // Attempt to save a song with unsafe javascript: URL
        mockMvc.perform(multipart("/song/save")
                        .param("title", "XSS Attempt")
                        .param("artist", "Bad Actor")
                        .param("album", "Album")
                        .param("genre", "Rock")
                        .param("language", "English")
                        .param("rating", "1")
                        .param("releaseYear", "2024")
                        .param("spotifyUrl", "javascript:alert('xss')"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-song"))
                .andExpect(model().hasErrors());
    }
}
