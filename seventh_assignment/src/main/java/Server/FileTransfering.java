package Server;

import java.io.*;
import java.net.Socket;


public class FileTransfering {
    private static String path="D:\\Maria\\term4\\computer science\\java_programing\\HW\\Seventh-Assignment-Socket-Programming\\" +
            "seventh_assignment\\src\\main\\java\\Server\\data";
    private static String download_path="D:\\Maria\\term4\\computer science\\java_programing\\HW\\Seventh-Assignment-Socket-Programming\\" +
            "seventh_assignment\\src\\main\\java\\Client\\Downloads";
    private static File folder= new File(path);
    private static File[] filesLists=folder.listFiles();
    private static Socket socket;
    private static FileReader fileReader;
    private static FileWriter fileWriter;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

    public FileTransfering(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }catch (IOException e){
            CloseEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    //display all files
    public static void displayFiles(){
        try {
            bufferedWriter.write(String.format("%-20s%s%20s%n", "############", "file:", "############"));
            bufferedWriter.newLine();

            bufferedWriter.write(String.format("%-5d%-15s%n",0,"close"));
            int i=1;
            for (File file:filesLists){
                bufferedWriter.write(String.format("%-5d%-15s%n",i,file.getName()));
            i++;
        }
        bufferedWriter.flush();
        }catch (IOException e){
            CloseEverything(socket,bufferedReader,bufferedWriter);
        }

    }

    //which file has chosen to be downloaded?
    public static File chooseFile(int index) {
        try {
            if (index == 0) {
                bufferedWriter.write("Server: You have exit from download table!");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                return null;
            } else if (!(0 < index && index < 11)) {

                    bufferedWriter.write("Server: didn't undrestand");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    return null;
                }
            else{
                System.out.println(filesLists[index-1].getName());
                return filesLists[index - 1];
            }

            }catch(IOException e){
                CloseEverything(socket, bufferedReader, bufferedWriter);
                return null;
            }
    }


    public static void downloadingFile(){
        displayFiles();
        try {
            int idex=Integer.parseInt(bufferedReader.readLine());
            File file=chooseFile(idex);

            if (file!=null){

                try {


                    fileReader=new FileReader(file);
                    fileWriter=new FileWriter(download_path+"//"+file.getName());
                    int content;
                    while ((content=fileReader.read())!=-1){
                        fileWriter.append((char) content);
                    }
                    bufferedWriter.write(file.getName()+" downloaded!");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();


                }finally {

                    if (fileReader!=null){
                        fileReader.close();
                    }
                }
            }

        }catch (IOException e){
            CloseEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public static void CloseEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try {
            if (socket!=null){
                socket.close();
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
