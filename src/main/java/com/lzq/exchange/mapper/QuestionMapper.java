package com.lzq.exchange.mapper;

import com.lzq.exchange.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) " +
            "values (#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offSize},#{size}")
    List<Question> list(@Param("offSize") Integer offSize,@Param("size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offSize},#{size}")
    List<Question> listId(@Param("userId")Integer userId,@Param("offSize") Integer offSize,@Param("size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countId(@Param("userId")Integer userId);
}
