package com.example.clientapp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientThread implements Runnable{
    private Socket s;
    private Handler handler;
    public Handler revHandler;
    BufferedReader br = null;
    OutputStream os=null;
    public  ClientThread(Handler handler) throws IOException {
        this.handler=handler;
    }
    @Override
    public void run() {
        try {
            s=new Socket("127.0.0.1",40000);
            br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            os=s.getOutputStream();
            new Thread(){
                @Override
                public void run() {
                    String content = null;
                    try {
                        while ((content = br.readLine())!=null){
                            Message msg=new Message();
                            msg.what=0x123;
                            msg.obj=content;
                            handler.sendMessage(msg);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }.start();
            Looper.prepare();
            revHandler = new Handler(){
                public void handleMessage(Message msg){
                    if(msg.what==0x345){
                        try {
                            os.write((msg.obj.toString()+"\r\n").getBytes("utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            };
            Looper.loop();
        }catch (SocketTimeoutException e1){
            System.out.println("网络连接超时");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}