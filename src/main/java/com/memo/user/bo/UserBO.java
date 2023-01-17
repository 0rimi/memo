package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.user.dao.UserDAO;
import com.memo.user.model.User;

@Service
public class UserBO {
	
	@Autowired
	private UserDAO userDAO;
	
	//아이디 중복체크
	public boolean existloginId(String loginId) {
		return userDAO.existloginId(loginId);
	}
	
	//회원추가
	public int addUser(User user) {
		return userDAO.insertUser(user);
	}
	
	//로그인
	public User getUserByLoginIdPassword(String loginId, String password) {
		return userDAO.seletUserByLoginIdPassword(loginId,password);
	}

}
