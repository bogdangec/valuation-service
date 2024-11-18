package co.quest.xms.valuation.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;

public final class TestUtils {
    private TestUtils() {
    }

    public static String loadJsonFromFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return copyToString(resource.getInputStream(), UTF_8);
    }
}
