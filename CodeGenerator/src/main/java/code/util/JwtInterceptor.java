package code.util;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义拦截器
 */
@Slf4j
public class JwtInterceptor extends BasicHttpAuthenticationFilter {

    private static final String AUTH_HEADER = "token"; //token
    private static final String CHARSET = "UTF-8"; //字符编码


    /**
     * 请求是否已经登录.
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String token = httpRequest.getHeader(AUTH_HEADER);
        if (token==null){
            log.error("无权访问，token异常");
            return false;
        }
        if (httpRequest.getRequestURI().contains("auth")){
            return true;
        }
        return executeLogin( request,  response);
    }

    /**
     * 执行登录.
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        String token = WebUtils.toHttp(request).getHeader(AUTH_HEADER); //获取请求头token
         JwtToken jwtToken = new JwtToken(token);
            try {
                this.getSubject(request, response).login(jwtToken); //Subject认证登录操作

                return true;
            } catch (AuthenticationException e) {
                log.info("登录失败", e.getMessage()); //出错处理
                return false;
        }
    }

    /**
     * 构建未授权的请求返回.
     */
    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setContentType("application/json;charset=" + CHARSET);
        httpResponse.setCharacterEncoding(CHARSET);
        R r = R.ok("无权访问，token异常");
        try {
            httpResponse.getWriter().print(JsonUtil.obj2String(r));
        } catch (IOException e) {
            log.error("发送质询时出错", e.getMessage());
        }
        return false;
    }




    /**
     * 对跨域提供支持.
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }


}
