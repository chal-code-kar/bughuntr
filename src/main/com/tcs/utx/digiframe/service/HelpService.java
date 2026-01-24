package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;
import com.tcs.utx.digiframe.model.Helper;
import com.tcs.utx.digiframe.model.help_wordcloud;

public interface HelpService {

    public List<Map<String,Object>> gethelpdetails();

    public void addhelp(Helper help);

    public String validateHelp(Helper help);

    public List<Map<String,Object>> wordcloud();

	public void help_wordcloud(help_wordcloud word);

	public void deleteHelp(int id);

	public void updateHelp(Helper help);
}
