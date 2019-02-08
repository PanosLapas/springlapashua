package com.hua.committee.dao;

import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
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
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hua.committee.HomeController;
import com.hua.committee.model.Committe;
import com.hua.committee.model.Member;
import com.hua.committee.dao.MemberDAO;

public class CommitteDAOImpl implements CommitteDAO {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("memberDAO")
	private MemberDAO memberDAO;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Committe save(Committe committe) {
		// TODO Auto-generated method stub
		Date date =new java.sql.Date(Calendar.getInstance().getTime().getTime());
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		logger.info("committe saving");
	    
	    String sql = "insert into Committe (UserId,Title, GeneralInfo,MeetingDate,CreationDate,Status,ReminderSent) values (?, ?, ?, ?, ?, ?,?)";
	    SqlUpdate su = new SqlUpdate();
	    su.setDataSource(dataSource);
	    su.setSql(sql);
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.declareParameter(new SqlParameter(Types.DATE));
	    su.declareParameter(new SqlParameter(Types.DATE));
	    su.declareParameter(new SqlParameter(Types.BOOLEAN));
	    su.declareParameter(new SqlParameter(Types.BOOLEAN));
	    su.setReturnGeneratedKeys(true);
	    su.compile();

	    Object[] params = new Object[]{committe.getUserId(), committe.getTitle(), committe.getGeneralInfo(), committe.getMeetingDate(), date,true,false};
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    su.update(params,keyHolder);
	    int id = keyHolder.getKey().intValue();
		
        logger.info("done saving");
		committe.setId(id);
		
		return committe;
		
		/*jdbcTemplate.update(
		"insert into Committe (UserId,Title, GeneralInfo,MeetingDate,CreationDate) values (?, ?, ?, ?, ?)",
		committe.getUserId(), committe.getTitle(), committe.getGeneralInfo(), committe.getMeetingDate(), date);*/
	}

	@Override
	public Committe getById(int id) {
		// TODO Auto-generated method stub
		String query = "select * from Committe where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object queryForObject = jdbcTemplate.queryForObject(query, new Object[] { id },
				new BeanPropertyRowMapper<Committe>(Committe.class));
		Committe committe = (Committe) queryForObject;

