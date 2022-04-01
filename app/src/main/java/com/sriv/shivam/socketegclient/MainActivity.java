package com.sriv.shivam.socketegclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    // declaring required variables
    private Socket client;
    private PrintWriter printwriter;
    private String message, xString, yString;
    EditText messageEditText, xAxis, yAxis;
    Button sendButton;
    final String SERVER_IP = "10.2.249.143";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xAxis = findViewById(R.id.xAxis);
        yAxis = findViewById(R.id.yAxis);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = messageEditText.getText().toString();
                xString = xAxis.getText().toString();
                yString = yAxis.getText().toString();
                Log.i("test", "On click");

                // start the Thread to connect to server
                new Thread(new ClientThread(message, xString, yString)).start();
            }
        });
    }

    // the ClientThread class performs
    // the networking operations
    class ClientThread implements Runnable {
        String finalMessage;
        private final String message;
        private final String xString;
        private final String yString;

        public ClientThread(String message, String xString, String yString) {
            this.message = message;
            this.xString = xString;
            this.yString = yString;

            finalMessage = this.xString + "&" + this.yString;

            Log.i("test", "Message: " + this.message);
            Log.i("test", finalMessage);
        }

        @Override
        public void run() {
            try {
                Log.i("test", "Try block");
                // the IP and port should be correct to have a connection established
                // Creates a stream socket and connects it to the specified port number on the named host.
                client = new Socket(SERVER_IP, 47430);  // connect to server
                printwriter = new PrintWriter(client.getOutputStream(),true);
                printwriter.write(finalMessage);  // write the message to output stream

                printwriter.flush();
                printwriter.close();

                // closing the connection
                client.close();

            } catch (IOException e) {
                Log.i("test", "Catch block: " + e.toString());
                e.printStackTrace();
            }

            // updating the UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageEditText.setText("");
                }
            });
        }
    }
}