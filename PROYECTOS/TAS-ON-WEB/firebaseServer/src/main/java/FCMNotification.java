import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class FCMNotification {

    // Method to send Notifications from server to client end.
   //BORRARR
   // public final static String AUTH_KEY_FCM = "AAAAfSetPz8:APA91bHWJ_RWIWCL718F8--J7bIbg-GTvmrfQASfW6XNizG4HUUUe4rTpO33f-lQ6htgElUj9Y2fAwJweNA2L0tlpRUGVDzhzgauxfukPysZbnEClGhPQANX2DZqf5ElkIOlYRQmcO_3";
   //MAYOREO
    public final static String AUTH_KEY_FCM = "AAAA-G4tYyw:APA91bH4n5nmGcmPYe0-CmAJvLZUHOfZObM2pVzxhxX_TFoo2ambR74OzrOWRVnPPqElf2OjpGXMza4CzaRpvyUN258ibvTbRK9Dpm53P2GdAMi__ltDmHG8YdTh2sMAe33FDJ2lFqrr";

    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";


   public static  String titleStuff(){
       DateTime jodaTime = new DateTime();

       DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");

                return formatter.print(jodaTime);
    }

    public static void pushFCMNotification(String DeviceIdKey) throws Exception {

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("to", DeviceIdKey.trim());
        JSONObject notificacion = new JSONObject();
            notificacion.put("title", titleStuff()); // Notification title
            notificacion.put("text", "Hello First Test notification"); // Notification body
            notificacion.put("content_available", "true"); // Notification body
            notificacion.put("priority", "HIGH"); // Notification body
            notificacion.put("sound", "default"); // Notification body

        JSONObject data = new JSONObject();
        data.put("title", titleStuff()); // Notification title
        data.put("text", "Hello First Test notification"); // Notification body
        data.put("key_1", "svenskaq"); // Notification body
        data.put("android_channel_id", "test-channel"); // Notification body
        data.put("sound", "default"); // Notification body



        jsonObject.put("notification", notificacion);
        jsonObject.put("data", data);
        jsonObject.put("priority", "high");

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(jsonObject.toString());
        wr.flush();
        wr.close();
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

String emulador="ewEOtFd0yYE:APA91bHxIFaRP03tTrQusSXnsPVNB332u2L8oReVh5oLY5xlt2jufMmxbyE7yK2NRD80v9H950EQO2jdUW3sWTobhWyBcv-c1QB3NgfpReuPfz9PL1q-emj76TzFJRti0_72FyDKYcV9";
       //String telefono="eI8mdoMSA20:APA91bHrx2CXpXbHVpxCBIbepbqwzNMurUm4YtTOTohFKGgOonZJm_02pC6D7rWF7xNbC38PDn5mKCki2BiMTneE19Gb3N-xPsc2pEFxgfIOy60G64dQyR9i-hYJQdSjV2ZCT0fzYkgO";

        FCMNotification.pushFCMNotification(emulador);
    }
}