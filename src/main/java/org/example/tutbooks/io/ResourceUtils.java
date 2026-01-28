package org.example.tutbooks.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceUtils {
    private ResourceUtils() {
    }

    public static String readResource(ClassLoader loader, String path) throws IOException {
        try (InputStream stream = loader.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
