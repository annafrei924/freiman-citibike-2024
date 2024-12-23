package freiman.citibike.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import com.google.gson.Gson;
import freiman.citibike.StationFinder;
import freiman.citibike.json.Station;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CitibikeRequestHandler implements RequestHandler<
        CitibikeRequestHandler.CitibikeRequest, CitibikeRequestHandler.CitibikeResponse> {

    private final S3Client s3Client;

    public CitibikeRequestHandler() {
        s3Client = S3Client.create();
    }

    @Override
    public CitibikeResponse handleRequest(CitibikeRequest request, Context context) {
        try {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("freiman.citibike1")
                    .key("coordinates.json")
                    .build();


            StationServiceFactory factory = new StationServiceFactory();
            StationService service = factory.getService();
            Map<String, Station> stations = factory.merge(service);
            StationFinder stationFinder = new StationFinder(stations);
            Station startStation = stationFinder.closestStation(request.from.lat, request.from.lon, false);
            Station endStation = stationFinder.closestStation(request.to.lat, request.to.lon, true);

            // Step 5: Build the response
            CitibikeResponse response = new CitibikeResponse();
            response.from = request.from;
            response.to = request.to;

            response.start = new CitibikeResponse.StationResponse(
                    startStation.lat,
                    startStation.lon,
                    startStation.name,
                    startStation.station_id
            );

            response.end = new CitibikeResponse.StationResponse(
                    endStation.lat,
                    endStation.lon,
                    endStation.name,
                    endStation.station_id
            );

            return response;

        } catch (Exception e) {
            System.err.println("Error handling request: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to handle the request", e);
        }
    }


    // Request and Response Definitions
    public static class CitibikeRequest {
        public Coordinate from;
        public Coordinate to;

        public static class Coordinate {
            public double lat;
            public double lon;
        }
    }

    public static class CitibikeResponse {
        public CitibikeRequest.Coordinate from;
        public CitibikeRequest.Coordinate to;
        public StationResponse start;
        public StationResponse end;

        public static class StationResponse {
            public double lat;
            public double lon;
            public String name;
            public String station_id;

            public StationResponse(double lat, double lon, String name, String station_id) {
                this.lat = lat;
                this.lon = lon;
                this.name = name;
                this.station_id = station_id;
            }
        }
    }
}
