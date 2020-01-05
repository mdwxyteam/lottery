package com.md.luck.lottery.config.authority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -3301605591108950415L;

    @Value("${jwt.secret}")
    private  String secret = "secret";

    @Value("${jwt.expiration}")
    private Long expiration = 7200000l;
//    private Long expiration = 7200l;


    @Value("${jwt.token}")
    private String tokenHeader = "Authorization";

    private Clock clock = DefaultClock.INSTANCE;

    public String generateToken(SecurityUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }
    public String generateOpenidToken(SecurityUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("openid", userDetails.getOpenid());
        return "Weixin" + doGenerateToken(claims, userDetails.getOpenid());
    }

    public static void main(String[] args) {
        SecurityUserDetails userDetails = new SecurityUserDetails("123", null);
        userDetails.setOpenid("4566578998");
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.generateOpenidToken(userDetails);
        System.out.println(token);
        if (jwtTokenUtil.validateOpenidToken(token, userDetails)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        Map map = jwtTokenUtil.getAllClaimsFromToken(token);
        System.out.println(map.size());
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration);
    }

    /**
     * 检验token userName
     * @param token token
     * @param userDetails userDetailsd
     * @return Boolean
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        SecurityUserDetails user = (SecurityUserDetails) userDetails;
        final String username = getDataFromToken(token);
        return (username.equals(user.getUsername())
                && !isTokenExpired(token)
        );
    }

    /**
     * 检验token openid
     * @param token token
     * @param userDetails userDetailsd
     * @return Boolean
     */
    public Boolean validateOpenidToken(String token, SecurityUserDetails userDetails) {
        SecurityUserDetails user =  userDetails;
        final String openid = getDataFromToken(token);
        return (openid.equals(user.getOpenid())
                && !isTokenExpired(token)
        );
    }

    public String getDataFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
