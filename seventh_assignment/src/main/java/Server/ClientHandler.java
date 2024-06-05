package Server;

import netscape.javascript.JSObject;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;

public class ClientHandler implements Runnable {
    Socket s;
    String username;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    // Arraylist to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    // constructor
    public ClientHandler(Socket s) {
        this.s = s;
        try {
            this.bufferedReader=new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.username=bufferedReader.readLine();
            ar.add(this);
            broadCasting("Server: "+username+" has intend the chat!");
        }catch (IOException e){
            closeEverything(s,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {
        while (s.isConnected()){
            try {
                broadCasting(username+": "+bufferedReader.readLine());

            }catch (IOException e){
                closeEverything(s,bufferedReader,bufferedWriter);
                break;
            }
        }
    }



    private void broadCasting(String message){
        for (ClientHandler clientHandler:ar){
            try {
                if (!clientHandler.username.equals(username)){
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }

            }catch (IOException e){
                closeEverything(s,bufferedReader,bufferedWriter);
            }
        }

    }

    private void removeClientHandler(){
        ar.remove(this);
        broadCasting("Server: "+username+" has left the chat!");
    }

    private void closeEverything(Socket s,BufferedReader bufferedReader,BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {

            if (s!=null){
                s.close();
            }
            if (bufferedReader!=null){
                bufferedReader.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
