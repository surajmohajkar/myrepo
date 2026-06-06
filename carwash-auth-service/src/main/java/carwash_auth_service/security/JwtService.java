package carwash_auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRETE_KEY= "mySuperSecretKeyForCarWashApplicationJwtSecurity2026";

    public String generateToken(String email, String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(SignatureAlgorithm.HS256,SECRETE_KEY)
                .compact();
    }

    public String extractEmail(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRETE_KEY)
                .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(SECRETE_KEY)
                    .parseClaimsJws(token);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}
