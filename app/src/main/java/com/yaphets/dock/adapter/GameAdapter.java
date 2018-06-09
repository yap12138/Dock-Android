package com.yaphets.dock.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaphets.dock.R;
import com.yaphets.dock.activity.GameDetailActivity;
import com.yaphets.dock.model.entity.Game;

import java.util.List;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mList;
    private Context mContext;

    public GameAdapter(Context context, List<Game> list) {
        this.mContext = context;
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
        holder.price.setText(String.format(Locale.CHINA, "售价：%4.1f",game.getPrice()));
        holder.pic.setImageBitmap(null);
        if (game.getInfo() != null && game.getInfo().getCover() != null) {
            Glide.with(mContext).load(game.getInfo().getCover()).into(holder.pic);
        }
        holder.intent.putExtra("game", game);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        TextView desc;
        TextView price;

        Intent intent;

        public ViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.img_pic);
            name = itemView.findViewById(R.id.tv_name);
            desc = itemView.findViewById(R.id.tv_description);
            price = itemView.findViewById(R.id.tv_price);

            intent = new Intent(mContext, GameDetailActivity.class);
            itemView.setOnClickListener(v -> {
                mContext.startActivity(intent);
            });
        }
    }
}
