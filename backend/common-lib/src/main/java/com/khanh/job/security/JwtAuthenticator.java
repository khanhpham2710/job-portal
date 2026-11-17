package com.khanh.job.security;

import com.khanh.job.config.AppUserPrincipal;
import com.khanh.job.config.CommonAppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticator {

  private final SecretKey key;
  private final String issuer;

  public JwtAuthenticator(CommonAppProperties.Jwt jwt) {
    CommonAppProperties.Token accessTokenConfig = jwt.getAccess();

    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenConfig.getSecret()));
    this.issuer = accessTokenConfig.getIssuer();
  }

  public Authentication authenticate(String token) {
    try {
      Claims claims =
          Jwts.parser()
              .verifyWith(key)
              .requireIssuer(issuer)
              .build()
              .parseSignedClaims(token)
              .getPayload();

      if (JwtClaims.TYPE_REFRESH.equals(claims.get(JwtClaims.TYPE, String.class))) {
        throw new BadCredentialsException("Refresh token is not allowed here");
      }

      String userId = claims.get(JwtClaims.USER_ID, String.class);
      if (userId == null || userId.isBlank()) {
        throw new BadCredentialsException("Token has no userId");
      }

      AppUserPrincipal principal =
          new AppUserPrincipal(UUID.fromString(userId), claims.get(JwtClaims.EMAIL, String.class));

      return UsernamePasswordAuthenticationToken.authenticated(
          principal, null, toAuthorities(claims.get(JwtClaims.ROLES, List.class)));
    } catch (JwtException | IllegalArgumentException ex) {
      // IllegalArgumentException gồm cả UUID.fromString lỗi
      throw new BadCredentialsException("Invalid or expired token", ex);
    }
  }

  private static List<GrantedAuthority> toAuthorities(@Nullable List<?> roles) {
    if (roles == null) {
      return List.of();
    }
    return roles.stream()
        .map(r -> (GrantedAuthority) new SimpleGrantedAuthority(String.valueOf(r)))
        .toList();
  }
}
