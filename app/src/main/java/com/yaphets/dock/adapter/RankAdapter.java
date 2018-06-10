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
import java.util.Locale;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<Game> mGames;
    private Context mContext;

    public RankAdapter(Context context, List<Game> games) {
        this.mContext = context;
        this.mGames = games;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_rank, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Game game = mGames.get(position);
        holder.rank.setText(String.valueOf(position+1));
        holder.resetRankColor();
        if (position < 3) {
            holder.rank.setTextColor(mContext.getColor(R.color.selectedTab));
        }
        holder.name.setText(game.getName());
        holder.price.setText(String.format(Locale.CHINA, "%.1f￥",game.getPrice()));
        if (game.getInfo() != null && game.getInfo().getCover() != null) {
            Glide.with(mContext).load(game.getInfo().getCover()).into(holder.cover);
        }
        holder.score.setRating(game.getScore());

        holder.intent.putExtra("game", game);
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public void changeDataSet(List<Game> games) {
        mGames = games;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank;
        ImageView cover;
        TextView name;
        RatingBar score;
        TextView price;

        private int defaultTextColor;

        Intent intent;

        public ViewHolder(View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.tv_rank);
            cover = itemView.findViewById(R.id.img_cover);
            name = itemView.findViewById(R.id.tv_name);
            score = itemView.findViewById(R.id.rb_score);
            price = itemView.findViewById(R.id.tv_price);

            defaultTextColor = rank.getCurrentTextColor();

            intent = new Intent(mContext, GameDetailActivity.class);
            itemView.setOnClickListener(v -> {
                mContext.startActivity(intent);
            });
        }

        void resetRankColor() {
            rank.setTextColor(defaultTextColor);
        }
    }
}
