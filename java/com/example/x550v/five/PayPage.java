package com.example.x550v.five;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayPage extends AppCompatActivity {
    public TextView remain_time, movieName, movieDate, theaterAddress, seat, price;
    public Button pay;
    int orderId = -1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_page);
        findViews();
        setAttr();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
    }

    private void pay() {
        String url = Controller.SERVER + Controller.PAY;
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response == null) {
                    Toast.makeText(PayPage.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Toast.makeText(PayPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequest(getApplicationContext(), Request.Method.POST, url, params, listener);
    }

    private void setAttr() {
        movieName.setText(sharedPreferences.getString("filmName", ""));
        movieDate.setText(sharedPreferences.getString("beginTime", ""));
        theaterAddress.setText(sharedPreferences.getString("hall", ""));
        Intent intent = getIntent();
        if (intent == null)
            return;
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return;
        orderId = bundle.getInt("orderId");
        ArrayList<String> selectedSeat = bundle.getStringArrayList("selectedSeat");
        float prices = bundle.getFloat("price");
        String total = "" + prices;
        price.setText(total);
        String seats = "";
        if (selectedSeat == null)
            return;
        for (int i = 0; i < selectedSeat.size(); ++i) {
            String select = selectedSeat.get(i);
            String[] xy = select.split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            seats += x + "排" + y + "列\n";
        }
        seat.setText(seats);
    }

    public void findViews() {
        remain_time = (TextView) findViewById(R.id.remain_time);
        movieName = (TextView) findViewById(R.id.moviename);
        movieDate = (TextView) findViewById(R.id.moviedate);
        theaterAddress = (TextView) findViewById(R.id.theateaddress);
        seat = (TextView) findViewById(R.id.seat);
        price = (TextView) findViewById(R.id.price);
        pay = (Button) findViewById(R.id.pay);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
    }
}
