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


//                        String html = "<html><head><title>Java HTTP Server</title></head><body>" +
//                                fetchJsonFromFile() + "</body></html>";
//
//                        final String CRLF = "\n\r";
//
//                        String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " +
//                                html.getBytes().length + CRLF + CRLF + html + CRLF + CRLF;



                        //send back message to client
                        bw.write(fetchJsonFromFile(messageFromClient).toJSONString());


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
    static JSONObject fetchJsonFromFile (String message) {
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
        JSONObject person = (JSONObject) fetchData.get(message);



        //hämta och srkia ut data
        //String nameP1 = p1.get("name").toString(), nameP2 = p2.get("name").toString();
        //int ageP1 = Integer.parseInt(p1.get("age").toString()), ageP2 = Integer.parseInt(p2.get("age").toString());


        return person;
    }
}