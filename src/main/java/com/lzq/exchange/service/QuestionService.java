package com.lzq.exchange.service;

import com.lzq.exchange.dto.PaginationDTO;
import com.lzq.exchange.dto.QuestionDTO;
import com.lzq.exchange.mapper.QuestionMapper;
import com.lzq.exchange.mapper.UserMapper;
import com.lzq.exchange.model.Question;
import com.lzq.exchange.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        Integer offSize = size * (page -1);
        List<Question> questionList = questionMapper.list(offSize,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questionList) {
            User user =  userMapper.findByCreator(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);
        return paginationDTO;
    }

    public PaginationDTO listId(Integer userId, Integer page, Integer size) {
        Integer offSize = size * (page - 1);
        List<Question> questionList = questionMapper.listId(userId,offSize,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questionList) {
            User user =  userMapper.findByCreator(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);
        Integer totalCount = questionMapper.countId(userId);
        paginationDTO.setPagination(totalCount,page,size);
        return paginationDTO;
    }
}
