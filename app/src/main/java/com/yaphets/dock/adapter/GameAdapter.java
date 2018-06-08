package com.yaphets.dock.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaphets.dock.R;
import com.yaphets.dock.model.entity.Game;

import java.util.List;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mList;

    public GameAdapter(List<Game> list) {
        this.mList =list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Game game = mList.get(position);
        holder.name.setText(game.getName());
        holder.desc.setText(game.getDescription());
        holder.price.setText(String.format(Locale.CHINA, "售价：%.1f",game.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        TextView desc;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.img_pic);
            name = itemView.findViewById(R.id.tv_name);
            desc = itemView.findViewById(R.id.tv_description);
            price = itemView.findViewById(R.id.tv_price);
        }
    }
}
