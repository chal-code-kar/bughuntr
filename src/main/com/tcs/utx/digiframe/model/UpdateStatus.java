package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties(ignoreUnknown = false)
public class UpdateStatus {

    @Size(max = 100)
    private String approver;
    @NotBlank @Size(max = 50)
    private String status;
    @Min(0) @Max(1000000)
    private int approved_amount;
    @Size(max = 2000)
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
