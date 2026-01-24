package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Faq;

public interface FAQService {
	
	 public boolean validateFAQPost(Faq faqquery);

	 public void FaqPostQuery(Faq faqquery);
	
	 public List<Map<String, Object>> FaqAllQuery();
   
	 public void deleteFAQ(int id);
	 
	 public void updateFAQ(int id, Faq editFaq);

	public boolean validateFAQUpdate(Faq editFaq);
}
