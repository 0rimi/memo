package com.memo.post.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;

@Service
public class PostBO {
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	public int addPost(int userId, String userLoginId,
			String subject, String content, MultipartFile file) {
		
		// 파일 업로드 > 경로
		String imgPath = null;
		if(file != null) {	// 파일이 있으면,
			imgPath = fileManagerService.saveFile(userLoginId, file);
		}
		
		return postDAO.insertPost(userId, subject, content, imgPath); 
	}
	
	

}
