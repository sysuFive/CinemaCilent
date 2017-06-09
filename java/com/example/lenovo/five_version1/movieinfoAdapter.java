package com.example.lenovo.five_version1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/6/7.
 */
public class movieinfoAdapter extends RecyclerView.Adapter<movieinfoAdapter.ViewHolder> {
    private ArrayList<movieinfo> movie_list;
    private LayoutInflater mInflater;
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, movieinfo item);
    }
    private OnItemClickListener  mOnItemClickListener;
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener =  mOnItemClickListener;
    }
    public movieinfoAdapter(Context context, ArrayList<movieinfo> items) {
        super();
        movie_list = items;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =  mInflater.inflate(R.layout.movies, viewGroup,  false);
        ViewHolder holder =  new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.img);
        return holder;
    }
    @Override
    public void onBindViewHolder( final ViewHolder viewHolder, final int i) {
        viewHolder.img.setImageURI(movie_list.get(i).getImg());
        if ( mOnItemClickListener !=  null)
        {
            viewHolder. itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder. itemView, i, movie_list.get(i));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return  movie_list.size();
    }
    public static class ViewHolder  extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        ImageView img;
    }
}
