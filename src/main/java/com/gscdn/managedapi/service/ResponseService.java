package com.gscdn.managedapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gscdn.managedapi.model.response.CommonResult;
import com.gscdn.managedapi.model.response.ListResult;
import com.gscdn.managedapi.model.response.SingleResult;
@Service
public class ResponseService {
	public enum CommonResponse{
		SUCCESS(0,"성공"),
		FAIL(-1,"실패");
		
		int code;
		String msg;
		
		private CommonResponse(int code, String msg) {
			this.code = code;
			this.msg = msg;
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
	
	public <T> SingleResult<T> getSingleResult(T data) {
		SingleResult<T> result = new SingleResult<>();
		result.setData(data);
		setSucessResult(result);
		return result;
	}
	
	public <T> ListResult<T> getListResult(List<T> data) {
		ListResult<T> result = new ListResult<>();
		result.setList(data);
		setSucessResult(result);
		return result;
	}
	public CommonResult getSuccessResult() {
		CommonResult result = new CommonResult();
		setSucessResult(result);
		return result;
	}
	
	public CommonResult getFailResult() {
		CommonResult result = new CommonResult();
		result.setSucess(false);
		result.setCode(CommonResponse.FAIL.code);
		result.setMsg(CommonResponse.FAIL.msg);
		return result;
	}
	
	private void setSucessResult(CommonResult result) {
		result.setSucess(true);
		result.setCode(CommonResponse.SUCCESS.getCode());
		result.setMsg(CommonResponse.SUCCESS.getMsg());
	}
}
