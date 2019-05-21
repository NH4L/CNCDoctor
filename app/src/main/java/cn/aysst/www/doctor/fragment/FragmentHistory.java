package cn.aysst.www.doctor.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import cn.aysst.www.doctor.R;
import cn.aysst.www.doctor.ReleaseTaskActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentHistory extends Fragment{
	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(500, TimeUnit.MILLISECONDS)
			.readTimeout(500, TimeUnit.MILLISECONDS)
			.build();

	public static FragmentHistory newInstance() {
		FragmentHistory fragment = new FragmentHistory();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_history, null);
		return view;
	}

}
