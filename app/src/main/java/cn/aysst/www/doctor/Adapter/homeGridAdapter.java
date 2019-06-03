package cn.aysst.www.doctor.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.aysst.www.doctor.R;
import cn.aysst.www.doctor.beans.homeGridBean;

public class homeGridAdapter extends BaseAdapter {
    private Context context;
    private List<homeGridBean> list;
    LayoutInflater layoutInflater;

    public homeGridAdapter(Context context,List<homeGridBean> imageView){
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.list=imageView;
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
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = layoutInflater.inflate(R.layout.home_grid_item,null);
        ImageView ima = (ImageView)convertView.findViewById(R.id.ima);
        ima.setBackgroundResource(list.get(position).getImage());
        TextView im = (TextView) convertView.findViewById(R.id.hometext);
        im.setText(list.get(position).getText());
        return convertView;
    }

}
