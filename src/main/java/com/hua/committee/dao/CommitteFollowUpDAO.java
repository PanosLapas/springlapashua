package com.hua.committee.dao;

import java.util.ArrayList;
import java.util.List;
import com.hua.committee.model.CommitteFollowUp;

public interface CommitteFollowUpDAO {
	
	//Create
	public CommitteFollowUp save(CommitteFollowUp history);
	  
	//Read
	public List<CommitteFollowUp> getByFilter(ArrayList<Integer> CommitteIds, ArrayList<Integer> UserIds, String date_);
	
}
