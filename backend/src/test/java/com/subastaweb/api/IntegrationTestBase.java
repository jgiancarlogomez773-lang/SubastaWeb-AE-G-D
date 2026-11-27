package com.subastaweb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subastaweb.domain.Category;
import com.subastaweb.domain.Role;
import com.subastaweb.domain.User;
import com.subastaweb.persistence.AuctionImageRepository;
import com.subastaweb.persistence.AuctionRepository;
import com.subastaweb.persistence.CategoryRepository;
import com.subastaweb.persistence.UserRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class IntegrationTestBase {

    static final Path UPLOADS = createUploadsDir();

    private static Path createUploadsDir() {
        try {
            return Files.createTempDirectory("subastaweb-test-uploads");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.storage.dir", UPLOADS::toString);
    }

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper json;
    @Autowired
    protected UserRepository users;
    @Autowired
    protected AuctionRepository auctions;
    @Autowired
    protected AuctionImageRepository auctionImages;
    @Autowired
    protected CategoryRepository categories;
    @Autowired
    protected PasswordEncoder encoder;

    @BeforeEach
    void cleanDatabaseAndUploads() throws IOException {
        // Orden por llaves foráneas: imágenes -> subastas -> usuarios -> categorías (el borrado en lote es inmediato).
        auctionImages.deleteAllInBatch();
        auctions.deleteAllInBatch();
        users.deleteAllInBatch();
        categories.deleteAllInBatch();
        categories.save(new Category("Arte", "arte", true));
        categories.save(new Category("Hogar", "hogar", true));
        try (Stream<Path> files = Files.list(UPLOADS)) {
            for (Path file : files.toList()) {
                Files.deleteIfExists(file);
            }
        }
    }

    protected User createUser(String email, String password, Role role) {
        return users.save(new User("Usuario Demo", email, encoder.encode(password), role));
    }

    protected String jsonOf(String... keyValues) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put(keyValues[i], keyValues[i + 1]);
        }
        return json.writeValueAsString(map);
    }

    protected MockHttpSession login(String email, String password) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf("email", email, "password", password)))
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    protected long uploadsCount() throws IOException {
        try (Stream<Path> files = Files.list(UPLOADS)) {
            return files.count();
        }
    }
}
