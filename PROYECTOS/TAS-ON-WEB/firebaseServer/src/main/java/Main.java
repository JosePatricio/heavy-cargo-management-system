import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String userDeviceIdKey="ff4QBDIXch8:APA91bHx-mIuJYlaKTZdcRNBckNJbIe2omoqFIAITV-LlClIsuBkLNaKKLzVFbtlVKOLuxQggGehGOBKZCsJekiennzB9-ymk7UjUrjSwC38tH3InWOX7QYJu5011gIXksvwRRvOX8Lk";
         String title="sNOTIFACIONES";
         String message=",SWENDENDN ";
try {

    CipherHash cipherHash = new CipherHash(128, 10000);
   // cipherHash.hashString(password)
String res=cipherHash.hashString("wfqMr0VH");
            System.out.println("   "+res+ "   ,  "+ (res.equals("038797868a2011e5d69b1656bbc2f2426b1e10f8")));
  //  FcmNotif obj= new FcmNotif();
    //obj.pushFCMNotification(userDeviceIdKey,title,message);
    FCMHelper OBJ= new FCMHelper();


   /* com.google.gson.JsonObject body = new com.google.gson.JsonObject();
    // JsonArray registration_ids = new JsonArray();
    // body.put("registration_ids", registration_ids);
    body.addProperty("to", userDeviceIdKey);
    body.addProperty("priority", "high");
    body.addProperty("body", "body string here");
    body.addProperty("title", "title string here");
    body.addProperty("channel", "test-channel");


    OBJ.sendNotification("to","",body);
*/


}catch (Exception e){
    System.out.println("errororo   "+e.getMessage());
}
    }

}
