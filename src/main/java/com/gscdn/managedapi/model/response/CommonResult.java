package com.gscdn.managedapi.model.response;

import io.swagger.annotations.ApiParam;

public class CommonResult {
	@ApiParam(value = "응답 성공여부 : true/false")
	private boolean sucess;
	@ApiParam(value = "응답 코드 번호")
	private int code;
	@ApiParam(value = "응답 메시지")
	private String msg;
	
	public boolean isSucess() {
		return sucess;
	}

	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
