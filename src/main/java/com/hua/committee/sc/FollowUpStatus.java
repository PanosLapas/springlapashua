package com.hua.committee.sc;

public enum FollowUpStatus {
	COMMITTE_CREATED("Administrator"),
	ADD_MEMBER("������������/�������� ���������"),
	DELETE_MEMBER("����� ���������"),
	CHANGE_MEETING_DATE("������������/�������� ���������");
	
	private String status;

	FollowUpStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
