package com.hua.committee.model;

import java.io.Serializable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "User")
public class User implements Serializable {

	private static final long serialVersionUID = -1355807851822025659L;
	private String LastName;
	private String FirstName;
	private String email;
	private String Username;
	private int id;
	private String password;
	private Set<Role> roles;
	private String RoleName;
	private int total;
	private boolean InCommitte;
	
	public String getRoleName() {
		return RoleName;
	}

	public void setRoleName(String roleName) {
		RoleName = roleName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Name=" + this.LastName + ", Email=" + this.email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	@ManyToMany
    @JoinTable(name = "User_Role", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public boolean getInCommitte(){
		return InCommitte;
	}
	public void setInCommitte(boolean inCommitte){
		InCommitte = inCommitte;
	}
}