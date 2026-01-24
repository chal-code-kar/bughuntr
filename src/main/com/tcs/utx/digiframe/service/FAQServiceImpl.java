package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.FAQDAO;
import com.tcs.utx.digiframe.model.Faq;
import com.tcs.utx.digiframe.util.TSSVStringUtil;


@Service
public class FAQServiceImpl implements FAQService {

	 @Autowired
	    private FAQDAO faqDAO;

	@Override
	public void FaqPostQuery(Faq faqquery) {
		 this.faqDAO.FaqPostQuery(faqquery);
		
	}
	
	@Override
	public boolean validateFAQPost(Faq faqquery) {

		if (faqquery.getFaqtitle() == null || faqquery.getFaqtitle().length() > 1000 || !TSSVStringUtil.matchPattern(faqquery.getFaqtitle().trim(), TSSVStringUtil.PATTERN_ASVSQUESTION)) {
			return false;
		} else if ((faqquery.getDescription() == null)) {
			return false;
		} else {
			;
		}
		return true;
	}

	
	 @Override
	   public List<Map<String, Object>> FaqAllQuery() {
	       return this.faqDAO.FaqAllQuery();
	   }
	 
	 @Override
	   public void deleteFAQ(int id) {
	       this.faqDAO.deleteFAQ(id);
	       
	   }
	 @Override
		public boolean validateFAQUpdate(Faq editFaq) {

			if (editFaq.getFaqtitle() == null || editFaq.getFaqtitle().length() > 500) {
				return false;
			} else if (editFaq.getDescription() == null) {
				return false;
			} else {
				;
			}
			return true;
		}
	 

	  @Override
	   public void updateFAQ(int id, Faq editFaq) {
	       this.faqDAO.updateFAQ(id,editFaq);
	       
	   }
  
	
}
