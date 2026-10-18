package com.mayank.musichub.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UrlValidationUtilTest {

    @Test
    void testValidSpotifyUrls() {
        assertTrue(UrlValidationUtil.isSpotifyUrl("https://open.spotify.com/track/4cOdK2wGLETKBW3PvgPWqT"));
        assertTrue(UrlValidationUtil.isSpotifyUrl("https://spotify.com/track/12345"));
    }

    @Test
    void testInvalidSpotifyUrls() {
        // HTTP instead of HTTPS
        assertFalse(UrlValidationUtil.isSpotifyUrl("http://open.spotify.com/track/12345"));
        // Wrong host
        assertFalse(UrlValidationUtil.isSpotifyUrl("https://malicious-spotify.com/track/12345"));
        // Unsafe schemes
        assertFalse(UrlValidationUtil.isSpotifyUrl("javascript:alert(1)"));
        assertFalse(UrlValidationUtil.isSpotifyUrl("file:///C:/secrets.txt"));
        assertFalse(UrlValidationUtil.isSpotifyUrl(null));
        assertFalse(UrlValidationUtil.isSpotifyUrl(""));
    }

    @Test
    void testValidYouTubeUrls() {
        assertTrue(UrlValidationUtil.isYouTubeUrl("https://www.youtube.com/watch?v=kJQP7kiw5Fk"));
        assertTrue(UrlValidationUtil.isYouTubeUrl("https://youtube.com/watch?v=kJQP7kiw5Fk"));
        assertTrue(UrlValidationUtil.isYouTubeUrl("https://youtu.be/kJQP7kiw5Fk"));
        assertTrue(UrlValidationUtil.isYouTubeUrl("https://music.youtube.com/watch?v=kJQP7kiw5Fk"));
    }

    @Test
    void testInvalidYouTubeUrls() {
        assertFalse(UrlValidationUtil.isYouTubeUrl("http://www.youtube.com/watch?v=123"));
        assertFalse(UrlValidationUtil.isYouTubeUrl("https://fake-youtube.com/watch?v=123"));
        assertFalse(UrlValidationUtil.isYouTubeUrl("javascript:void(0)"));
        assertFalse(UrlValidationUtil.isYouTubeUrl(null));
    }

    @Test
    void testValidPreviewUrls() {
        assertTrue(UrlValidationUtil.isValidPreviewUrl("https://audio-ssl.itunes.apple.com/preview.mp3"));
        assertTrue(UrlValidationUtil.isValidPreviewUrl("https://cdn.example.com/audio/preview.mp3"));
    }

    @Test
    void testInvalidPreviewUrls() {
        assertFalse(UrlValidationUtil.isValidPreviewUrl("http://insecure.com/preview.mp3"));
        assertFalse(UrlValidationUtil.isValidPreviewUrl("javascript:alert('xss')"));
        assertFalse(UrlValidationUtil.isValidPreviewUrl("file:///etc/passwd"));
        assertFalse(UrlValidationUtil.isValidPreviewUrl(null));
        assertFalse(UrlValidationUtil.isValidPreviewUrl("   "));
    }
}
