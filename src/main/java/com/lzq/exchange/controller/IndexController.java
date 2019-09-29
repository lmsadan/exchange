package com.lzq.exchange.controller;

import com.lzq.exchange.dto.PaginationDTO;
import com.lzq.exchange.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {


    @Autowired
    private QuestionService questionService;


    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", defaultValue = "", required = false) String search){
        PaginationDTO paginationDTO;
        if (search == null || search.equals("")){
            paginationDTO = questionService.list(page,size);
        }else {
            paginationDTO = questionService.listSearch(page,size,null,search);
        }
        model.addAttribute("pagination",paginationDTO);
        model.addAttribute("search",search);
        return "index";
    }



}
