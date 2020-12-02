package code.controller.common;

import code.util.R;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class NoPermissionException {

    /**
     * 无权限访问
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public R handleShiroException(Exception ex) {

        return R.error(401,"未授权");
    }

    /**
     * 认证失败，异常
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public R AuthorizationException(Exception ex) {
        return R.error(402,"权限认证失败");
    }
}
