package com.hua.committee.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hua.committee.model.Document;

public class DocumentDAOImpl implements DocumentDAO{

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void save(Document document) {
		// TODO Auto-generated method stub
		//Date date = new Date();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(
				"insert into Document (CommitteId,Title,Type,Content) values (?, ?, ?, ?)",
				document.getCommitteId(), document.getTitle(), document.getType(), document.getContent());
	}

	@Override
	public Document getById(int id) {
		// TODO Auto-generated method stub
		String query = "select * from Document where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object queryForObject = jdbcTemplate.queryForObject(query, new Object[] { id },
				new BeanPropertyRowMapper<Document>(Document.class));
		Document document = (Document) queryForObject;

		return document;
	}

	@Override
	public List<Document> getByCommitteId(int id) {
		// TODO Auto-generated method stub
		List<Document> documents = new ArrayList<Document>();
		
		String query = "select Id,CommitteId,Title,Type from Document where CommitteId = " + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	
		List<Map<String, Object>> Rows = jdbcTemplate.queryForList(query);
	
		for (Map<String, Object> Row : Rows) {
			Document doc = new Document();
			doc.setId(Integer.parseInt(String.valueOf(Row.get("Id"))));
			doc.setCommitteId(Integer.parseInt(String.valueOf(Row.get("CommitteId"))));
			doc.setTitle(String.valueOf(Row.get("Title")));
			doc.setType(String.valueOf(Row.get("Type")));
			documents.add(doc);
		}
		
		return documents;
	}
	
	@Override
	public List<Document> getByCommitteIdWithContent(int id) {
		// TODO Auto-generated method stub
		List<Document> documents = new ArrayList<Document>();
		
		String query = "select Id,CommitteId,Title,Type,Content from Document where CommitteId = " + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	
		List<Map<String, Object>> Rows = jdbcTemplate.queryForList(query);
	
		for (Map<String, Object> Row : Rows) {
			Document doc = new Document();
			doc.setId(Integer.parseInt(String.valueOf(Row.get("Id"))));
			doc.setCommitteId(Integer.parseInt(String.valueOf(Row.get("CommitteId"))));
			doc.setTitle(String.valueOf(Row.get("Title")));
			doc.setType(String.valueOf(Row.get("Type")));
			doc.setContent((byte[]) Row.get("Content"));
			documents.add(doc);
		}
		
		return documents;
	}

	@Override
	public void update(Document document) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(int id) {
		 
		String query = "delete from Document where id = " + id;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute(query);

	}

	@Override
	public List<Document> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getByIdWithoutContent(int id) {
		// TODO Auto-generated method stub
		String query = "select Id,CommitteId,Title,Type from Document where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Object queryForObject = jdbcTemplate.queryForObject(query, new Object[] { id },
				new BeanPropertyRowMapper<Document>(Document.class));
		Document document = (Document) queryForObject;

		return document;
	}

}
