
package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Helper {


    int srno;
    @NotBlank @Size(max = 255)
    private String name;
    @NotBlank @Size(max = 5000)
    private String description;
    @Min(0)
    private int parent_srno;
    
  
    public int getSrno() {
        return srno;
    }
    public int getParent_srno() {
        return parent_srno;
    }
    public void setParent_srno(int parent_srno) {
        this.parent_srno = parent_srno;
    }
    public void setSrno(int srno) {
        this.srno = srno;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
 
}
