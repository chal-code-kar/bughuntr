package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class BlogDAOImpl implements BlogDAO{
	private static final Logger LOG = LoggerFactory.getLogger(BlogDAOImpl.class);
	
	private static final String ERROR_MSG_1 = "BlogDAOImpl | Error in getBlogs - ";
	private static final String ERROR_MSG_2 = "BlogDAOImpl | Error in addBlog - ";
	private static final String ERROR_MSG_3 = "BlogDAOImpl | Error in deleteBlog - ";
	private static final String ERROR_MSG_4 = "BlogDAOImpl | Error in updateBlog - ";
	
	
	@Autowired
    public JdbcTemplate jdbcTemplate;
	
	@Override
    public List<Map<String, Object>> getBlogs() {
		try {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getBlogs);
        return data;
    }catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_1, e);
        return null;
    }
	}

	@Override
	public void addBlog(Blog blog, int emp_id) {
		try {
		String emp= Integer.toString(emp_id);
		jdbcTemplate.update("INSERT INTO b_blog (title, brief, emp_name, createdate, category) "
				+ "VALUES ('" + blog.getTitle() + "', '" + blog.getBrief() + "', (SELECT full_name FROM ssa_eai_hr_emp_basic_dtls WHERE employee_number = '" + emp + "'), NOW(), '" + blog.getCategory() + "')");
		}catch(DataAccessException e) {
	    	LOG.error(ERROR_MSG_2, e);
		}
	}

	@Override
	public void deleteBlog(int srno) {
		try {
		jdbcTemplate.update(BugHuntrQueryConstants.deleteBlog,srno);   
		
	}catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_3, e);
	}
}

	@Override
	public void updateBlog(Blog blog) {
		try {
		jdbcTemplate.update("UPDATE b_blog SET title = '" + blog.getTitle() + "', brief = '" + blog.getBrief() + "', category = '" + blog.getCategory() + "' WHERE srno = '" + blog.getSrno() + "'");
		}catch(DataAccessException e) {
    	LOG.error(ERROR_MSG_4, e);
	}
}


}
