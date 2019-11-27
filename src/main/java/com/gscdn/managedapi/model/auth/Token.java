package com.gscdn.managedapi.model.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
	private String access_token;
	private String token_type;
	private String expires_in;
	private String scope;
	private String CustType;
	private String level;
	private String CustNm;
	private String CustomerID;
	private String EMail;
	private String Mobile;
	private String empstate;
	private String CustID;
	private String EmpNM;
	private String permit;
	private String MasterAct;
	private String OwnAccounts;
	private String DeptName;
	private String jti;
}
