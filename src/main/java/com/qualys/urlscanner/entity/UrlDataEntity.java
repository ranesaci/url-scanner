package com.qualys.urlscanner.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "url_scan_details")
public class UrlDataEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6779450806484843025L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="url_id")
	private Integer urlId;

	@Column(name="url")
	private String url;
	@Column(name="redirect_url")
	private String redirectUrl;
	
	@Column(name="status")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean status;
	@Column(name="ip_address")
	private String ipAddress;
	@Column(name="website_title")
	private String websiteTitle;
	@Column(name="website_body")
	private String websiteBody;
	@Column(name="image_count")
	private Integer imageCount;
	@Column(name="link_count")
	private Integer linkCount;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_on")
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
	public Date getSubmittedOn() {
		return SubmittedOn;
	}
	public void setSubmittedOn(Date submittedOn) {
		SubmittedOn = submittedOn;
	}
	
	
	

}
