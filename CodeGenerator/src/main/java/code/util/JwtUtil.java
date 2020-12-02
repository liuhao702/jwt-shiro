package code.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成token，验证token
 */
public final class JwtUtil {

    private static final String  ISSUER = "GPDI"; //发行人
    public static final String REDIS_USERNAME = "User"; //签发姓名
    public static final String CLAIM_NAME = "Shiro-Jwt"; //签发姓名
    public static final long TOKENTIME = 60000*60;          // 1分钟=60000毫秒



    /**
     * Generate token with username and password. 创建token
     */
    public static String generateToken(String username, String password) {
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date nowTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        Algorithm algorithm = Algorithm.HMAC256(password);
        return  JWT.create()
                .withHeader(map) // 设置头部信息 Header
                .withIssuer(ISSUER) // 设置签名是有谁生成
                .withIssuedAt(nowTime)
                .withExpiresAt( new Date(nowMillis+TOKENTIME)) //设置过期时间为1分钟
                .withClaim(CLAIM_NAME, username)
                .sign(algorithm);
    }

    /**
     * Verify token.验证合法性
     */
    public static boolean verifyToken(String username, String password, String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        Algorithm algorithm = Algorithm.HMAC256(password);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER).withClaim(CLAIM_NAME, username).build();
        try {
        } catch (JWTVerificationException  e) {
            return false;
        }
        return true;
    }

    /**
     * Return claim. 获取用户名称信息
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(CLAIM_NAME).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}