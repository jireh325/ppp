package com.example.identity_profile_service.infrastructure.adapter.in.rest.controller;

import com.example.identity_profile_service.application.command.CreateUserCommand;
import com.example.identity_profile_service.application.command.DeactivateUserCommand;
import com.example.identity_profile_service.application.command.UpdateUserProfileCommand;
import com.example.identity_profile_service.application.port.in.UserProfileUseCase;
import com.example.identity_profile_service.application.dto.UserProfileDto;
import com.example.identity_profile_service.application.query.SearchUsersQuery;
import com.example.identity_profile_service.domain.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileUseCase userProfileUseCase;

    // ========== ENDPOINTS PUBLICS ==========

    @PostMapping
    public ResponseEntity<UserProfileDto> createUser(@RequestBody CreateUserCommand command) {
        UserProfileDto createdUser = userProfileUseCase.createUserProfile(command);
        return ResponseEntity.ok(createdUser);
    }

    // ========== ENDPOINTS AUTHENTIFIÉS ==========

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserIdFromJwt(jwt);
        UserProfileDto profile = userProfileUseCase.getCurrentUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateUserProfileCommand command) {

        UUID userId = extractUserIdFromJwt(jwt);
        // Créer une commande avec le userId authentifié
        UpdateUserProfileCommand authenticatedCommand = new UpdateUserProfileCommand(
                userId,
                command.email(),
                command.username(),
                command.firstName(),
                command.lastName(),
                command.urlProfil()
        );

        UserProfileDto updatedProfile = userProfileUseCase.updateUserProfile(authenticatedCommand);
        return ResponseEntity.ok(updatedProfile);
    }

    // ========== ENDPOINTS ADMINISTRATEURS ==========

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        List<UserProfileDto> users = userProfileUseCase.searchUsers(new SearchUsersQuery(null, null, null, null, null, null, null, null));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #userId == authentication.principal.claims['sub']")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable UUID userId) {
        UserProfileDto profile = userProfileUseCase.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #userId == authentication.principal.claims['sub']")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @PathVariable UUID userId,
            @RequestBody UpdateUserProfileCommand command) {

        UpdateUserProfileCommand authenticatedCommand = new UpdateUserProfileCommand(
                userId,
                command.email(),
                command.username(),
                command.firstName(),
                command.lastName(),
                command.urlProfil()
        );

        UserProfileDto updatedProfile = userProfileUseCase.updateUserProfile(authenticatedCommand);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable UUID userId,
            @RequestParam String reason,
            @AuthenticationPrincipal Jwt jwt) {

        UUID adminId = extractUserIdFromJwt(jwt);
        DeactivateUserCommand command = new DeactivateUserCommand(userId, adminId, reason);

        userProfileUseCase.deactivateUser(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<UserProfileDto>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) Integer minReputation,
            @RequestParam(required = false) Integer maxReputation) {

        SearchUsersQuery query = new SearchUsersQuery(
                username, email, status, userType, minReputation, maxReputation, null, null
        );

        List<UserProfileDto> users = userProfileUseCase.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<UserProfileDto>> getUsersByStatus(@PathVariable UserStatus status) {
        List<UserProfileDto> users = userProfileUseCase.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/type/{userType}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<UserProfileDto>> getUsersByType(@PathVariable String userType) {
        List<UserProfileDto> users = userProfileUseCase.getUsersByType(userType);
        return ResponseEntity.ok(users);
    }

    // ========== MÉTHODES HELPER ==========

    private UUID extractUserIdFromJwt(Jwt jwt) {
        // Extraire l'ID utilisateur du JWT
        // Cela dépend de votre configuration Keycloak
        String subject = jwt.getSubject();
        return UUID.fromString(subject);
    }
}