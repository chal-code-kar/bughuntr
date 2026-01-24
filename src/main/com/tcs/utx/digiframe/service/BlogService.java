package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Blog;
import com.tcs.utx.digiframe.model.Helper;

public interface BlogService {

	public List<Map<String, Object>> getBlogs();
	
	public void addBlog(Blog blog,int emp_id);
    
    public void deleteBlog(int srno);

    public boolean validateBlog(Blog blog);

	public void updateBlog(Blog blog);
	
	

}
