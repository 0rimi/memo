package com.memo.user.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.user.model.User;

@Repository
public interface UserDAO {

	public boolean existloginId(String loginId);
	
	public int insertUser(User user);
	
	public User seletUserByLoginIdPassword(
			@Param("loginId") String loginId,
			@Param("password") String password);
	
}
