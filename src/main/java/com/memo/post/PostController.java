package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String postListView(
			@RequestParam(value="prevId",required = false) Integer prevIdParam,
			@RequestParam(value="nextId",required = false) Integer nextIdParam,
			Model model,
			HttpSession session) {
		Integer userId = (Integer)session.getAttribute("userId");
		
		if(userId == null) {	//로그인이 안된사람
			return "redirect:/user/sign_in_view";
		}
		
		//글목록보내기
		int prevId = 0;
		int nextId = 0;
		
		List<Post> posts = postBO.getPostsByUserId(userId, prevIdParam, nextIdParam);
		
		if(posts.isEmpty() == false) {	//포스트목록이 비어있을때 에러방지
			prevId = posts.get(0).getId();
			nextId = posts.get(posts.size() - 1).getId();
			
			// prev방향의 끝 : list의 가장큰값과 prevId(id)인덱스 값이 같을때
			if(postBO.isPrevLastPage(prevId, userId)) {	//true면
				prevId = 0;
			}
			// next방향의 끝 : list의 마지막값과 nextId(id)인덱스 값이 같을때
			if(postBO.isNextLastPage(nextId, userId)) {
				nextId = 0;
			}
		}
		
		model.addAttribute("prevId", prevId);	//가져온리스트중 가장 앞쪽
		model.addAttribute("nextId", nextId);	//가져온리스트중 가장 뒷쪽
		model.addAttribute("posts", posts);
		
		//페이지보내기
		model.addAttribute("viewName", "post/postList");
		
		return "template/layout";
	}
	
	
	@GetMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}
	
	@GetMapping("/post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			Model model, HttpSession session) {
		
		Integer userId = (Integer)session.getAttribute("userId");
		if(userId == null) {
			return "redirect:/user/sign_in_view";
		}
		
		//DB select by userId, postId
		Post post = postBO.getPostByPostIdUserId(postId, userId);
		
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/postDetail");
		
		return "template/layout";
	}
	
}
