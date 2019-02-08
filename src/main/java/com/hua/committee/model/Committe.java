package com.hua.committee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Committe")
public class Committe implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int UserId;
	private String Title;
	private String GeneralInfo;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date MeetingDate;
	private Date CreationDate;
	private byte Status;
	private String Member;
	private boolean IsCommitteAdmin; 
	private boolean HasPresident;
	private int total;
	private byte ReminderSent;
	
	private int total_pages;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getGeneralInfo() {
		return GeneralInfo;
	}
	public void setGeneralInfo(String generalInfo) {
		GeneralInfo = generalInfo;
	}
	public Date getMeetingDate() {
		return MeetingDate;
	}
	public void setMeetingDate(Date meetingDate) {
		MeetingDate = meetingDate;
	}
	public Date getCreationDate() {
		return CreationDate;
	}
	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}
	public byte getStatus() {
		return Status;
	}
	public void setStatus(byte status) {
		Status = status;
	}
	public String getMember() {
		return Member;
	}
	public void setMember(String member) {
		Member = member;
	}
	public boolean getIsCommitteAdmin(){
		return IsCommitteAdmin;
	}
	public void setIsCommitteAdmin(boolean isCommitteAdmin){
		IsCommitteAdmin = isCommitteAdmin;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal_pages() {
		return total_pages;
	}
	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}
	public boolean isHasPresident() {
		return HasPresident;
	}
	public void setHasPresident(boolean hasPresident) {
		HasPresident = hasPresident;
	}
	public byte getReminderSent() {
		return ReminderSent;
	}
	public void setReminderSent(byte reminderSent) {
		ReminderSent = reminderSent;
	}
}
