package app.alone.com.appmusic.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.alone.com.appmusic.R;

public class LoadingView extends LinearLayout {
	private fr.castorflex.android.circularprogressbar.CircularProgressBar mLoadingbar;
	private TextView mLoadingText;
	private Button mRetryBtn;
	private RetryListener listener;
	private Context mContext;
	private View mLoadingView;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public LoadingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public void setRetryListener(RetryListener listener) {
		this.listener = listener;
	}

	private void init(Context ctx) {
		mContext = ctx;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_loading_data, this);
		mLoadingbar = (fr.castorflex.android.circularprogressbar.CircularProgressBar) findViewById(R.id.feature_list_progress);
		mLoadingText = (TextView) findViewById(R.id.feature_help_label);
		mRetryBtn = (Button) findViewById(R.id.feature_help_btn);
		mRetryBtn.setOnClickListener(onRetry);
		mLoadingView = findViewById(R.id.view_feature_loading);
	}

	public void setBacground(String hexCode) {
		mLoadingView.setBackgroundColor(Color.parseColor(hexCode));
	}

	public void setTextLoadingColor(String hexCode) {
		mLoadingText.setTextColor(Color.parseColor(hexCode));
	}

	public TextView getTextLoading() {
		return mLoadingText;
	}

	private OnClickListener onRetry = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLoadingbar.setVisibility(View.VISIBLE);
			mRetryBtn.setVisibility(View.GONE);
			mLoadingText.setText(mContext.getString(R.string.loading));
			if (listener != null) {
				listener.onRetry();
			}
		}
	};

	public void startLoading(String textLoading) {
		this.setVisibility(View.VISIBLE);
		mLoadingbar.setVisibility(View.VISIBLE);
		mRetryBtn.setVisibility(View.GONE);
		if (TextUtils.isEmpty(textLoading)) {
			textLoading = getContext().getString(R.string.you_waiting);
		}
		mLoadingText.setVisibility(View.VISIBLE);
		mLoadingText.setText(textLoading);
	}

	public void loadingFinish(boolean isSuccess, String failMessage) {
		if (isSuccess) {
			this.setVisibility(View.GONE);
		} else {
			this.setVisibility(View.VISIBLE);
			mRetryBtn.setVisibility(View.VISIBLE);
			mLoadingbar.setVisibility(View.GONE);
			mLoadingText.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(failMessage)) {
				mLoadingText
						.setText(mContext.getText(R.string.load_data_error));
			} else {
				mLoadingText.setText(failMessage);
			}
		}
	}

	public void showMessageOnLy(String msg) {
		this.setVisibility(VISIBLE);
		mRetryBtn.setVisibility(INVISIBLE);
		mLoadingText.setText(msg);
		mLoadingbar.setVisibility(GONE);
	}

	public void showText(boolean isShow) {
		if (isShow) {
			mLoadingText.setVisibility(View.VISIBLE);
		} else {
			mLoadingText.setVisibility(View.GONE);
		}
	}

	public interface RetryListener {
		public void onRetry();
	}
}
