package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

public interface WorkListDAO {

    public Map<String, Object> getWorklistForResearcher(int emp_id);

    public Map<String, Object> getWorklistForProgramAdmin(int emp_id);
    
    public List<Map<String, Object>> reportsForResearcher(int emp_id, String role);
    
    public List<Map<String, Object>> reportsForAdmin(int emp_id);
    
}
