package com.memo.post.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
	public List<Map<String,Object>> selectPostTest();

	public int insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imgPath") String imgPath
			);
	
	//글 목록 불러오기
	public List<Post> selectPostsByUserId(
			@Param("userId") int userId, 
			@Param("direction") String direction,
			@Param("standardId") Integer standardId,
			@Param("limit") int limit);
	
	//마지막페이지 id가져오기
	public int selectPostIdByUserIdSort(
			@Param("userId") int userId,
			@Param("sort") String sort);
	
	//수정할 글 불러오기
	public Post selectPostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId
			);
	
	// 글 수정 : update
	public void updatePostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imgPath") String imgPath);
	
	// 글 삭제 : delete
	public int deletePostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId);
}
