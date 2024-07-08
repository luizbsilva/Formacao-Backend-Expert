package br.com.kayros.controllers.impl;

import br.com.kayros.controllers.AuthController;
import br.com.kayros.model.request.AuthenticateRequest;
import br.com.kayros.model.request.RefreshTokenRequest;
import br.com.kayros.model.response.AuthenticationResponse;
import br.com.kayros.model.response.RefreshTokenResponse;
import br.com.kayros.security.JWTAuthenticationImpl;
import br.com.kayros.service.RefreshTokenService;
import br.com.kayros.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

  private final JWTUtils jwtUtils;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final RefreshTokenService refreshTokenService;

  @Override
  public ResponseEntity<AuthenticationResponse> authenticate(final AuthenticateRequest request) throws Exception {
    return ResponseEntity.ok().body(
        new JWTAuthenticationImpl(jwtUtils, authenticationConfiguration.getAuthenticationManager())
            .authenticate(request)
            .withRefreshToken(refreshTokenService.save(request.email()).getId())
    );
  }

  @Override
  public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
    return ResponseEntity.ok().body(
        refreshTokenService.refreshToken(request.refreshToken())
    );
  }
}
