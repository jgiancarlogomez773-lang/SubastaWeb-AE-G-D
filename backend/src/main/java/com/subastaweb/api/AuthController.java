package com.subastaweb.api;

import com.subastaweb.api.dto.LoginRequest;
import com.subastaweb.api.dto.RegisterRequest;
import com.subastaweb.api.dto.UserResponse;
import com.subastaweb.application.AuthService;
import com.subastaweb.domain.User;
import com.subastaweb.security.SessionLoginService;
import com.subastaweb.security.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final SessionLoginService sessionLogin;

    public AuthController(AuthService authService, SessionLoginService sessionLogin) {
        this.authService = authService;
        this.sessionLogin = sessionLogin;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request.fullName(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request,
                              HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        User user = authService.authenticate(request.email(), request.password());
        SessionUser sessionUser = new SessionUser(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
        sessionLogin.establish(sessionUser, httpRequest, httpResponse);
        return UserResponse.from(sessionUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication,
                                       HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal SessionUser user) {
        return UserResponse.from(user);
    }
}
