package com.hua.committee.dao;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.hua.committee.HomeController;
import com.hua.committee.model.UserRole;

public class UserRoleDAOImpl implements UserRoleDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public UserRole save(UserRole userRole) {
		// TODO Auto-generated method stub
	    
	    String sql = "insert into User_Role (UserId,RoleId,Username,Rolename,CommitteId) values (?, ?, ?, ?, ?)";
	    SqlUpdate su = new SqlUpdate();
	    su.setDataSource(dataSource);
	    su.setSql(sql);
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.setReturnGeneratedKeys(true);
	    su.compile();

	    Object[] params = new Object[]{userRole.getUserId(),userRole.getRoleId(),userRole.getUserName(),userRole.getRoleName(),userRole.getCommitteId()};
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    su.update(params,keyHolder);
		
		
		return userRole;
	}

	@Override
	public void deleteByCommitteId(int id,int userid) {
		// TODO Auto-generated method stub
		String query = "delete from User_Role where CommitteId = " + id + " AND UserId = " + userid;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute(query);
	}
	
	@Override
	public boolean getUserRole(int userId,int committeId)
	{
		String query = "select RoleId from User_Role where UserId =" + userId + " AND CommitteId=" + committeId;
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> Rows = jdbcTemplate.queryForList(query);
		boolean role = false;

		for (Map<String, Object> Row : Rows) {
			if(Integer.parseInt(String.valueOf(Row.get("RoleId")))==2)
			{
				role=true;
			}
			else if(Integer.parseInt(String.valueOf(Row.get("RoleId")))==3)
			{
				role=false;
			}
		}
	    
	    return role;
	}

}
