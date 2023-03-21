import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) {
        final int port = 6969;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket;


            while (true) {

                try{
                    socket = serverSocket.accept();
                    // create a new socket everytime a new client connects

                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    while(true) {
                        //Read what client has sent
                        String messageFromClient = br.readLine();
                        System.out.println("Client: " + messageFromClient);

                        //send back message to client
                        bw.write(fetchJsonFromFile().toJSONString());
                        bw.newLine();
                        bw.flush();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        } catch(IOException e) {
            System.out.println(e);
        }

    }
    static JSONObject fetchJsonFromFile () {
        String filePath = "data.json";

        //hämta data från JSON fil
        JSONObject fetchData = null;
        try {
            fetchData = (JSONObject) new JSONParser().parse(new FileReader(filePath));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //konvertera data till ett JSONObject
        JSONObject p1 = (JSONObject) fetchData.get("p1");
        JSONObject p2 = (JSONObject) fetchData.get("p2");

        //hämta och srkia ut data
        String nameP1 = p1.get("name").toString(), nameP2 = p2.get("name").toString();
        int ageP1 = Integer.parseInt(p1.get("age").toString()), ageP2 = Integer.parseInt(p2.get("age").toString());

        //System.out.println("Mitt namn är " + nameP1 + " jag är " + ageP1 + " år");
        //System.out.println("Mitt namn är " + nameP2 + " jag är " + ageP2 + " år");

        return p1;
    }
}