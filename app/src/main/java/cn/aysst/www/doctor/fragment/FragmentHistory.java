package cn.aysst.www.doctor.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import cn.aysst.www.doctor.Adapter.MyHistoryAdapter;
import cn.aysst.www.doctor.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.aysst.www.doctor.beans.CNCProblem;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class FragmentHistory extends Fragment {
	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(500, TimeUnit.MILLISECONDS)
			.readTimeout(500, TimeUnit.MILLISECONDS)
			.build();
    private RecyclerView recyclerView;
    private MyHistoryAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

	public static FragmentHistory newInstance() {
		FragmentHistory fragment = new FragmentHistory();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_history, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_cnc_history);
        adapter = new MyHistoryAdapter(getActivity());
        //设置LayoutManager为LinearLayoutManager
        layoutManager= new LinearLayoutManager(getActivity());
        for(int i=0; i<5; i++){
            CNCProblem CNCProblem =new CNCProblem();
            CNCProblem.setBrand("fanuc");
            CNCProblem.setQuestion("主轴温度也忒高了吧" + i);
            CNCProblem.setQuestype("系统报警");
            CNCProblem.setType("OA-i");
            CNCProblem.setSolution("往机床里面浇水就行啦，直接大水漫灌!!");
            adapter.addItem(CNCProblem);
        }

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_onhistory);
        swipeRefreshLayout.setColorSchemeResources(R.color.btn);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTask();
            }
        });

        //设置LayoutManager和Adapter
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

		return view;
	}

    private void refreshTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "正在刷新", Toast.LENGTH_SHORT);
                    }
                });
            }
        }).start();
    }

}
