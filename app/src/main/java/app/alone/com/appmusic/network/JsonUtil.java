package app.alone.com.appmusic.network;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static int getInt(JSONObject json, String key, boolean require,
			int defaultvalue) throws JSONException {
		try {
			return json.getInt(key);
		} catch (JSONException exception) {
			if (require) {
				throw exception;
			}
			return defaultvalue;
		}
	}

	public static int getInt(JSONObject json, String key, int defaultvalue) {
		try {
			return json.getInt(key);
		} catch (JSONException exception) {
			return defaultvalue;
		}
	}

	public static long getLong(JSONObject json, String key, boolean require,
			long defaultValue) throws JSONException {
		try {
			return json.getLong(key);
		} catch (JSONException exception) {
			if (require) {
				throw exception;
			}
			return defaultValue;
		}
	}

	public static String getString(JSONObject json, String key,
			boolean require, String defaultValue) throws JSONException {
		try {
			return json.getString(key).intern();
		} catch (JSONException exception) {
			if (require) {
				throw exception;
			}
			return defaultValue.intern();
		}
	}

	public static boolean getBoolean(JSONObject json, String key,
			boolean require, boolean defaultValue) throws JSONException {
		try {
			return json.getBoolean(key);
		} catch (JSONException exception) {
			if (require) {
				throw exception;
			}
			return defaultValue;
		}
	}

	public static float getFloat(JSONObject json, String key, float defaultValue) {
		try {
			float value = Float.valueOf(json.getString(key));
			return value;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	public static float getFloat(JSONObject json, String key, boolean require,
			float defaultValue) throws JSONException {
		try {
			float value = Float.valueOf(json.getString(key));
			return value;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			if (require) {
				throw e;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			if (require) {
				throw e;
			}
		}
		return defaultValue;
	}

}
