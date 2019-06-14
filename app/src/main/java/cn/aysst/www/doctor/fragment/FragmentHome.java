package cn.aysst.www.doctor.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cn.aysst.www.doctor.Adapter.homeGridAdapter;
import cn.aysst.www.doctor.NewsDetailActivity;
import cn.aysst.www.doctor.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.aysst.www.doctor.SearchResult;
import cn.aysst.www.doctor.beans.Voice;
import cn.aysst.www.doctor.beans.homeGridBean;
import cn.aysst.www.doctor.utils.Http;
import cn.aysst.www.doctor.utils.NetworkJudge;
import cn.aysst.www.doctor.utils.PermissionsUtils;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentHome extends Fragment implements View.OnClickListener {
	private Button btnQueryById;
	private FloatingSearchView sv;
	//		private MaterialSearchView msv;
	private Menu menu;
    private GridView gridView;
	private TextView t4;
	private CardView t1;
	private CardView t7;
	private CardView t3;
	private CardView t8;
	private CardView t6;
	private CardView t;
	private CardView t5;
	private CardView t9;
	private TextView text;
    private TextView change;
	private ImageView voice;
    private MaterialEditText editTextBrand;
    private MaterialEditText editTextType;
    private MaterialEditText editTextQuesType;
    private MaterialEditText editTextID;

	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(20000, TimeUnit.MILLISECONDS)
			.readTimeout(20000, TimeUnit.MILLISECONDS)
			.build();

	public static FragmentHome newInstance() {
		FragmentHome fragment = new FragmentHome();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_home,null);

        SpeechUtility.createUtility(view.getContext(), SpeechConstant.APPID +"=5c78eb4b");



        //btn1 = (ToggleButton)view.findViewById(R.id.toggle_button);
        btnQueryById = (Button)view.findViewById(R.id.btn_query_by_id);
        t = (CardView)view.findViewById(R.id.t);
        t1 = (CardView)view.findViewById(R.id.t1);
        t3 = (CardView)view.findViewById(R.id.t3);
        t5 = (CardView)view.findViewById(R.id.t5);
        sv = (FloatingSearchView)view.findViewById(R.id.floatingsearch);
//		msv = (MaterialSearchView)view.findViewById(R.id.search_view);
        t4 = (TextView) view.findViewById(R.id.t4);
        t6 = (CardView)view.findViewById(R.id.t6);
        t7 = (CardView) view.findViewById(R.id.t7);
        t8 = (CardView) view.findViewById(R.id.t8);
        t9 = (CardView) view.findViewById(R.id.t9);
        voice=(ImageView)view.findViewById(R.id.voice);
        btnQueryById.setVisibility(Button.GONE);

        change= (TextView)view.findViewById(R.id.change);
        List<homeGridBean> list = new ArrayList<homeGridBean>();

        homeGridBean hgb1 = new homeGridBean();
        hgb1.setImage(R.drawable.mfactory);
        hgb1.setText("M工厂数控");
        hgb1.setUrl("http://www.m-cnc.com/");

        homeGridBean hgb2 = new homeGridBean();
        hgb2.setImage(R.drawable.tieba);
        hgb2.setText("数控维修吧");
        hgb2.setUrl("https://tieba.baidu.com/f?kw=%E6%95%B0%E6%8E%A7%E6%9C%BA%E5%BA%8A%E7%BB%B4%E4%BF%AE");

        homeGridBean hgb3 = new homeGridBean();
        hgb3.setImage(R.drawable.gongkong);
        hgb3.setText("中国工控网");
        hgb3.setUrl("http://www.gongkong.com/");

        homeGridBean hgb4 = new homeGridBean();
        hgb4.setImage(R.drawable.daniu);
        hgb4.setText("大牛数控");
        hgb4.setUrl("https://www.d6sk.com/");

        homeGridBean hgb5 = new homeGridBean();
        hgb5.setImage(R.drawable.changbo);
        hgb5.setText("常泊维修");
        hgb5.setUrl("http://www.czcbwx.cn/");

        homeGridBean hgb6 = new homeGridBean();
        hgb6.setImage(R.drawable.yixiu);
        hgb6.setText("易修网");
        hgb6.setUrl("http://www.easiu.com/gongyingjixieshebei/shukongjichuang/");

        homeGridBean hgb7 = new homeGridBean();
        hgb7.setImage(R.drawable.canye);
        hgb7.setText("数控产业网");
        hgb7.setUrl("http://www.jc81.com/");

        homeGridBean hgb8 = new homeGridBean();
        hgb8.setImage(R.drawable.gsk);
        hgb8.setText("广州数控");
        hgb8.setUrl("http://www.gsk.com.cn/");
        list.add(hgb1);
        list.add(hgb2);
        list.add(hgb3);
        list.add(hgb4);
        list.add(hgb5);
        list.add(hgb6);
        list.add(hgb7);
        list.add(hgb8);
        gridView = (GridView)view.findViewById(R.id.home);
        homeGridAdapter h = new homeGridAdapter(getActivity(),list);
        gridView.setAdapter(h);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = list.get(position).getUrl();
                Uri uri = Uri.parse(s);
                Intent intent =  new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        t1.setVisibility(CardView.GONE);
        t3.setVisibility(CardView.GONE);
        t5.setVisibility(CardView.GONE);
        t.setVisibility(CardView.GONE);
        setHasOptionsMenu(true);

        editTextBrand = (MaterialEditText) view.findViewById(R.id.inquire_brand);
        editTextType = (MaterialEditText) view.findViewById(R.id.inquire_type);
        editTextQuesType = (MaterialEditText) view.findViewById(R.id.inquire_questype);
        editTextID = (MaterialEditText) view.findViewById(R.id.inquire_id);

        sv.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                String question = sv.getQuery();
                if (!(question == null)) {

                    if (!(question.equals(",,##"))){
                        // TODO 待实现输入无意义词让用户重新输入
                        findSolutionByQues(question);
                    } else {
                        Toast.makeText(getContext(), "请输入有意义的问题", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "请输入问题", Toast.LENGTH_SHORT).show();
                }

            }
        });

        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("url","http://new.haiwell.com/solution/733-cn.html");
                startActivity(intent);
            }
        });
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
                intent.putExtra("url","http://www.ca800.com/apply/d_1o18s0esa2p81_1.html");
                startActivity(intent);
            }
        });
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
                intent.putExtra("url","http://www.bethel-cnc.com/bethel/news/show_2356.html");
                startActivity(intent);
            }
        });
        t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
                intent.putExtra("url","http://www.bethel-cnc.com/bethel/news/show_1543.html");
                startActivity(intent);
            }
        });

        voice.setOnClickListener(this);
        btnQueryById.setOnClickListener(this);

        return view;
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice:
                String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
                if (PermissionsUtils.getInstance().chekPermissions((Activity) getContext(), permissions, permissionsResult))
                    initSpeech(getContext());
                break;
            case R.id.btn_query_by_id:
                if (!NetworkJudge.isNetworkConnected(getActivity())){
                    Toast.makeText(getActivity(), "请检查网络连接!", Toast.LENGTH_SHORT).show();
                } else {
                    if (editTextBrand.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "请输入数控机床品牌!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (editTextType.getText().toString().trim().equals("")) {
                            Toast.makeText(getActivity(), "请输入数控机床系统名称!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (editTextQuesType.getText().toString().trim().equals("")) {
                                Toast.makeText(getActivity(), "请输入故障类型!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (editTextID.getText().toString().trim().equals("")) {
                                    Toast.makeText(getActivity(), "请输入故障报警错误代码!", Toast.LENGTH_SHORT).show();
                                } else {
                                    String brand = editTextBrand.getText().toString().trim();
                                    String type = editTextType.getText().toString().trim();
                                    String questype = editTextQuesType.getText().toString().trim();
                                    String id = editTextID.getText().toString().trim();
                                    Log.d(Http.TAG, brand + "\n" + type + "\n" + questype + "\n" + id);
                                    findSolutionById(brand, type, questype, id);

                                }
                            }
                        }
                    }
                }
                break;

        }
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
//            Toast.makeText(getActivity(), "权限通过!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(getActivity(), "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 初始化语音识别
     */
    public void initSpeech(final Context context) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    String result = parseVoice(recognizerResult.getResultString());

                    sv.setSearchText(sv.getQuery() + result);
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.searchmenu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_gallery:
//			msv.setVisibility(MaterialSearchView.GONE);
                sv.setVisibility(FloatingSearchView.GONE);
                btnQueryById.setVisibility(Button.VISIBLE);
                t6.setVisibility(CardView.GONE);
                gridView.setVisibility(GridView.GONE);
                t7.setVisibility(CardView.GONE);
                t8.setVisibility(CardView.GONE);
                t9.setVisibility(CardView.GONE);
                t4.setVisibility(TextView.GONE);
                change.setVisibility(TextView.GONE);
                voice.setVisibility(ImageView.GONE);
                t1.setVisibility(CardView.VISIBLE);
                t3.setVisibility(CardView.VISIBLE);
                t5.setVisibility(CardView.VISIBLE);
                t.setVisibility(CardView.VISIBLE);
                break;
            default:
