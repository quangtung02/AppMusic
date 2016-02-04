package app.alone.com.appmusic.network;

import android.content.Context;

import app.alone.com.appmusic.R;

public class ErrorCode {
	public static int SUCCESS = 1;
	public static int ERROR_PARSER = -1;
	public static int SESSION_EXPIRED = 401;
	
	public static void initErrorCode(Context ctx) {
		try {
			SUCCESS = Integer.valueOf(ctx.getString(R.string.request_code_success));
			SESSION_EXPIRED = Integer.valueOf(ctx.getString(R.string.request_code_session_expired));
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
	}
}
