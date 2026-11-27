package com.subastaweb.application;

import com.subastaweb.domain.DomainException;
import com.subastaweb.domain.ErrorCode;
import com.subastaweb.domain.FieldIssue;
import com.subastaweb.domain.Role;
import com.subastaweb.domain.User;
import com.subastaweb.persistence.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    /** BCrypt solo usa los primeros 72 bytes de la contraseña. */
    private static final int BCRYPT_MAX_BYTES = 72;

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final String dummyHash;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
        // Hash de relleno para que el login tarde igual exista o no el correo.
        this.dummyHash = encoder.encode("relleno-para-igualar-tiempos");
    }

    /** HU-01 / RN-01: el alta pública siempre crea USER, sin importar lo que envíe el cliente. */
    @Transactional
    public User register(String fullName, String email, String rawPassword) {
        String normalized = normalizeEmail(email);
        String name = fullName.trim();
        List<FieldIssue> issues = new ArrayList<>();
        if (name.length() < 2 || name.length() > 100) {
            issues.add(new FieldIssue("fullName", "El nombre debe tener entre 2 y 100 caracteres."));
        }
        if (tooLong(rawPassword)) {
            issues.add(new FieldIssue("password", "La contraseña es demasiado larga."));
        }
        if (!issues.isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Los datos enviados no son válidos.", issues);
        }
        if (users.existsByEmail(normalized)) {
            throw emailTaken();
        }
        try {
            return users.saveAndFlush(
                    new User(name, normalized, encoder.encode(rawPassword), Role.USER));
        } catch (DataIntegrityViolationException e) {
            throw emailTaken();
        }
    }

    /** HU-02: valida credenciales. No distingue entre correo inexistente y contraseña errónea. */
    @Transactional(readOnly = true)
    public User authenticate(String email, String rawPassword) {
        Optional<User> user = users.findByEmail(normalizeEmail(email));
        String hash = user.map(User::getPasswordHash).orElse(dummyHash);
        boolean matches = !tooLong(rawPassword) && encoder.matches(rawPassword, hash);
        if (user.isEmpty() || !matches) {
            throw new DomainException(ErrorCode.INVALID_CREDENTIALS, "Correo o contraseña incorrectos.");
        }
        return user.get();
    }

    /** Solo para el procedimiento controlado de arranque (AdminBootstrap). Devuelve true si lo creó. */
    @Transactional
    public boolean createAdminIfAbsent(String fullName, String email, String rawPassword) {
        String normalized = normalizeEmail(email);
        if (rawPassword.length() < 8 || tooLong(rawPassword)) {
            throw new IllegalArgumentException("ADMIN_PASSWORD debe tener entre 8 y 72 bytes.");
        }
        if (users.existsByEmail(normalized)) {
            return false;
        }
        users.save(new User(fullName.trim(), normalized, encoder.encode(rawPassword), Role.ADMIN));
        return true;
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean tooLong(String rawPassword) {
        return rawPassword.getBytes(StandardCharsets.UTF_8).length > BCRYPT_MAX_BYTES;
    }

    private static DomainException emailTaken() {
        return new DomainException(ErrorCode.EMAIL_ALREADY_REGISTERED, "Ya existe una cuenta con ese correo.");
    }
}
