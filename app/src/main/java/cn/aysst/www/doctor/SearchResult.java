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

import java.io.IOException;
import java.net.URLDecoder;
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
        ld = new LoadingDialog(this)
        .setLoadingText("爬取中....")
        .setSuccessText("爬取成功")
        .setShowTime(2000);
        CNCProblem cnc = new CNCProblem();
        Intent intent = getIntent();
        int searchType = intent.getIntExtra("searchType", 0);

        crawlAgainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // cpv.setVisibility(View.VISIBLE);
                adapter.clean();

                ld.show();

                ld.loadSuccess();
                crawlAgain("爬虫查询");

                //
//  cpv.setVisibility(View.INVISIBLE);
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

    private void crawlAgain(String searchType) {
        adapter.clean();

        CNCProblem cnc = new CNCProblem();
        cnc.setSolution("纠正SDB 类型 -2000 关闭...");
        cnc.setSolutionDetail("纠正SDB 类型 -2000 关闭/ 打开系统。");
        cnc.setPercentage(0.88);
        cnc.setSearchType(searchType);
        adapter.addItem(cnc);

        CNCProblem cnc2 = new CNCProblem();
        cnc2.setSolution("在未被系统占用的指定机床数据中...");
        cnc2.setSolutionDetail("在未被系统占用的指定机床数据中配置一个 M 功能( M0 到 M5、M17、M30、M40 到 M45 以及利用实用ISO 非标准语言的 M98、 M99)。 用复位键清除报警。");
        cnc2.setPercentage(0.34);
        cnc2.setSearchType(searchType);
        adapter.addItem(cnc2);

    }

}
