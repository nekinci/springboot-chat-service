package com.spotify.api.util;

import com.spotify.api.models.User;
import com.spotify.api.models.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final long JWT_TOKEN_VALIDITY = 30*24*60*60; // 30 gün
    public static final String SPOTIFY_ID_CLAIM = "spotify_id";
    public static final String NAME_CLAIM = "name";
    public static final String FAMILY_NAME_CLAIM = "family_name";
    public static final String SPOTIFY_ACCESS_TOKEN_CLAIM = "spotify_access_token";
    public static final String SPOTIFY_REFRESH_TOKEN_CLAIM = "spotify_refresh_token";

    @Value("${jwt.secret}")
    private String secret;


    public String getEmailFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getClaimFromToken(String token, String claimName){
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get(claimName, String.class);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    /*
    * For only custom JWT Implementation not Spotify Access Token.
    * */
    private Boolean isTokenExpired(String token){
        final Date date = getExpirationDateFromToken(token);
        return date.before(new Date());
    }

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put(SPOTIFY_ID_CLAIM, user.getSpotifyId());
        claims.put(NAME_CLAIM, user.getName());
        claims.put(FAMILY_NAME_CLAIM, user.getSurname());
        claims.put(SPOTIFY_ACCESS_TOKEN_CLAIM, user.getUserToken().getToken());
        claims.put(SPOTIFY_REFRESH_TOKEN_CLAIM, user.getUserToken().getRefreshToken());
        return doGenerateToken(claims, user.getEmail());
    }

    public String doGenerateToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, User user){
        final String email = getEmailFromToken(token);
        return (email.equals(user.getEmail()) && !isTokenExpired(token));
    }

    /**
     *
     * If token expired return false, else return true
     * @author Niyazi Ekinci
     * @version 1.0
     *
     * */
    public static boolean checkSpotifyToken(UserToken token){
        if (token.getLastChangeAt() == null) return false;
        return !(token.getLastChangeAt().plusSeconds(token.getExpiresIn()).isBefore(LocalDateTime.now()) || token.getLastChangeAt().plusSeconds(token.getExpiresIn()).isEqual(LocalDateTime.now()));
    }


}

