package cn.aysst.www.doctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.aysst.www.doctor.R;
import cn.aysst.www.doctor.ResultDetailIdActivity;
import cn.aysst.www.doctor.ResultDetailQuesActivty;
import cn.aysst.www.doctor.beans.CNCProblem;
import cn.aysst.www.doctor.utils.Http;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {


    public final static String SEARCH_BY_ID = "id";
    public final static String SEARCH_BY_QUES = "ques";
    public final static int ITEM_TYPE_HEADER = 0;
    public final static int ITEM_TYPE_TEXT = 1;
    public List<CNCProblem> searchList = new ArrayList<>();
    private Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

   class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView solution_text;
        TextView percentage_text;
        TextView search_type_text;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            percentage_text = (TextView) view.findViewById(R.id.search_precentage);
            solution_text = (TextView) view.findViewById(R.id.search_solution);
            search_type_text = (TextView) view.findViewById(R.id.search_type);
        }
    }

   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if(context == null){
           context = parent.getContext();
       }
       View view = LayoutInflater.from(context).inflate(R.layout.item_layout_search, parent, false);
       final ViewHolder holder = new ViewHolder(view);

       holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int position = holder.getAdapterPosition();
               CNCProblem cnc = searchList.get(position);
               Log.d(Http.TAG, cnc.getSearchType() + "-----------");
               Log.d(Http.TAG, cnc.getPercentage() + "-----------");
               Log.d(Http.TAG, cnc.getSolutionDetail() + "-----------");

               if (cnc.getSearchType().equals("精确查询")) {
                   Intent intent1 = new Intent(context, ResultDetailIdActivity.class);
                   intent1.putExtra(SearchAdapter.SEARCH_BY_ID, cnc);
                   context.startActivity(intent1);
               } else if (cnc.getSearchType().equals("普通查询") || cnc.getSearchType().equals("爬虫查询")){
                   Intent intent2 = new Intent(context, ResultDetailQuesActivty.class);
                   intent2.putExtra(SearchAdapter.SEARCH_BY_QUES, cnc);
                   context.startActivity(intent2);
               }
           }

       });
       return holder;
   }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(Http.TAG, position + "---------------------------");
        CNCProblem cnc = searchList.get(position);
        holder.solution_text.setText(cnc.getSolution());
        holder.percentage_text.setText(cnc.getPercentage() + "");
        holder.search_type_text.setText(cnc.getSearchType());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }


    public void refreshItems(List<CNCProblem> items) {
        searchList.clear();
        searchList.addAll(items);
        notifyDataSetChanged();
    }


    public void addItem(CNCProblem item) {
        if (! searchList.contains(item)) {
            searchList.add(item);
            notifyDataSetChanged();
        }
    }
    public void addFirstItem(CNCProblem item) {
        if (! searchList.contains(item)) {
            searchList.add(0, item);
            notifyDataSetChanged();
        }

    }
    public void removeItem(int position) {
        searchList.remove(position);
        notifyItemRemoved(position);
    }
    public void clean() {
        searchList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_HEADER : ITEM_TYPE_TEXT;
    }
}
