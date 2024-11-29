package co.quest.xms.valuation.api;

import co.quest.xms.valuation.api.dto.AuthResponse;
import co.quest.xms.valuation.api.dto.LoginRequest;
import co.quest.xms.valuation.api.dto.RegisterRequest;
import co.quest.xms.valuation.api.mapper.UserMapper;
import co.quest.xms.valuation.application.service.TokenService;
import co.quest.xms.valuation.domain.model.User;
import co.quest.xms.valuation.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static co.quest.xms.valuation.api.mapper.UserMapper.toDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        User registeredUser = userService.register(request.username(), request.email(), request.password());
        String jwt = tokenService.generateToken(registeredUser);
        return ResponseEntity.ok(new AuthResponse (jwt, toDto(registeredUser)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        User authenticatedUser = userService.authenticate(request.username(), request.password());
        String jwt = tokenService.generateToken(authenticatedUser);
        return ResponseEntity.ok(new AuthResponse (jwt, toDto(authenticatedUser)));
    }
}

