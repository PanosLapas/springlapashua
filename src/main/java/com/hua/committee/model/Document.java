package com.hua.committee.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Document")
public class Document implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String Title;
	private int CommitteId;
	private String Type;
	private byte[] Content;
	
	public byte[] getContent() {
		return Content;
	}
	public void setContent(byte[] content) {
		Content = content;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	
	public int getCommitteId() {
		return CommitteId;
	}
	public void setCommitteId(int committeId) {
		CommitteId = committeId;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	
}
