package Server;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class ClientHandler implements Runnable {
    Socket s;
    String username;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    public static String chatHostory="";

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
            cheatsheet();

            broadCasting("Server: "+username+" has intend the chat!");


            bufferedWriter.write(chatHostory);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        }catch (IOException e){
            closeEverything(s,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {
        while (s.isConnected()){
            try {
                String read=bufferedReader.readLine();

                if (read.equals("data")){
                    FileTransfering fileTransfering= new FileTransfering(s);
                    fileTransfering.downloadingFile();
                }
                else{
                    //saving chat message
                    ChatHistory(username+": "+read);
                    broadCasting(username+": "+read);
                }

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
    private void cheatsheet(){
        try {
            bufferedWriter.write(String.format("%-20s%s%20s%n", "############", "Cheat sheet:", "############"));
            bufferedWriter.newLine();
            bufferedWriter.write(String.format("%-5d%-15s%n",1,"For downloading files please Enter \"data\""));
            bufferedWriter.write(String.format("%-5d%-15s%n",2,"If you don't want to download after entering the \"data\",please enter the option 0"));


            bufferedWriter.flush();
        }catch (IOException e){
            closeEverything(s,bufferedReader,bufferedWriter);
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
    //Chat history for a new arriver
    private static void ChatHistory(String message){
        chatHostory=chatHostory+"\n"+message;
    }

}
