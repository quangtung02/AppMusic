package app.alone.com.appmusic.network.api;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.main.BaseActivity;
import app.alone.com.appmusic.network.BaseJson;
import app.alone.com.appmusic.network.BaseRequest;

public class ApiBase {
	public static String[] methodNotListenTimeout;
	

	protected static ErrorListener getBaseErrorListener(
			final BaseAPIRequestListener listener, final Context ctx) {
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (listener != null) {
					listener.onRequestError("Có lỗi khi kết nối tới server!");
				}
				if (ctx != null && ctx instanceof BaseActivity) {
					// TODO show toast
//					CrountonUtils
//							.showCustomViewCrouton(
//									(ActivitiBase) ctx,
//									ctx.getString(R.string.have_error_when_connect_server));
				}
			}
		};
		return errorListener;
	}

	protected static Map<String, String> getParaOffsetLimit(int offset,
			int limit) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("offset", String.valueOf(offset));
		para.put("limit", String.valueOf(limit));
		return para;
	}

	protected static Map<String, String> getParaOffsetLimitWithId(int id,
			int offset, int limit) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("id", String.valueOf(id));
		para.put("offset", String.valueOf(offset));
		para.put("limit", String.valueOf(limit));
		return para;
	}

	protected static Map<String, String> getParaOffsetLimit(int offset,
			int limit, Map<String, String> other) {
		Map<String, String> para = getParaOffsetLimit(offset, limit);
		if (other != null) {
			para.putAll(other);
		}
		return para;
	}

	public interface XListener extends BaseAPIRequestListener {
		public void onFinish(boolean isSuccess, String message);
	}

	public interface BaseAPIRequestListener {
		public void onStartRequest();

		public void onRequestError(String message);
	}

	/**
	 * Return success or not
	 * 
	 * @param methodName
	 * @param paras
	 * @param //albumsRequestListener
	 * @param ctx
	 */
	public static String XRequest(String methodName, Map<String, String> paras,
			final XListener xListener, Context ctx) {
		BaseRequest vegaRequest = new BaseRequest(methodName, "data", paras,
				true, new Listener<BaseJson>() {

					@Override
					public void onResponse(BaseJson response) {
						if (xListener != null) {
							xListener.onFinish(response.isSuccess(),
									response.message);
						}
					}
				}, getBaseErrorListener(xListener, ctx));
		if (xListener != null) {
			xListener.onStartRequest();
		}
		RequestUtil.getInstant().addRequestQueue(vegaRequest, ctx, methodName);
		return methodName;
	}

	protected <T> String loadListObjectFromUrl(
			String methodName,
			Map<String, String> paras,
			final ListObjectRequestListener<T> listener, 
			Context ctx,
			final Class<T> _class, 
			String dataName, 
			boolean haveExtraData,
			String requestTag, 
			boolean needCache) {
		BaseRequest vegaRequest = new BaseRequest(methodName, dataName, paras,
				needCache, new Listener<BaseJson>() {

					@Override
					public void onResponse(BaseJson response) {
						ArrayList<T> listObject = null;
						if (listener != null) {
							if (response.isSuccess()) {
								listObject = (ArrayList<T>) response
										.toList(_class);
							}
							listener.onFinshRequestListObject(
									response.isSuccess(), 
									response.message,
									listObject, 
									response);
						}
					}
				}, getBaseErrorListener(listener, ctx), haveExtraData);
		if (listener != null) {
			listener.onStartRequest();
		}
		String tag = (TextUtils.isEmpty(requestTag)) ? methodName : requestTag;
		RequestUtil.getInstant().addRequestQueue(vegaRequest, ctx, tag);
		return tag;
	}
	
	protected <T> String loadListObjectFromUrl(String methodName,
			Map<String, String> paras,
			final ListObjectRequestListener<T> listener, Context ctx,
			final Class<T> _class, String dataName, boolean haveExtraData) {
		return loadListObjectFromUrl(methodName, paras, listener, ctx, _class,
				dataName, haveExtraData, null, true);
	}

	protected <T> String loadListObjectFromUrl(String methodName,
			Map<String, String> paras,
			final ListObjectRequestListener<T> listener, Context ctx,
			final Class<T> _class, String dataName) {
		return loadListObjectFromUrl(methodName, paras, listener, ctx, _class,
				dataName, false, null, true);
	}

	protected <T> String loadListObjectFromUrl(String methodName,
			Map<String, String> paras,
			final ListObjectRequestListener<T> listener, Context ctx,
			final Class<T> _class) {
		return loadListObjectFromUrl(methodName, paras, listener, ctx, _class,
				"data", false, null, true);
	}
	
	public interface ListObjectRequestListener<T> extends
			BaseAPIRequestListener {
		void onFinshRequestListObject(boolean isSuccess, String message,
											 ArrayList<T> listObject, BaseJson json);
	}
	
	public static void initMethodNotListenTimeout(Context ctx) {
		methodNotListenTimeout = ctx.getResources().getStringArray(R.array.method_not_listen_time_out);
	}

	public static boolean isNeedRetryWhenTimeOut(String method) {
		if (methodNotListenTimeout != null || !TextUtils.isEmpty(method)) {
			for (int i = 0, j = methodNotListenTimeout.length; i < j; i++) {
				if (method.contains(methodNotListenTimeout[i])) {
					return false;
				}
			}
		}
		return true;
	}

}
