package com.example.lqy.loadingview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    LoadingView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (LoadingView) findViewById(R.id.lv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back_alpha) {
            if(lv.getmBackAlpha() == 0){
                lv.setmBackAlpha(128);
            }else{
                lv.setmBackAlpha(0);
            }
            return true;
        }else if(id == R.id.action_back_color){
            if(lv.getmBackColor() == 0xFFFF00FF){
                lv.setmBackColor(0xFF00FFFF);
            }else{
                lv.setmBackColor(0xFFFF00FF);
            }
            return true;
        }else if(id == R.id.action_color){
            if(lv.getmColor() == 0xFF888888){
                lv.setmColor(0xFFEF9878);
            }else {
                lv.setmColor(0xFF888888);
            }
            return true;
        }else if(id == R.id.action_line_width){
            if(lv.getCircleWidth() == 5.0f){
                lv.setCircleWidth(10.0f);
            }else {
                lv.setCircleWidth(5.0f);
            }
            return true;
        }else if(id == R.id.action_points){
            if(lv.getmBubleNum() == LoadingView.DEFAULT_NUM){
                lv.setmBubleNum(LoadingView.MAX_NUM);
            }else {
                lv.setmBubleNum(LoadingView.DEFAULT_NUM);
            }
            return true;
        }else if(id == R.id.action_status){
            if(lv.getmStyle() == 1){
                lv.setmStyle(LoadingView.POINT);
            }else if(lv.getmStyle() == 0){
                lv.setmStyle(LoadingView.STRIP);
            }else{
                lv.setmStyle(LoadingView.CIRCLE);
            }
            return true;
        }else if(id == R.id.action_text){
            if(lv.getmText().equals("")){
                lv.setmText("Loading");
            }else {
                lv.setmText("");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
