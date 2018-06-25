package com.example.casclient3.connect;

import com.example.casclient3.MyModel.MyUsernamePasswordCredential;
import org.apache.commons.lang3.BooleanUtils;
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

public class Login  extends AbstractPreAndPostProcessingAuthenticationHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Login.class);

    public Login(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    /*@Override
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
    }*/

    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {

        String passwprd = "";
        String username = "";
        if (credential instanceof MyUsernamePasswordCredential){
            MyUsernamePasswordCredential mycredential1 = (MyUsernamePasswordCredential) credential;
            String capcha = mycredential1.getCapcha();
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String right = attributes.getRequest().getSession().getAttribute("capcha").toString();
            if(!capcha.equalsIgnoreCase(right)){
                System.out.println("验证码错误" + capcha);
                throw new FailedLoginException("验证码错误");
            }
            passwprd = mycredential1.getPassword();
            username = mycredential1.getUsername();
        } else {
            UsernamePasswordCredential credential1 = (UsernamePasswordCredential)credential;
            passwprd = credential1.getPassword();
            username = credential1.getUsername();
        }

        DriverManagerDataSource d=new DriverManagerDataSource();
        d.setDriverClassName("com.mysql.jdbc.Driver");
        d.setUrl("jdbc:mysql://127.0.0.1:3306/gxc");
        d.setUsername("root");
        d.setPassword("");
        JdbcTemplate template=new JdbcTemplate();
        template.setDataSource(d);

        //查询数据库加密的的密码
        Map<String,Object> user = template.queryForMap("SELECT `password` FROM app_user WHERE username = ?", username);

        if(user==null){
            throw new FailedLoginException("没有该用户");
        }

        //返回多属性（暂时不知道怎么用，没研究）
        Map<String, Object> map=new HashMap<>();
        map.put("email", "3105747142@qq.com");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(passwprd.equals(user.get("password").toString())){
            if (credential instanceof MyUsernamePasswordCredential){
                MyUsernamePasswordCredential mycredential1 = (MyUsernamePasswordCredential) credential;
                return createHandlerResult(mycredential1, principalFactory.createPrincipal(username, map), null);
            } else {
            UsernamePasswordCredential credential1 = (UsernamePasswordCredential)credential;
            return createHandlerResult(credential1, principalFactory.createPrincipal(username, map), null);
        }

        }
        throw new FailedLoginException("Sorry, login attemp failed.");
    }

    public boolean supports(Credential credential) {
        if (!UsernamePasswordCredential.class.isInstance(credential)) {
            logger.debug("Credential is not one of username/password and is not accepted by handler [{}]", this.getName());
            return false;
        } else if (this.credentialSelectionPredicate == null) {
            logger.debug("No credential selection criteria is defined for handler [{}]. Credential is accepted for further processing", this.getName());
            return true;
        } else {
            logger.debug("Examining credential [{}] eligibility for authentication handler [{}]", credential, this.getName());
            boolean result = this.credentialSelectionPredicate.test(credential);
            logger.debug("Credential [{}] eligibility is [{}] for authentication handler [{}]", new Object[]{credential, this.getName(), BooleanUtils.toStringTrueFalse(result)});
            return result;
        }
    }
}



