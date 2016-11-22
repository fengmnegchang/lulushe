package com.fgj.lulushe.activity.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 通用格式解析工具类
 *
 */
public class JsonUtils {
	
	
	/**
	 * 根据传入的string解析出JsonBean对象
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> JsonBean<T> parseJsonBean(String jsonStr,Class<T> clazz)
			throws Exception {
		
		JsonBean<T> bean = new JsonBean<T>(); //返回对象
		
		List<T> list = null; //包含的实体列表
		JSONObject jObject = new JSONObject(jsonStr);

		bean.setCode(jObject.getInt("code"));
		bean.setMessage(jObject.getString("message"));
		String resultStr = jObject.getString("result");
		if(!resultStr.equals("null")) {
			list = new ArrayList<T>();
			JSONArray jArray = jObject.getJSONArray("result");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jso = (JSONObject) jArray.opt(i);

				Field[] fs = clazz.getDeclaredFields();

				T t = clazz.newInstance();

				for (Field field : fs) {
					String fieldName = field.getName();
					if ("_id".equals(fieldName)) {
						continue;
					}
					
					
					Method m = clazz.getDeclaredMethod(
							"set" + CommTool.toUpperCaseFirstOne(fieldName),
							field.getType());
					Object arg = jso.get(fieldName);
					if (!arg.toString().equals("null")) {
						
						if(field.getType().getName().equals("int")) {
							m.invoke(t, Integer.valueOf(arg.toString()));
						} else if(field.getType().getName().equals("long")) {
							m.invoke(t, Long.valueOf(arg.toString()));
						}  else if(field.getType().getName().equals("short")) {
							m.invoke(t, Short.valueOf(arg.toString()));
						} else if(field.getType().getName().equals("Date")){
							m.invoke(t, Date.parse(arg.toString()));
						}else {
							m.invoke(t, arg);
						}
						
					}
				}
				list.add(t);
			}
			
			bean.setResult(list);
		}
		
		
		return bean;
	}
}
