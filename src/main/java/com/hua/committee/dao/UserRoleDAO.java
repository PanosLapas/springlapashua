package com.hua.committee.dao;


import com.hua.committee.model.UserRole;

public interface UserRoleDAO {
	//Create
	  public UserRole save(UserRole userRole);
	  //Read
//	  public Member getById(int id);
//	  //DeleteByCommitteId
//	  public List<Member> deleteByCommitteId(int id);
//	  //GetCommitteMembers
//	  public List<Member> getByCommitteId(int id);
	  //Delete
	  public void deleteByCommitteId(int id,int userid);
	  //Read
	  public boolean getUserRole(int userId,int committeId);
}
