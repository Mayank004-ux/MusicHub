package com.mayank.musichub.service;

import com.mayank.musichub.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AudioStorageServiceTest {

    private AudioStorageService audioStorageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        audioStorageService = new AudioStorageService();
        ReflectionTestUtils.setField(audioStorageService, "uploadDir", tempDir.toString());
        ReflectionTestUtils.setField(audioStorageService, "maxFileSize", 10 * 1024 * 1024L); // 10 MB for test
        audioStorageService.init();
    }

    private byte[] createMockMp3Bytes() {
        // "ID3" header (bytes 0x49, 0x44, 0x33, 0x03, 0x00, 0x00, ...)
        byte[] bytes = new byte[64];
        bytes[0] = 0x49; // 'I'
        bytes[1] = 0x44; // 'D'
        bytes[2] = 0x33; // '3'
        bytes[3] = 0x03;
        return bytes;
    }

    private byte[] createMockWavBytes() {
        // "RIFF" .... "WAVE"
        byte[] bytes = new byte[64];
        bytes[0] = 0x52; // 'R'
        bytes[1] = 0x49; // 'I'
        bytes[2] = 0x46; // 'F'
        bytes[3] = 0x46; // 'F'
        bytes[8] = 0x57; // 'W'
        bytes[9] = 0x41; // 'A'
        bytes[10] = 0x56; // 'V'
        bytes[11] = 0x45; // 'E'
        return bytes;
    }

    private byte[] createMockM4aBytes() {
        // .... "ftyp"
        byte[] bytes = new byte[64];
        bytes[4] = 0x66; // 'f'
        bytes[5] = 0x74; // 't'
        bytes[6] = 0x79; // 'y'
        bytes[7] = 0x70; // 'p'
        return bytes;
    }

    @Test
    void testStoreAndLoadValidMp3File() throws IOException {
        MockMultipartFile mp3File = new MockMultipartFile(
                "audioFile",
                "sample_track.mp3",
                "audio/mpeg",
                createMockMp3Bytes()
        );

        AudioStorageService.AudioStorageResult result = audioStorageService.storeAudioFile(mp3File);
        assertNotNull(result);
        assertNotNull(result.storageKey());
        assertTrue(result.storageKey().endsWith(".mp3"));
        assertEquals("sample_track.mp3", result.originalFileName());

        Resource resource = audioStorageService.loadAudioAsResource(result.storageKey());
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void testStoreValidWavFile() throws IOException {
        MockMultipartFile wavFile = new MockMultipartFile(
                "audioFile",
                "acoustic.wav",
                "audio/wav",
                createMockWavBytes()
        );

        AudioStorageService.AudioStorageResult result = audioStorageService.storeAudioFile(wavFile);
        assertNotNull(result);
        assertTrue(result.storageKey().endsWith(".wav"));
    }

    @Test
    void testStoreValidM4aFile() throws IOException {
        MockMultipartFile m4aFile = new MockMultipartFile(
                "audioFile",
                "recording.m4a",
                "audio/mp4",
                createMockM4aBytes()
        );

        AudioStorageService.AudioStorageResult result = audioStorageService.storeAudioFile(m4aFile);
        assertNotNull(result);
        assertTrue(result.storageKey().endsWith(".m4a"));
    }

    @Test
    void testRejectEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "audioFile",
                "empty.mp3",
                "audio/mpeg",
                new byte[0]
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                audioStorageService.validateAudioFile(emptyFile)
        );
        assertTrue(ex.getMessage().contains("empty"));
    }

    @Test
    void testRejectOversizedFile() {
        // Limit set to 10MB in setUp, provide 11MB
        byte[] bigBytes = new byte[11 * 1024 * 1024];
        bigBytes[0] = 0x49; bigBytes[1] = 0x44; bigBytes[2] = 0x33;

        MockMultipartFile bigFile = new MockMultipartFile(
                "audioFile",
                "huge.mp3",
                "audio/mpeg",
                bigBytes
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                audioStorageService.validateAudioFile(bigFile)
        );
        assertTrue(ex.getMessage().contains("maximum allowed size"));
    }

    @Test
    void testRejectUnsupportedExtension() {
        MockMultipartFile exeFile = new MockMultipartFile(
                "audioFile",
                "malware.exe",
                "application/octet-stream",
                new byte[]{0x4D, 0x5A, 0x00, 0x00}
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                audioStorageService.validateAudioFile(exeFile)
        );
        assertTrue(ex.getMessage().contains("Unsupported audio format"));
    }

    @Test
    void testRejectInvalidSignature() {
        // .mp3 extension but containing plain text or random non-audio bytes
        byte[] textBytes = "This is definitely not an MP3 file.".getBytes();
        MockMultipartFile fakeMp3 = new MockMultipartFile(
                "audioFile",
                "fake.mp3",
                "audio/mpeg",
                textBytes
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                audioStorageService.validateAudioFile(fakeMp3)
        );
        assertTrue(ex.getMessage().contains("missing valid MP3/ID3 audio signature"));
    }

    @Test
    void testPathTraversalPrevention() {
        assertThrows(SecurityException.class, () ->
                audioStorageService.resolveSafePath("../outside.mp3")
        );
        assertThrows(SecurityException.class, () ->
                audioStorageService.resolveSafePath("subdir/file.mp3")
        );
        assertThrows(SecurityException.class, () ->
                audioStorageService.resolveSafePath("..\\windows.mp3")
        );
    }

    @Test
    void testSafeDownloadFilenameGeneration() {
        Song song = new Song();
        song.setArtist("Imagine Dragons");
        song.setTitle("Bones");
        song.setAudioStorageKey("random-uuid.mp3");

        String filename = audioStorageService.generateSafeDownloadFilename(song);
        assertEquals("Imagine Dragons - Bones.mp3", filename);

        // Special characters sanitized
        Song complexSong = new Song();
        complexSong.setArtist("AC/DC & Friends!");
        complexSong.setTitle("Rock \"N\" Roll: 2024?");
        complexSong.setAudioStorageKey("uuid.wav");

        String safeComplex = audioStorageService.generateSafeDownloadFilename(complexSong);
        assertEquals("ACDC Friends - Rock N Roll 2024.wav", safeComplex);
    }

    @Test
    void testDeleteAudioFile() throws IOException {
        MockMultipartFile mp3File = new MockMultipartFile(
                "audioFile",
                "delete_me.mp3",
                "audio/mpeg",
                createMockMp3Bytes()
        );
        AudioStorageService.AudioStorageResult result = audioStorageService.storeAudioFile(mp3File);
        assertTrue(audioStorageService.loadAudioAsResource(result.storageKey()).exists());

        boolean deleted = audioStorageService.deleteAudioFile(result.storageKey());
        assertTrue(deleted);
        assertThrows(RuntimeException.class, () ->
                audioStorageService.loadAudioAsResource(result.storageKey())
        );
    }
}
