package cn.aysst.www.doctor;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.aysst.www.doctor.DialogFragment.EditDialogFragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static cn.aysst.www.doctor.utils.Http.BASE_URL;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener, EditDialogFragment.NoticeDialogListener {

    private RadioButton radioButtonZ;
    private RadioButton radioButtonW;
    private TextView textViewSetNum;
    private String editMoney;

    private Bundle bundle;
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        getSupportActionBar().setTitle("在线充值");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioButtonZ = (RadioButton)findViewById(R.id.zhifubao_on_recharge);
        radioButtonW = (RadioButton)findViewById(R.id.weixin_on_recharge);
        textViewSetNum = (TextView)findViewById(R.id.show_num_on_recharge);

        SharedPreferences preferences = getSharedPreferences("userInfo", RechargeActivity.MODE_PRIVATE);
        float preMoney = preferences.getFloat("money", 0f);
        ((TextView)findViewById(R.id.show_gold_on_recharge)).setText(preMoney + "");

        findViewById(R.id.set_num_on_recharge).setOnClickListener(this);
        findViewById(R.id.pay_on_recharge).setOnClickListener(this);
        radioButtonZ.setOnClickListener(this);
        radioButtonW.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_num_on_recharge:
                setGoldNum();
                break;

            case R.id.pay_on_recharge:
                if (!(textViewSetNum.getText().toString().trim().length() == 0)) {
                    String money = textViewSetNum.getText().toString().trim();
                    SharedPreferences preferences = getSharedPreferences("userInfo", ReleaseTaskActivity.MODE_PRIVATE);
                    String name = preferences.getString("name", "");
                    new RechargeMoneyAsyncTask().execute(name, money);
                } else {
                    Toast.makeText(this, "请输入金额", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.zhifubao_on_recharge:
                if (radioButtonW.isChecked()){
                    radioButtonW.setChecked(true);
                }
                break;

            case R.id.weixin_on_recharge:
                if (radioButtonZ.isChecked()){
                    radioButtonZ.setChecked(false);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setGoldNum(){
        EditDialogFragment dialogFragment = new EditDialogFragment();
        bundle = new Bundle();
        bundle.putString("message","自定义充值钱数");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"EditNameDialog");
    }

    @Override
    public void onDialogPositiveClick(EditDialogFragment dialog) {
        if(bundle != null){
            switch (bundle.getString("message")){
                case "自定义充值钱数":
                    editMoney = dialog.myData;
                    boolean result = true;
                    if (editMoney.equals("")) {
                        Toast.makeText(this, "输入金额为空", Toast.LENGTH_LONG).show();
                        result = false;
                    } else {
                        if(editMoney.equals("0")) {
                            Toast.makeText(this, "输入金额必须大于0", Toast.LENGTH_LONG).show();
                            result = false;
                        } else {
                            for (int i=0; i<editMoney.length(); i++){
                                char ch = editMoney.charAt(i);
                                if (ch < '0' || ch > '9') {
                                    System.out.println(ch);
                                    result = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (result) {
                        textViewSetNum.setText(editMoney);
                    }

                default:
                    break;
            }
        }
    }

    @Override
    public void onDialogNegativeClick(EditDialogFragment dialog) {

    }

    /**
     * 签到赚赏金的task
     */
    class RechargeMoneyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return httpRechargeMoneyPostReq(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("fail")) {
                SharedPreferences preferences = getSharedPreferences("userInfo", RechargeActivity.MODE_PRIVATE);
                float preMoney = preferences.getFloat("money", 0f) + Float.parseFloat(editMoney.trim());
                ((TextView)findViewById(R.id.show_gold_on_recharge)).setText(preMoney + "");
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("money", preMoney + "");
                editor.commit();
                Toast.makeText(RechargeActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RechargeActivity.this, "服务器繁忙, 请重试!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        finish();
    }
    /**
     * 向服务器发送加钱的请求
     * @param name
     * @param money
     * @return
     */
    private String httpRechargeMoneyPostReq(String name, String money) {
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
