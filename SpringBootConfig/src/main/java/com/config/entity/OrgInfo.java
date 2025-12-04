package com.config.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "org_info")
public class OrgInfo {

    @Id
    private String pk_org_id;
	private String org_name;
    private String org_email;
    private String org_status;

    public String getPk_org_id() {
		return pk_org_id;
	}
	public void setPk_org_id(String pk_org_id) {
		this.pk_org_id = pk_org_id;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getOrg_email() {
		return org_email;
	}
	public void setOrg_email(String org_email) {
		this.org_email = org_email;
	}
	public String getOrg_status() {
		return org_status;
	}
	public void setOrg_status(String org_status) {
		this.org_status = org_status;
	}

    

}
