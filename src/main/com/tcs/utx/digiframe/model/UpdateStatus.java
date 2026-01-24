package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateStatus {
	
    private String approver;
    private String status;
    private int approved_amount;
    private String rejectRemarks;
    
    public String getApprover() {
        return approver;
    }
    public void setApprover(String approver) {
        this.approver = approver;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getApproved_amount() {
        return approved_amount;
    }
    public void setApproved_amount(int approved_amount) {
        this.approved_amount = approved_amount;
    }
    public String getRejectRemarks() {
        return rejectRemarks;
    }
    public void setRejectRemarks(String rejectRemarks) {
        this.rejectRemarks = rejectRemarks;
    }
}
