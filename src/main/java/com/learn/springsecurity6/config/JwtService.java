package com.learn.springsecurity6.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // https://www.allkeysgenerator.com/
    // required minimum 256 bit
    private static final String SECRET_KEY = "6E3272357538782F413F4428472B4B6250655367566B59703373367639792442";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); //subject should be username
    }

    // extract single claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        //without extra claims
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims, //will contain claim or the extra claim that we want to add
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) //means, when the claim was created and this information will help us to calculate
                    //  the expiration date or to check if the token is still valid
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // how long this token should be valid
                .signWith(getSigiInKey(), SignatureAlgorithm.HS256)
                .compact(); // is the one that will generate and return the token
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) ;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigiInKey()) //is a secret that is used to digitally sign the JWT,
                // signing key used to create the signature part of the JWT
                // which is used to verify that the sender of JWT is who it claims to be and
                // ensure that the message wasn't change along the way
                // sign algorithm
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigiInKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte); //algorithm signing key
    }
}
