package com.hua.committee.dao;

import java.util.List;

import com.hua.committee.model.User;

//CRUD operations
public interface UserDAO {
   
  //Create
  public void save(User user);
  //Read
  public User getById(int id);
  //Read
  public User getByUsername(String username);
//Read
  public User getByEmail(String username);
  //Update
  public void update(User user);
  //Delete
  public Boolean deleteById(int id);
  //Get All
  public List<User> getAll(int page,int offset);
  //Get All - no page offset
  public List<User> getAllUsers();
  //get count
  public int getCount();
  //Get All By Search
  public List<User> getAllSearch(String username);
}