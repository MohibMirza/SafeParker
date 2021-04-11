import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {

        String latitude = "51.545341";
        String longitude = "-0.075787";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://ukpolicedata.p.rapidapi.com/crimes-street/all-crime?lat=" + latitude + "&lng=" + longitude)
                .get()
                .addHeader("x-rapidapi-key", "dbab3697b8mshb40165c6fee159cp12a686jsn90fae68406de")
                .addHeader("x-rapidapi-host", "ukpolicedata.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println("Successful: " + response.isSuccessful());
        System.out.println("MSG: " + response.message());

        // System.out.println(response.body().string());

        System.out.println(response.toString());

        String jsonString = response.body().string();

        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement el = parser.parse(jsonString);
        jsonString = gson.toJson(el); // done

        parse(jsonString);

        try {
            FileWriter myWriter = new FileWriter("data.json");
            myWriter.write(jsonString );
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Vector<Crime> crimeSet = parse(jsonString);

        System.out.println("CrimeSet Size: " + crimeSet.size());

        printCrimeSet(Double.parseDouble(latitude), Double.parseDouble(longitude), 1000, crimeSet);

    }

    public static Vector<Crime> parse(String jsonString) {
        Vector<Crime> crimeSet = new Vector<Crime>();

        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement el = parser.parse(jsonString);
        JsonArray ja = el.getAsJsonArray();

        for (int i = 0; i < ja.size(); i++) {
            JsonObject crimeReport = ja.get(i).getAsJsonObject();
            String category = crimeReport.get("category").getAsString();
            String latitude = crimeReport.get("location").getAsJsonObject().get("latitude").getAsString();
            String longitude = crimeReport.get("location").getAsJsonObject().get("longitude").getAsString();

            Crime crime = new Crime(category, latitude, longitude);
            crimeSet.add(crime);
        }
        return crimeSet;
    }

    public static void printCrimeSet(double latitude, double longitude, int distanceLimiter, Vector<Crime> crimeSet) {

        Vector<Integer> numb = new Vector<Integer>(); //parallel vectors to store total
        Vector<String> type = new Vector<String>();   //number of each appeared type of crime
        for(int i = 0; i < crimeSet.size(); i++) {
            Crime crime = crimeSet.get(i);

            double dist = crime.distance(latitude, longitude);

            if(dist < distanceLimiter) {
                //System.out.println(crime.category + ": " + dist);
                if (!(type.contains(crime.category))) {
                    //in case this type was not yet added to the vector
                    type.add(crime.category);
                    numb.add(0);
                }
                else
                {
                    // numb.setElementAt(type.indexOf(crime.category)) = numb.setElementAt(type.indexOf(crime.category)) + 1;
                    int j = type.indexOf(crime.category);
                    numb.set(j, numb.get(j)+1);
                }
            }
        }
        for(int i = 0; i < numb.size(); i++) {
            System.out.println(type.get(i) + " : " + numb.get(i));
        }

    }
}
