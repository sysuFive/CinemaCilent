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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectSeat extends AppCompatActivity {
    private  SeatTable seat;
    public Button pay;
    TextView filmName, filmTime;
    int col, row, SessionId = -1;
    boolean [][] allSeat;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);
        findViews();

        filmName.setText(sharedPreferences.getString("filmName", ""));
        filmTime.setText(sharedPreferences.getString("beginTime", ""));

        setSeat();
        seat.setData(row,col);
        seat.setScreenName(sharedPreferences.getString("hall", ""));
        seat.setMaxSelected(3);
        seat.setSeatChecker(new SeatTable.SeatChecker() {
            @Override
            public boolean isValidSeat(int r, int c) {
                return r <= row && c < col;
            }
            @Override
            public boolean isSold(int r, int c) {
                return allSeat[r][c];
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
            seats.put(json);
        }
        return seats;
    }

    private void makeReservation() throws JSONException {
        final ArrayList<String> selectedSeat = seat.getSelectedSeat();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        SessionId = sharedPreferences.getInt("SessionId", -1);
        float price = sharedPreferences.getInt("price", 0);
        final float total = price * selectedSeat.size();
        params.put("filmSessionId", "" + SessionId);
        params.put("price", "" + total);
        params.put("orderSit", getSeats(selectedSeat).toString());
        String url = Controller.SERVER + Controller.MAKE;
        PostRequest request = new PostRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
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
                                int orderId = response.getInt("orderId");
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectSeat.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void setSeat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        SessionId = sharedPreferences.getInt("SessionId", -1);
        String url = Controller.SERVER + Controller.SIT + SessionId;
        PostRequest request = new PostRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectSeat.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
        requestQueue.add(request);
    }
}
