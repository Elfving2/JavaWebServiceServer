import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final int port = 6969;
        try {
            ServerSocket serverSocket = new ServerSocket(port);


            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("client connected");

                //read data from client
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String time = reader.readLine();

                System.out.println(time);

                //send data to client
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                writer.println(fetchJsonFromFile());

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