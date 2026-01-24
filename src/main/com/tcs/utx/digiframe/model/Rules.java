package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rules {
	
	
    public String rules_category;
    public String rules_details;
    
    public String getRules_category() {
        return rules_category;
    }
    public void setRules_category(String rules_category) {
        this.rules_category = rules_category;
    }
    public String getRules_details() {
        return rules_details;
    }
    public void setRules_details(String rules_details) {
        this.rules_details = rules_details;
    }
    
    
}
