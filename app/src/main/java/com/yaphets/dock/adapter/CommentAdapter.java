package com.yaphets.dock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaphets.dock.R;
import com.yaphets.dock.model.entity.Comment;
import com.yaphets.dock.model.entity.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> mComments;
    private Context mContext;

    public CommentAdapter(Context context, List<Comment> list) {
        this.mContext = context;
        this.mComments = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        User user = comment.getUser();
        if (user != null && user.getThumb() != null) {
            Glide.with(mContext).load(user.getThumb()).into(holder.thumb);
        }
        holder.name.setText(user.getNickname());
        holder.score.setRating(comment.getScore());
        holder.SetTime(comment.getTime());
        holder.content.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView name;
        RatingBar score;
        TextView time;
        TextView content;

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

        public ViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.img_thumb);
            name = itemView.findViewById(R.id.tv_name);
            score = itemView.findViewById(R.id.rb_score);
            time = itemView.findViewById(R.id.tv_comment_time);
            content = itemView.findViewById(R.id.tv_comment_content);
        }

        void SetTime(Timestamp timestamp) {
            time.setText(sdf.format(timestamp));
        }
    }
}
