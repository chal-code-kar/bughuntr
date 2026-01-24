package com.tcs.utx.digiframe.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.Faq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class FAQDAOImpl implements FAQDAO {
	private static final Logger LOG = LoggerFactory.getLogger(FAQDAOImpl.class);
	
	private static final String ERROR_MSG_1 = "FAQDAOImpl | Exception in FaqPostQuery - ";
	
	private static final String ERROR_MSG_2 = "FAQDAOImpl | Exception in FaqAllQuery - ";
	
	private static final String ERROR_MSG_3 = "FAQDAOImpl | Exception in deleteFAQ - ";
	
	private static final String ERROR_MSG_4 = "FAQController | Error in  updateFAQ - ";

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Override
	public void FaqPostQuery(Faq faqquery) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.FaqPostQuery, faqquery.getFaqtitle(), faqquery.getDescription());
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_1, e);
		}
	}

	@Override
	public List<Map<String, Object>> FaqAllQuery() {
		try {
			List<Map<String, Object>> faqdatas = jdbcTemplate.queryForList(BugHuntrQueryConstants.FaqAllQuery);
			return faqdatas;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_2, e);
			return null;

		}

	}

	@Override
	public void deleteFAQ(int id) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.FaqDelete, id);
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_3, e);
		}

	}

	@Override
	public void updateFAQ(int id, Faq editFaq) {
		try {
			jdbcTemplate.update("Update b_faq set faqtitle='"+editFaq.getFaqtitle()+"', description='"+editFaq.getDescription()+"' WHERE srno='"+id+"'");

		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_4, e);
		}

	}
}

