package testpack;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws JSONException {

        int offset = 0;
        boolean exit=true;

        String update = new String();
        String mainURL = "https://api.telegram.org/bot1874110464:AAEWIfTMgRkPdlpdz3bblpRZ88g5_PkLIlI";
        String getUpdateURL;
        String URLtext;

        HttpClient client = HttpClient.newHttpClient();

        while(exit) {

            getUpdateURL=mainURL+"/getUpdates?offset="+offset+"&timeout=10";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(getUpdateURL))
                    .build();
            update = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            System.out.println(update);

            JSONObject json = new JSONObject(update);
            JSONArray jsonarr = json.getJSONArray("result");

            if(jsonarr.length()!=0) {
                String text = jsonarr.getJSONObject(jsonarr.length() - 1)
                        .getJSONObject("message").getString("text");
                offset = jsonarr.getJSONObject(jsonarr.length() - 1)
                        .getInt("update_id");
                System.out.println(text +" "+ offset);
                offset++;
                URLtext=encodeValue(text);

                HttpRequest request1 = HttpRequest.newBuilder().uri(URI
                        .create(mainURL + "/sendMessage?chat_id=354117191&text=" + URLtext))
                        .build();
                client.sendAsync(request1, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .join();

                if (text.equals("/exit")) {
                    exit = false;
                }
            }
        }

    }

    private static String encodeValue(String value) {
        try{
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        }catch (UnsupportedEncodingException ex){
            throw new RuntimeException(ex.getCause());
        }
    }
}
