package com.hua.committee.dao;

import java.util.List;

import com.hua.committee.model.Document;

public interface DocumentDAO{
	//Create
	  public void save(Document document);
	  //Read
	  public Document getById(int id);
	  //Read without content
	  public Document getByIdWithoutContent(int id);
	  //Read
	  public List<Document> getByCommitteIdWithContent(int id);
	  //
	  public List<Document> getByCommitteId(int id);
	  //Update
	  public void update(Document document);
	  //Delete
	  public void deleteById(int id);
	  //Get All
	  public List<Document> getAll();
}
