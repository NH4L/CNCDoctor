package cn.aysst.www.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.aysst.www.doctor.Adapter.SearchAdapter;
import cn.aysst.www.doctor.beans.CNCProblem;
import cn.aysst.www.doctor.beans.myBean;
import cn.aysst.www.doctor.utils.Http;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.rahatarmanahmed.cpv.CircularProgressView;


public class SearchResult extends Activity {
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private TextView crawlAgainText;
    private CircularProgressView cpv;
    private LoadingDialog ld;
    private int number = 0;

    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20000, TimeUnit.MILLISECONDS)
            .readTimeout(20000, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ImageView backImageView = (ImageView)findViewById(R.id.back_to_home);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        crawlAgainText = (TextView) findViewById(R.id.crawl_again);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_search_page);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new GridLayoutManager(this,1);
        adapter = new SearchAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        ld = new LoadingDialog(this).setLoadingText("爬取中....").setSuccessText("爬取成功").setShowTime(2000);
        CNCProblem cnc = new CNCProblem();
        Intent intent = getIntent();
        int searchType = intent.getIntExtra("searchType", 0);
        Log.d(Http.TAG, "+++++++++++" + intent.getStringExtra("question") + "___________________");
        crawlAgainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clean();
                ld.show();
                ld.loadSuccess();
//                crawlAgain(cnc.getQuestion(), number);
                crawlAgain((String) intent.getStringExtra("question"), number);
            }
        });

        switch (searchType) {
            case CNCProblem.SEARCH_TYPE_BY_ID:
                String searchType_1 = "精确查询";
                String problem = intent.getStringExtra("problem");
                String type = intent.getStringExtra("type");
                String solution = intent.getStringExtra("solution");
                String brand = intent.getStringExtra("brand");
                String questype = intent.getStringExtra("questype");
                String id = intent.getStringExtra("id");
                String time = intent.getStringExtra("time");

                cnc.setSearchType(searchType_1);
                cnc.setSolution(Http.cutStringMiddle(solution));
                cnc.setSolutionDetail(solution);
                cnc.setType(type);
                cnc.setBrand(brand);
                cnc.setQuestype(questype);
                cnc.setId(id);
                cnc.setTime(time);
                cnc.setQuestion(problem);
                cnc.setPercentage(1.00);

                adapter.addItem(cnc);

                break;
            case CNCProblem.SEARCH_TYPE_BY_QUES:
                JSONObject data = null;
                try {
                    data = new JSONObject(intent.getStringExtra("solution"));
                    Log.d(Http.TAG, data.toString());
                    String question = data.getString("question");
                    String time_1 = intent.getStringExtra("time");
                    for (int i=0; i<5; i++) {
                        JSONObject problemObj = data.getJSONObject("problem" + i);
                        String solution_1 = problemObj.getString("solution");
                        double precentage = problemObj.getDouble("percentage");
                        Log.d(Http.TAG, "解决方法" + solution_1 + ", 概率" + precentage);
                        DecimalFormat df = new DecimalFormat("#.00");

                        CNCProblem cncProblem = new CNCProblem();
                        cncProblem.setQuestion(question);
                        cncProblem.setTime(time_1);
                        cncProblem.setSolution(Http.cutStringMiddle(solution_1));
                        cncProblem.setSolutionDetail(solution_1);
                        cncProblem.setPercentage(Double.parseDouble(df.format(precentage)));
                        cncProblem.setSearchType("普通查询");
                        adapter.addItem(cncProblem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
        }


    }
    public static double format2(double value) {

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return Double.parseDouble(bd.toString());
    }
    private void crawlAgain(String question, int number) {
        adapter.clean();
        new CrawlSolutionBySocketAsyncTask().execute(number + "0", question);
    }

    class CrawlSolutionBySocketAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return crawlOnSocket(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray info = obj.getJSONArray("answer");
                for (int i=0; i<obj.getInt("answercount"); i++) {
                    JSONObject object = (JSONObject) info.get(i);
                    CNCProblem cnc = new CNCProblem();
                    cnc.setSolution(Http.cutStringTitle(object.getString("content")));
                    cnc.setSolutionDetail(object.getString("title") + object.getString("content"));
                    cnc.setPercentage(format2(0.9 - i * 0.11));
                    cnc.setSearchType("爬虫查询");
                    cnc.setUrl(object.getString("url"));
                    Thread.sleep(400);
                    adapter.addItem(cnc);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            number ++;
        }
    }

    private String crawlOnSocket(String number, String keyword) {
        String result = "fail";
        try {
            JSONObject obj = new JSONObject();
            obj.put("count", number);
            obj.put("keyword", keyword);

            Socket socket = new Socket(Http.BASE_IP, Http.BASE_PORT);
            //获取输出流，向服务器端发送信息
            OutputStream os = socket.getOutputStream();//字节输出流
            PrintWriter out = new PrintWriter(os);//将输出流包装为打印流
            out.write(obj.toString());
            out.flush();
            socket.shutdownOutput();//关闭输出流

            InputStream is = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            result = in.readLine();

//            while((info=in.readLine())!=null){
//                //System.out.println("我是客户端，Python服务器说："+info);
//                Log.d("MAIN","我是客户端，Python服务器说："+info);
//                Message msg = new Message();
//                Bundle data = new Bundle();
//                data.putString("value","我是客户端，Python服务器说："+info);
//                msg.setData(data);
//            }
            is.close();
            in.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


}
