package com.example.x550v.five;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by X550V on 2017/6/6.
 */

public class FilmInfoAdapter  extends RecyclerView.Adapter<FilmInfoAdapter.ViewHolder> {
    private ArrayList<PersonInfo> person_list;
    private LayoutInflater mInflater;
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, PersonInfo item);
    }
    private OnItemClickListener  mOnItemClickListener;
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener =  mOnItemClickListener;
    }
    public FilmInfoAdapter(Context context, ArrayList<PersonInfo> items) {
        super();
        person_list = items;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =  mInflater.inflate(R.layout.person, viewGroup,  false);
        ViewHolder holder =  new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.img);
        holder.name =(TextView)view.findViewById(R.id.name);
        holder.job = (TextView)view.findViewById(R.id.job);
        return holder;
    }
    @Override
    public void onBindViewHolder( final ViewHolder viewHolder, final int i) {
        viewHolder.img.setImageURI( person_list.get(i).getImg());
        viewHolder.name.setText( person_list.get(i).getName());
        viewHolder.job.setText( person_list.get(i).getJob());
        if ( mOnItemClickListener !=  null)
        {
            viewHolder. itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder. itemView, i, person_list.get(i));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return  person_list.size();
    }
    public static class ViewHolder  extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        ImageView img;
        TextView  name;
        TextView  job;
    }
}


