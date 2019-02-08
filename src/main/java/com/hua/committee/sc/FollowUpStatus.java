package com.hua.committee.sc;

public enum FollowUpStatus {
	COMMITTE_CREATED("Administrator"),
	ADD_MEMBER("Διαχειριστής/Πρόεδρος Επιτροπής"),
	DELETE_MEMBER("Μέλος Επιτροπής"),
	CHANGE_MEETING_DATE("Διαχειριστής/Πρόεδρος Επιτροπής");
	
	private String status;

	FollowUpStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
