package com.memo.post.bo;

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
	
	// 글목록 가져오기
	public List<Post> getPostsByUserId(int userId){
		return postDAO.selectPostsByUserId(userId);
	}
	
	// 수정할 글 가져오기
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdUserId(postId,userId);
	}
	
	

}
