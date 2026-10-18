package com.mayank.musichub.service;

import com.mayank.musichub.model.Song;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Service managing authorized music file storage, validation, retrieval, and deletion.
 * Uploaded audio files are stored in a dedicated directory outside public static resources.
 */
@Service
public class AudioStorageService {

    @Value("${app.audio.upload-dir:uploads/audio}")
    private String uploadDir;

    @Value("${app.audio.max-file-size:52428800}") // Default 50 MB
    private long maxFileSize;

    private Path storageDirectory;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("mp3", "wav", "m4a");

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "audio/mpeg",
            "audio/mp3",
            "audio/wav",
            "audio/wave",
            "audio/x-wav",
            "audio/mp4",
            "audio/x-m4a",
            "audio/m4a",
            "audio/aac"
    );

    @PostConstruct
    public void init() {
        try {
            this.storageDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(this.storageDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize audio storage directory at: " + uploadDir, e);
        }
    }

    public Path getStorageDirectory() {
        return storageDirectory;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Storage result metadata.
     */
    public record AudioStorageResult(
            String storageKey,
            String originalFileName,
            String contentType,
            long fileSize
    ) {}

    /**
     * Validates an uploaded audio file for non-emptiness, size limit, supported extension,
     * reported MIME type, and magic bytes / audio signature.
     */
    public void validateAudioFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty audio file.");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(String.format(
                    "Audio file exceeds maximum allowed size of %d MB (file size: %.2f MB).",
                    maxFileSize / (1024 * 1024),
                    file.getSize() / (1024.0 * 1024.0)
            ));
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Audio file must have a valid filename.");
        }

        String extension = getFileExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported audio format ." + extension + 
                    ". Supported formats are: " + String.join(", ", ALLOWED_EXTENSIONS).toUpperCase(Locale.ROOT));
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.isBlank()) {
            String normalizedMime = contentType.toLowerCase(Locale.ROOT).trim();
            if (!ALLOWED_MIME_TYPES.contains(normalizedMime) && !normalizedMime.startsWith("audio/")) {
                throw new IllegalArgumentException("Invalid audio content type: " + contentType);
            }
        }

        // Validate file signature / magic bytes
        validateMagicBytes(file, extension);
    }

    /**
     * Validates magic bytes for MP3, WAV, and M4A audio formats.
     */
    private void validateMagicBytes(MultipartFile file, String extension) {
        byte[] header = new byte[16];
        int bytesRead;
        try (InputStream is = file.getInputStream()) {
            bytesRead = is.read(header);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read audio file header for format validation.", e);
        }

        if (bytesRead < 4) {
            throw new IllegalArgumentException("Malformed audio file: insufficient header bytes.");
        }

        switch (extension) {
            case "mp3" -> {
                // ID3 tag: "ID3" (0x49, 0x44, 0x33) OR MPEG Sync Frame: first byte 0xFF and second byte & 0xE0 == 0xE0
                boolean isId3 = (header[0] == 0x49 && header[1] == 0x44 && header[2] == 0x33);
                boolean isSyncFrame = ((header[0] & 0xFF) == 0xFF && (header[1] & 0xE0) == 0xE0);
                if (!isId3 && !isSyncFrame) {
                    throw new IllegalArgumentException("Invalid MP3 file: missing valid MP3/ID3 audio signature.");
                }
            }
            case "wav" -> {
                // "RIFF" at offset 0 and "WAVE" at offset 8
                if (bytesRead < 12) {
                    throw new IllegalArgumentException("Malformed WAV file: header too short.");
                }
                boolean isRiff = (header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46);
                boolean isWave = (header[8] == 0x57 && header[9] == 0x41 && header[10] == 0x56 && header[11] == 0x45);
                if (!isRiff || !isWave) {
                    throw new IllegalArgumentException("Invalid WAV file: missing valid RIFF/WAVE header.");
                }
            }
            case "m4a" -> {
                // "ftyp" at offset 4
                if (bytesRead < 8) {
                    throw new IllegalArgumentException("Malformed M4A file: header too short.");
                }
                boolean isFtyp = (header[4] == 0x66 && header[5] == 0x74 && header[6] == 0x79 && header[7] == 0x70);
                if (!isFtyp) {
                    throw new IllegalArgumentException("Invalid M4A file: missing valid MP4/M4A ftyp signature.");
                }
            }
        }
    }

    /**
     * Stores an authorized audio file safely and returns the storage result metadata.
     */
    public AudioStorageResult storeAudioFile(MultipartFile file) throws IOException {
        validateAudioFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String safeStorageKey = UUID.randomUUID().toString() + "." + extension;

        Path targetLocation = resolveSafePath(safeStorageKey);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        String determinedContentType = file.getContentType();
        if (determinedContentType == null || determinedContentType.isBlank()) {
            determinedContentType = switch (extension) {
                case "mp3" -> "audio/mpeg";
                case "wav" -> "audio/wav";
                case "m4a" -> "audio/mp4";
                default -> "application/octet-stream";
            };
        }

        return new AudioStorageResult(
                safeStorageKey,
                sanitizeOriginalFilename(originalFilename),
                determinedContentType,
                file.getSize()
        );
    }

    /**
     * Loads an audio file as a Spring Resource, verifying the path is within the storage directory.
     */
    public Resource loadAudioAsResource(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            throw new IllegalArgumentException("Storage key must not be empty.");
        }

        Path filePath = resolveSafePath(storageKey);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Audio file not found or not readable: " + storageKey);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error resolving audio file: " + storageKey, e);
        }
    }

    /**
     * Deletes an audio file from disk safely.
     */
    public boolean deleteAudioFile(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return false;
        }
        try {
            Path filePath = resolveSafePath(storageKey);
            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            // Log and return false without failing the outer operation
            System.err.println("Failed to delete audio file: " + storageKey + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Resolves a storage key to a Path, ensuring it is strictly inside the configured storage directory
     * to prevent path traversal vulnerabilities.
     */
    public Path resolveSafePath(String filenameOrKey) {
        if (filenameOrKey == null || filenameOrKey.contains("..") || filenameOrKey.contains("/") || filenameOrKey.contains("\\")) {
            throw new SecurityException("Path traversal or illegal character detected in storage key: " + filenameOrKey);
        }
        Path resolved = storageDirectory.resolve(filenameOrKey).normalize().toAbsolutePath();
        if (!resolved.startsWith(storageDirectory.toAbsolutePath())) {
            throw new SecurityException("Access denied: path is outside configured storage directory.");
        }
        return resolved;
    }

    /**
     * Generates a safe, readable download filename based on song artist and title metadata.
     */
    public String generateSafeDownloadFilename(Song song) {
        String ext = "mp3";
        if (song.getAudioStorageKey() != null) {
            ext = getFileExtension(song.getAudioStorageKey());
        } else if (song.getAudioFileName() != null) {
            ext = getFileExtension(song.getAudioFileName());
        }

        String artist = (song.getArtist() != null) ? song.getArtist().trim() : "MusicHub";
        String title = (song.getTitle() != null) ? song.getTitle().trim() : "Track";

        String baseName = artist + " - " + title;
        // Keep alphanumeric, spaces, hyphens, underscores
        String sanitized = baseName.replaceAll("[^a-zA-Z0-9 \\-_]", "").trim();
        if (sanitized.isBlank()) {
            sanitized = "downloaded_track";
        }
        // Collapse consecutive spaces
        sanitized = sanitized.replaceAll("\\s+", " ");
        return sanitized + "." + ext;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT).trim();
    }

    private String sanitizeOriginalFilename(String filename) {
        if (filename == null) return "audio_track";
        // Strip path if client sent full path
        String clean = Paths.get(filename).getFileName().toString();
        // Remove potentially dangerous characters
        return clean.replaceAll("[^a-zA-Z0-9._\\- ]", "_");
    }
}
