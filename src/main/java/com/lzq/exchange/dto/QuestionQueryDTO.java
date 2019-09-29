package com.lzq.exchange.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private Long userId;
    private String search;
    private Integer offSize;
    private Integer size;
}
