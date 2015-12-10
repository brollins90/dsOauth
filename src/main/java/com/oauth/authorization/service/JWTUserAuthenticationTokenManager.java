package com.oauth.authorization.service;

import com.oauth.authorization.domain.User;
import com.oauth.fakebookApplication.model.implementation.FakebookUser;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.joda.time.DateTime;

public class JWTUserAuthenticationTokenManager implements UserAuthenticationTokenManager {

    public static final String tokenKeyString = "Blake<3LovesMatt_xoxox!";

    @Override
    public String generateAuthToken(User user) {

        byte[] key = tokenKeyString.getBytes();

        String compact = Jwts.builder().claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .setIssuer("dist-systems-oauth").setIssuedAt(DateTime.now().toDate())
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
    public User getUserFromToken(String authToken) {
        User user = new User();

        try {
            Jwt s = Jwts.parser().setSigningKey(tokenKeyString.getBytes())
                    .parse(authToken);

            DefaultClaims ss = (DefaultClaims) s.getBody();
            user.setUsername((String) ss.get("username"));
            user.setEmail((String) ss.get("email"));

        } catch (SignatureException e) {
            System.out.println("bad jwt");
            e.printStackTrace();
        }

        return user;
    }
}
