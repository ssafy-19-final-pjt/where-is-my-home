package com.ssafy.home.global.auth;

import com.ssafy.home.domain.user.dto.response.TokenResponse;
import com.ssafy.home.domain.user.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // openssl rand -hex 32
    @Value("{jwt.secretKey:test-where-is-my-home}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        String secret = Base64.getEncoder().encodeToString(this.secret.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Date createAccessTokenExpireTime(Date now){
        return new Date(now.getTime() + (1000L * 60));
    }

    private Date createRefreshTokenExpireTime(Date now){
        return new Date(now.getTime() + (1000L * 60 *60));
    }

    public TokenResponse createJwtTokenResponse(Member m) {
        return TokenResponse.builder()
                .accessToken(createAccessToken(m))
                .refreshToken(createRefreshToken(m.getId()))
                .build();
    }

    public String createAccessToken(Member m) {
        String token = "";

        Claims claims = Jwts.claims()
                .add("id", m.getId())
                .add("name",m.getName())
                .build();

        Date now = new Date();

        token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)//발행시간
                .expiration(createAccessTokenExpireTime(now))//만료시간 1분
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        return token;
    }

    public String createRefreshToken(Long id) {
        String token = "";

        Claims claims = Jwts.claims()
                .add("id", id)
                .build();

        Date now = new Date();

        token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)//발행시간
                .expiration(createRefreshTokenExpireTime(now))//만료시간 60분
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        return token;
    }

    // Jwt Token의 유효성 및 만료 기간 검사합니다
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts
                    .parser().verifyWith(this.secretKey).build()
                    .parseSignedClaims(jwtToken);
            // parseClaimsJws가 만료시간을 체크해서 확인할 필요 없음
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public Long getInfoId(String token) {
        return parseJson(token).getLong("id");
    }

    public String getInfoName(String accessToken) {
        return parseJson(accessToken).getString("name");
    }


    private JSONObject parseJson(String token){
        Claims claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();
        return new JSONObject(claims);
    }
}
