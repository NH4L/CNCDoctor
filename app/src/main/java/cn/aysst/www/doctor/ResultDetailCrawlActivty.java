package cn.aysst.www.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.aysst.www.doctor.Adapter.SearchAdapter;
import cn.aysst.www.doctor.beans.CNCProblem;
import cn.aysst.www.doctor.utils.Http;
import com.wx.goodview.GoodView;


public class ResultDetailCrawlActivty extends Activity {

    private ImageView backImageView;
    private GoodView goodView;
    private GoodView bookmarkView;
    private CNCProblem cnc;
    private TextView precentText;
    private TextView solutionText;
    private TextView urlText;

    private int goodFlag;
    private int bookmarkFlag;
    private ImageView good;
    private ImageView bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detailed_crawl);

        Intent intent = getIntent();
        cnc = intent.getParcelableExtra(SearchAdapter.SEARCH_BY_QUES);//从碎片里获得任务类Task

        precentText = (TextView) findViewById(R.id.precentage_crawl);
        solutionText = (TextView) findViewById(R.id.solution_crawl);
        urlText = (TextView) findViewById(R.id.crawl_url);

        backImageView = (ImageView)findViewById(R.id.back_to_result_crawl);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        good = (ImageView)findViewById(R.id.good_crawl);
        bookmark = (ImageView)findViewById(R.id.bookmark_crawl);

        SharedPreferences preferences2 = getSharedPreferences("resultDetail", ResultDetailCrawlActivty.MODE_PRIVATE);
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

        precentText.setText(cnc.getPercentage() + "");
        solutionText.setText(cnc.getQuestionDetail());
        urlText.setText(cnc.getUrl());
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
        SharedPreferences preferences2 = getSharedPreferences("resultDetail", ResultDetailQuesActivty.MODE_PRIVATE);
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
        SharedPreferences preferences1 = getSharedPreferences("resultDetail", ResultDetailCrawlActivty.MODE_PRIVATE);
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
}
