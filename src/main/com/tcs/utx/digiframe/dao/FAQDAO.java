package com.tcs.utx.digiframe.dao;


import java.util.List;
import java.util.Map;

import com.tcs.utx.digiframe.model.Faq;

public interface FAQDAO {

	void FaqPostQuery(Faq faqquery);

	 public List<Map<String, Object>> FaqAllQuery();
     
	 public void deleteFAQ(int id);

	 public void updateFAQ(int id, Faq editFaq);
}
