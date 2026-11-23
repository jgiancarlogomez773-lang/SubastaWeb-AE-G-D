package com.subastaweb.domain;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTypeTest {

    @Test
    void detectsJpeg() {
        byte[] jpeg = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0};
        assertThat(ImageType.detect(jpeg)).contains(ImageType.JPEG);
    }

    @Test
    void detectsPng() {
        byte[] png = {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0};
        assertThat(ImageType.detect(png)).contains(ImageType.PNG);
    }

    @Test
    void detectsWebp() {
        byte[] webp = "RIFF....WEBPVP8 ".getBytes(StandardCharsets.US_ASCII);
        assertThat(ImageType.detect(webp)).contains(ImageType.WEBP);
    }

    @Test
    void rejectsOtherContent() {
        assertThat(ImageType.detect("GIF89a".getBytes(StandardCharsets.US_ASCII))).isEmpty();
        assertThat(ImageType.detect("texto".getBytes(StandardCharsets.UTF_8))).isEmpty();
        assertThat(ImageType.detect(new byte[0])).isEmpty();
        assertThat(ImageType.detect(null)).isEmpty();
    }
}
