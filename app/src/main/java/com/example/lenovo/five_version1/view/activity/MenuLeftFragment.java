package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.lenovo.five_version1.R;
import com.example.lenovo.five_version1.controller.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/23.
 */

public class MenuLeftFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SimpleAdapter sa;
    List<Map<String, Object>> data;
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
        sharedPreferences = getContext().getSharedPreferences("Setting", getContext().MODE_PRIVATE);
        data= new ArrayList<>();
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
        TextView username = (TextView) mView.findViewById(R.id.selfname);
        username.setText(sharedPreferences.getString("username", ""));

        sa = new SimpleAdapter(getContext(), data, R.layout.sliding_menu_item,
                new String[]  {"itempic", "itemname", "itemdetail"}, new int[] {R.id.itempic, R.id.itemname, R.id.itemdetail});
        mCategories.setAdapter(sa);
        getWalletInfo();
        mCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        getWalletInfo();
                        break;
                    case 1:
                        showTickets();
                        break;
                    case 2:
                        showComments();
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

    private void getWalletInfo() {
        String url = Controller.SERVER + Controller.WALLET;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        double balance = jsonObject.getJSONObject("message").getDouble("balance");
                        String strBalance = balance + ".000";
                        data.get(0).put("itemdetail", strBalance.substring(0, strBalance.indexOf('.')+3));
                        sa.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getContext(), Request.Method.POST, url,
                new HashMap<String, String>(), listener, cookie);
    }

    private void showTickets() {
        Intent t = new Intent(getContext(), Mytickets.class);
        startActivity(t);
    }

    private void  showComments() {
        Intent t = new Intent(getContext(), Mycomments.class);
        startActivity(t);
    }

    private void quit() {
        Intent t = new Intent(getContext(), MainActivity.class);
        startActivity(t);
    }

}
