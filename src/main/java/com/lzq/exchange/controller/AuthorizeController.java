package com.lzq.exchange.controller;

import com.lzq.exchange.dto.AccessTokenDTO;
import com.lzq.exchange.dto.GithubUser;
import com.lzq.exchange.mapper.UserMapper;
import com.lzq.exchange.model.User;
import com.lzq.exchange.model.UserExample;
import com.lzq.exchange.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(githubUser != null && githubUser.getId() !=null){
            String accountId = String.valueOf(githubUser.getId());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andAccountidEqualTo(accountId);
            List<User> users = userMapper.selectByExample(example);
            if (users.size() != 0){
                users.get(0).setGmtmodified(System.currentTimeMillis());
                request.getSession().setAttribute("user",users.get(0));
                UserExample userExample = new UserExample();
                userExample.createCriteria()
                        .andIdEqualTo(users.get(0).getId());
                userMapper.updateByExampleSelective(users.get(0),userExample);
                return "redirect:/";
            }
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountid(accountId);
            user.setGmtcreate(System.currentTimeMillis());
            user.setGmtmodified(user.getGmtcreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            return "redirect:/";
        }

    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
