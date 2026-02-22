package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Researcher {

    int id;
    @NotBlank @Size(max = 255)
    String Name;
    @Size(max = 2000)
    String Bio;
    @Size(max = 500)
    String Avatar_blob;
    @Size(max = 1000)
    String Skills;
    @Size(max = 500)
    String avatar;
    @Size(max = 500)
    String teams;
    int Srno;
    long points;
    long total_bounty_earned;
    @Size(max = 500)
    String profile_pic;
    
    
    
    
    public String getName() {
        return Name;
    }

   

    public String getTeams() {
		return teams;
	}



	public void setTeams(String teams) {
		this.teams = teams;
	}



	public void setName(String name) {
        Name = name;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar_blob() {
        return Avatar_blob;
    }

    public void setAvatar_blob(String avatar_blob) {
        Avatar_blob = avatar_blob;
    }

    public String getSkills() {
        return Skills;
    }

    public void setSkills(String skills) {
        Skills = skills;
    }

    public int getSrno() {
        return Srno;
    }

    public void setSrno(int srno) {
        Srno = srno;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public long getTotal_bounty_earned() {
		return total_bounty_earned;
	}

	public void setTotal_bounty_earned(long totalRewardEarned) {
		this.total_bounty_earned = totalRewardEarned;
	}
    
    

}
