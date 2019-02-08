package com.hua.committee.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "User_Role")
public class UserRole implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int UserId;
	public int RoleId;
	public String UserName;
	public String RoleName;
	public int CommitteId;
	
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public int getRoleId() {
		return RoleId;
	}
	public void setRoleId(int roleId) {
		RoleId = roleId;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getRoleName() {
		return RoleName;
	}
	public void setRoleName(String roleName) {
		RoleName = roleName;
	}
	public int getCommitteId() {
		return CommitteId;
	}
	public void setCommitteId(int committeId) {
		CommitteId = committeId;
	}
	
}
