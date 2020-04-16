package project;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class AuthorizationService {
    private AuthorizationService(){}

    private final static String SECRET = "HmLZ0n34rYUIiQi0BM3E";
    private final static Algorithm ALGORITHM_HMAC = Algorithm.HMAC256(SECRET);
    private final static String ISSUER = "SE2Project";
    private final static JWTVerifier VERIFIER_HMAC =
            JWT.require(ALGORITHM_HMAC)
            .withIssuer(ISSUER).build();

    private final static String USERNAME_CLAIM_STRING = "username";
    private final static String ACCESS_CLAIM_STRING = "access";
    private final static long TOKEN_EXPIRY_TIME = 1 * 60 * 1000; // 1 minute

    public static String generateToken(User user){
        String privilege = getAccessString(user);
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(USERNAME_CLAIM_STRING, user.getUsername())
                .withClaim(ACCESS_CLAIM_STRING, privilege)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRY_TIME))
                .sign(ALGORITHM_HMAC);
    }

    public static Privilege verifyToken(String token){
        DecodedJWT jwt;
        try {
            jwt = VERIFIER_HMAC.verify(token);
        }
        catch (JWTVerificationException exc){
            return null;
        }
        if (jwt.getExpiresAt() == null)
            return null;

        Claim usernameClaim = jwt.getClaim(USERNAME_CLAIM_STRING);
        Claim accessClaim = jwt.getClaim(ACCESS_CLAIM_STRING);

        if (usernameClaim.isNull() || accessClaim.isNull())
            return null;

        return new Privilege(
                usernameClaim.asString(),
                getAccessEnum(accessClaim.asString())
        );
    }

    private static String getAccessString(User user){
        if (user instanceof Administrator){
            return "admin";
        }
        return "local";
    }

    private static AccessType getAccessEnum(String p){
        switch (p){
            case "admin":
                return AccessType.ADMIN;
            case "local":
                return AccessType.LOCAL;
            default:
                return AccessType.LOCAL;
        }
    }
}
