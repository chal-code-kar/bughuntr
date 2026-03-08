package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Jobs {

    @NotBlank @Size(max = 100)
    public String jobtype;
    @Min(0)
    public int pricing;
    @Min(0)
    public int maxpricing;
    @NotBlank @Size(max = 50)
    public String reward_type;
    public String getJobtype() {
        return jobtype;
    }
    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }
    public int getPricing() {
        return pricing;
    }
    public void setPricing(int pricing) {
        this.pricing = pricing;
    }
    public int getMaxpricing() {
        return maxpricing;
    }
    public void setMaxpricing(int maxpricing) {
        this.maxpricing = maxpricing;
    }
    public String getReward_type() {
        return reward_type;
    }
    public void setReward_type(String reward_type) {
        this.reward_type = reward_type;
    }

    

}
