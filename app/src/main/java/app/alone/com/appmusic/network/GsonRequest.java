package app.alone.com.appmusic.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
	private final Class<T> _class;
	private final Map<String, String> _headers;
	private final Gson _gson;

	public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		// TODO Auto-generated constructor stub
		this._headers = headers;
        this._gson = new Gson();
        this._class = clazz;
	}
	
	public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		super(Request.Method.GET, url, errorListener);
		// TODO Auto-generated constructor stub
		this._headers = null;
        this._gson = new Gson();
        this._class = clazz;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return super.getParams();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		return (_headers != null) ? _headers : super.getHeaders();
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(_gson.fromJson(json, _class),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return Response.error(new ParseError(e));
		}  catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
	}

	@Override
	protected void deliverResponse(T response) {
		// TODO Auto-generated method stub

	}
}
