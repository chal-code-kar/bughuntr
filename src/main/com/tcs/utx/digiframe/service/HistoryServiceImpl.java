package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.HistoryDAO;
import com.tcs.utx.digiframe.model.History;
import com.tcs.utx.digiframe.util.TSSVStringUtil;

@Service
public class HistoryServiceImpl implements HistoryService {

	@Autowired
	private HistoryDAO HistoryDAO;

	@Override
	public List<Map<String, Object>> getHistory() {
		return this.HistoryDAO.getHistory();
	}

	@Override
	public void Posthistory(History posthistory) {
		this.HistoryDAO.Posthistory(posthistory);

	}

	@Override
	public void updateHistory(int srno, History edithistory) {
		this.HistoryDAO.updateHistory(srno, edithistory);
	}

	@Override
	public void deleteHistory(int srno) {
		this.HistoryDAO.deleteHistory(srno);

	}

	@Override
	public boolean validateAddHistory(History posthistory) {

		if (posthistory.getFontname() == null || posthistory.getFontname().trim().length() <1   || posthistory.getFontname().trim().length() > 50) {
			return false;
		} else if (posthistory.getReleaseverinfo() == null || posthistory.getReleaseverinfo().trim().length() < 1 || posthistory.getReleaseverinfo().trim().length() > 100
				|| !TSSVStringUtil.matchPattern(posthistory.getReleaseverinfo().trim(),
						TSSVStringUtil.PATTERN_MARKDOWN)) {
			return false;
		} else if (posthistory.getReleaseitemname() == null  || posthistory.getReleaseitemname().trim().length() < 1 || posthistory.getReleaseitemname().trim().length() > 1000
				|| !TSSVStringUtil.matchPattern(posthistory.getReleaseitemname().trim(),
						TSSVStringUtil.PATTERN_MARKDOWN)) {
			return false;
		} else {

			;
		}

		return true;
	}

}
