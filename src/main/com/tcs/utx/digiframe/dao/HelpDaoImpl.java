package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.Helper;
import com.tcs.utx.digiframe.model.help_wordcloud;
@Repository
public class HelpDaoImpl implements HelpDao {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> gethelpdetails() {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.gethelpdetails);
        return data;
    }

    @Override
    public void addhelp(Helper help) {
        jdbcTemplate.update(BugHuntrQueryConstants.addhelp,help.getName(),help.getDescription(),help.getParent_srno());
    }
    @Override
    public List<Map<String, Object>> getWordcloud() {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(BugHuntrQueryConstants.getData);
        return data;
    }
    
    @Override
    public void deletehelp(int srno) {
       jdbcTemplate.update(BugHuntrQueryConstants.deletehelp,srno);        
    }
    
    @Override
    public void updatehelp(Helper help) {
    	jdbcTemplate.update("UPDATE help SET name = ?, description = ?, parentsrno = ? WHERE srno = ?",
    			help.getName(), help.getDescription(), help.getParent_srno(), help.getSrno());
    }
    
    @Override
    public void help_wordcloud(help_wordcloud word) {
        List<Map<String, Object>> data = jdbcTemplate.queryForList("Select * from b_help_wordcloud where word=?", word.getWord());
        if(data.size()==0) {
            jdbcTemplate.update(BugHuntrQueryConstants.insertWord,word.getWord());
        }
        else
        {
            int count=(int) data.get(0).get("count");
            jdbcTemplate.update(BugHuntrQueryConstants.updateCount,count+1,word.getWord());
        }
            
    }

}