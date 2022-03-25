package com.game;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<RankList> data;
    private LayoutInflater layoutInflater;


    public RVAdapter(Context context, List<RankList> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        for(int i = 0; i < data.size(); i++){
            System.out.println(data.size());
           System.out.println(data.get(i).getNick() + "-" + data.get(i).getPoints());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = layoutInflater.inflate(R.layout.recyclerview_rank, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankList rankList = data.get(position);

        holder.lp.setText(String.valueOf(position + 1));
        holder.nick.setText(rankList.getNick());
        holder.point.setText(String.valueOf(rankList.getPoints()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lp, nick, point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lp = itemView.findViewById(R.id.lp);
            nick = itemView.findViewById(R.id.nick);
            point = itemView.findViewById(R.id.point);
        }
    }
}
