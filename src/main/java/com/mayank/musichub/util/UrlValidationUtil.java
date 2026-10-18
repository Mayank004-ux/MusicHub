package com.mayank.musichub.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Utility for validating streaming service URLs and preview links.
 * Enforces HTTPS scheme and domain restrictions to prevent unsafe schemes
 * (such as javascript:, file:, data:) and untrusted hosts.
 */
public final class UrlValidationUtil {

    private static final Set<String> SPOTIFY_HOSTS = Set.of(
            "open.spotify.com",
            "spotify.com"
    );

    private static final Set<String> YOUTUBE_HOSTS = Set.of(
            "www.youtube.com",
            "youtube.com",
            "youtu.be",
            "m.youtube.com",
            "music.youtube.com"
    );

    private static final Set<String> APPLE_MUSIC_HOSTS = Set.of(
            "music.apple.com",
            "itunes.apple.com"
    );

    private UrlValidationUtil() {
    }

    /**
     * Checks whether the supplied URL is a valid HTTPS URL.
     */
    public static boolean isValidHttpsUrl(String urlString) {
        if (urlString == null || urlString.isBlank()) {
            return false;
        }
        try {
            URI uri = new URI(urlString.trim());
            String scheme = uri.getScheme();
            String host = uri.getHost();
            return "https".equalsIgnoreCase(scheme) && host != null && !host.isBlank();
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Validates that the URL is a legitimate Spotify URL using HTTPS.
     */
    public static boolean isSpotifyUrl(String urlString) {
        if (!isValidHttpsUrl(urlString)) {
            return false;
        }
        try {
            URI uri = new URI(urlString.trim());
            String host = uri.getHost().toLowerCase();
            return SPOTIFY_HOSTS.contains(host) || host.endsWith(".spotify.com");
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Validates that the URL is a legitimate YouTube URL using HTTPS.
     */
    public static boolean isYouTubeUrl(String urlString) {
        if (!isValidHttpsUrl(urlString)) {
            return false;
        }
        try {
            URI uri = new URI(urlString.trim());
            String host = uri.getHost().toLowerCase();
            return YOUTUBE_HOSTS.contains(host) || host.endsWith(".youtube.com");
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Validates that the URL is a legitimate Apple Music URL using HTTPS.
     */
    public static boolean isAppleMusicUrl(String urlString) {
        if (!isValidHttpsUrl(urlString)) {
            return false;
        }
        try {
            URI uri = new URI(urlString.trim());
            String host = uri.getHost().toLowerCase();
            return APPLE_MUSIC_HOSTS.contains(host) || host.endsWith(".apple.com");
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Validates an external audio preview URL (must use HTTPS, have a valid host, and not use prohibited schemes).
     */
    public static boolean isValidPreviewUrl(String urlString) {
        if (urlString == null || urlString.isBlank()) {
            return false;
        }
        return isValidHttpsUrl(urlString);
    }
}
