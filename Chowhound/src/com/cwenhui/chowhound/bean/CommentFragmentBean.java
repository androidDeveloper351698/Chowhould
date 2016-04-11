package com.cwenhui.chowhound.bean;

public class CommentFragmentBean {
	private float ratingBar;
	private String deliveryComment;
	private String userName;
	private String comment;
	private String consumeTime;
	private String userImg;

	public CommentFragmentBean(float ratingBar, String deliveryComment,
			String userName, String comment, String consumeTime, String userImg) {
		super();
		this.ratingBar = ratingBar;
		this.deliveryComment = deliveryComment;
		this.userName = userName;
		this.comment = comment;
		this.consumeTime = consumeTime;
		this.userImg = userImg;
	}

	public float getRatingBar() {
		return ratingBar;
	}

	public void setRatingBar(float ratingBar) {
		this.ratingBar = ratingBar;
	}

	public String getDeliveryComment() {
		return deliveryComment;
	}

	public void setDeliveryComment(String deliveryComment) {
		this.deliveryComment = deliveryComment;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

}
