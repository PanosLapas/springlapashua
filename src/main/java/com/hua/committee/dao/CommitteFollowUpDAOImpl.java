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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.hua.committee.HomeController;
import com.hua.committee.model.CommitteFollowUp;
import com.hua.committee.dao.MemberDAO;

public class CommitteFollowUpDAOImpl implements CommitteFollowUpDAO{
	
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
	public CommitteFollowUp save(CommitteFollowUp history) {
		// TODO Auto-generated method stub
		Date date =new java.sql.Date(Calendar.getInstance().getTime().getTime());
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		logger.info("committeFollowUp saving");
		String sql = "";
		if(history.getCommitteId() > 0)
			sql = "insert into CommitteFollowUp (CommitteId,UserId,MemberId,CreationDate,Action) values (?, ?, ?, ?, ?)";
		else
			sql = "insert into CommitteFollowUp (UserId,MemberId,CreationDate,Action) values (?, ?, ?, ?)";
	    
	    SqlUpdate su = new SqlUpdate();
	    su.setDataSource(dataSource);
	    su.setSql(sql);
	    if(history.getCommitteId() > 0)
	    	su.declareParameter(new SqlParameter(Types.INTEGER));
	    
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.declareParameter(new SqlParameter(Types.INTEGER));
	    su.declareParameter(new SqlParameter(Types.DATE));
	    su.declareParameter(new SqlParameter(Types.VARCHAR));
	    su.setReturnGeneratedKeys(true);
	    su.compile();
	    
	    Object[] params = new Object[]{};
	    if(history.getCommitteId() > 0)
	    	params = new Object[]{history.getCommitteId(),history.getUserId(),history.getMemberId(),date,history.getAction()};
	    else
	    	params = new Object[]{history.getUserId(),history.getMemberId(),date,history.getAction()};
	    
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    su.update(params,keyHolder);
	    int id = keyHolder.getKey().intValue();
		
        logger.info("done saving FollowUp");
		history.setId(id);
		
		return history;
	}

	@Override
	public List<CommitteFollowUp>  getByFilter(ArrayList<Integer> CommitteIds, ArrayList<Integer> UserIds, String date_) {
		// TODO Auto-generated method stub
	   SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
	   SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
	   java.util.Date date;
	   
	   try {
	     date = originalFormat.parse(date_);
	     System.out.println("Old Format from:   " + originalFormat.format(date));
	     System.out.println("New Format from:   " + targetFormat.format(date));
	     date_=targetFormat.format(date);

	    } catch (ParseException ex) {
	      // Handle Exception.
	    }
		
		List<CommitteFollowUp> historyList = new ArrayList<CommitteFollowUp>();
		// JDBC Code - Start
		String query_plus="";
		
		String committe_id_in = "";
	   for(int i=0; i<CommitteIds.size(); i++)
	   {
		   committe_id_in= committe_id_in + CommitteIds.get(i).toString() + ",";
	   }
		
		if(CommitteIds.size() > 0)
		{
			committe_id_in = committe_id_in.substring(0, committe_id_in.length() - 1);
			query_plus=" where CommitteId in(" + committe_id_in + ")";
		}
		
		String user_id_in = "";
	   for(int j=0; j<UserIds.size(); j++)
	   {
		   user_id_in= user_id_in + UserIds.get(j).toString() + ",";
	   }
		
		if(UserIds.size() > 0)
		{
			user_id_in = user_id_in.substring(0, user_id_in.length() - 1);
			if(query_plus != "")
			{
				query_plus= query_plus + " and UserId in(" + user_id_in + ")";
			}
			else
			{
				query_plus=" where UserId in(" + user_id_in + ")";
			}
		}
		
		if(date_ != "")
		{
			if(query_plus != "")
			{
				query_plus= query_plus + " AND CreationDate='" + date_ + "' ";
			}
			else
			{
				query_plus= " where CreationDate='" + date_ + "' ";
			}
		}
		
		String query = "select * from CommitteFollowUp " + query_plus ;//+ " limit " + limit + " offset " + offset;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> historyRows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> historyRow : historyRows) {
			CommitteFollowUp history = new CommitteFollowUp();
			if(historyRow.get("CommitteId") != null)
				history.setCommitteId(Integer.parseInt(String.valueOf(historyRow.get("CommitteId"))));
			history.setUserId(Integer.parseInt(String.valueOf(historyRow.get("UserId"))));
			history.setMemberId(Integer.parseInt(String.valueOf(historyRow.get("MemberId"))));
			history.setAction(String.valueOf(historyRow.get("Action")));
			
			String DATE_FORMAT_I = "yyyy-MM-dd hh:mm:ss.S";
			//String DATE_FORMAT_O = "dd-MM-yyyy";

			SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
			//SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

			
			try {
				date = formatInput.parse(String.valueOf(historyRow.get("CreationDate")));
				history.setCreationDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//committe.setMeetingDate((Date) dateString);
			//logger.info(committe.getMeetingDate().toString());
			
			historyList.add(history);
		}

		return historyList;
	}
	
}
