package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// Server Class
public class Server {
    // TODO: Implement the server-side operations
    // Arraylist to store active clients
    static ArrayList<ClientHandler> ar=new ArrayList<>();
    // counter for clients
    static int i = 0;
    // TODO: Add constructor and necessary methods

    public static void main(String[] args) throws IOException {
        // TODO: Implement the main method to start the server
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);
        Socket s;
        // running infinite loop for getting
        // client request
        while (true){
            // Accept the incoming request
            s = ss.accept();
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos);
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
            System.out.println("Adding this client to active client list");

            // add this client to active clients list
            ar.add(mtch);

            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }
    }
}