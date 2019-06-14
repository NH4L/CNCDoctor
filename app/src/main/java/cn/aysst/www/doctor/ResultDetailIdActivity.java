package cn.aysst.www.doctor;

import android.app.Activity;
import android.content.Context;
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

    private int goodFlag;
    private int bookmarkFlag;
    private ImageView good;
    private ImageView bookmark;


    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.MILLISECONDS)
            .readTimeout(1000, TimeUnit.MILLISECONDS)
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
                onBackPressed();
            }
        });

        good = (ImageView)findViewById(R.id.good_id);
        bookmark = (ImageView)findViewById(R.id.bookmark_id);

        Log.d(Http.TAG, cnc.toString());
        brandText.setText(cnc.getBrand());
        typeText.setText(cnc.getSolutionDetail());
        solutionText.setText(cnc.getQuestionDetail());
        SharedPreferences preferences1 = getSharedPreferences("userInfo", ResultDetailIdActivity.MODE_PRIVATE);
        String username = preferences1.getString("name", "");

        SharedPreferences preferences2 = getSharedPreferences("resultDetail", ResultDetailIdActivity.MODE_PRIVATE);
        String temp = preferences2.getString(cnc.getQuestionDetail(), "");
        System.out.println(temp + "------------------");
        if (temp.equals("")) {
            SharedPreferences.Editor editor = preferences2.edit();
            editor.putString(cnc.getQuestionDetail(), "0#0");
            editor.commit();//提交修改
            Log.d(Http.TAG, "原来为空");
        } else {
            String a[] = temp.split("#");
            if (a[0].equals("1")) {
                goodFlag = 1;
                good.setImageResource(R.drawable.good_checked);
            } else {
                goodFlag = 0;
                good.setImageResource(R.drawable.good);
            }

            if (a[1].equals("1")) {
                bookmarkFlag = 1;
                bookmark.setImageResource(R.drawable.bookmark_checked);
            } else {
                bookmarkFlag = 0;
                bookmark.setImageResource(R.drawable.bookmark);
            }
        }


        saveHistory(username, cnc.getBrand(), cnc.getQuestionDetail(), cnc.getSolutionDetail(), cnc.getTypeDetail(), cnc.getQuestype());

        goodView = new GoodView(this);
        bookmarkView = new GoodView(this);

    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences("resultDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (goodFlag == 0 && bookmarkFlag ==0) {
            editor.putString(cnc.getQuestionDetail(), "0#0");
            Log.d(Http.TAG, "最后是没点赞+没收藏");
        } else if (goodFlag == 0 && bookmarkFlag ==1){
            editor.putString(cnc.getQuestionDetail(), "0#1");
            Log.d(Http.TAG, "最后是没点赞+收藏");
        } else if (goodFlag == 1 && bookmarkFlag ==0) {
            editor.putString(cnc.getQuestionDetail(), "1#0");
            Log.d(Http.TAG, "最后是点赞+没收藏");
        } else {
            editor.putString(cnc.getQuestionDetail(), "1#1");
            Log.d(Http.TAG, "最后是点赞+收藏");
        }
        editor.commit();//提交修改

        finish();
    }

    public void good(View view){
        SharedPreferences preferences2 = getSharedPreferences("resultDetail", ResultDetailIdActivity.MODE_PRIVATE);
        String temp = preferences2.getString(cnc.getQuestionDetail(), "");
        String good[] = temp.split("#");
        for (String i : good){
            System.out.println(i + "##########");
        }
        Log.d(Http.TAG, good.length + "--------------------");
        if (good[0].equals("0")) {
            goodFlag = 1;
        } else {
            goodFlag = 0;
        }


        if(goodFlag == 1) {
            ((ImageView) view).setImageResource(R.drawable.good_checked);
            goodView.setText("+1");
            goodView.show(view);
            goodFlag = 1;

        } else {
            ((ImageView)view).setImageResource(R.drawable.good);
            goodView.setText("-1");
            goodView.show(view);
            goodFlag = 0;

        }
    }

    public void bookmark(View view){
        SharedPreferences preferences1 = getSharedPreferences("resultDetail", ResultDetailIdActivity.MODE_PRIVATE);
        String temp = preferences1.getString(cnc.getQuestionDetail(), "");
        String bookmark[] = temp.split("#");
        if (bookmark[1].equals("0"))
            bookmarkFlag = 1;
        else
            bookmarkFlag = 0;

        if(bookmarkFlag == 1) {
            ((ImageView) view).setImageResource(R.drawable.bookmark_checked);
            bookmarkView.setText("收藏成功");
            bookmarkView.show(view);
            goodFlag = 1;
        } else {
            ((ImageView)view).setImageResource(R.drawable.bookmark);
            bookmarkView.setText("取消");
            bookmarkView.show(view);
            goodFlag = 0;
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
