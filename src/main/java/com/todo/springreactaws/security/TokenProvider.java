package com.todo.springreactaws.security;

import com.todo.springreactaws.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY= "F1RPX30pMqDbiAkmlfArbrmVkDD4RqISskGZmBFax5o260GVXzXXWUZTR5JyskiHMIV9M10icegkpi46AdvrcX1E6CmTUBC6IFbTPiD";
    public String create(UserEntity userEntity) {
        Date expiryDate = Date.from(Instant.now()
                .plus(1, ChronoUnit.DAYS));

        /*
            { // header
            "alg":"HS512"
            }.
            { // payload
            "sub":"40288093784915d201784916a40c0001",
            "iss":  "demo app",
            "iat":1595733657,
            "exp":1596597657
            }.
            // SECRET_KEY를 이용해 서명한 부분
            Nn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNrXdJJNWkRv50_17bPLQPwhMobT4vBOG6Q3JYjhDrKF1BSaUxZog
         */
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512,  SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }
    public String validateAndGetUserId(String token) {
        // parseClaimsJws 메서드가 Base 64로 디코딩 및 파싱.
        // 즉,  헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명 후,   token의 서명과 비교.
        // 위조되지 않았다면 페이로드(Claims) 리턴,  위조라면 예외를 날림
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
