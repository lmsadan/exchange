package com.lzq.exchange.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questionDTOS;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }
        this.page = page;
        this.totalPage = totalPage;

        if (totalPage<=7 && totalPage>=0){
            for (int i = 1; i <= totalPage; i++) {
                pages.add(i);
            }
        }else{
            if(page >= totalPage-3){
                for (int i = totalPage-6; i <= totalPage; i++) {
                    pages.add(i);
                }
            }else if(page > 4){
                for (int i = page-3; i <= page+3; i++) {
                    pages.add(i);
                }
            }else{
                for (int i = 1; i <= 7; i++) {
                    pages.add(i);
                }
            }
        }


    }
}
