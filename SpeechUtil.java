package com.hichao.look.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.hichao.look.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import android.content.Context;
import android.view.WindowManager;

public class SpeechUtil {
	private Context mContext ;
	private SpeechRecognizer mIat;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private RecognizerDialog iatDialog;
	private static SpeechUtil instance;

	private OnSpeechListener listener;
	
	private SpeechUtil(Context context){
		mContext = context;
		SpeechUtility.createUtility(mContext, SpeechConstant.APPID +mContext.getResources().getString(R.string.iflytek_cloud_appid));
		mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
		iatDialog = new RecognizerDialog(mContext,mInitListener);
			
	}
	public static SpeechUtil getInstance(Context context){
		instance = new SpeechUtil(context);
		return instance;		
	}
	
	public SpeechUtil setSpeechListener(OnSpeechListener listener) {
		this.listener = listener;
		return this;
	}
	
	public void getSpeechContent(){
		setParames();
		startSpeech();
	}
	
	private void startSpeech() {
		iatDialog.setListener(recognizerDialogListener);
	
	
		iatDialog.show();
		
		
	}
	private void setParames() {
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
		
	}

	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				if(listener != null) {
					listener.onSpeechInitError(code);
				}
			}
		}
	};
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			if(iatDialog != null && iatDialog.isShowing()) {
				iatDialog.dismiss();
			}
			String text = JsonParser.parseIatResult(results.getResultString());
			String sn = null;
			try {
				JSONObject resultJson = new JSONObject(results.getResultString());
				sn = resultJson.optString("sn");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mIatResults.put(sn, text);
			StringBuffer resultBuffer = new StringBuffer();
			for (String key :  mIatResults.keySet()) {
				resultBuffer.append(mIatResults.get(key));
			}
			if(listener != null) {
				listener.onSpeechComplete(resultBuffer.toString());
			}
			
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			if(iatDialog != null && iatDialog.isShowing()) {
				iatDialog.dismiss();
			}
			if(listener != null) {
				listener.onSpeechError(error);
			}
		}

	};
	
	public interface OnSpeechListener {
		
		public void onSpeechComplete(String result);
		
		public void onSpeechError(SpeechError error);
		
		public void onSpeechInitError(int code);
	}
	
}
