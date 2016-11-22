package com.fgj.lulushe.activity.util;

import java.io.Serializable;
import java.util.List;

/**
 * 接口请求返回通用格式实体类
 * @author EX-WANLIYONG001
 *
 * @param <T>
 */
public class JsonBean<T> implements Serializable{
	
	private static final long serialVersionUID = -6182189632617616248L;
	
	private int code; //状态码
	private String message; //状态文本描述
	private List<T> result; //结果列表集

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

}
