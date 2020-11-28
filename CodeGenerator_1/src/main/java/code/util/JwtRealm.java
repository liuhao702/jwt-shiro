package code.util;

import code.entity.UserDataEntity;
import code.service.UserDataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.rmi.ServerError;
import java.util.concurrent.TimeUnit;

/**
 *jwtRealm认证
 */
@Component
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    UserDataService userDataService;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 设置realm支持的authenticationToken类型.
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return null != token && token instanceof JwtToken;
    }


    public Object getRedisToken(String token){
        Object redisToken =redisTemplate.opsForValue().get(JwtUtil.CLAIM_NAME+"_"+token);
        if (redisToken!=null&&!"".equals(redisToken)){
            log.info("token存在!");
            redisTemplate.expire(JwtUtil.CLAIM_NAME+"_"+token,JwtUtil.TOKENTIME , TimeUnit.MILLISECONDS);
            redisTemplate.expire(JwtUtil.REDIS_USERNAME+"_"+JwtUtil.getUsername(redisToken.toString()),JwtUtil.TOKENTIME , TimeUnit.MILLISECONDS);
            return redisToken;
        }
        return "NoToken";
    }

    /**
     * 登陆认证.身份认证
     *
     * @return 登陆信息
     * @throws AuthenticationException 未登陆抛出异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        System.err.println(authenticationToken.getPrincipal()+"qqqqqqqqqqq");
        String token = (String) authenticationToken.getCredentials();
        Object tokenStr= getRedisToken(token);
        if (tokenStr.equals("NoToken")){
            log.error("token不存在，或已失效!");
            new AuthenticationException("token不存在，或已失效!");
        }
        // 根据token获取用户名称
        verifyUser(tokenStr.toString());
        return createAuthenticationInfo(tokenStr.toString());
    }

    /**
     * 授权认证.
     */
    @Override

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 根据token获取用户名称
        Object tokenStr= getRedisToken(principals.toString());
        if (tokenStr.equals("NoToken")){
            log.error("token不存在，或已失效!");
            new AuthenticationException("token不存在，或已失效!");
        }
        String userName = JwtUtil.getUsername(tokenStr.toString());
        QueryWrapper<UserDataEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",userName); //查询拼接条件
        UserDataEntity user = userDataService.getOne(wrapper);//查询是否存在该用户
        return createAuthorizationInfo(user);
    }

    /**
     * 验证token是否有效
     * @param token
     * @return
     */
    private UserDataEntity verifyUser(String token) {
        // 根据token获取权限授权
        String username = JwtUtil.getUsername(token); //根据token获取用户名称
        if (username == null) { //验证用户名称是否为空
            throw new AccountException("Token invalid");
        }
        UserDataEntity user =(UserDataEntity)redisTemplate.opsForValue().get(JwtUtil.REDIS_USERNAME+"_"+username);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }
//        if (!JwtUtil.verifyToken(username, user.getUserPwd(), token)) {
//            throw new UnknownAccountException("Username or password error");
//       }
        return user;
    }
//    private UserDataEntity verifyUser(String token) {
//        // 根据token获取权限授权
//        String username = JwtUtil.getUsername(token); //根据token获取用户名称
//        if (username == null) { //验证用户名称是否为空
//            throw new AccountException("Token invalid");
//        }
//        QueryWrapper<UserDataEntity> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_name",username); //查询拼接条件
//        UserDataEntity user = userDataService.getOne(wrapper);//查询是否存在该用户
//        if (user == null) {
//            throw new AuthenticationException("User didn't existed!");
//        }
//        if (!JwtUtil.verifyToken(username, user.getUserPwd(), token)) {
//            throw new UnknownAccountException("Username or password error");
//        }
//        return user;
//    }

    private SimpleAuthenticationInfo createAuthenticationInfo(String token) {
        if (token==null){
            new AuthenticationException("token为空!");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(token, token, getName());
        return authenticationInfo;
    }

    private SimpleAuthorizationInfo createAuthorizationInfo(UserDataEntity user) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermission(user.getPerms());
        return authorizationInfo;
    }
}
