package cn.aysst.www.doctor.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import cn.aysst.www.doctor.Adapter.MyHistoryAdapter;
import cn.aysst.www.doctor.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.aysst.www.doctor.beans.CNCProblem;
import cn.aysst.www.doctor.utils.Http;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

public class FragmentHistory extends Fragment {
	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(1000, TimeUnit.MILLISECONDS)
			.readTimeout(1000, TimeUnit.MILLISECONDS)
			.build();
    private RecyclerView recyclerView;
    private MyHistoryAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

	public static FragmentHistory newInstance() {
		FragmentHistory fragment = new FragmentHistory();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_history, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_cnc_history);
        adapter = new MyHistoryAdapter(getActivity());
        initMyHistory();
        //设置LayoutManager为LinearLayoutManager
        layoutManager= new LinearLayoutManager(getActivity());

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_onhistory);
        swipeRefreshLayout.setColorSchemeResources(R.color.btn);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTask();
            }
        });

        //设置LayoutManager和Adapter
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
		return view;
	}


    private void refreshTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //刷新时间1秒
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshMyHistory();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"刷新成功", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });
            }
        }).start();
    }

    private void initMyHistory() {
        new InitMyHistoryAsyncTask().execute();
    }

    class InitMyHistoryAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostGetHistoryReq();
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String msg = obj.getString("msg");
                    Log.d(Http.TAG, "code=" + code + ", msg=" + msg);
                    JSONObject data = obj.getJSONObject("data");
                    if (code == 0 && msg.equals("success")) {
                        int historyCount = data.getInt("count");
                        for (int i=historyCount-1; i>=0; i--) {
                            JSONObject object = data.getJSONObject("problem" + i);
                            String brand = object.getString("brand");
                            String question = object.getString("question");
                            String questype = object.getString("questype");
                            String solution = object.getString("solution");
                            String type = object.getString("type");

                            CNCProblem cncProblem = new CNCProblem();
                            cncProblem.setBrand(brand);
                            cncProblem.setQuestion(Http.cutStringTitle(question));
                            cncProblem.setQuestype(questype);
                            cncProblem.setType(Http.cutStringSmall(type));
                            cncProblem.setTypeDetail(type);
                            cncProblem.setSolution(Http.cutStringContent(solution));
                            cncProblem.setSolutionDetail(solution);
//                            cncProblem.setQuestion(question);
                            adapter.addItem(cncProblem);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void refreshMyHistory() {
        new refreshMyHistoryAsyncTask().execute();
    }

    class refreshMyHistoryAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostGetHistoryReq();
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String msg = obj.getString("msg");
                    Log.d(Http.TAG, "code=" + code + ", msg=" + msg);
                    JSONObject data = obj.getJSONObject("data");
                    if (code == 0 && msg.equals("success")) {
                        int historyCount = data.getInt("count");
                        for (int i=historyCount-1; i>=0; i--) {
                            JSONObject object = data.getJSONObject("problem" + i);
                            String brand = object.getString("brand");
                            String question = object.getString("question");
                            String questype = object.getString("questype");
                            String solution = object.getString("solution");
                            String type = object.getString("type");

                            question = Http.cutStringTitle(question);
                            solution = Http.cutStringContent(solution);
                            brand = Http.cutStringSmall(brand);

                            CNCProblem cncProblem = new CNCProblem();
                            cncProblem.setBrand(brand);
                            cncProblem.setQuestion(question);
                            cncProblem.setQuestype(questype);
                            cncProblem.setType(type);
                            cncProblem.setSolution(solution);


                            adapter.addFirstItem(cncProblem);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String httpPostGetHistoryReq() {
	    String result = null;
        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        FormBody body = new FormBody.Builder()
                .add("username", name)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/cnc/gethistory")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                Log.d("myList", result);
                result = URLDecoder.decode(result, "UTF-8");
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
