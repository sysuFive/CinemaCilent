package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lenovo.five_version1.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/23.
 */

public class MenuLeftFragment extends Fragment {

    private View mView;
    private ListView mCategories;
    private int[] img = {
           R.drawable.money,
            R.drawable.ticket,
           R.drawable.comments,
            R.drawable.quit
    };
    private String[] name = new String[] {"钱包余额", "我的订单", "我的评论", "退出账号"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (mView == null)
        {
            initView(inflater, container);
        }

        return mView;
    }

    private void initView(LayoutInflater inflater, ViewGroup container)
    {
        final List<Map<String, Object>> data= new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("itemname", name[i]);
            tmp.put("itempic", img[i]);
            tmp.put("itemdetail", "");
            if (i == 0) {
                //TODO get remains
                tmp.put("itemdetail", "100.0");
            }
            data.add(tmp);
        }


        mView = inflater.inflate(R.layout.activity_sliding, container, false);
        RelativeLayout rv= (RelativeLayout) mView.findViewById(R.id.slidingtop);

        rv.setBackgroundResource(R.drawable.slidebg);
        mCategories = (ListView) mView
                .findViewById(R.id.listview_categories);
        TextView usrname = (TextView) mView.findViewById(R.id.selfname);
        //TODO get username

        final SimpleAdapter sa = new SimpleAdapter(getContext(), data, R.layout.sliding_menu_item,
                new String[]  {"itempic", "itemname", "itemdetail"}, new int[] {R.id.itempic, R.id.itemname, R.id.itemdetail});
        mCategories.setAdapter(sa);

        mCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setTime();
                        break;
                    case 1:
                        showtickets();
                        break;
                    case 2:
                        showcomments();
                        break;
                    case 3:
                        quit();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setTime() {

    }

    private void showtickets() {
        Intent t = new Intent(getContext(), Mytickets.class);
        startActivity(t);
    }

    private void  showcomments() {
        Intent t = new Intent(getContext(), Mycomments.class);
        startActivity(t);
    }

    private void quit() {
        Intent t = new Intent(getContext(), MainActivity.class);
        startActivity(t);

    }

}
