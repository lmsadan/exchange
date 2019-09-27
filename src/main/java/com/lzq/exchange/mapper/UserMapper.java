package com.lzq.exchange.mapper;

import com.lzq.exchange.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name,accountId,token,gmtCreate,gmtModified) " +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);//类自动注入

    @Select("Select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
