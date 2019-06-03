package cn.aysst.www.doctor.beans;


import android.content.Context;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;

/**
 * 语音对象封装
 */
public class Voice {

    public ArrayList<WSBean> ws;

    public class WSBean {
        public ArrayList<CWBean> cw;
    }

    public class CWBean {
        public String w;
    }


}
