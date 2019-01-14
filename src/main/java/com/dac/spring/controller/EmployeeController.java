package com.dac.spring.controller;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class EmployeeController {

    @Autowired
    private RestFB restFB;

    @RequestMapping(value = {"/", "/login"})
    public String login() {
        return "/login";
    }

    @RequestMapping("/login-facebook")
    public String loginFacebook(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");
        if (code == null) {
            return "redirect:/login?facebook=error";
        }
        String accessToken = restFB.getToken(code);
        com.restfb.types.User user = restFB.getUserInfo(accessToken);
        UserDetails userDetails = restFB.buildUser(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return "redirect:/user";
    }

    @RequestMapping("/user")
    public String user(){
        return "/user";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "/admin";
    }

    @RequestMapping("/403")
    public String accessDenied(){
        return "/403";
    }
}
