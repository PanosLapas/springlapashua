package com.hua.committee.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jca.cci.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hua.committee.HomeController;
import com.hua.committee.model.User;


public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		 		jdbcTemplate.update(
		 		        "insert into User (LastName,FirstName, Email,Username, Password) values (?, ?, ?, ?, ?)",
		 		        user.getLastName(),user.getFirstName(), user.getEmail(),user.getUsername(), user.getPassword());
	}

	@Override
	public User getById(int id) {
		// TODO Auto-generated method stub
		String query = "select * from User where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object queryForObject = jdbcTemplate.queryForObject(query, new Object[] { id },
				new BeanPropertyRowMapper<User>(User.class));
		User user = (User) queryForObject;

		return user;
	}
	
	@Override
	public User getByUsername(String username) {
		// TODO Auto-generated method stub
		String query = "select * from User where Username = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object queryForObject = jdbcTemplate.queryForObject(query, new Object[] { username },
				new BeanPropertyRowMapper<User>(User.class));
		User user = (User) queryForObject;

		return user;
	}
	
	@Override
	public User getByEmail(String email) {
		// TODO Auto-generated method stub
		String query = "select * from User where Email = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object queryForObject = jdbcTemplate.queryForObject(query, new Object[] { email },
				new BeanPropertyRowMapper<User>(User.class));
		User user = (User) queryForObject;

		return user;
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update User set  LastName = ?, FirstName = ?, Email = ?,Username = ? , Password = ? where Id = ?",
				user.getLastName(),user.getFirstName(), user.getEmail(),user.getUsername(), user.getPassword(), user.getId());
	}

	@Override
	public Boolean deleteById(int id) {
		// TODO Auto-generated method stub
		Boolean state=false;
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update("delete from User where Id = ?", id);
			state=true;
		} catch (InvalidResultSetAccessException e) {
			state=false;
			logger.info("InvalidResultSetAccessException");
			
			
		} catch (DataAccessException e) {
			state=false;
			logger.info("DataAccessException " + e.getMessage());
		
		}
		
		return state;
	}

	@Override
	public List<User> getAll(int page,int offset) {
		List<User> usersList = new ArrayList<User>();
		int limit = 5;
		//if(limit == 0)
			//limit = 5;
		// JDBC Code - Start
		String query = "select u.Id as Id, u.LastName as LastName, u.FirstName as FirstName, u.Email as Email from User u order by u.Id limit " + limit + " offset " + offset;
		//," +
		//   " r.Rolename as Rolename
		//left join User_Role r on r.UserId = u.Id
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> usersRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> userRow : usersRows) {
			User user = new User();
			user.setId(Integer.parseInt(String.valueOf(userRow.get("Id"))));
			user.setLastName(String.valueOf(userRow.get("LastName")));
			user.setFirstName(String.valueOf(userRow.get("FirstName")));
			user.setUsername(String.valueOf(userRow.get("Username")));
			user.setEmail(String.valueOf(userRow.get("Email")));
			//user.setPassword(String.valueOf(userRow.get("Password")));
			/*if(userRow.get("RoleName") != null)
				user.setRoleName(RolesEnum.valueOf(String.valueOf(userRow.get("Rolename"))).rolename());
			else
				user.setRoleName("");
			*/
			usersList.add(user);
		}

		return usersList;
	}
	
	@Override
	public List<User> getAllUsers() {
		List<User> usersList = new ArrayList<User>();
		//int limit = 5;
		//if(limit == 0)
			//limit = 5;
		// JDBC Code - Start
		String query = "select Id,Email from User";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> usersRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> userRow : usersRows) {
			User user = new User();
			user.setId(Integer.parseInt(String.valueOf(userRow.get("Id"))));
			user.setEmail(String.valueOf(userRow.get("Email")));
			usersList.add(user);
		}

		return usersList;
	}

	@Override
	public int getCount() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM User";

		int total = jdbcTemplate.queryForObject(sql,Integer.class);

		return total;
	}
	
	@Override
	public List<User> getAllSearch(String username) {
		List<User> usersList = new ArrayList<User>();

		// JDBC Code - Start
		String query = "select u.Id as Id, u.LastName as LastName, u.FirstName as FirstName, u.Email as Email from User u where u.Email like '%" + username + 
				"%' or u.LastName like '%" + username + "%' or u.FirstName like '%" + username + "%'  order by u.Id";
		//," +
		//   " r.Rolename as Rolename
		//left join User_Role r on r.UserId = u.Id
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> usersRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> userRow : usersRows) {
			User user = new User();
			user.setId(Integer.parseInt(String.valueOf(userRow.get("Id"))));
			user.setLastName(String.valueOf(userRow.get("LastName")));
			user.setFirstName(String.valueOf(userRow.get("FirstName")));
			user.setUsername(String.valueOf(userRow.get("Username")));
			user.setEmail(String.valueOf(userRow.get("Email")));
			//user.setPassword(String.valueOf(userRow.get("Password")));
			/*if(userRow.get("RoleName") != null)
				user.setRoleName(RolesEnum.valueOf(String.valueOf(userRow.get("Rolename"))).rolename());
			else
				user.setRoleName("");
			*/
			usersList.add(user);
		}

		return usersList;
	}

}