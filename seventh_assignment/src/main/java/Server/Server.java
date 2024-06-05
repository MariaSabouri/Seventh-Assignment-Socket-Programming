package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Server Class
public class Server {
    // TODO: Implement the server-side operations        // TODO: Add constructor and necessary methods
    private ServerSocket ss;

    public Server(ServerSocket ss) {
        this.ss=ss;
    }
    private void startServer(){

        try {
            while (!ss.isClosed()){
                Socket s=ss.accept();
                System.out.println("a user has joined! ");
                ClientHandler clientHandler=new ClientHandler(s);
                Thread thread=new Thread(clientHandler);
                thread.start();

            }

        }catch (IOException e){
            closeEverything();
        }

    }


    private void closeEverything() {
        try {
            if (ss!=null){
                ss.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(6666);
        Server server=new Server(serverSocket);
        server.startServer();


    }
}