		return committe;
	}
	
	@Override
	public List<Committe> getByTitle(String title) {
		// TODO Auto-generated method stub
		List<Committe> committesList = new ArrayList<Committe>();

		// JDBC Code - Start
		String query = "select Id from Committe where title like '%" + title + "%'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> comsRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> comRow : comsRows) {
			Committe com = new Committe();
			com.setId(Integer.parseInt(String.valueOf(comRow.get("Id"))));
			committesList.add(com);
		}

		return committesList;
	}

	@Override
	public void update(Committe committe) {
		// TODO Auto-generated method stub
		Date date =new java.sql.Date(Calendar.getInstance().getTime().getTime());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update Committe set  Title = ?, GeneralInfo = ?, MeetingDate = ? , ModificationDate = ? , ModifiedBy = ? where Id = ?",
				committe.getTitle(), committe.getGeneralInfo(), committe.getMeetingDate(),date, auth.getName(), committe.getId());
	}
	
	@Override
	public void updateReminder(Committe committe) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update Committe set  ReminderSent = ? where Id = ?",
				true, committe.getId());
	}

	@Override
	public List<Committe> deleteById(int id) {
		// TODO Auto-generated method stub
		//Boolean state = false;
		
		//Delete docs if existing
		String query = "select Id,CommitteId from Document where CommitteId = " + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	
		List<Map<String, Object>> Rows = jdbcTemplate.queryForList(query);
	
		for (Map<String, Object> Row : Rows) {
			
			//Document doc = new Document();
			//doc.setId(Integer.parseInt(String.valueOf(Row.get("Id"))));
			
			query = "delete from Document where id = " + Integer.parseInt(String.valueOf(Row.get("Id")));
			jdbcTemplate = new JdbcTemplate(dataSource);

			jdbcTemplate.execute(query);
		}
		
		//get members if exist and delete User_Role
		List<Member> members = new ArrayList<Member>();
		members=memberDAO.getByCommitteId(id);
		int userId=0;
		
		for(int i=0;i<members.size();i++)
		{
			userId = members.get(i).getUserId();
			//delete User_Role for this member
			query = "delete from User_Role where CommitteId = " + id + " and UserId=" + userId ;
			jdbcTemplate = new JdbcTemplate(dataSource);

			jdbcTemplate.execute(query);
			
		}
		
		//delete Members if existing
		query = "select Id,CommitteId from Member where CommitteId = " + id;
		jdbcTemplate = new JdbcTemplate(dataSource);
	
		Rows = jdbcTemplate.queryForList(query);
	
		for (Map<String, Object> Row : Rows) {
			
			query = "delete from Member where id = " + Integer.parseInt(String.valueOf(Row.get("Id")));
			jdbcTemplate = new JdbcTemplate(dataSource);

			jdbcTemplate.execute(query);
		}
		
		//delete User_Roles if existing
//		query = "select CommitteId from User_Role where CommitteId = " + id;
//		jdbcTemplate = new JdbcTemplate(dataSource);
//	
//		Rows = jdbcTemplate.queryForList(query);
//	
//		for (Map<String, Object> Row : Rows) {
//			
//			query = "delete from User_Role where id = " + Integer.parseInt(String.valueOf(Row.get("CommitteId")));
//			jdbcTemplate = new JdbcTemplate(dataSource);
//
//			jdbcTemplate.execute(query);
//		}
		
		
		//delete committe
		try {
			jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update("delete from Committe where Id = ?", id);
			//state = true;
		} catch (InvalidResultSetAccessException e) {
			//state = false;
			logger.info("InvalidResultSetAccessException");
		} catch (DataAccessException e) {
			//state = false;
			logger.info("DataAccessException " + e.getMessage());
		}

		//get list of active committes
		
		List<Committe> committesList = new ArrayList<Committe>();
		// JDBC Code - Start
		query = "select * from Committe";
		jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> committesRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> committeRow : committesRows) {
			Committe committe = new Committe();
			committe.setId(Integer.parseInt(String.valueOf(committeRow.get("Id"))));
			committe.setTitle(String.valueOf(committeRow.get("Title")));
			committe.setGeneralInfo(String.valueOf(committeRow.get("GeneralInfo")));
			logger.info(String.valueOf(committeRow.get("MeetingDate")));
			
			String DATE_FORMAT_I = "yyyy-MM-dd hh:mm:ss.S";
			//String DATE_FORMAT_O = "dd-MM-yyyy";

			SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
			//SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

			java.util.Date date;
			try {
				date = formatInput.parse(String.valueOf(committeRow.get("MeetingDate")));
				committe.setMeetingDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//committe.setMeetingDate((Date) dateString);
			//logger.info(committe.getMeetingDate().toString());
			
			committesList.add(committe);
		}

		return committesList;
	}

	@Override
	public List<Committe> getAll(int page,int offset) {
		// TODO Auto-generated method stub
		int limit = 5;
		//if(limit == 0)
			//limit = 5;
		
		List<Committe> committesList = new ArrayList<Committe>();
		// JDBC Code - Start
		String query = "select * from Committe limit " + limit + " offset " + offset;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> committesRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> committeRow : committesRows) {
			Committe committe = new Committe();
			committe.setId(Integer.parseInt(String.valueOf(committeRow.get("Id"))));
			committe.setTitle(String.valueOf(committeRow.get("Title")));
			committe.setGeneralInfo(String.valueOf(committeRow.get("GeneralInfo")));
			logger.info(String.valueOf(committeRow.get("MeetingDate")));
			
			String DATE_FORMAT_I = "yyyy-MM-dd hh:mm:ss.S";
			//String DATE_FORMAT_O = "dd-MM-yyyy";

			SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
			//SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

			java.util.Date date;
			try {
				date = formatInput.parse(String.valueOf(committeRow.get("MeetingDate")));
				committe.setMeetingDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//committe.setMeetingDate((Date) dateString);
			//logger.info(committe.getMeetingDate().toString());
			
			committesList.add(committe);
		}

		return committesList;
	}
	
	@Override
	public List<Committe> getAllMeetingDate(String meeting_date) {
		// TODO Auto-generated method stub
		
		List<Committe> committesList = new ArrayList<Committe>();
		
		SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
	   SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
	   java.util.Date date;
	   
	   	try {
			date = originalFormat.parse(meeting_date);
			 System.out.println("Old Format from:   " + originalFormat.format(date));
		     System.out.println("New Format from:   " + targetFormat.format(date));
		     meeting_date=targetFormat.format(date);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
		// JDBC Code - Start
		String query = "select * from Committe where MeetingDate='" + meeting_date + "' AND ReminderSent=0";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> committesRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> committeRow : committesRows) {
			Committe committe = new Committe();
			committe.setId(Integer.parseInt(String.valueOf(committeRow.get("Id"))));
			committe.setTitle(String.valueOf(committeRow.get("Title")));
			committe.setGeneralInfo(String.valueOf(committeRow.get("GeneralInfo")));
			logger.info(String.valueOf(committeRow.get("MeetingDate")));
			
			String DATE_FORMAT_I = "yyyy-MM-dd hh:mm:ss.S";
			//String DATE_FORMAT_O = "dd-MM-yyyy";

			SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
			//SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

			try {
				date = formatInput.parse(String.valueOf(committeRow.get("MeetingDate")));
				committe.setMeetingDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//committe.setMeetingDate((Date) dateString);
			//logger.info(committe.getMeetingDate().toString());
			
			committesList.add(committe);
		}

		return committesList;
	}
	
	@Override
	public List<Committe> getAllByUserId(int id,int page,int offset) {
		// TODO Auto-generated method stub
		int limit = 5;
		
		List<Committe> committesList = new ArrayList<Committe>();
		// JDBC Code - Start
		String query="";
		if(offset > 0)
		{
			query = "select com.Id as Id,com.Title as Title,com.GeneralInfo as GeneralInfo, com.MeetingDate as MeetingDate, r.RoleId as RoleId from Committe com join Member mem on mem.CommitteId=com.Id join User_Role r on r.CommitteId=com.Id where mem.UserId=" + id + " and r.UserId=" + id + " limit " + limit + " offset " + offset;
		}
		else
		{
			query = "select com.Id as Id,com.Title as Title,com.GeneralInfo as GeneralInfo, com.MeetingDate as MeetingDate, r.RoleId as RoleId from Committe com join Member mem on mem.CommitteId=com.Id join User_Role r on r.CommitteId=com.Id where mem.UserId=" + id + " and r.UserId=" + id;
		}
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> committesRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> committeRow : committesRows) {
			Committe committe = new Committe();
			committe.setId(Integer.parseInt(String.valueOf(committeRow.get("Id"))));
			committe.setTitle(String.valueOf(committeRow.get("Title")));
			committe.setGeneralInfo(String.valueOf(committeRow.get("GeneralInfo")));
			logger.info(String.valueOf(committeRow.get("MeetingDate")));
			
			String DATE_FORMAT_I = "yyyy-MM-dd hh:mm:ss.S";
			//String DATE_FORMAT_O = "dd-MM-yyyy";

			SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
			//SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

			java.util.Date date;
			try {
				date = formatInput.parse(String.valueOf(committeRow.get("MeetingDate")));
				committe.setMeetingDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//get role in committe
			if(Integer.parseInt(String.valueOf(committeRow.get("RoleId")))==2)
			{
				committe.setIsCommitteAdmin(true);
			}
			else if(Integer.parseInt(String.valueOf(committeRow.get("RoleId")))==3)
			{
				committe.setIsCommitteAdmin(false);
			}
			
			committesList.add(committe);
		}

		return committesList;
	}
	
	@Override
	public int getCount() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM Committe";

		int total = jdbcTemplate.queryForObject(sql,Integer.class);

		return total;
	}
	
	@Override
	public int getCountByMember(int id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM Committe com join Member mem on mem.CommitteId=com.Id where mem.UserId=" + id;

		int total = jdbcTemplate.queryForObject(sql,Integer.class);

		return total;
	}
	
	@Override
	public List<Committe> getAllSearch(int page,int offset,String date_from, String date_to, String title) {
		// TODO Auto-generated method stub
		//int limit = 5;
		//if(limit == 0)
			//limit = 5;
		//cast date_from to right pattern
		
	   SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
	   SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
	   java.util.Date date;
	   try {
	     date = originalFormat.parse(date_from);
	     System.out.println("Old Format from:   " + originalFormat.format(date));
	     System.out.println("New Format from:   " + targetFormat.format(date));
	     date_from=targetFormat.format(date);

	    } catch (ParseException ex) {
	      // Handle Exception.
	    }
	   
	   //cast date_to to right pattern
	   
	   try {
	     date = originalFormat.parse(date_to);
	     System.out.println("Old Format to:   " + originalFormat.format(date));
	     System.out.println("New Format to:   " + targetFormat.format(date));
	     date_to=targetFormat.format(date);

	    } catch (ParseException ex) {
	      // Handle Exception.
	    }
		
		List<Committe> committesList = new ArrayList<Committe>();
		// JDBC Code - Start
		String query_plus="";
		if(title != "")
			query_plus=" where Title like '%" + title + "%' ";
		
		if(date_from != "")
		{
			if(query_plus != "")
			{
				query_plus= query_plus + " AND MeetingDate>='" + date_from + "' ";
			}
			else
			{
				query_plus= " where MeetingDate>='" + date_from + "' ";
			}
		}
		
		if(date_to != "")
		{
			if(query_plus != "")
			{
				query_plus= query_plus + " AND MeetingDate<='" + date_to + "' ";
			}
			else
			{
				query_plus= " where MeetingDate<='" + date_to + "' ";
			}
		}
		
		String query = "select * from Committe" + query_plus ;//+ " limit " + limit + " offset " + offset;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> committesRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> committeRow : committesRows) {
			Committe committe = new Committe();
			committe.setId(Integer.parseInt(String.valueOf(committeRow.get("Id"))));
			committe.setTitle(String.valueOf(committeRow.get("Title")));
			committe.setGeneralInfo(String.valueOf(committeRow.get("GeneralInfo")));
			//logger.info(String.valueOf(committeRow.get("MeetingDate")));
			
			String DATE_FORMAT_I = "yyyy-MM-dd hh:mm:ss.S";
			//String DATE_FORMAT_O = "dd-MM-yyyy";

			SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
			//SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

			
			try {
				date = formatInput.parse(String.valueOf(committeRow.get("MeetingDate")));
				committe.setMeetingDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//committe.setMeetingDate((Date) dateString);
			//logger.info(committe.getMeetingDate().toString());
			
			committesList.add(committe);
		}

		return committesList;
	}
	
	@Override
	public List<Committe> getAllSearchByMember(int userId,int page,int offset,String date_from, String date_to, String title) {
		// TODO Auto-generated method stub
		//int limit = 5;
		//if(limit == 0)
			//limit = 5;
		//cast date_from to right pattern
		
	   SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
	   SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
	   java.util.Date date;
	   try {
	     date = originalFormat.parse(date_from);
	     System.out.println("Old Format from:   " + originalFormat.format(date));
	     System.out.println("New Format from:   " + targetFormat.format(date));
	     date_from=targetFormat.format(date);

	    } catch (ParseException ex) {
	      // Handle Exception.
	    }
	   
	   //cast date_to to right pattern
	   
	   try {
	     date = originalFormat.parse(date_to);
	     System.out.println("Old Format to:   " + originalFormat.format(date));
	     System.out.println("New Format to:   " + targetFormat.format(date));
	     date_to=targetFormat.format(date);

	    } catch (ParseException ex) {
	      // Handle Exception.
	    }
		
		List<Committe> committesList = new ArrayList<Committe>();
		// JDBC Code - Start
		String query_plus="";
		if(title != "")
			query_plus=" where com.Title like '%" + title + "%' ";
		
		if(date_from != "")
		{
			if(query_plus != "")
			{
				query_plus= query_plus + " AND com.MeetingDate>='" + date_from + "' ";
			}
			else
			{
				query_plus= " where com.MeetingDate>='" + date_from + "' ";
			}
		}
		
		if(date_to != "")
		{
			if(query_plus != "")
			{
				query_plus= query_plus + " AND com.MeetingDate<='" + date_to + "' ";
			}
			else
			{
				query_plus= " where com.MeetingDate<='" + date_to + "' ";
			}
		}
		
		String query="";
		if(query_plus != "")
		{
			query = "select com.Id as Id,com.Title as Title, com.GeneralInfo as GeneralInfo, com.MeetingDate as MeetingDate, r.RoleId as RoleId from Committe com " + 
		"join Member mem on mem.CommitteId=com.Id join User_Role r on r.CommitteId=com.Id " + query_plus + " and mem.UserId = " + userId + " and r.UserId=" + userId;//+ " limit " + limit + " offset " + offset;
		}
		else
		{
			query = "select com.Id as Id,com.Title as Title, com.GeneralInfo as GeneralInfo, com.MeetingDate as MeetingDate, r.RoleId as RoleId from Committe com " + 
					"join Member mem on mem.CommitteId=com.Id join User_Role r on r.CommitteId=com.Id and mem.UserId = " + userId + " and r.UserId=" + userId;
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> committesRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> committeRow : committesRows) {
			Committe committe = new Committe();
			committe.setId(Integer.parseInt(String.valueOf(committeRow.get("Id"))));
			committe.setTitle(String.valueOf(committeRow.get("Title")));
			committe.setGeneralInfo(String.valueOf(committeRow.get("GeneralInfo")));
			//logger.info(String.valueOf(committeRow.get("MeetingDate")));
			
			String DATE_FORMAT_I = "yyyy-MM-dd hh:mm:ss.S";
			//String DATE_FORMAT_O = "dd-MM-yyyy";

			SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
			//SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

			
			try {
				date = formatInput.parse(String.valueOf(committeRow.get("MeetingDate")));
				committe.setMeetingDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//get role in committe
			if(Integer.parseInt(String.valueOf(committeRow.get("RoleId")))==2)
			{
				committe.setIsCommitteAdmin(true);
			}
			else if(Integer.parseInt(String.valueOf(committeRow.get("RoleId")))==3)
			{
				committe.setIsCommitteAdmin(false);
			}
			
			committesList.add(committe);
		}

		return committesList;
	}

}
