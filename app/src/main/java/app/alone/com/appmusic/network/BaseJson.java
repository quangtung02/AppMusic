package app.alone.com.appmusic.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseJson{
	
	public int error = -1;
	public String message;
	public int count = 0;
	public String data;
	
	// Use when have extra data
	public JSONObject allObject;
	
	public <T> T to(Class<T> _class) {
		Gson gson = new Gson();
		return gson.fromJson(data, _class);
	}
	
	public Boolean isSuccess() {
		return error == ErrorCode.SUCCESS;
	}
	
	public String getErrorMessage() {
		return message;
	}
	
	public <T> List<T> toList(Class<T> _class) {
		List<T> listData = new ArrayList<T>();
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(data);
		} catch (JSONException e1) {
			error = ErrorCode.ERROR_PARSER;
			message = e1.getMessage();
			return listData;
		}
		Gson g = new Gson();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				// showLog(ja.getString(i), myClass);
				listData.add(g.fromJson(jsonArray.getString(i), _class));
				// listener.loading(i,ja.length());
			}
		} catch (JSONException e) {
			error = ErrorCode.ERROR_PARSER;
			message = e.getMessage();
		} catch (JsonSyntaxException e) {
			error = ErrorCode.ERROR_PARSER;
			message = e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			error = ErrorCode.ERROR_PARSER;
			message = e.getMessage();
			e.printStackTrace();
		}
		return listData;
	}
	
	public <T> List<T> appendList(Class<T> _class, List<T> listData) {
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(data);
		} catch (JSONException e1) {
			error = ErrorCode.ERROR_PARSER;
			message = e1.getMessage();
			return listData;
		}
		Gson g = new Gson();
		try {

			for (int i = 0; i < jsonArray.length(); i++) {
				// showLog(ja.getString(i), myClass);
				listData.add(g.fromJson(jsonArray.getString(i), _class));
				// listener.loading(i,ja.length());
			}
		} catch (JSONException e) {
			error = ErrorCode.ERROR_PARSER;
			message = e.getMessage();
		}
		return listData;
	}
}
