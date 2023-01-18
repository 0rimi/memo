package com.memo.post.model;

import java.util.Date;

public class Post {
	
	//field
	private int id;
	private String userId;
	private String subject;
	private String content;
	private String imgPath;
	private Date createdAt;
	private Date updatedAt;
	
	//g&s
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	@Override
	public String toString() {
		return "Post [id=" + id + ", userId=" + userId + ", subject=" + subject + ", content=" + content + ", imgPath="
				+ imgPath + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
