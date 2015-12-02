package com.oauth.fakebookApplication.model.implementation;

import com.oauth.fakebookApplication.model.UserAuthenticationTokenManager;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.joda.time.DateTime;

public class JWTUserAuthenticationTokenManager implements UserAuthenticationTokenManager {

    public static final String tokenKeyString = "Blake<3LovesMatt_xoxox!";

    @Override
    public String generateAuthToken(FakebookUser user) {

        byte[] key = tokenKeyString.getBytes();

        String compact = Jwts.builder().claim("username", user.getName())
                .claim("email", user.getEmail())
                .claim("userId", user.getUserId()).setSubject("Auth-Token").setIssuer("capstone.com").setIssuedAt(DateTime.now().toDate())
                .signWith(SignatureAlgorithm.HS256, key).compact();

        return compact;
    }

    @Override
    public boolean validateAuthToken(String authToken) {
        boolean isValid = false;
        try {
            Jwt s = Jwts.parser().setSigningKey(tokenKeyString.getBytes())
                    .parse(authToken);
            DefaultClaims ss = (DefaultClaims) s.getBody();
            DateTime issuedAt = new DateTime(ss.getIssuedAt());
            if (issuedAt.getMillis() < DateTime.now().minusDays(5).getMillis()) {
                isValid = false;
                System.out.println("token expired");
            } else {
                isValid = true;
                System.out.println("token still good");
            }

        } catch (SignatureException e) {
            System.out.println("bad jwt");
            e.printStackTrace();
        }
        return isValid;
    }

    @Override
    public FakebookUser getUserFromToken(String authToken) {
        FakebookUser user = new FakebookUser();

        try {
            Jwt s = Jwts.parser().setSigningKey(tokenKeyString.getBytes())
                    .parse(authToken);

            DefaultClaims ss = (DefaultClaims) s.getBody();
            user.setName((String) ss.get("username"));
            user.setEmail((String) ss.get("email"));
            user.setUserId((int) ss.get("userId"));

        } catch (SignatureException e) {
            System.out.println("bad jwt");
            e.printStackTrace();
        }

        return user;
    }
}
