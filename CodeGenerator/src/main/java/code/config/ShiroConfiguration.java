package code.config;

import code.util.JwtInterceptor;
import code.util.JwtRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@Slf4j
public class ShiroConfiguration {

    private static final String JWT_FILTER_NAME = "jwt";

    /**
     * 配置securityManager 管理subject（默认）,并把自定义realm交由manager.
     */
    @Bean("securityManager")
    public SecurityManager securityManager(JwtRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        /*
         * 关闭Shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        securityManager.setRealm(realm);
        return securityManager;
    }

    /**
     * 注入安全过滤器.
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
            @Value("${lc.web.security.anonymous.file}") String anonFile,
            @Value("${lc.web.security.anonymous.url}") String anonUrl) {
        String[] anonFiles = null;
        String[] anonUrls = null;
        if (!StringUtils.isEmpty(anonFile)) {
            anonFiles = anonFile.split(",");
        }
        if (!StringUtils.isEmpty(anonUrl)) {
            anonUrls = anonUrl.split(",");
        }
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setFilters(filterMap());
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(definitionMap(anonFiles, anonUrls));
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/auth");
        shiroFilterFactoryBean.setSuccessUrl("/account-info");
        return shiroFilterFactoryBean;
    }
 
    /**
     * 自定义拦截器，处理所有请求.
     */
    private Map<String, Filter> filterMap() {
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(JWT_FILTER_NAME,  new JwtInterceptor());
        return filterMap;
    }

    /**
     * 拦截器.过滤静态文件，登录
     */
    private Map<String, String> definitionMap(String[] anonFiles, String[] anonUrls) {
        Map<String, String> definitionMap = new LinkedHashMap<>();
//        Map<String, String> definitionMap = new HashMap<>();
        if (anonFiles != null) {
                for (String file : anonFiles) {
                    definitionMap.put("/**/*" + file, "anon");
                }
            }
            if (anonUrls != null) {
                for (String url : anonUrls) {
                    definitionMap.put(url, "anon");
                }
        }
        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
//        definitionMap.put("/login", "anon");
        definitionMap.put("/**", JWT_FILTER_NAME);
//        definitionMap.put("/add", "perms[user:add]");
//        definitionMap.put("/update", "perms[user:update]");
        //所有的请求都要经过授权认证
        log.error(definitionMap+"---------------------------------------------------");
        return definitionMap;
}

    /**
     * 开启注解.
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        //  强制使用cglib代理，防止和aop冲突
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean("authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor advisor(DefaultSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}