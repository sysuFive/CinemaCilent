package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.lenovo.five_version1.controller.Controller;
import com.example.lenovo.five_version1.R;
import com.example.lenovo.five_version1.model.SeatTable;

public class SelectSeat extends AppCompatActivity {
    private SeatTable seat;
    public Button pay;
    TextView filmName, filmTime;
    int col, row, SessionId = -1;
    boolean[][] allSeat;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);
        findViews();
        setAttr();
        setSeat();
        setListener();
    }

    private void setListener() {
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send message to server
                try {
                    makeReservation();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAttr() {
        filmName.setText(sharedPreferences.getString("filmName", ""));
        filmTime.setText(sharedPreferences.getString("beginTime", ""));
    }

    public void findViews() {
        pay = (Button) findViewById(R.id.buy);
        seat = (SeatTable) findViewById(R.id.seat);
        filmName = (TextView) findViewById(R.id.name);
        filmTime = (TextView) findViewById(R.id.time);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
    }

    private JSONArray getSeats(ArrayList<String> selectedSeat) throws JSONException {
        JSONArray seats = new JSONArray();
        for (int i = 0; i < selectedSeat.size(); ++i) {
            String select = selectedSeat.get(i);
            String[] xy = select.split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            JSONObject json = new JSONObject();
            json.put("x", x);
            json.put("y", y);
            json.put("valid", allSeat[x][y]);
            seats.put(json);
        }
        return seats;
    }

    private void makeReservation() throws JSONException {
        final ArrayList<String> selectedSeat = seat.getSelectedSeat();
        final Map<String, String> params = new HashMap<>();
        SessionId = sharedPreferences.getInt("SessionId", -1);
        int userId = sharedPreferences.getInt("userId", -1);
        String price = sharedPreferences.getString("price", "0.0");
        final float total = Float.parseFloat(price) * selectedSeat.size();
        params.put("filmSessionId", "" + SessionId);
        params.put("price", "" + total);
        params.put("userId", "" + userId);
        params.put("time", Controller.getCurTime());
        params.put("orderSit", getSeats(selectedSeat).toString());
        String url = Controller.SERVER + Controller.MAKE;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response == null) {
                    Toast.makeText(SelectSeat.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int status = response.getInt("status");
                    boolean success = status == 1;
                    if (success) {
                        int orderId = response.getInt("message");
                        Intent t = new Intent(SelectSeat.this, PayPage.class);
                        t.putExtra("selectedSeat", selectedSeat);
                        t.putExtra("price", total);
                        t.putExtra("orderId", orderId);
                        startActivity(t);
                    } else {
                        Toast.makeText(SelectSeat.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getApplicationContext(), Request.Method.POST, url, params, listener, cookie);
    }

    private void setSeatTable() {
        seat.setData(row, col);
        seat.setScreenName(sharedPreferences.getString("hall", ""));
        seat.setMaxSelected(3);
        seat.setSeatChecker(new SeatTable.SeatChecker() {
            @Override
            public boolean isValidSeat(int r, int c) {
                return r <= row && c < col;
            }

            @Override
            public boolean isSold(int r, int c) {
                return !allSeat[r][c];
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
    }

    private void setSeat() {
        final Map<String, String> params = new HashMap<>();
        SessionId = sharedPreferences.getInt("SessionId", -1);
        String url = Controller.SERVER + Controller.SIT + SessionId;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response == null) {
                    Toast.makeText(SelectSeat.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    col = -1;
                    row = -1;
                    allSeat = new boolean[100][100];
                    JSONArray seats = response.getJSONArray("sits");
                    for (int i = 0; i < seats.length(); ++i) {
                        JSONObject seat = seats.getJSONObject(i);
                        int x = seat.getInt("x");
                        int y = seat.getInt("y");
                        if (x < 100 && y < 100)
                            allSeat[x][y] = seat.getBoolean("valid");
                        if (x > col)
                            col = x;
                        if (y > row)
                            row = y;
                    }
                    setSeatTable();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequest(getApplicationContext(), Request.Method.GET, url, params, listener);
    }
}