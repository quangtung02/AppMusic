package app.alone.com.appmusic.network.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashSet;
import java.util.Set;


public class RequestUtil {
	private static RequestUtil instant;
	private Set<Request<?>>  mapSetRequestCanceled;
	private Set<Request<?>> mapSetRequestFail;
	private RequestQueue requestQueue;
	public Request<?> requestCancel;
	public Request<?> getCancelRequest(){
		return requestCancel;
	}

	public enum MapRequest{
		Cancel,
		Fail
	}

	private RequestUtil() {

	}

	public static RequestUtil getInstant() {
		if (instant == null) {
			synchronized (RequestUtil.class) {
				if (instant == null) {
					instant = new RequestUtil();
				}
			}
		}
		return instant;
	}
	
	public static void clearInstant() {
		synchronized (RequestUtil.class) {
			if (instant != null) {
				instant.clearRequestCancel();
				instant.clearRequestFail();
				if (instant.requestQueue != null) {
					instant.requestQueue.stop();
					instant.requestQueue = null;
				}
				instant = null;
			}
		}
	}
	

	public void addRequestCancelNeedReload(Request<?> request) {
		synchronized (RequestUtil.class) {
			if (mapSetRequestCanceled == null) {
				mapSetRequestCanceled = new HashSet<Request<?>>();
			}
			mapSetRequestCanceled.add(request);
		}
	}
	
	public Set<Request<?>> getListRequestCancel() {
		synchronized (RequestUtil.class) {
			return mapSetRequestCanceled;
		}
	}
	
	public void clearRequestCancel() {
		synchronized (RequestUtil.class) {
			if (mapSetRequestCanceled != null) {
				mapSetRequestCanceled.clear();
				mapSetRequestCanceled = null;
			}
		}
	}
	
	public void addRequestFail(Request<?> request) {
		synchronized (RequestUtil.class) {
			if (mapSetRequestFail == null) {
				mapSetRequestFail = new HashSet<Request<?>>();
			}
			mapSetRequestFail.add(request);
		}
	}
	
	public Set<Request<?>> getListRequestFail() {
		synchronized (RequestUtil.class) {
			return mapSetRequestFail;
		}
	}
	
	public void clearRequestFail() {
		synchronized (RequestUtil.class) {
			if (mapSetRequestFail != null) {
				mapSetRequestFail.clear();
				mapSetRequestFail = null;
			}
		}
	}
	
	public void addRequestQueue(Request<?> request, Context ctx, String tag) {
		synchronized (RequestUtil.class) {
			if (requestQueue == null) {
				requestQueue = Volley.newRequestQueue(ctx.getApplicationContext()); 
			}
			request.setTag(tag);
			requestQueue.add(request);
		}
	}
	
	public void cancelAllRequest() {
		synchronized (RequestUtil.class) {
			if (requestQueue != null) {
				requestQueue.cancelAll(new RequestQueue.RequestFilter() {

					@Override
					public boolean apply(Request<?> request) {
						// TODO Auto-generated method stub
						requestCancel = request;
						return true;
					}
				});
			}
		}
	}
	
	public void cancelRequest(final String tag) {
		synchronized (RequestUtil.class) {
			if (requestQueue != null) {
				requestQueue.cancelAll(tag);
			}
		}
	}

}
