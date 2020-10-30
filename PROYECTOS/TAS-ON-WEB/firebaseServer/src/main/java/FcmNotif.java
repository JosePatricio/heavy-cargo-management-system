import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
public class FcmNotif {
    public final static String AUTH_KEY_FCM ="AIzaSyA1cK20bPysz0ZxJmlI7LLuI8PZ6-vkAvY";
    public final static String API_URL_FCM ="https://fcm.googleapis.com/fcm/send";
    //userDeviceIdKey is the device id you will query from your database
    public void pushFCMNotification(String userDeviceIdKey, String title, String message) throws Exception{
        String authKey = AUTH_KEY_FCM;   //You FCM AUTH key
        String FMCurl = API_URL_FCM;
        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization","key="+authKey);
        conn.setRequestProperty("Content-Type","application/json");
        JSONObject json = new JSONObject();
        json.put("to",userDeviceIdKey.trim());
        JSONObject info = new JSONObject();
        info.put("title", title); //Notification title
        info.put("body", message); //Notification body
      //  info.put("image", "https://lh6.googleusercontent.com/-sYITU_cFMVg/AAAAAAAAAAI/AAAAAAAAABM/JmQNdKRPSBg/photo.jpg");
        info.put("type", "message");
        info.put("priority", "high");
        info.put("channelId", "test-channel");
        info.put("channelName", "Test Channel");



        json.put("data", info);
        System.out.println(json.toString());
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        conn.getInputStream();
    }
}