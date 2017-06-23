package com.example.x550v.five.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.x550v.five.R;
import com.example.x550v.five.model.CinemaFilm;

import java.util.ArrayList;

/**
 * Created by X550V on 2017/6/10.
 */

public class CinemaFilmAdapter extends RecyclerView.Adapter<CinemaFilmAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<CinemaFilm> filmCards;
    private LayoutInflater mInflater;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, CinemaFilm item);
    }

    private CinemaFilmAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CinemaFilmAdapter.OnItemClickListener mOnItemClickListener)
    {
        this.onItemClickListener =  mOnItemClickListener;
    }

    public CinemaFilmAdapter(Context context, ArrayList<CinemaFilm> items) {
        super();
        filmCards = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CinemaFilmAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =  mInflater.inflate(R.layout.movieintheateritem, viewGroup,  false);
        view.setOnClickListener(this);
        CinemaFilmAdapter.ViewHolder holder =  new CinemaFilmAdapter.ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.movieimg);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.img.setImageResource(filmCards.get(position).getRid());
        viewHolder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取position
            int position = (int)v.getTag();
            onItemClickListener.onItemClick(v, position, filmCards.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return  filmCards.size();
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        public ImageView img;
    }

}

