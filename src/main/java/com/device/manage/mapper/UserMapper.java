package com.device.manage.mapper;

import com.device.manage.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectAll();
    User selectById(Long id);
    User selectByUsername(String username);
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    User selectByUsernameAndPasswordAndProjectId(@Param("username") String username, @Param("password") String password, @Param("projectId") Long projectId);
    int insert(User user);
    int update(User user);
    int deleteById(Long id);
}