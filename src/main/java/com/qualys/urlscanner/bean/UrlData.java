package com.qualys.urlscanner.bean;

import java.io.Serializable;
import java.util.Date;

public class UrlData implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1566114613030613339L;
	
	private Integer urlId;
	private String url;
	private String redirectUrl;
	private Boolean status;
	private String ipAddress;
	private String websiteTitle;
	private String websiteBody;
	private Integer imageCount;
	private Integer linkCount;
	private Date SubmittedOn;
	
	
	
	public Integer getUrlId() {
		return urlId;
	}
	public void setUrlId(Integer urlId) {
		this.urlId = urlId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getWebsiteTitle() {
		return websiteTitle;
	}
	public void setWebsiteTitle(String websiteTitle) {
		this.websiteTitle = websiteTitle;
	}
	public Date getSubmittedOn() {
		return SubmittedOn;
	}
	public void setSubmittedOn(Date submittedOn) {
		SubmittedOn = submittedOn;
	}
	public String getWebsiteBody() {
		return websiteBody;
	}
	public void setWebsiteBody(String websiteBody) {
		this.websiteBody = websiteBody;
	}
	public Integer getImageCount() {
		return imageCount;
	}
	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}
	public Integer getLinkCount() {
		return linkCount;
	}
	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}
	
	
	

}
