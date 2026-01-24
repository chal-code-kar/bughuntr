package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.BlogDAO;
import com.tcs.utx.digiframe.model.Blog;
import com.tcs.utx.digiframe.util.TSSVStringUtil;


@Service
public class BlogServiceImpl implements BlogService{
	@Autowired
    private BlogDAO blogDao;

    @Override
    public List<Map<String, Object>> getBlogs() {
        return this.blogDao.getBlogs();
    }

	@Override
	public void addBlog(Blog blog, int emp_id) {
		this.blogDao.addBlog(blog, emp_id);
	}

	@Override
	public void deleteBlog(int srno) {
		this.blogDao.deleteBlog(srno);
		
	}

	@Override
	public boolean validateBlog(Blog blog) {
		if (blog.getTitle() == null || blog.getTitle().trim().length() > 100) {
            return false;
        } else if(blog.getCategory() == null || blog.getCategory().trim().length() < 2 || blog.getCategory().trim().length() > 60 || !TSSVStringUtil.matchPattern(blog.getCategory().trim(), TSSVStringUtil.PATTERN_FOR_NAMES)) {
        	return false;
        } else if(blog.getBrief() == null ) {
        	return false;
        } else {
            return true;
        }
	}

	@Override
	public void updateBlog(Blog blog) {
		this.blogDao.updateBlog(blog);
	}

}
