package com.tcs.utx.digiframe.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.utx.digiframe.model.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Repository
public class HistoryDAOImpl implements HistoryDAO {
	private static final Logger LOG = LoggerFactory.getLogger(HistoryDAOImpl.class);

	private static final String ERROR_MSG_1 = "HistoryDAOImpl | Error in  getHistory - ";

	private static final String ERROR_MSG_2 = "HistoryDAOImpl | Error in  Posthistory - ";

	private static final String ERROR_MSG_3 = "HistoryDAOImpl | Error in  updateHistory - ";

	private static final String ERROR_MSG_4 = "HistoryDAOImpl | Error in  deleteHistory - ";
	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getHistory() {
		try {
			List<Map<String, Object>> showhistory = jdbcTemplate.queryForList(BugHuntrQueryConstants.GETHistory);
			return showhistory;
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_1, e);
			return null;

		}
	}

	@Override
	public void Posthistory(History posthistory) {
		try {
			jdbcTemplate.update("INSERT INTO b_bughuntr_announcement(fontname, releaseverinfo, releaseitemname) VALUES(?, ?, ?)",
					posthistory.getFontname(), posthistory.getReleaseverinfo(), posthistory.getReleaseitemname());
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_2, e);

		}
	}

	@Override
	public void updateHistory(int srno, History edithistory) {
		try {
			jdbcTemplate.update("UPDATE b_bughuntr_announcement SET fontname = ?, releaseverinfo = ?, releaseitemname = ? WHERE srno = ?",
					edithistory.getFontname(), edithistory.getReleaseverinfo(), edithistory.getReleaseitemname(), srno);
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_3, e);
		}
	}

	@Override
	public void deleteHistory(int srno) {
		try {
			jdbcTemplate.update(BugHuntrQueryConstants.deleteHistory, srno);
		} catch (DataAccessException e) {
			LOG.error(ERROR_MSG_4, e);
		}

	}
}
