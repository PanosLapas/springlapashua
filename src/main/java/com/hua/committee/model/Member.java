package com.hua.committee.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Member")
public class Member implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int id;
	public int CommitteId;
	public String LastName;
	public String FirstName;
	public String Email;
	public String Role;
	public int UserId;
	public boolean IsCommittePresident;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommitteId() {
		return CommitteId;
	}
	public void setCommitteId(int committeId) {
		CommitteId = committeId;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public boolean getIsCommittePresident(){
		return IsCommittePresident;
	}
	public void setIsCommittePresident(boolean IsCommittePresident) {
		this.IsCommittePresident = IsCommittePresident;
	}
}
