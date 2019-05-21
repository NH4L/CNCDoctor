package cn.aysst.www.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.aysst.www.doctor.utils.Http;
import cn.aysst.www.doctor.utils.NetworkJudge;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
//create table task(task_id int auto_increment primary key,task_name varchar(100),task_builder varchar(50),task_type varchar(50),task_money float(11,2),task_description text(100),task_publish_time varchar(50),task_provider_num int(10) default 0,task_answer_num int(10) default 0,task_watch_num int(10) default 0,task_review_num int(10) default 0,task_provide_isprivate tinyint(1) default 0)auto_increment=10000 default charset=utf8;
// create table provide(provide_id int auto_increment primary key,provide_taskName varchar(50),provide_taskId int(10),provide_builder varchar(50),provide_urlSum text(1000),provide_publishTime varchar(50),provide_isPrivate tinyint(1) default 0,provide_getMoney float(11,2) default 0.0)auto_increment=10000 default charset=utf8;
public class ReleaseTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ImageView backImageView;
    private EditText edit_task_name, edit_money, edit_task_description;
    private TextView save_release;
    private String task_name, task_type, task_description, task_builder;
    private float task_money;
    private int task_provide_isPrivate;
    private RadioGroup taskType_radioGroup;
    private RadioGroup taskProvide_radioGroup;
    private RadioButton radio_task_type;
    private RadioButton radio_task_provide_isPrivate;
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releasetask);
        initView();
    }
    protected void initView() {
        Intent intent = getIntent();
        task_builder = (String)intent.getStringExtra("username");

        toolbar = (Toolbar)findViewById(R.id.toolbar_onrelasetask);
        backImageView = (ImageView)findViewById(R.id.back_to_release);
        edit_money = (EditText) findViewById(R.id.edit_money);
        edit_money.setFilters(new InputFilter[]{                 //在编辑的时候就进行判断
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals(".")) {
                                Toast.makeText(ReleaseTaskActivity.this, "只能输入数字!", Toast.LENGTH_SHORT).show();
                                return "";
                            }
                        }
                        return null;
                    }}

        });
        save_release = (TextView)findViewById(R.id.text_save_and_release);
        edit_task_name = (EditText)findViewById(R.id.name_task_onreleasetask);
        edit_task_description = (EditText)findViewById(R.id.info_task_onreleasetask);
        taskType_radioGroup = (RadioGroup)findViewById(R.id.task_type_radiogroup);
        taskType_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_task_type = (RadioButton)findViewById(taskType_radioGroup.getCheckedRadioButtonId());
                String type = radio_task_type.getText().toString();
                switch (type){
                    case "图片":
                        task_type = "image";
                        break;
                    case "文档":
                        task_type = "text";
                        break;
                    case "音频":
                        task_type = "audio";
                        break;
                }
            }
        });
        taskProvide_radioGroup = (RadioGroup)findViewById(R.id.task_provide_isPrivate_radioGroup);
        taskProvide_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_task_provide_isPrivate = (RadioButton)findViewById(taskProvide_radioGroup.getCheckedRadioButtonId());
                String type = radio_task_provide_isPrivate.getText().toString();
                switch (type){
                    case "私有":
                        task_provide_isPrivate = 1;
                        break;
                    case "公开":
                        task_provide_isPrivate = 0;
                        break;
                }
            }
        });
        setSupportActionBar(toolbar);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save_release.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_save_and_release:
                if (edit_task_name.getText().toString().trim().length() == 0){
                    Toast.makeText(this, "请填写任务名!", Toast.LENGTH_SHORT).show();
                } else {
                    if (task_type == null) {
                        Toast.makeText(this, "请选择任务类型!", Toast.LENGTH_SHORT).show();
                    }else {
                        if (edit_money.getText().toString().trim().length() == 0) {
                            Toast.makeText(this, "请输入任务赏金金额!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (edit_task_description.getText().toString().trim().length() == 0) {
                                Toast.makeText(this, "请输入任务详细描述!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!NetworkJudge.isNetworkConnected(this)) {
                                    Toast.makeText(this, "发布失败,请检查网络连接!", Toast.LENGTH_SHORT).show();
                                } else {
                                    task_name = edit_task_name.getText().toString().trim();
                                    task_money = Float.parseFloat(edit_money.getText().toString().trim());
                                    task_description = edit_task_description.getText().toString().trim();
                                    SharedPreferences preferences = getSharedPreferences("userInfo", ReleaseTaskActivity.MODE_PRIVATE);
                                    task_builder = preferences.getString("name", "");
                                    float money = preferences.getFloat("money", 0.0f);
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    final String time = df.format(new Date());
                                    if (money > Float.parseFloat(edit_money.getText().toString().trim())) {
                                        final JSONObject task_json = new JSONObject();
                                        try {
                                            task_json.put("task_name", task_name);
                                            task_json.put("task_builder", task_builder);
                                            task_json.put("task_money", task_money);
                                            task_json.put("task_type", task_type);
                                            task_json.put("task_description", task_description);
                                            task_json.put("task_publish_time", time);
                                            task_json.put("task_provide_isprivate", task_provide_isPrivate);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("task", task_json.toString());
                                        releaseTask(task_json);
                                    }
                                    else {
                                        Toast.makeText(this, "任务赏金大于自身赏金数,请充值或者减小金额", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        finish();
    }

    public void releaseTask(JSONObject taskJson) {
        new ReleaseTaskAsyncTask().execute(taskJson.toString());
    }

    class ReleaseTaskAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostInsTaskReq(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(ReleaseTaskActivity.this, "任务发布成功!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ReleaseTaskActivity.this, "发布失败,请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String httpPostInsTaskReq(String taskJson) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("msg", "taskBuildAction")
                .add("task", taskJson)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/TaskServlet")
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
