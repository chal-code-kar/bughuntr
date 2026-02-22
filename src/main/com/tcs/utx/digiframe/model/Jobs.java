package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Jobs {
	
    public String jobtype;
    public int pricing;
    public int maxpricing;
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
