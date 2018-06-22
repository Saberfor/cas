package com.example.casclient3.connect;

import com.example.casclient3.MyModel.MyUsernamePasswordCredential;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class Login  extends AbstractUsernamePasswordAuthenticationHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Login.class);

    public Login(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential transformedCredential, String originalPassword) throws GeneralSecurityException, PreventedException {
        DriverManagerDataSource d=new DriverManagerDataSource();
        d.setDriverClassName("com.mysql.jdbc.Driver");
        d.setUrl("jdbc:mysql://127.0.0.1:3306/gxc");
        d.setUsername("root");
        d.setPassword("");
        JdbcTemplate template=new JdbcTemplate();
        template.setDataSource(d);




        String username=transformedCredential.getUsername();
        String pd=transformedCredential.getPassword();
        System.out.println(username);
        System.out.println(pd);
        //查询数据库加密的的密码
        Map<String,Object> user = template.queryForMap("SELECT `password` FROM app_user WHERE username = ?", transformedCredential.getUsername());

        if(user==null){
            throw new FailedLoginException("没有该用户");
        }

        //返回多属性（暂时不知道怎么用，没研究）
        Map<String, Object> map=new HashMap<>();
        map.put("email", "XXXXX@qq.com");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(transformedCredential.getPassword().equals(user.get("password").toString())){
            return createHandlerResult(transformedCredential, principalFactory.createPrincipal(username, map), null);
        }
        throw new FailedLoginException("Sorry, login attemp failed.");
    }

    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        MyUsernamePasswordCredential mycredential1 = (MyUsernamePasswordCredential) credential;

        String capcha = mycredential1.getCapcha();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String right = attributes.getRequest().getSession().getAttribute("capcha").toString();
        if(!capcha.equalsIgnoreCase(right)){
            System.out.println("验证码错误" + capcha);
            throw new FailedLoginException("验证码错误");
        }

        DriverManagerDataSource d=new DriverManagerDataSource();
        d.setDriverClassName("com.mysql.jdbc.Driver");
        d.setUrl("jdbc:mysql://127.0.0.1:3306/gxc");
        d.setUsername("root");
        d.setPassword("");
        JdbcTemplate template=new JdbcTemplate();
        template.setDataSource(d);




        String username=mycredential1.getUsername();
        //查询数据库加密的的密码
        Map<String,Object> user = template.queryForMap("SELECT `password` FROM app_user WHERE username = ?", mycredential1.getUsername());

        if(user==null){
            throw new FailedLoginException("没有该用户");
        }

        //返回多属性（暂时不知道怎么用，没研究）
        Map<String, Object> map=new HashMap<>();
        map.put("email", "3105747142@qq.com");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(mycredential1.getPassword().equals(user.get("password").toString())){
            return createHandlerResult(mycredential1, principalFactory.createPrincipal(username, map), null);
        }
        throw new FailedLoginException("Sorry, login attemp failed.");
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof MyUsernamePasswordCredential;
    }
}



