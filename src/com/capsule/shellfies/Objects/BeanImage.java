package com.capsule.shellfies.Objects;

public class BeanImage {
	int id, countLike, commentCount;
	String title, desc, url;

	public int getId() {
		return id;
	}

	public int getCountLike() {
		return countLike;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public String getTitle() {
		return title;
	}

	public String getDesc() {
		return desc;
	}

	public String getUrl() {
		return url;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCountLike(int countLike) {
		this.countLike = countLike;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}