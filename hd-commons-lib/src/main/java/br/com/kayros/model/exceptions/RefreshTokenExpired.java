package br.com.kayros.model.exceptions;

public class RefreshTokenExpired extends RuntimeException {
  public RefreshTokenExpired(String message) {
    super(message);
  }
}