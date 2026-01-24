package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Blog;

public interface BlogDAO {

	public List<Map<String, Object>> getBlogs();
	
	public void addBlog(Blog blog, int emp_id);
    
    public void deleteBlog(int srno);
    
    public void updateBlog(Blog blog);

}
