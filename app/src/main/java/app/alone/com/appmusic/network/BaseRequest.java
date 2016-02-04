package app.alone.com.appmusic.network;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import app.alone.com.appmusic.main.MainApplication;
import app.alone.com.appmusic.network.api.ApiBase;
import app.alone.com.appmusic.network.api.RequestUtil;

public class BaseRequest extends Request<BaseJson> {
	public static final String error = "Code";
	public static final String message = "Message";
	public static final String total = "total";
	public String data = "DATA";
	private Listener<BaseJson> _responseListener;
	private ErrorListener errorListener;
	private String methodName = "";
	private int initialTimeoutMs = 20 * 1000;
	private int maxNumRetries = 1;
	private float backoffMultiplier = 1.0f;
	private Map<String, String> paras;
	private boolean haveExtraData = false;

	public BaseRequest(String methodName, String dataName,
			Map<String, String> para, boolean shouldCache,
			Listener<BaseJson> responseListener, ErrorListener errorListener) {
		super(Method.POST, MainApplication.baseUrl, errorListener);
		// Log.i("Request", genGetUrl(methodName, para));
		init(methodName, dataName, para, shouldCache, responseListener,
				errorListener);
	}

	public BaseRequest(String methodName, String dataName,
			Map<String, String> para, boolean shouldCache,
			Listener<BaseJson> responseListener, ErrorListener errorListener,
			boolean haveExtra) {
		super(Method.POST, MainApplication.baseUrl, errorListener);
		// Log.i("Request", genGetUrl(methodName, para));
		init(methodName, dataName, para, shouldCache, responseListener,
				errorListener);
		haveExtraData = haveExtra;
	}

	private void init(String methodName, String dataName,
			Map<String, String> para, boolean shouldCache,
			Listener<BaseJson> responseListener, ErrorListener errorListener) {
		_responseListener = responseListener;
		this.errorListener = errorListener;
		this.methodName = methodName;
		data = dataName;
		paras = para;
		setShouldCache(shouldCache);
		setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs, maxNumRetries,
				backoffMultiplier));
	}

	protected static String genGetUrl(String methodName,
			Map<String, String> para) {
		StringBuilder getParaBuilder = new StringBuilder();
		if (para != null && para.size() > 0) {
			getParaBuilder.append("?");
			Set<String> listKey = para.keySet();
			for (String key : listKey) {
				getParaBuilder.append(key + "=" + para.get(key));
				getParaBuilder.append("&");
			}
			if (getParaBuilder.length() > 0) {
				getParaBuilder.deleteCharAt(getParaBuilder.length() - 1);
			}
		}

		// Log.e("", "LINK:" + ApplicationBase.baseUrl + methodName
		// + getParaBuilder.toString());

		return MainApplication.baseUrl + methodName + getParaBuilder.toString();
	}

	/**
	 * Init when agent timeout, need recreate agent, secure code
	 * 
	 * @param temp
	 */
	public BaseRequest(BaseRequest temp) {
		super(Method.POST, MainApplication.baseUrl,
				temp.errorListener);
		_responseListener = temp._responseListener;
		this.methodName = temp.methodName;
		data = temp.data;
		paras = temp.paras;
		errorListener = temp.errorListener;
		setTag(temp.getTag());
		haveExtraData = temp.haveExtraData;
		setShouldCache(temp.shouldCache());
		setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs, maxNumRetries,
				backoffMultiplier));
	}

	@Override
	protected Response<BaseJson> parseNetworkResponse(NetworkResponse response) {
		BaseJson vegaJson = new BaseJson();
		KeepSession.get().checkSessionCookie(response.headers);
		JSONObject jsonObj = null;
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			json = json.replaceAll("ï»¿", "");
			Log.e("xxxx","ddddd"+ json);
			jsonObj = new JSONObject(json);
			vegaJson.error = jsonObj.getInt(error);
			vegaJson.message = jsonObj.getString(message);
			String total = JsonUtil.getString(jsonObj, "total", false, "");

			if (!TextUtils.isEmpty(total)) {
				vegaJson.count = Integer.valueOf(total).intValue();
			} else {
				vegaJson.count = JsonUtil.getInt(jsonObj, "total", false, 0);
			}

			if (haveExtraData) {
				vegaJson.allObject = jsonObj;
			}

			if (vegaJson.error == ErrorCode.SESSION_EXPIRED
					) {//&& isNeedListenTimeout()
				if (ApiBase.isNeedRetryWhenTimeOut(methodName)) {
					RequestUtil.getInstant().addRequestCancelNeedReload(
							new BaseRequest(this));
					RequestUtil.getInstant()
							.cancelAllRequest();
//					for (Request<?> requestCanced : ) {
//						RequestUtil.getInstant().addRequestCancelNeedReload(
//								requestCanced);
//					}
				}

//				if (onSessionTimeout != null) {
//					onSessionTimeout.onSessionTimeout();
//				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return Response.error(new ParseError(e));
		}

		try {
			vegaJson.data = jsonObj.getString(data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return Response.success(vegaJson,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(BaseJson response) {
		// TODO Auto-generated method stub
		_responseListener.onResponse(response);
	}

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		// TODO Auto-generated method stub
		if (volleyError.networkResponse != null
				&& volleyError.networkResponse.data != null) {
			String errorStr = new String(volleyError.networkResponse.data);
			errorStr += " in " + methodName;
			VolleyError error = new VolleyError(errorStr);
			volleyError = error;
		} else {
			String errorStr = "Network error in ";
			errorStr += methodName;
			VolleyError error = new VolleyError(errorStr);
			volleyError = error;
		}
		RequestUtil.getInstant().addRequestFail(new BaseRequest(this));
		return volleyError;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		Map<String, String> headers = super.getHeaders();
		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		KeepSession.get().addSessionCookie(headers);
		return headers;

	}
}
