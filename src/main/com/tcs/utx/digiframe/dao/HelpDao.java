package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Helper;
import com.tcs.utx.digiframe.model.help_wordcloud;

public interface HelpDao {

    public List<Map<String,Object>> gethelpdetails();

    public void addhelp(Helper help);
    
   
    public List<Map<String,Object>> getWordcloud();

	public void help_wordcloud(help_wordcloud word);

	public void deletehelp(int srno);

	public void updatehelp(Helper help);


}