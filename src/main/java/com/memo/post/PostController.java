package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	// http://localhost/post/post_list_view
	@GetMapping("/post_list_view")
	public String postListView(Model model, HttpSession session) {
		Integer userId = (Integer)session.getAttribute("userId");
		
		if(userId == null) {	//로그인이 안된사람
			return "redirect:/user/sign_in_view";
		}
		
		//글목록가져오기
		List<Post> posts = postBO.getPostsByUserId(userId);
		model.addAttribute("posts", posts);
		System.out.println(posts);
		
		model.addAttribute("viewName", "post/postList");
		
		return "template/layout";
	}
	
	
	@GetMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}
	
}
