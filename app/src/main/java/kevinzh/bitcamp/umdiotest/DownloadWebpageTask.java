package kevinzh.bitcamp.umdiotest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kevin on 4/11/2015.
 */
// Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an HttpUrlConnection. Once the connection
// has been established, the AsyncTask downloads the contents of the webpage as
// an InputStream. Finally, the InputStream is converted into a string, which is
// displayed in the UI by the AsyncTask's onPostExecute method.
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    DownloadWebpageTask(TextView target){
        super();
        this.target = target;
    }

    private final TextView target;

    private String text = "OOPS WEB CODE FAILED";

    @Override
    protected String doInBackground(String... urls) {
        final String url = urls[0];
        final Timer timer = new Timer();

//        timer.schedule( new TimerTask() {
//            public void run() {
//                new DownloadWebpageTask(target).execute(url);
//            }
//        }, 4000);


        // params comes from the execute() call: params[0] is the url.
        try {
            Log.d("~!@#$%^&*DEBUG*&^%$#@!~", "The url is " + urls[0]);
            text = downloadUrl(urls[0]);
            return text;
        } catch (IOException e) {
            return e.toString();
        }
    }


    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

        if(text == null) {
            text = "[OOPS I'M NULL]";
        }

        Log.d("~!@#$%^&*DEBUG*&^%$#@!~", "The result is " + result);
        Log.d("~!@#$%^&*DEBUG*&^%$#@!~", "The text is " + text);

        try {
            String stuffToOutput = "";
//            JSONObject classes = new JSONObject(result);
            JSONArray classesArray = new JSONArray(result);

            JSONObject aClass;

            for (int i = 0; i < classesArray.length(); i++){
                aClass = classesArray.getJSONObject(i);

                if (Math.random()<0.1){
                    stuffToOutput += " ";
                    stuffToOutput += aClass.getString("course_id");
                    stuffToOutput += " ";
                    stuffToOutput += "";
                }
            }

            outputString(stuffToOutput);
        } catch (JSONException e) {
            e.printStackTrace();
            outputString("JSON ERROR :(\n" + text);
        }


        super.onPostExecute(result);
    }

    private void outputString(String s){
        target.setText(s.toCharArray(),0,s.length());
    }



    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 20;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("~!@#$%^&*DEBUG*&^%$#@!~", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];

        int num_read;
        String output = "";
        while((num_read = reader.read(buffer)) >= 0){
            output += new String(buffer, 0, num_read);
        }

        return output;
    }
}