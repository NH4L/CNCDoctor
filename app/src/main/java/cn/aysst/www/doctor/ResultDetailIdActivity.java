package cn.aysst.www.doctor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import cn.aysst.www.doctor.Adapter.SearchAdapter;
import cn.aysst.www.doctor.beans.CNCProblem;
import cn.aysst.www.doctor.utils.Http;
import com.wx.goodview.GoodView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ResultDetailIdActivity extends Activity {
    private ImageView backImageView;
    private GoodView goodView;
    private GoodView bookmarkView;
    private TextView brandText;
    private TextView solutionText;
    private TextView typeText;
    private CNCProblem cnc;

    private int i = 1;

    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detailed_id);

        Intent intent = getIntent();
        cnc = intent.getParcelableExtra(SearchAdapter.SEARCH_BY_ID);//从碎片里获得任务类Task

        brandText = (TextView)findViewById(R.id.result_detail_brand);
        solutionText = (TextView)findViewById(R.id.result_detail_id_solution);
        typeText = (TextView)findViewById(R.id.result_detail_type);

        backImageView = (ImageView)findViewById(R.id.back_to_result_id);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        brandText.setText(cnc.getBrand());
        typeText.setText(cnc.getType());
        solutionText.setText(cnc.getQuestionDetail());
        SharedPreferences preferences = getSharedPreferences("userInfo", ResultDetailIdActivity.MODE_PRIVATE);
        String username = preferences.getString("name", "");
        saveHistory(username, cnc.getBrand(), cnc.getQuestion(), cnc.getSolution(), cnc.getType(), cnc.getQuestype());

        goodView = new GoodView(this);
        bookmarkView = new GoodView(this);

    }
    public void good(View view){
        if(i==1) {
            ((ImageView) view).setImageResource(R.drawable.good_checked);
            goodView.setText("+1");
            goodView.show(view);
            i=0;
        } else {
            ((ImageView)view).setImageResource(R.drawable.good);
            goodView.setText("-1");
            goodView.show(view);
            i=1;
        }
    }

    public void bookmark(View view){
        if(i==1) {
            ((ImageView) view).setImageResource(R.drawable.bookmark_checked);
            goodView.setText("收藏成功");
            goodView.show(view);
            i=0;
        } else {
            ((ImageView)view).setImageResource(R.drawable.bookmark);
            goodView.setText("-1");
            goodView.show(view);
            i=1;
        }
    }

    public void saveHistory(String username, String brand, String question, String solution, String type, String questype) {
        JSONObject object = new JSONObject();
        try {
            object.put("username", username);
            object.put("brand", brand);
            object.put("question", question);
            object.put("solution", solution);
            object.put("type", type);
            object.put("questype", questype);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ReleaseSaveAsyncTask().execute(object.toString());

    }
    class ReleaseSaveAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostInsSaveReq(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String msg = obj.getString("msg");
                    Log.d(Http.TAG, "code=" + code + ", msg=" + msg);
                    if (code == 0 && msg.equals("success")) {
                        Log.d(Http.TAG, "同步历史成功");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String httpPostInsSaveReq(String object) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("historyJson", object)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/cnc/addhistory")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String postData = response.body().string();
                return postData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
