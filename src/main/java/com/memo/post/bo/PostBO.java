package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	//private Logger logger = LoggerFactory.getLogger(PostBO.class);
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//페이징(페이지갯수)
	private static final int POST_MAX_SIZE = 3; 
	
	@Autowired
	private PostDAO postDAO;
	@Autowired
	private FileManagerService fileManagerService;
	
	//글쓰기
	public int addPost(int userId, String userLoginId,
			String subject, String content, MultipartFile file) {
		
		// 파일 업로드 > 경로
		String imgPath = null;
		if(file != null) {	// 파일이 있으면,
			imgPath = fileManagerService.saveFile(userLoginId, file);
		}
		
		return postDAO.insertPost(userId, subject, content, imgPath); 
	}
	
	//글수정 : update
	public void updatePost(int userId, String userLoginId, 
			String subject, int postId, String content, MultipartFile file) {
		
		//기존 글 불러오기
		Post post = getPostByPostIdUserId(postId, userId);
		if(post == null) {
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다. postId :{}, userId:{}",postId,userId);
			return;
		}
		
		//file이 없으면 기존 file(img) : 업로드 성공 후 기존이미지 제거
		String imgPath = null;
		if(file != null) {	//file존재
			//업로드
			imgPath = fileManagerService.saveFile(userLoginId, file);
			//업로드성공, 기존이미지 제거
			//imgPath가 null이 아니고, 기존 글에 imgPath가 null이 아닐경우
			if(imgPath != null && post.getImgPath() != null) {
				//(기존)이미지 제거
				fileManagerService.deleteFile(post.getImgPath());
			}
		}
		
		//db update
		postDAO.updatePostByPostIdUserId(postId, userId, subject, content, imgPath);
	}
	
	//글삭제하기
	public int deletePostByPostIdUserId(int postId, int userId) {
		//기존글불러오기
		Post post = getPostByPostIdUserId(postId, userId);
		if(post == null) {
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다. postId :{}, userId:{}",postId,userId);
			return 0;
		}
		//이미지 있는지 확인
		if(post.getImgPath() != null) {
			fileManagerService.deleteFile(post.getImgPath());
		}
		//삭제
		return postDAO.deletePostByPostIdUserId(postId,userId);
	}
	
	// 글 목록 가져오기
	public List<Post> getPostsByUserId(int userId,Integer prevId, Integer nextId){
		// 게시글 번호 : 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 4 3 2 페이지에 있을때
		// 1) 이전 : 정방향 4보다 큰 3개 (5 6 7) > List reverse(7 6 5)
		// 2) 다음 : 2보다 작은 3개 DESC
		// 3) 첫페이지(이전, 다음 없음) DESC 3개
		String direction = null;	//방향
		Integer standardId = null;	//기준 postId
		
		if(prevId != null) {	//이전
			direction = "prev";
			standardId = prevId;
			
			System.out.println("direction : "+direction+", standardId : "+standardId);
			
			List<Post> posts = postDAO.selectPostsByUserId(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(posts);
			System.out.println(posts);
			
			return posts;
		}else if(nextId != null) {	//다음
			direction = "next";
			standardId = nextId;
		}
		
		//첫페이지일때(페이징x) standardId, direction = null
		//다음페이지일때 standardId(nextId받은거), direction(next)
		return postDAO.selectPostsByUserId(userId, direction, standardId,POST_MAX_SIZE);
	}
	
	//페이지가 마지막값인지 아닌지
	public boolean isPrevLastPage(int prevId, int userId) {
		int maxPostId = postDAO.selectPostIdByUserIdSort(userId, "DESC");
		return maxPostId == prevId ? true : false; 
	}
	
	public boolean isNextLastPage(int nextId, int userId) {
		int minPostId = postDAO.selectPostIdByUserIdSort(userId, "ASC");
		return minPostId == nextId ? true : false; 
	}
	
	// 글 하나 가져오기
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdUserId(postId,userId);
	}
	
	

}
