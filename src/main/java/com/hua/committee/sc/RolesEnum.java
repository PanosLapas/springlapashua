package com.hua.committee.sc;

public enum RolesEnum {
	ROLE_ADMIN("Administrator"),
	ROLE_COMMITTE_ADMIN("������������/�������� ���������"),
	ROLE_COMMITTE_MEMBER("����� ���������");
	
	private String rolename;

	RolesEnum(String rolename) {
        this.rolename = rolename;
    }

    public String rolename() {
        return rolename;
    }
}
