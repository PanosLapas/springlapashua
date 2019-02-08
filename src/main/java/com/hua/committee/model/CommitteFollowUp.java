package com.hua.committee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "CommitteFollowUp")
public class CommitteFollowUp implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int UserId;
	private int MemberId;
	private int CommitteId;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date CreationDate;
	private String Action;
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
	public int getMemberId() {
		return MemberId;
	}
	public void setMemberId(int memberId) {
		MemberId = memberId;
	}
	public int getCommitteId() {
		return CommitteId;
	}
	public void setCommitteId(int committeId) {
		CommitteId = committeId;
	}
	public Date getCreationDate() {
		return CreationDate;
	}
	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}
	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
}
