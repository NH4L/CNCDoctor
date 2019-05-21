package cn.aysst.www.doctor;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static cn.aysst.www.doctor.utils.Http.BASE_URL;


public class AttendActivity extends AppCompatActivity implements View.OnClickListener {
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.time_on_attend)).setText(getTime3());
        ((Button)findViewById(R.id.btnattend_on_attend)).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnattend_on_attend:
                float money = 1.5f;
                SharedPreferences preferences = getSharedPreferences("userInfo", AttendActivity.MODE_PRIVATE);
                String name = preferences.getString("name", "");

                SharedPreferences.Editor editor = preferences.edit();
                if (getTime3().equals(preferences.getString("attendTime", ""))) {
                    Toast.makeText(this, "请勿重复签到", Toast.LENGTH_LONG).show();
                } else {
                    editor.putString("attendTime", getTime3());
                    new AttendAsyncTask().execute(name, money + "");
                }
                editor.commit();//提交修改

            break;
        }
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        finish();
    }

    private String getTime3(){
        Calendar calendar = Calendar.getInstance();
        String created = calendar.get(Calendar.YEAR) + "年"
                + (calendar.get(Calendar.MONTH)+1) + "月"//从0计算
                + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        return created;
    }

    private String getTime4(){
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        String time=t.year+"年 "+(t.month+1)+"月 "+t.monthDay+"日";
        return time;
    }

    /**
     * 签到赚赏金的task
     */
    class AttendAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return httpAttendPostReq(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("fail")) {
                Toast.makeText(AttendActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AttendActivity.this, "服务器繁忙, 请重试!", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 向服务器发送加钱的请求
     * @param name
     * @param money
     * @return
     */
    private String httpAttendPostReq(String name, String money) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("msg", "addMoney")
                .add("name", name)
                .add("money", money)
                .build();

        final Request request = new Request.Builder()
                .url(BASE_URL + "/ExecuteUserInfoServlet")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String postResult = response.body().string();
                return postResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
