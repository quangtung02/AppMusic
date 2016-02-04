package app.alone.com.appmusic.network;


import java.util.Map;


public class KeepSession {
	
	private static final String SET_COOKIE_KEY = "Set-Cookie";
	private static final String COOKIE_KEY = "Cookie";
	private static KeepSession _instance;
	public static String cookies = ""; 
	
	
	public KeepSession() {
		// TODO Auto-generated constructor stub
	}
	
	public static KeepSession get() {
		if(_instance == null) {
			synchronized (KeepSession.class) {
				if (_instance == null) {
					_instance = new KeepSession();
				}
			}
		}
		return _instance;
	}
	/**
	 * Adds session cookie to headers if exists.
	 * 
	 * @param headers
	 */
	public void addSessionCookie(Map<String, String> headers) {
		if (cookies.length() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(cookies);
			if (headers.containsKey(COOKIE_KEY)) {
				builder.append("; ");
				builder.append(headers.get(COOKIE_KEY));
			}
//			VegaLog.e("cookies header: " + builder.toString());
			headers.put(COOKIE_KEY, builder.toString());
		}
	}
	
	
	/**
	 * Checks the response headers for session cookie and saves it if it finds
	 * it.
	 * 
	 * @param headers
	 *            Response Headers.
	 */
	public void checkSessionCookie(Map<String, String> headers) {
		if (headers.get(SET_COOKIE_KEY) != null) {
//			if (headers.containsKey(SET_COOKIE_KEY)
//					&& headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
				String cookie = headers.get(SET_COOKIE_KEY);
				if (cookie.length() > 0) {
					String[] splitCookie = cookie.split(";");
					cookies = splitCookie[0];
				}
//			}
		}
	}
}
