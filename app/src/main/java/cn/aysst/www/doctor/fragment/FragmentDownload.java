package cn.aysst.www.doctor.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.v7.widget.*;
import android.util.Log;
import android.widget.*;
import cn.aysst.www.doctor.Adapter.MyGridAdapter;
import cn.aysst.www.doctor.MainActivity;
import cn.aysst.www.doctor.R;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.aysst.www.doctor.beans.DloadBean;
import cn.aysst.www.doctor.utils.DownloadUtil;
import cn.aysst.www.doctor.utils.Http;
import cn.aysst.www.doctor.utils.NullListInAdapterException;
import com.daimajia.numberprogressbar.NumberProgressBar;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FragmentDownload extends Fragment {

    private FloatingActionButton floatingActionButton;

    private GridView gridView;
    private ProgressBar progressBar;
    public static FragmentDownload newInstance() {
		FragmentDownload fragment = new FragmentDownload();                ;
		return fragment;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_download, null);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_download);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
//        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.pull_up_on_download);
//
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url = "http://192.168.43.197:8080/files/texts/马扎克 INTEGREX i-150 参考、报警、M代码.pdf";
//                new downloadFileAsyncTask().execute(url);
//
//            }
//        });

        gridView = (GridView) view.findViewById(R.id.GridView1);
        List<DloadBean> list = new ArrayList<DloadBean>();
        DloadBean dl = new DloadBean();
        dl.setTitle("FANUC 0i-A 参数说明书");
        dl.setUrl(Http.BASE_URL + "/files/texts/FANUC 0i-A 参数说明书.pdf");
        dl.setImage(R.drawable.pdf_download);

        DloadBean dx = new DloadBean();
        dx.setTitle("FANUC_TC报警操作说明书");
        dx.setImage(R.drawable.word_download);
        dx.setUrl(Http.BASE_URL + "/files/texts/FANUC_TC报警操作说明书.docx");

        DloadBean d = new DloadBean();
        d.setTitle("Mazak马扎克伺服报警说明");
        d.setImage(R.drawable.word_download);
        d.setUrl(Http.BASE_URL + "/files/texts/Mazak马扎克伺服报警说明.docx");

        DloadBean d1 = new DloadBean();
        d1.setTitle("发那科数控系统报警代码表");
        d1.setImage(R.drawable.pdf_download);
        d1.setUrl(Http.BASE_URL + "/files/texts/发那科数控系统报警代码表.pdf");

        DloadBean da = new DloadBean();
        da.setTitle("故障诊断及维修技术500例");
        da.setImage(R.drawable.word_download);
        da.setUrl(Http.BASE_URL + "/files/texts/故障诊断及维修技术500例.docx");

        DloadBean db = new DloadBean();
        db.setTitle("广州数控GSK928系列报警代号及处理方法");
        db.setImage(R.drawable.pdf_download);
        db.setUrl(Http.BASE_URL + "/files/texts/马扎克 INTEGREX i-150 参考、报警、M代码.pdf");

        DloadBean dc = new DloadBean();
        dc.setTitle("广州数控常见故障维修手册");
        dc.setImage(R.drawable.pdf_download);
        dc.setUrl(Http.BASE_URL + "/files/texts/广州数控常见故障维修手册.pdf");

        DloadBean dd = new DloadBean();
        dd.setTitle("马扎克 INTEGREX i-150 参考、报警、M代码");
        dd.setImage(R.drawable.pdf_download);
        dd.setUrl(Http.BASE_URL + "/files/texts/马扎克 INTEGREX i-150 参考、报警、M代码.pdf");

        DloadBean de =new DloadBean();
        de.setTitle("三菱6060S系列报警参数说明书");
        de.setImage(R.drawable.word_download);
        de.setUrl(Http.BASE_URL + "/files/texts/三菱6060S系列报警参数说明书.docx");

        DloadBean df = new DloadBean();
        df.setTitle("三菱数控系统E60E68E80系列报警参数手册");
        df.setImage(R.drawable.pdf_download);
        df.setUrl(Http.BASE_URL + "/files/texts/三菱数控系统E60E68E80系列报警参数手册.pdf");

        DloadBean dg = new DloadBean();
        dg.setTitle("三菱数控系统M700VM70V系列");
        dg.setImage(R.drawable.pdf_download);
        dg.setUrl(Http.BASE_URL + "/files/texts/三菱数控系统M700VM70V系列.pdf");

        DloadBean dh = new DloadBean();
        dh.setTitle("三菱数控系统M800 M80系列报警参数说明书");
        dh.setImage(R.drawable.pdf_download);
        dh.setUrl(Http.BASE_URL + "/files/texts/三菱数控系统M800 M80系列报警参数说明书.pdf");

        DloadBean di = new DloadBean();
        di.setTitle("西门子数控机床故障代码");
        di.setImage(R.drawable.word_download);
        di.setUrl(Http.BASE_URL + "/files/texts/西门子数控机床故障代码.docx");

        list.add(da);
        list.add(db);
        list.add(dc);
        list.add(dd);
        list.add(de);
        list.add(df);
        list.add(dg);
        list.add(dh);
        list.add(di);
        list.add(dl);
        list.add(dx);
        list.add(d);
        list.add(d1);
        MyGridAdapter myGridAdapter = new MyGridAdapter(getActivity(),list);
        gridView.setAdapter(myGridAdapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("请确认是否下载该文件")
                        .setMessage("若确认下载请点击'确认'键，反之则点击'取消'")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.setVisibility(ProgressBar.VISIBLE);
                                //String url = "http://192.168.43.197:8080/files/texts/马扎克 INTEGREX i-150 参考、报警、M代码.pdf";
                                DloadBean p = (DloadBean)myGridAdapter.getItem(position);
                                String url = p.getUrl();
                                new downloadFileAsyncTask().execute(url);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


                return true;
            }
        });

        return view;
    }


    /**
     * 一个发布任务AsyncTask
     */
    class downloadFileAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return downFile(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
//                Toast.makeText(getActivity(), "下载成功!", Toast.LENGTH_SHORT).show();
                Log.d(Http.TAG,"download success");
            }
        }
    }

    /**
     * 下载文件
     * @param url
     */
    private String downFile(String url) {
        progressBar.setProgress(0);
        progressBar.setMax(100);
        DownloadUtil.get().download(url, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (progressBar != null && progressBar.getProgress() == progressBar.getMax()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    new Thread(){
                        public void run(){
                            Looper.prepare();//给当前线程初始化Looper
                            Toast.makeText(getActivity(),"下载成功!",Toast.LENGTH_SHORT).show();//Toast初始化的时候会new Handler();无参构造默认获取当前线程的Looper，如果没有prepare过，则抛出题主描述的异常。上一句代码初始化过了，就不会出错。
                            Looper.loop();//这句执行，Toast排队show所依赖的Handler发出的消息就有人处理了，Toast就可以吐出来了。但是，这个Thread也阻塞这里了，因为loop()是个for (;;) ...
                        }
                    }.start();
                }

            }
            @Override
            public void onDownloading(int progress) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                e.printStackTrace();
                Log.i("DOWNLOAD","download fail");
                new Thread(){
                    public void run(){
                        Looper.prepare();//给当前线程初始化Looper
                        Toast.makeText(getActivity(),"下载失败请重试!",Toast.LENGTH_LONG).show();//Toast初始化的时候会new Handler();无参构造默认获取当前线程的Looper，如果没有prepare过，则抛出题主描述的异常。上一句代码初始化过了，就不会出错。
                        Looper.loop();//这句执行，Toast排队show所依赖的Handler发出的消息就有人处理了，Toast就可以吐出来了。但是，这个Thread也阻塞这里了，因为loop()是个for (;;) ...
                    }
                }.start();
                //下载异常进行相关提示操作
            }
        });
        return "success";
    }
}
