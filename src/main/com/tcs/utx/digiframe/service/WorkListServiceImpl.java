package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tcs.utx.digiframe.dao.WorkListDAO;

@Service
public class WorkListServiceImpl implements WorkListService{

    @Autowired
    private WorkListDAO dao;

    @Override
    public Map<String, Object> getWorklistForResearcher(int emp_id) {
        return this.dao.getWorklistForResearcher(emp_id);
    }
    
    @Override
    public Map<String, Object> getWorklistForProgramAdmin(int emp_id) {
        return this.dao.getWorklistForProgramAdmin(emp_id);
    }

    @Override
    public List<Map<String, Object>> reportsForResearcher(int emp_id, String role) {
        return this.dao.reportsForResearcher(emp_id, role);
    }

    @Override
    public List<Map<String, Object>> reportsForAdmin(int emp_id) {
        return this.dao.reportsForAdmin(emp_id);
    }
}
