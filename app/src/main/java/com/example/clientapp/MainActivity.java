package com.example.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText input;
    TextView show;
    Button send;
    Handler handler;
    ClientThread clientThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input=(EditText)findViewById(R.id.input);
        send=(Button) findViewById(R.id.send);
        show=(TextView) findViewById(R.id.show);
        handler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==0x123){
                    show.append("\n"+msg.obj.toString());
                }
            }
        };
        try {
            clientThread = new ClientThread(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(clientThread).start();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg= new Message();
                msg.what=0x345;
                msg.obj=input.getText().toString();
                clientThread.revHandler.sendMessage(msg);
                input.setText("");
            }
        });
    }
}
