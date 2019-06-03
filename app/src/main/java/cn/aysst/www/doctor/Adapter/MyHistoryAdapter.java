package cn.aysst.www.doctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.aysst.www.doctor.R;
import cn.aysst.www.doctor.ResultDetailIdActivity;
import cn.aysst.www.doctor.beans.CNCProblem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DefaultAccount on 2017/9/15.
 */

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.ViewHolder> {
    public final static int ITEM_TYPE_HEADER = 0;
    public final static int ITEM_TYPE_TEXT = 1;
    public final List<CNCProblem> cncList = new ArrayList<>();
    private Context context;
    private ViewHolder holder;
    public MyHistoryAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView question_text;
        TextView brand_text;
        TextView type_text;
        TextView solution_text;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            question_text = (TextView) view.findViewById(R.id.history_question);
            solution_text = (TextView) view.findViewById(R.id.history_solution);
            type_text = (TextView) view.findViewById(R.id.history_type);
            brand_text = (TextView) view.findViewById(R.id.history_brand);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_history, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CNCProblem cnc = cncList.get(position);
        holder.question_text.setText(cnc.getQuestion());
        holder.brand_text.setText(cnc.getBrand());
        holder.type_text.setText(cnc.getType());
        holder.solution_text.setText(cnc.getSolution());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CNCProblem cnc = cncList.get(position);
//                Toast.makeText(context, cnc.getQuestion() + "  "  + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ResultDetailIdActivity.class);
                intent.putExtra(SearchAdapter.SEARCH_BY_ID, cnc);
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return cncList.size();
    }


    public void refreshItems(List<CNCProblem> items) {
        cncList.clear();
        cncList.addAll(items);
        notifyDataSetChanged();
    }


    public void addItem(CNCProblem item) {
        if (! cncList.contains(item)) {
            cncList.add(item);
            notifyDataSetChanged();
        }
    }
    public void addFirstItem(CNCProblem item) {
        if (! cncList.contains(item)) {
            cncList.add(0, item);
            notifyDataSetChanged();
        }

    }
    public void removeItem(int position) {
        cncList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_HEADER : ITEM_TYPE_TEXT;
    }


}
