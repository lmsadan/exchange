package com.lzq.exchange.mapper;

import com.lzq.exchange.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name,accountId,token,gmtCreate,gmtModified,avatar_url) " +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatar_url})")
    void insert(User user);//类自动注入

    @Select("Select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{creator}")
    User findByCreator(@Param("creator") Integer creator);

    @Select("select * from user where accountId = #{accountId}")
    User findById(@Param("accountId")String accountId);
}
