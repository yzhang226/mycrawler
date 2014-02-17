package org.omega.crawler.bean;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AnnCoinBean implements Comparable<AnnCoinBean> {
	
	private Integer id;
	private Integer topicid;
	private String author;
	private String title;
	private String link;
	private Date publishDate;
	private String publishContent;
	private Integer replies;
	private Integer views;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTopicid() {
		return topicid;
	}
	public void setTopicid(Integer topicid) {
		this.topicid = topicid;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getPublishContent() {
		return publishContent;
	}
	public void setPublishContent(String publishContent) {
		this.publishContent = publishContent;
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
	
	public String toHtml() {
		/*
		 * <td>Publish Date</td>
		<td>Title</td>
		<td>Author</td>
		<td>Replies</td>
		<td>Views</td>
		 */
		StringBuilder sb = new StringBuilder("<tr>");
		sb.append("<td>").append(null != publishDate ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishDate) : "").append("</td>");
		sb.append("<td>").append(title).append("</td>");
		sb.append("<td>").append(author).append("</td>");
		sb.append("<td>").append(replies).append("</td>");
		sb.append("<td>").append(views).append("</td>");
		sb.append("</tr>\n");
		
		return sb.toString();
	}
	
	@Override
	public int compareTo(AnnCoinBean o) {
		if (this == o) {
			return 0;
		}
		
		if (o == null || o.getPublishDate() == null) {
			return 1;
		}
		
		if (this.getPublishDate() == null && o.getPublishDate() != null) {
			return -1;
		}
		
		return (int) (this.getPublishDate().getTime() - o.getPublishDate().getTime());
	}
	
}
