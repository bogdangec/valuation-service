package co.quest.xms.valuation.api;

import co.quest.xms.valuation.api.dto.*;
import co.quest.xms.valuation.application.service.TokenService;
import co.quest.xms.valuation.application.service.UserService;
import co.quest.xms.valuation.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static co.quest.xms.valuation.api.mapper.UserMapper.toDto;

@CrossOrigin
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
        return ResponseEntity.ok(new AuthResponse(jwt, toDto(registeredUser)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        User authenticatedUser = userService.authenticate(request.email(), request.password());
        String jwt = tokenService.generateToken(authenticatedUser);
        return ResponseEntity.ok(new AuthResponse(jwt, toDto(authenticatedUser)));
    }

    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request, @RequestHeader("x-user-id") String userId) {
        userService.changePassword(userId, request.currentPassword(), request.newPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/username")
    public ResponseEntity<UserDto> updateUsername(@RequestBody @Valid ChangeUsernameRequest request, @RequestHeader("x-user-id") String userId) {
        User user = userService.updateUsername(userId, request.newUsername());
        return ResponseEntity.ok(toDto(user));

    }
}

