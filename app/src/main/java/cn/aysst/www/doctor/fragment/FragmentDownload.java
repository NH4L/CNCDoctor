package cn.aysst.www.doctor.fragment;
import android.content.SharedPreferences;
import android.support.v7.widget.*;
import android.util.Log;
import cn.aysst.www.doctor.MainActivity;
import cn.aysst.www.doctor.R;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.aysst.www.doctor.utils.Http;
import cn.aysst.www.doctor.utils.NullListInAdapterException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentDownload extends Fragment{
	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(500, TimeUnit.MILLISECONDS)
			.readTimeout(500, TimeUnit.MILLISECONDS)
			.build();

	public static FragmentDownload newInstance() {
		FragmentDownload fragment = new FragmentDownload();                ;
		return fragment;
	}
}
