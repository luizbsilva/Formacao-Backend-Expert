package br.com.kayros.service.impl;

import static java.time.LocalDateTime.now;

import br.com.kayros.model.exceptions.RefreshTokenExpired;
import br.com.kayros.model.exceptions.ResourceNotFoundException;
import br.com.kayros.model.response.RefreshTokenResponse;
import br.com.kayros.models.RefreshToken;
import br.com.kayros.repositories.RefreshTokenRepository;
import br.com.kayros.security.dtos.UserDetailsDTO;
import br.com.kayros.service.RefreshTokenService;
import br.com.kayros.utils.JWTUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

//  @Value("${jwt.expiration-sec.refresh-token}")
//  private Long refreshTokenExpirationSec;

  private final RefreshTokenRepository repository;
  private final UserDetailsService userDetailsService;
  private final JWTUtils jwtUtils;

  @Override
  public RefreshToken save(String username) {
    return repository.save(
        RefreshToken.builder()
            .id(UUID.randomUUID().toString())
            .createdAt(now())
            .expiresAt(now().plusHours(1L))
            .username(username)
            .build()
    );
  }

  @Override
  public RefreshTokenResponse refreshToken(String refreshTokenId) {
    final var refreshToken = repository.findById(refreshTokenId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Refresh token not found. Id: " + refreshTokenId));

    if (refreshToken.getExpiresAt().isBefore(now())) {
      throw new RefreshTokenExpired("Refresh token expired. Id: " + refreshTokenId);
    }

    return new RefreshTokenResponse(
        jwtUtils.generateToken(
            (UserDetailsDTO) userDetailsService.loadUserByUsername(refreshToken.getUsername()))
    );
  }
}
