package com.tcs.utx.digiframe.service;

import java.util.List;
import com.tcs.utx.digiframe.model.help_wordcloud;
import java.util.Map;

import com.tcs.utx.digiframe.dao.HelpDao;
import com.tcs.utx.digiframe.model.Helper;
import com.tcs.utx.digiframe.util.TSSVStringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelpServiceImpl implements HelpService{

    @Autowired
    private HelpDao helpDAO;

    @Override
    public List<Map<String, Object>> gethelpdetails() {
        return this.helpDAO.gethelpdetails();
    }

    @Override
    public void addhelp(Helper help) {
        this.helpDAO.addhelp(help);
    }

    @Override
    public String validateHelp(Helper help) {
        if (help.getName() == null || help.getName().trim().length() < 2 || help.getName().trim().length() > 100) {
            return "Validation failed for Menu Name";
        } else if ((help.getParent_srno()+"").length() !=0 && !TSSVStringUtil.matchPattern((help.getParent_srno()+"").trim(), TSSVStringUtil.PATTERN_NUMERIC)) {
            return "Validation failed for Parent Menu";
        } else if(help.getDescription() == null || help.getDescription().trim().length() < 4 ) {
        	return "Validation failed for Description";
        } else {
            return null;
        }
    }
    
    @Override
    public void deleteHelp(int srno) {
        this.helpDAO.deletehelp(srno);
    }
    
    @Override
    public void updateHelp(Helper help) {
        this.helpDAO.updatehelp(help);
    }
    
    @Override
    public List<Map<String, Object>> wordcloud() {
        return this.helpDAO.getWordcloud();
    }

    @Override
    public void help_wordcloud(help_wordcloud word) {
        this.helpDAO.help_wordcloud(word);
    }
}
