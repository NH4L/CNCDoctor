package cn.aysst.www.doctor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.aysst.www.doctor.R;
import cn.aysst.www.doctor.beans.DloadBean;

public class MyGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<DloadBean> list;
    private ImageView mImageView;
    LayoutInflater layoutInflater;

    public MyGridAdapter(Context mContext,List<DloadBean>list){
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext=mContext;
        this.list=list;
    }
    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup  parent){
        convertView = layoutInflater.inflate(R.layout.grid_item,null);
        mImageView = (ImageView) convertView.findViewById(R.id.image);
        TextView tv = (TextView)convertView.findViewById(R.id.title);
        mImageView.setBackgroundResource(R.drawable.pdf_download);
        tv.setText(list.get(position).getTitle());
        return convertView;
    }
}
