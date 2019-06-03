package cn.aysst.www.doctor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.aysst.www.doctor.Adapter.SearchAdapter;
import cn.aysst.www.doctor.beans.CNCProblem;
import cn.aysst.www.doctor.utils.Http;
import com.wx.goodview.GoodView;


public class ResultDetailQuesActivty extends Activity {

    private ImageView backImageView;
    private GoodView goodView;
    private GoodView bookmarkView;
    private CNCProblem cnc;
    private TextView precentText;
    private TextView solutionText;
    private int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detailed_ques);

        Intent intent = getIntent();
        cnc = intent.getParcelableExtra(SearchAdapter.SEARCH_BY_QUES);//从碎片里获得任务类Task

        precentText = (TextView) findViewById(R.id.precentage_ques);
        solutionText = (TextView) findViewById(R.id.solution_ques);

        backImageView = (ImageView)findViewById(R.id.back_to_result_ques);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        precentText.setText(cnc.getPercentage() + "");
        solutionText.setText(cnc.getQuestionDetail());

        goodView = new GoodView(this);
        bookmarkView = new GoodView(this);

    }
    public void good(View view){
        if(i == 1) {
            ((ImageView) view).setImageResource(R.drawable.good_checked);
            goodView.setText("+1");
            goodView.show(view);
            i = 0;
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
}
