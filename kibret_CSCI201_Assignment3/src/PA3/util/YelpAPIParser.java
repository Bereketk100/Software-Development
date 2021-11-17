package PA3.util;

import com.google.gson.*;
import PA3.models.Location;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

public class YelpAPIParser {
    public static Location getLocation(String restaurant, Location location) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            String builder = "https://api.yelp.com/v3/businesses/search" + "?term=" + restaurant + "&" +
                    "latitude=" + location.getLatitude() + "&" +
                    "longitude=" + location.getLongitude();
            Request request = new Request.Builder().url(builder).method("GET", null).addHeader(
                    "Authorization",
                    "Bearer 7wCO8Rbxb8qVtoOzKUj9KDCl-9jHzEM31mx_C-hO0xq8ZAuQLyY3liR9pK7pDcRGuP8a2BdT8IOAvEfupwYhI0fuNir-ptHTBCSNtGi7NV4PKr2LN27a3tzYULeJYXYx")
                    .build();
            Response response = client.newCall(request).execute();

            Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new MyDeserializer()).create();
            Location val = gson.fromJson(Objects.requireNonNull(response.body()).string(), Location.class);

            return val;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

}

// Code adapted from https://stackoverflow.com/questions/23070298/get-nested-json-object-with-gson-using-retrofit
class MyDeserializer implements JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonElement content = je.getAsJsonObject().getAsJsonArray("businesses").get(0).getAsJsonObject()
                .get("coordinates");

        return new Gson().fromJson(content, Location.class);
    }
}
