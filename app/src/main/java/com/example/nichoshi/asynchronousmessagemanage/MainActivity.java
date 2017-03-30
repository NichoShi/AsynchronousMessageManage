package com.example.nichoshi.asynchronousmessagemanage;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int CHANGE = 0;
    private Button changeBtn;
    private Button asyncChangeBtn;
    private TextView textView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == CHANGE){
                textView.setText("Changed by handler");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        changeBtn = (Button) findViewById(R.id.changeBtn);
        asyncChangeBtn = (Button) findViewById(R.id.asyncChangeBtn);
        Log.d("main thread",Thread.currentThread().getId()+"");

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("new thread",Thread.currentThread().getId()+"");
                        Message message = new Message();
                        message.what = CHANGE;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        asyncChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });
    }

    class MyAsyncTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("new thread by AsyncTask",Thread.currentThread().getId()+"");
            publishProgress();
            return true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            textView.setText("Changed by AsyncTask");
        }
    }
}
