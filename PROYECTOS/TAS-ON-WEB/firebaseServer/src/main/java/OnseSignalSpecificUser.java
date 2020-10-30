import org.joda.time.DateTime;
        import org.joda.time.format.DateTimeFormat;
        import org.joda.time.format.DateTimeFormatter;

        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.Scanner;

public class OnseSignalSpecificUser {

    public static  String titleStuff(){
        DateTime jodaTime = new DateTime();

        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");

        return formatter.print(jodaTime);
    }
    public static void main(String[] args) {

        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic ODU4ZjVkNzQtNTkzZC00NGRhLTlhN2MtMDJhOGRkNjg0N2Ji");
            con.setRequestMethod("POST");

            String myEmulator="\""+"799b8fc3-3972-41a3-aec9-b39b43cc7ecb"+"\"";
            String myDevice="\""+"799b8fc3-3972-41a3-aec9-b39b43cc7ecb"+"\"";

            String strJsonBody = "{"
                    +   "\"app_id\": \"1d288e0b-0893-4bc0-ae84-26e3ca096599\","
                    +   "\"include_player_ids\": ["+myDevice+"],"
                    +   "\"data\": {\"Title\": \"NTest from java\"},"
                    +   "\"contents\": {\"en\": \" NOTI FORM JAVA   " +titleStuff()+  "  \"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }

    }

}