//				msv.setVisibility(MaterialSearchView.VISIBLE);
                sv.setVisibility(FloatingSearchView.VISIBLE);
                btnQueryById.setVisibility(Button.GONE);
                gridView.setVisibility(GridView.VISIBLE);
                t6.setVisibility(CardView.VISIBLE);
                t7.setVisibility(CardView.VISIBLE);
                t8.setVisibility(CardView.VISIBLE);
                t9.setVisibility(CardView.VISIBLE);
                t4.setVisibility(TextView.VISIBLE);
                change.setVisibility(TextView.VISIBLE);
                voice.setVisibility(ImageView.VISIBLE);
                t1.setVisibility(CardView.GONE);
                t3.setVisibility(CardView.GONE);
                t5.setVisibility(CardView.GONE);
                t.setVisibility(CardView.GONE);
        }
        return true;
    }

    private void findSolutionById(String brand, String type, String questype, String id) {
	    new FindSoluByIdAsyncTask().execute(brand, type, questype, id);
    }

    private void findSolutionByQues(String question) {
        new FindSoluByQuesAsyncTask().execute(question);
    }

    class FindSoluByIdAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostGetSoluById(params[0], params[1], params[2], params[3]);
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String msg = obj.getString("msg");
                    Log.d(Http.TAG, "code=" + code + ", msg=" + msg);
                    JSONObject data = obj.getJSONObject("data");
                    Log.d(Http.TAG, data.toString());
                    if (code == 0 && msg.equals("success")) {
                        if (!(data.getString("problem") == null)) {
                            String problem = data.getString("problem");

                            String solution = data.getString("solution");
                            String type = data.getString("type");
                            String brand = data.getString("brand");
                            String questype = data.getString("questype");
                            String id = data.getString("id");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//查询时间
                            String time = df.format(new Date());

                            Intent intent = new Intent(getActivity(), SearchResult.class);
                            intent.putExtra("searchType", 0);
                            intent.putExtra("problem", problem);
                            intent.putExtra("type", type);
                            intent.putExtra("solution", solution);
                            intent.putExtra("brand", brand);
                            intent.putExtra("questype", questype);
                            intent.putExtra("id", id);
                            intent.putExtra("time", time);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "输入故障代码或故障类型有误，请重新输入查询!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "目前网络繁忙，请稍后重试!", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class FindSoluByQuesAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostGetSoluByQues(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String msg = obj.getString("msg");
                    Log.d(Http.TAG, "code=" + code + ", msg=" + msg);
                    JSONObject data = obj.getJSONObject("data");
                    Log.d(Http.TAG, data.toString());
                    if (code == 0 && msg.equals("success")) {
                        String question = data.getString("question");
                        for (int i=0; i<5; i++) {
                            JSONObject problemObj = data.getJSONObject("problem" + i);
                            String solution = problemObj.getString("solution");
                            double precentage = problemObj.getDouble("percentage");
                            Log.d(Http.TAG, "解决方法" + solution + ", 概率" + precentage);
                        }
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //查询时间
                        String time = df.format(new Date());

                        Intent intent = new Intent(getActivity(), SearchResult.class);
                        intent.putExtra("searchType", 1);
                        intent.putExtra("solution", data.toString());
                        intent.putExtra("time", time);
                        intent.putExtra("question", question);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "查询繁忙,请稍后重试", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String httpPostGetSoluById(String brand, String type, String questype, String id) {
        String result = null;

        FormBody body = new FormBody.Builder()
                .add("brand", brand)
                .add("type", type)
                .add("questype", questype)
                .add("id", id)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/cnc/getsolutionbyid")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                Log.d(Http.TAG, result);
                result = URLDecoder.decode(result, "UTF-8");
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String httpPostGetSoluByQues(String question) {
        String result = null;

        FormBody body = new FormBody.Builder()
                .add("question", question)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/cnc/getsolutionbyques")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                Log.d(Http.TAG, result);
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
