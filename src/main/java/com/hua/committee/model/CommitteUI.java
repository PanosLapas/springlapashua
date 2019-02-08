package com.hua.committee.model;

import java.io.Serializable;
import java.util.List;

public class CommitteUI implements Serializable{

	private static final long serialVersionUID = 1L;
	private Committe Committe; 
	private List<Document> Documents;
	private List<Member> Members;
	
	public Committe getCommitte() {
		return Committe;
	}
	public void setCommitte(Committe committe) {
		Committe = committe;
	}
	public List<Document> getDocuments() {
		return Documents;
	}
	public void setDocuments(List<Document> documents) {
		Documents = documents;
	}
	public List<Member> getMembers() {
		return Members;
	}
	public void setMemberss(List<Member> members) {
		Members = members;
	}
}
