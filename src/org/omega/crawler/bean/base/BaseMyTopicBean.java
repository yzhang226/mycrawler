package org.omega.crawler.bean.base;

import java.io.Serializable;


public class BaseMyTopicBean implements Serializable {
	
	private static final long serialVersionUID = -5430310744786224042L;
	
	private Integer id;
	private Short boardId;
	private Integer topicId;
	private String author;
	private String title;
	private Integer replies;
	private Integer views;
	
	private String content;
	
	private Integer lastPostTime;
	private Integer publishTime;
	private Integer createTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Short getBoardId() {
		return boardId;
	}

	public void setBoardId(Short boardId) {
		this.boardId = boardId;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getReplies() {
		return replies;
	}

	public void setReplies(Integer replies) {
		this.replies = replies;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getLastPostTime() {
		return lastPostTime;
	}

	public void setLastPostTime(Integer lastPostTime) {
		this.lastPostTime = lastPostTime;
	}

	public Integer getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Integer publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public String toHtml() {
		/*
		 * <td>Publish Date</td>
		<td>Title</td>
		<td>Author</td>
		<td>Replies</td>
		<td>Views</td>
		 */
		StringBuilder sb = new StringBuilder("<tr>");
		sb.append("<td>").append(title).append("</td>");
		sb.append("<td>").append(author).append("</td>");
		sb.append("<td>").append(replies).append("</td>");
		sb.append("<td>").append(views).append("</td>");
		sb.append("</tr>\n");
		
		return sb.toString();
	}
	
}
