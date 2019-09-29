package com.lzq.exchange.mapper;

import com.lzq.exchange.dto.QuestionQueryDTO;
import com.lzq.exchange.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> listSearch(QuestionQueryDTO questionQueryDTO);
}
