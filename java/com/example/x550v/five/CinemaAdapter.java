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

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<CinemaCard> cinemaCards;
    private LayoutInflater mInflater;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, CinemaCard item);
    }

    private OnItemClickListener  onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.onItemClickListener =  mOnItemClickListener;
    }

    public CinemaAdapter(Context context, ArrayList<CinemaCard> items) {
        super();
        cinemaCards = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =  mInflater.inflate(R.layout.theateritems, viewGroup,  false);
        view.setOnClickListener(this);
        ViewHolder holder =  new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.theaterimg);
        holder.name =(TextView)view.findViewById(R.id.theatername);
        holder.address = (TextView)view.findViewById(R.id.theateraddress);
        holder.phone = (TextView) view.findViewById(R.id.theaterphone);
        holder.container = (ConstraintLayout) view.findViewById(R.id.theater_container);
        return holder;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取position
            int position = (int)v.getTag();
            onItemClickListener.onItemClick(v,position, cinemaCards.get(position));
        }
    }

    @Override
    public void onBindViewHolder( final ViewHolder viewHolder, final int i) {
        final CinemaCard cinemaCard =  cinemaCards.get(i);
        viewHolder.img.setImageResource(cinemaCard.getRid());
        viewHolder.name.setText(cinemaCard.getName());
        viewHolder.address.setText(cinemaCard.getAddress());
        viewHolder.phone.setText(cinemaCard.getPhone());
        viewHolder.itemView.setTag(i);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(viewHolder. itemView, i, cinemaCard);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  cinemaCards.size();
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }

        ImageView img;
        TextView  name;
        TextView  address;
        TextView phone;
        ConstraintLayout container;
    }

}
