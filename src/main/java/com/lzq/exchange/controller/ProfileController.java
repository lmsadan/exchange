package com.lzq.exchange.controller;

import com.lzq.exchange.dto.PaginationDTO;
import com.lzq.exchange.mapper.UserMapper;
import com.lzq.exchange.model.User;
import com.lzq.exchange.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller()
public class ProfileController {

    @Autowired
    private QuestionService questionService;


    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          Model model,
                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "size",defaultValue = "7")Integer size,
                          @RequestParam(name = "search", defaultValue = "", required = false) String search,
                          HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        PaginationDTO paginationDTO;
        if (search == null || search.equals("")){
            paginationDTO = questionService.listId(page,size,user.getId());
        }else {
            paginationDTO = questionService.listSearch(page,size,user.getId(),search);
        }
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }

}
