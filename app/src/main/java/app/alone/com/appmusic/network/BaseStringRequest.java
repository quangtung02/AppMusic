package app.alone.com.appmusic.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseStringRequest extends StringRequest {
	private final Map<String, String> _params;

	/**
	 * @param method
	 * @param url
	 * @param params
	 *            A {@link HashMap} to post with the request. Null is allowed
	 *            and indicates no parameters will be posted along with request.
	 * @param listener
	 * @param errorListener
	 */
	public BaseStringRequest(int method, String url, LinkedHashMap<String, String> params,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);

		_params = params;
	}

	@Override
	protected Map<String, String> getParams() {
		return _params;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.android.volley.toolbox.StringRequest#parseNetworkResponse(com.android
	 * .volley.NetworkResponse)
	 */
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		// since we don't know which of the two underlying network vehicles
		// will Volley use, we have to handle and store session cookies manually
//		Map<String, String> headers = response.headers;
//		String cookie = headers.get("Set-Cookie");
//		// VegaLog.e(">> Cookie:"+cookie);
//		MainApplication.get().getDataStore().putString("cookie", cookie);
		return super.parseNetworkResponse(response);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.android.volley.Request#getHeaders()
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();

//		if (headers == null || headers.equals(Collections.emptyMap())) {
//			headers = new HashMap<String, String>();
//		}
//		VegaLog.e(">> Cookie:"
//				+ MainApplication.get().getDataStore().getString("cookie"));
//		headers.put("Cookie",
//				MainApplication.get().getDataStore().getString("cookie"));
		return headers;
	}

}
