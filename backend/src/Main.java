import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {

        String latitude = "51.48205";
        String longitude = "-0.11204";

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


        Vector<Crime> crimeSet = parse(jsonString, Double.parseDouble(latitude), Double.parseDouble(longitude));

        System.out.println("CrimeSet Size: " + crimeSet.size());

        printCrimeSet(Double.parseDouble(latitude), Double.parseDouble(longitude), 5000, crimeSet);
        
        Map<String, Integer> incidentCount = countIncidents(crimeSet);
        double score = calculateScore(incidentCount);
        jsonCrimeWithScore(new CrimeSum(crimeSet,score));
       
        

    }
    
    public static Map<String, Integer> countIncidents(Vector<Crime> crimeSet){
    	Map<String, Integer> incidentCount = new HashMap<String,Integer>();
    	int count = 0;
    	for (int i=0; i<crimeSet.size(); i++) {
    		String category = crimeSet.get(i).category;
    		if(!incidentCount.containsKey(category)) {
    			incidentCount.put(category, 1);
    		} else {
    			incidentCount.put(category, incidentCount.get(category)+1);
    		}
    	}
    	
    	return incidentCount;
    }
    
    public static double calculateScore(Map<String,Integer> incidentCount) {
    	int vehicleTheft = incidentCount.containsKey("vehicle-theft") ? incidentCount.get("vehicle-crime") : 0;
    	int arsonIncident = (incidentCount.containsKey("criminal-damage-arson")) ? incidentCount.get("criminal-damage-arson") : 0;
    	int theftIncident = incidentCount.containsKey("other-theft") ? incidentCount.get("other-theft") : 0;
    	int robberyIncident = incidentCount.containsKey("robbery") ? incidentCount.get("robbery") : 0;
    	int violenceIncident = incidentCount.containsKey("violent-crime") ? incidentCount.get("violent-crime") : 0; 
    	
    	double vehicleSafety = safetyScore(vehicleTheft, threshold.VEHICLE_MIN, threshold.VEHICLE_MAX);
    	
    	double arsonSafety = safetyScore(arsonIncident, threshold.ARSON_MIN, threshold.ARSON_MAX);
    	
    	double theftSafety = safetyScore(theftIncident, threshold.THEFT_MIN, threshold.THEFT_MAX);
    	
    	double robberySafety = safetyScore(robberyIncident, threshold.ROBBERY_MIN, threshold.ROBBERY_MAX);
    	
    	double violenceSafety = safetyScore(violenceIncident, threshold.VIOLENT_MIN, threshold.VIOLENT_MAX);
    	
    	double average = (vehicleSafety + arsonSafety + theftSafety + robberySafety + violenceSafety) / 5.0;
    	
    	return average;
    	
    }
    
    public static double safetyScore(int value, int threshold_min, int threshold_max) {
    	
    	if(value <= threshold_min) {
    		return 10;
    	}else if(value >= threshold_max){
    		return 0;
    	}
    	
    	double num = value - threshold_min;
    	double denom = threshold_max - threshold_min;
    	
    	double dangerScore = num / denom;
    	
    	return (10.0 - dangerScore);
    }

    public static Vector<Crime> parse(String jsonString, double lat, double lng) {
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
            if(crime.distance(lat, lng) <= 1000) crimeSet.add(crime);
        }
        return crimeSet;
    }

    public static void printCrimeSet(double latitude, double longitude, int distanceLimiter, Vector<Crime> crimeSet) {

        for(int i = 0; i < crimeSet.size(); i++) {
            Crime crime = crimeSet.get(i);

            double dist = crime.distance(latitude, longitude);

            if(dist < distanceLimiter) System.out.println(crime.category + ": " + dist);
        }

    }
    
    public static void jsonCrimeSet(Vector<Crime> crimeSet) {
    	Map<String, Integer> solution = new HashMap<String, Integer>();
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();  
        String json = gson.toJson(crimeSet);
        
        try {
            FileWriter myWriter = new FileWriter("test1.json");
            myWriter.write(json);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public static void jsonCrimeWithScore(CrimeSum solution) {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();  
        String json = gson.toJson(solution);
        
        try {
            FileWriter myWriter = new FileWriter("data.json");
            myWriter.write(json);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
}
