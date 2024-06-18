package com.test.mallapi.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class JWTUtil {

    /*
        JWTUtil은 JWT 문자열 생성을 위해 generateToken(), 검증을 위해 validateToken() 메서드를 작성한다.
        생성 시에 필요한 암호키를 지정해야 하는데, 길이가 짧으면 문제가 생기므로 30자 이상의 문자열을 지정하는 것을 권장.
     */
    private static String key = "1234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> valueMap, int min) {
        SecretKey key = null;

        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();

        return jwtStr;
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try {
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)  //  파싱 및 검증, 실패 시 에러
                    .getBody();
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJWTException("MalFormed");
        }catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException("Expired");
        }catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("Invalid");
        }catch (JwtException jwtException) {
            throw new CustomJWTException("JWTError");
        }catch (Exception e) {
            throw new CustomJWTException("Error");
        }

        return claim;
    }
}
