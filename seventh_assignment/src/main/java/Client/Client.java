package Client;

import Server.ClientHandler;
import Server.FileTransfering;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

// Client Class
public class Client {
    final static int ServerPort = 6666;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUsename;

    public Client( Socket socket,String clientUsename) {
        try {
            this.socket=socket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsename = clientUsename;

        }catch (IOException e){
            CloseEveryThing(socket,bufferedReader,bufferedWriter);
        }

    }
    private void sendMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bufferedWriter.write(clientUsename);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    Scanner sc=new Scanner(System.in);
                    while (socket.isConnected()){
                        String messageToSend=sc.nextLine();
                        bufferedWriter.write(messageToSend);

                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        System.out.println("sended");

                    }



                }catch (IOException e){
                    CloseEveryThing(socket,bufferedReader,bufferedWriter);
                }
            }
        }).start();


    }
    private void receiveMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageToRead;
                while (socket.isConnected()){
                    try {
                        messageToRead=bufferedReader.readLine();
                        System.out.println(messageToRead);
                    }catch (IOException e){
                        CloseEveryThing(socket,bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();

    }


    private static void CloseEveryThing(Socket s,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
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

    public static void main(String[] args) throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter your account name:");
        String usename= sc.nextLine();
        Socket s=new Socket("localhost",ServerPort);
        Client client= new Client(s,usename);

        System.out.println(ClientHandler.chatHostory);



        client.sendMessage();
        client.receiveMessage();


    }


}