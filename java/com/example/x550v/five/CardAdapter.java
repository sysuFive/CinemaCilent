package com.example.x550v.five;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by X550V on 2017/6/9.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<FilmCard> filmCards;
    private LayoutInflater mInflater;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, FilmCard item);
    }

    private OnItemClickListener  onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.onItemClickListener =  mOnItemClickListener;
    }

    public CardAdapter(Context context, ArrayList<FilmCard> items) {
        super();
        filmCards = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =  mInflater.inflate(R.layout.movieitems, viewGroup,  false);
        view.setOnClickListener(this);
        ViewHolder holder =  new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.movieimg);
        holder.name =(TextView)view.findViewById(R.id.moviename);
        holder.actors = (TextView)view.findViewById(R.id.movieactor);
        holder.type = (TextView) view.findViewById(R.id.movieinfo);
        holder.rate = (TextView) view.findViewById(R.id.moviegrade);
        holder.container = (ConstraintLayout) view.findViewById(R.id.movie_container);
        return holder;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取position
            int position = (int)v.getTag();
            onItemClickListener.onItemClick(v,position, filmCards.get(position));
        }
    }

    @Override
    public void onBindViewHolder( final ViewHolder viewHolder, final int i) {
        viewHolder.img.setImageResource(filmCards.get(i).getImg());
        viewHolder.name.setText( filmCards.get(i).getName());
        viewHolder.type.setText( filmCards.get(i).getType());
        viewHolder.rate.setText( filmCards.get(i).getRate());
        viewHolder.actors.setText( filmCards.get(i).getActors());
        viewHolder.itemView.setTag(i);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(viewHolder. itemView, i, filmCards.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return  filmCards.size();
    }
    public static class ViewHolder  extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        ImageView img;
        TextView  name;
        TextView  type;
        TextView rate;
        TextView actors;
        ConstraintLayout container;
    }

}
