package com.lzq.exchange.service;

import com.lzq.exchange.dto.PaginationDTO;
import com.lzq.exchange.dto.QuestionDTO;
import com.lzq.exchange.dto.QuestionQueryDTO;
import com.lzq.exchange.mapper.QuestionExtMapper;
import com.lzq.exchange.mapper.QuestionMapper;
import com.lzq.exchange.mapper.UserMapper;
import com.lzq.exchange.model.Question;
import com.lzq.exchange.model.QuestionExample;
import com.lzq.exchange.model.User;
import org.apache.ibatis.session.RowBounds;
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
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {
        Integer offSize = size * (page -1);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offSize, size));
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        return getPaginationDTO(questions,totalCount,page,size);
    }

    public PaginationDTO listId(Integer page, Integer size, Long userId) {
        Integer offSize = size * (page - 1);
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria()
                .andIdEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1, new RowBounds(offSize, size));
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
        return getPaginationDTO(questions,totalCount,page,size);
    }


    public PaginationDTO listSearch(Integer page, Integer size, Long userId, String search) {
        Integer offSize = size * (page -1);
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setUserId(userId);
        questionQueryDTO.setOffSize(offSize);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setSearch(search);
        List<Question> questionList = questionExtMapper.listSearch(questionQueryDTO);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        return getPaginationDTO(questionList,totalCount,page,size);
    }

    private PaginationDTO getPaginationDTO(List<Question> questionList, Integer totalCount, Integer page, Integer size){
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList) {
            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);
        Integer total = totalCount;
        paginationDTO.setPagination(total,page,size);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(question,questionDTO);
        User user =  userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question, example);
        }
    }
}
