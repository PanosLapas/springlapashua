package com.hua.committee.dao;

import java.util.List;
import com.hua.committee.model.Member;

public interface MemberDAO {

	  //Create
	  public Member save(Member member);
	  //Read
	  public Member getById(int id);
	  //DeleteByCommitteId
	  public List<Member> deleteByCommitteId(int id);
	  //GetCommitteMembers
	  public List<Member> getByCommitteId(int id);
	  //Delete
	  public void deleteById(int id);
	
}
