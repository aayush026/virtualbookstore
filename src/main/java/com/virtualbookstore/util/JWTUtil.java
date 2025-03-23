package com.virtualbookstore.util;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	private String secretKey="ovbPSSMEoColkrhm8NbS2b3vZLtD2gj4vvMSbd1J+ew=";
	
	public String extractUserName(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token,Function<Claims,T> claimsResolver) {
		final Claims claims=extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(getSigningKey()) // Use a proper signing key
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}

	private Key getSigningKey() {
	    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
	    return Keys.hmacShaKeyFor(keyBytes);
	}
	 
	 public String generateToken(String username) {
	        return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour validity
	                .signWith(SignatureAlgorithm.HS256, secretKey)
	                .compact();
	    }
	 

	 public boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUserName(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }
	    
	    public boolean isTokenValid(String token, String username) {
	        final String extractedUsername = extractUserName(token);
	        return (extractedUsername.equals(username) && !isTokenExpired(token));
	    }
}
