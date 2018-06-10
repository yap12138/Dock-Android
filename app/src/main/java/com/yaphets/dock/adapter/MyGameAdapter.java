package com.yaphets.dock.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaphets.dock.R;
import com.yaphets.dock.activity.GameDetailActivity;
import com.yaphets.dock.model.entity.Game;

import java.util.List;

public class MyGameAdapter extends RecyclerView.Adapter<MyGameAdapter.ViewHolder> {

    private List<Game> mList;
    private Context mContext;

    public MyGameAdapter(Context context, List<Game> list) {
        this.mContext = context;
        this.mList =list;
    }

    public void replaceDataSet(List<Game> games) {
        mList = games;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Game game = mList.get(position);
        if (game.getInfo() != null && game.getInfo().getCover() != null) {
            Glide.with(mContext).load(game.getInfo().getCover()).into(holder.cover);
        }
        holder.name.setText(game.getName());
        holder.score.setRating(game.getScore());

        holder.intent.putExtra("game", game);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;
        RatingBar score;

        Intent intent;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.img_cover);
            name = itemView.findViewById(R.id.tv_name);
            score = itemView.findViewById(R.id.rb_score);

            intent = new Intent(mContext, GameDetailActivity.class);
            itemView.setOnClickListener(v -> {
                mContext.startActivity(intent);
            });
        }
    }
}
