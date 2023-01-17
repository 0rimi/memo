package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController	//controller+responseBod
public class UserRestController {
	
	@Autowired
	private UserBO userBO;
	
	/**
	 * 아이디 중복확인 API
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is_duplicated_id")
	public Map<String,Object> isDuplicatedId(
			@RequestParam("loginId") String loginId){
		
		Map<String,Object> result = new HashMap<>();
		
		// db, id 중복체크
		boolean isDuplicated = userBO.existloginId(loginId);	
		System.out.println(isDuplicated);
		
		if(isDuplicated) {	//true면 중복
			result.put("code",1);
			result.put("result", true);
		}else {	//false면 중복아님
			result.put("code",1);
			result.put("result", false);
		}
		
		return result;
	}
	
	@PostMapping("/sign_up")
	public Map<String,Object> signUp(@ModelAttribute User user){
		Map<String,Object> result = new HashMap<>();
		System.out.println(user);

		// 비밀번호 해싱(hashing) - md5
		String hashedPw = EncryptUtils.md5(user.getPassword());
		user.setPassword(hashedPw);
		
		// db insert
		int row = userBO.addUser(user);
		System.out.println(user);
		
		//result
		if(row>0) {	//성공시
			result.put("code", 1);
		}else {
			result.put("code", 500);
		}
		
		return result;
	}
	
	@PostMapping("/sign_in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<>();
		
		//비밀번호 해싱
		String hashedPw = EncryptUtils.md5(password);
		
		//db select
		User user = userBO.getUserByLoginIdPassword(loginId, hashedPw);
		
		if(user != null) {//일치행 존재(로그인)
			result.put("code",1);
			result.put("result","성공");
			
			//세션이라는 공간에 유저 정보를 담는다.(로그인상태 유지)
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());			
		}else {//일치행 없음(로그인실패)
			result.put("code",500);
			result.put("errorMessage","존재하지 않는 사용자입니다.");
		}
		
		return result;
	}
	
}
