package com.hua.committee.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.hua.committee.HomeController;
import com.hua.committee.model.Member;

public class MemberDAOImpl implements MemberDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public Member save(Member member) {
		// TODO Auto-generated method stub
		logger.info("member saving");
	    
	    String sql = "insert into Member (CommitteId,LastName,FirstName,Email,Role,UserId) values (?, ?, ?, ?, ?, ?)";
	    SqlUpdate su = new SqlUpdate();
	    su.setDataSource(dataSource);
	    su.setSql(sql);
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.setReturnGeneratedKeys(true);
	    su.compile();

	    Object[] params = new Object[]{member.getCommitteId(),member.getLastName(),member.getFirstName(),member.getEmail(),member.getRole(),member.getUserId()};
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    su.update(params,keyHolder);
	    int id = keyHolder.getKey().intValue();
		
        logger.info("done saving member");
		member.setId(id);
		
		return member;
	}
	
	@Override
	public Member getById(int id) {
		// TODO Auto-generated method stub
		String query = "select * from Member where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object queryForObject = jdbcTemplate.queryForObject(query, new Object[] { id },
				new BeanPropertyRowMapper<Member>(Member.class));
		Member member = (Member) queryForObject;

		return member;
	}
	
	@Override
	public List<Member> deleteByCommitteId(int id) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				String query = "delete from Member where CommitteId = " + id;
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

				jdbcTemplate.execute(query);
				
		return null;
	}
	
	@Override
	public List<Member> getByCommitteId(int id) {
		
		// TODO Auto-generated method stub
		List<Member> membersList = new ArrayList<Member>();
		// JDBC Code - Start
		String query = "select * from Member where CommitteId = " + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> membersRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> memberRow : membersRows) {
			Member member = new Member();
			member.setId(Integer.parseInt(String.valueOf(memberRow.get("Id"))));
			member.setLastName(String.valueOf(memberRow.get("LastName")));
			member.setFirstName(String.valueOf(memberRow.get("FirstName")));
			member.setEmail(String.valueOf(memberRow.get("Email")));
			member.setRole(String.valueOf(memberRow.get("Role")));
			member.setUserId(Integer.parseInt(String.valueOf(memberRow.get("UserId"))));
			member.setCommitteId(id);
			membersList.add(member);
		}

		return membersList;
	}
	
	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		String query = "delete from Member where id = " + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute(query);
	}
	
	
	
}
