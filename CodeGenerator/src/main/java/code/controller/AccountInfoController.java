package code.controller;

import code.entity.UserDataEntity;
import code.service.UserDataService;
import code.util.JwtToken;
import code.util.JwtUtil;
import code.util.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
public class AccountInfoController {

    @Autowired
    UserDataService userDataService;
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/account-info")
    public String toIndex(Model model){
       model.addAttribute("msg","helloWord");
        return "成功------";
    }

    @GetMapping("/add")
    @RequiresRoles(value={"admin1", "user"}, logical= Logical.OR)
    @RequiresPermissions("user:add1a")
    public String toAdd(Model model){
        model.addAttribute("msg","helloWord");
        return "index2";
    }
    @RequiresPermissions("user:update")
    @RequiresRoles(value={"admin", "root1"}, logical= Logical.OR)
    @GetMapping("/update")
    public String toUpdate(Model model){
        model.addAttribute("msg","helloWord");
        return "index3";
    }
    @GetMapping("/toLogin")
    public String toLogin(Model model){
        model.addAttribute("msg","helloWord");
        return "登录";
    }

    @GetMapping("/tologins")
    public R tologins(String userName , String passWord, Model model){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户登录的数据
        UsernamePasswordToken tokenUser =  new UsernamePasswordToken(userName,passWord);
        try {
            subject.login(tokenUser);
            tokenUser.setRememberMe(true);
            Map<String, Object>map=new HashMap<>();
            String token=JwtUtil.generateToken(userName,passWord);
            map.put("msg","登录成功");
            map.put("data",tokenUser);
            map.put("token", JwtUtil.generateToken(userName,passWord));
            return R.ok(map);
        }catch (UnknownAccountException e){
            return R.error("用户名错误");
        }catch (IncorrectCredentialsException e){
            return R.error("密码错误");
        }
    }

    @GetMapping("/login")
    public R login(String userName , String passWord,Model model){
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(passWord)) {
            return R.error("账户或密码不能为空");
        }
//        QueryWrapper<UserDataEntity> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_name",userName)
//                .eq("user_pwd",passWord); //查询拼接条件
        UserDataEntity user = userDataService.login(userName,passWord);//查询是否存在该用户
        if (user == null) {
            return R.error("账户或密码错误");
        }
        try {
            Map<String, Object>map=new HashMap<>();
            map.put("msg","登录成功");
            map.put("data",user);
            String token=JwtUtil.generateToken(userName,passWord);
            redisTemplate.opsForValue().set(JwtUtil.CLAIM_NAME+"_"+token,token,JwtUtil.TOKENTIME, TimeUnit.MILLISECONDS);
            redisTemplate.opsForValue().set(JwtUtil.REDIS_USERNAME+"_"+user.getUserName(),user,JwtUtil.TOKENTIME, TimeUnit.MILLISECONDS);
            map.put("token",redisTemplate.opsForValue().get(JwtUtil.CLAIM_NAME+"_"+token));
            return R.ok(map);
        }catch (UnknownAccountException e){
            return R.error("用户名错误");
        }catch (IncorrectCredentialsException e){
            return R.error("密码错误");
        }
    }

    @ResponseBody
//    @ExceptionHandler(UnauthorizedException.class)
    @RequestMapping("/auth")
    public R auth(){
        return R.error(401,"未授权");
    }

    @RequestMapping("/getUser/{id}")
    public R update(@PathVariable("id") Long id){
        UserDataEntity byId = userDataService.getById(id);
        return R.ok();
    }

}
