package com.hua.committee.dao;

import java.util.List;

import com.hua.committee.model.Committe;

public interface CommitteDAO {
	//Create
	  public Committe save(Committe committe);
	  //Read
	  public Committe getById(int id);
	  //Read
	  public List<Committe> getByTitle(String title);
	  //Update
	  public void update(Committe committe);
	//Update
	  public void updateReminder(Committe committe);
	  //Delete
	  public List<Committe> deleteById(int id);
	  //Get All
	  public List<Committe> getAll(int page,int offset);
	  //Get All
	  public List<Committe> getAllMeetingDate(String meeting_date);
	  //Get All by Member Id
	  public List<Committe> getAllByUserId(int id,int page,int offset);
	  //get count
	  public int getCount();
	  //get count by member
	  public int getCountByMember(int id);
	  //Get AllSearch
	  public List<Committe> getAllSearch(int page,int offset,String date_from, String date_to, String title);
	//Get AllSearch By Member
	  public List<Committe> getAllSearchByMember(int userId,int page,int offset,String date_from, String date_to, String title);
}
