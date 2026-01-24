package com.tcs.utx.digiframe.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.utx.digiframe.dao.ETLDAO;
import com.tcs.utx.digiframe.model.Etldata;
import com.tcs.utx.digiframe.util.TSSVStringUtil;

@Service
public class ETLServiceImpl implements ETLService {

	 @Autowired
	 private ETLDAO ETLDAO;

	@Override
	public List<Map<String, Object>> ETLData() {
		return this.ETLDAO.ETLData();
	}


	@Override
	public void Postetldata(Etldata postetldata) {
		this.ETLDAO.Postetldata(postetldata);
		
	}


	
	@Override
	public boolean validateETLPost(Etldata postetldata) {
		String empid = ""+postetldata.getEmployee_number();
		if (empid.length() == 0 ||  empid.length() < 3 || empid.length() > 10) {
			return false;
		} 
		else if (postetldata.getFull_name() == null || postetldata.getFull_name().length() > 100 || !TSSVStringUtil
	                .matchPattern(postetldata.getFull_name().trim(), TSSVStringUtil.PATTERN_FOR_NAMES)) {
	            return false;
        }else if (postetldata.getIou_name() == null || postetldata.getIou_name().length() > 100 || !TSSVStringUtil
                .matchPattern(postetldata.getIou_name().trim(), TSSVStringUtil.PATTERN_FOR_NAMES)) {
            return false;
        }else {
		; }
		 return true;
	}

		
	}

	
	
		
	

