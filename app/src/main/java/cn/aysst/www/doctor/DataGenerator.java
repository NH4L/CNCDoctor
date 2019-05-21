package cn.aysst.www.doctor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.aysst.www.doctor.fragment.*;

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.drawable.tab_home_selector,R.drawable.tab_release_selector,R.drawable.tab_notice_selector};
    public static final int []mTabResPressed = new int[]{R.drawable.tab_home_selected,R.drawable.tab_release_selected,R.drawable.tab_notice_selected};
    public static final String []mTabTitle = new String[]{"首页", "发布", "通知"};

    public static Fragment[] getFragments(){
        Fragment fragments[] = new Fragment[3];
        fragments[0] = FragmentHome.newInstance();
        fragments[1] = FragmentHistory.newInstance();
        fragments[2] = FragmentDownload.newInstance();
        return fragments;
    }


    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}