package com.example.demo;

import com.idsmanager.dingdang.jwt.DingdangUserRetriever;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class SSOController {

    private static final Logger LOG = LoggerFactory.getLogger(SSOController.class);

    @RequestMapping(value = "/jwt/sso/login")
    public String ssoUrl(@RequestParam String id_token, String redirect_url, Model model, HttpServletRequest request) {
        //1.接收方法为GET方式,参数名为id_token
        //2.<解析令牌>为解析id_token并验证代码
        //1.使用公钥，解析id_token
        // 使用publicKey解密上一步获取的id_token令牌
        DingdangUserRetriever retriever = new DingdangUserRetriever(id_token, "{\"kty\":\"RSA\",\"kid\":\"3946389972347076401\",\"alg\":\"RS256\",\"n\":\"hsYPTw1Fpqj9DEOf2izoM2AulLawpyo9nkOMVY98lCENlw9xmwGZilV0Zx9V-q136OvGooebP1d-6D3SghP_HIbJXx76rXQQDoEL0t6ygSXsSZBVQRKQmm5zdq2AplaoxZngFCOKrQ5GRfVsq36bpd2nsLuDRxNsEmRR_ESf1TwTX26Hm91Cnu4YWOESS-vyEbBbbBCV0r1jaBRalKBu-UZJ0sl8XGGbHddnZZ7cd0XtnJG1f9AZDXuIjx81LMjMgNtF7uQ9Rb5PpIt9XcRnlloaXnfBBViIuL-BmhOtELUCucyC0amKHS5MRbRzSeUnRSsl_FgDG8-zYZXrMiXcvw\",\"e\":\"AQAB\"}");
        DingdangUserRetriever.User user = null;
        try {
            //2.获取用户信息
            user = retriever.retrieve();
            LOG.info("get user:",user);
        } catch (Exception e) {
            LOG.warn("Retrieve SSO user failed", e);

            return "error";
        }
//3.判断用户名是否在自己系统存在isExistedUsername()方法为业务系统自行判断数据库中是否存在
        if (!user.equals(null)) {
            //4.如果存在,登录成功，返回登录成功后的页面
            //User spUser = userService.updateLoginTimes(user.getUsername());
            //request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, saveSecurity(spUser));
            System.out.println( "login is success");

            //5.如果注册时添加redirect_url，那么返回此自定义url页面
            if (StringUtils.isNotEmpty(redirect_url)) {
                return "redirect:" + redirect_url;
            }
            //6.否则返回系统默认操作页面
            return "redirect:../../index";
        } else {
            //7.如果不存在,返回登录失败页面,提示用户不存在
            model.addAttribute("error", "username { " + user.getUsername() + " } not exist");
            return "error";
        }
    }


}
