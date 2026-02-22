package com.tcs.utx.digiframe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@JsonIgnoreProperties(ignoreUnknown = false)
public class Role {



    @NotBlank @Size(max = 100)
    private String roleName;
    private int role_bounty_id;
    public int getRole_bounty_id() {
        return role_bounty_id;
    }
    public void setRole_bounty_id(int role_bounty_id) {
        this.role_bounty_id = role_bounty_id;
    }
    @Size(max = 500)
    private String roleDescription;
    @NotBlank @Size(max = 100)
    private String module;
    @NotBlank @Size(max = 100)
    private String submodule;
    @NotBlank @Size(max = 50)
    private String permission;
    private boolean status;

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleDescription() {
        return roleDescription;
    }
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }
    public String getSubmodule() {
        return submodule;
    }
    public void setSubmodule(String submodule) {
        this.submodule = submodule;
    }
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

}
