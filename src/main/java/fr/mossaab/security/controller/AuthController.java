package fr.mossaab.security.controller;

import fr.mossaab.security.dto.request.AuthenticationRequest;
import fr.mossaab.security.dto.request.RefreshTokenRequest;
import fr.mossaab.security.dto.request.RegisterRequest;
import fr.mossaab.security.dto.request.ResetPasswordRequest;
import fr.mossaab.security.dto.response.AuthenticationResponse;
import fr.mossaab.security.dto.response.RefreshTokenResponse;
import fr.mossaab.security.service.AuthenticationService;
import fr.mossaab.security.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.regex.Pattern;


@Tag(name = "Аутентификация", description = "API для работы с аутентификацией пользователей")
@RestController
@RequestMapping("/authentication")
@SecurityRequirements()
@RequiredArgsConstructor
public class AuthController {
    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$", Pattern.CASE_INSENSITIVE);
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Регистрация пользователя", description = "Позволяет новому пользователю зарегистрироваться в системе.")
    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> register(@RequestPart RegisterRequest request, @RequestPart(required = false) MultipartFile image) throws IOException, ParseException {
        authenticationService.register(request, image);
        return ResponseEntity.ok().body("Код активации для активации аккаунта успешно отправлен на почтовый адрес");
    }


    @Operation(summary = "Активация пользователя", description = "Позволяет отправить код активации для регистрации.")
    @GetMapping("/activate/{code}")
    public ResponseEntity<Object> activateUser(@PathVariable String code) {
        authenticationService.activateUser(String.valueOf(code));
        return new ResponseEntity<>("Пользователь успешно зарегистрирован", HttpStatus.OK);

    }

    @Operation(summary = "Вход пользователя", description = "Этот endpoint позволяет пользователю войти в систему.")
    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authenticationResponse.getJwtCookie())
                .header(HttpHeaders.SET_COOKIE, authenticationResponse.getRefreshTokenCookie())
                .body("Вход в систему пользователя успешно совершен");
    }

    @Operation(summary = "Отправить повторный код активации", description = "Этот endpoint позволяет отправить повторный код активации пользователю.")
    @PostMapping("/resend-activation-code")
    public ResponseEntity<Object> resendActivationCode(@RequestBody Map<String, String> requestBody) throws ParseException {
        authenticationService.resendActivationCode(requestBody.get("email"));
        return new ResponseEntity<>("Код подтверждения аккаунта успешно отправлен на почту", HttpStatus.OK);
    }


    @Operation(summary = "Обновление токена", description = "Этот endpoint позволяет обновить токен.")
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(request));
    }

    @Operation(summary = "Обновление токена через куки", description = "Этот endpoint позволяет обновить токен с использованием куки.")
    @PostMapping("/refresh-token-cookie")
    public ResponseEntity<Void> refreshTokenCookie(HttpServletRequest request) {
        return authenticationService.refreshTokenUsingCookie(request);
    }


    @Operation(summary = "Выход из системы", description = "Этот endpoint позволяет пользователю выйти из системы.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        return authenticationService.logout(request);
    }


    @Operation(summary = "Запрос на смену пароля", description = "Этот endpoint отправляет код активации на почту пользователя.")
    @PostMapping("/request-password-reset")
    public ResponseEntity<Object> requestPasswordReset(@RequestBody Map<String, String> requestBody) {
        authenticationService.requestPasswordReset(requestBody.get("email"));
        return new ResponseEntity<>("Код для смены пароля успешно отправлен", HttpStatus.OK);
    }

    @Operation(summary = "Смена пароля", description = "Этот endpoint позволяет сменить пароль пользователя с использованием кода активации.")
    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request) {
        return authenticationService.resetPassword(request);
    }
}
