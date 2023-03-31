import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            Socket socket;
            while (true) {
                try {
                    socket = serverSocket.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    //Read what client has sent
                    String messageFromClient = br.readLine();
                    if(messageFromClient.equals("{\"close\":\"close\"}")) {
                        break;
                    } else {
                        //hämta klientens meddelande och skicka den till openUpData
                        String returnData = openUpData(messageFromClient);
                        bw.write(returnData);
                        bw.newLine();
                        bw.flush();
                    }

                } catch (IOException e) {
                   throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String openUpData(String message) {
        //Steg 1, Bygg upp JSON object basserat på inkommande String
        try {
            JSONParser parser = new JSONParser();
            //converte string to jsonobject
            JSONObject jsonOb = (JSONObject) parser.parse(message);

            System.out.println("option " + " = " + jsonOb);
            //Steg 2, Läs av URL och HTTP-metod för att veta vad klienten vill
            String url = jsonOb.get("httpURL").toString();
            String method = jsonOb.get("httpMethod").toString();

            //Steg 2.5, Dela upp URL med .split() metod
            String[] urls = url.split("/");

            //Steg 3, Anvämnd en switchCase för att kolla vilken data som skall användas
            if (urls[0].equals("users")) {
                if (method.equals("get")) {
                    //hämta data om personer
                    JSONObject jsonReturn = (JSONObject) parser.parse(new FileReader("data.json"));

                    //inkludera HTTP status code
                    jsonReturn.put("httpStatusCode", 200);

                    //retunera jsons tring
                    return jsonReturn.toJSONString();

                } else if (method.equals("post")) {
                    try {
                        FileReader fileReader = new FileReader("data.json");
                        JSONObject users = (JSONObject) parser.parse(fileReader);

                        //create and add new user to "users"
                        JSONObject newUser = new JSONObject();
                        newUser.put("name", jsonOb.get("name"));
                        newUser.put("age", jsonOb.get("age"));
                        newUser.put("favoriteColor", jsonOb.get("favoriteColor"));
                        JSONArray usersArray = (JSONArray) users.get("users");

                        //add user to file
                        usersArray.add(newUser);
                        users.put("users", usersArray);
                        FileWriter fileWriter = new FileWriter("data.json");
                        fileWriter.write(users.toJSONString());

                        fileWriter.close();
                        fileReader.close();
                        JSONObject success = new JSONObject();
                        success.put("success", "you have successfully added a new user!");
                        return success.toString();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}