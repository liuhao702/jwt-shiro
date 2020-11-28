package code.util;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 存token，传递
 */
@Data
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    // 秘钥
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }

}
