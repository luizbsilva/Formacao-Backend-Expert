package br.com.kayros.service;

import br.com.kayros.model.response.RefreshTokenResponse;
import br.com.kayros.models.RefreshToken;

public interface RefreshTokenService {
  RefreshToken save(final String username);

  RefreshTokenResponse refreshToken(final String refreshTokenId);
}
