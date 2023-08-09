package com.example.networklab7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnChat;
    EditText edMessage;
    private String serverURL = "http://192.168.1.5:3000";
    private Socket ssocket;
    {
        try {
            ssocket = IO.socket(serverURL);
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChat = findViewById(R.id.btnChat);
        btnLogin = findViewById(R.id.btnLogin);
        edMessage = findViewById(R.id.edMessage);

        ssocket.connect();
        ssocket.on("receiver_message", onNewMessage);
    }

    public Socket get_socket(){
        return ssocket;
    }
    public void chat(View view){
        ssocket.emit("send_message", edMessage.getText().toString());


        edMessage.setText("");
    }
    public void login(View view){
        ssocket.emit("user_login", edMessage.getText().toString());
        edMessage.setText("");
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    message = data.optString("data");
                    Log.d("chat", "==>" + message);
                    //arraylist add string data vao
                }
            });
        }
    };
}