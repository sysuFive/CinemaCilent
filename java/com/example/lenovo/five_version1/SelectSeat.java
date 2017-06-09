package com.example.lenovo.five_version1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

public class SelectSeat extends AppCompatActivity {
    private  SeatTable seat;
    public Button pay;

    public void findview() {
        pay = (Button) findViewById(R.id.buy);
        seat = (SeatTable) findViewById(R.id.seat);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);
        findview();
        seat.setScreenName("8号厅荧幕");//设置屏幕名称
        seat.setMaxSelected(3);//设置最多选中
        seat.setSeatChecker(new SeatTable.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                if(column==2) {
                    return false;
                }
                return true;
            }
            @Override
            public boolean isSold(int row, int column) {
                if(row==6&&column==6){
                    return true;
                }
                return false;
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
        seat.setData(10,15);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendmessage to server
                Intent t = new Intent(SelectSeat.this, PayPage.class);
                startActivity(t);

            }
        });

    }
}
