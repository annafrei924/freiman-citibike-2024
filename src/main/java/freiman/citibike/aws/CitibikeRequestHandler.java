package freiman.citibike.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import freiman.citibike.StationFinder;
import freiman.citibike.json.Station;
import java.util.Map;

public class CitibikeRequestHandler implements
        RequestHandler<CitibikeRequest, CitibikeResponse> {

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

            // Step 5: Build the response
            CitibikeResponse response = new CitibikeResponse();
            response.from = request.from;
            response.to = request.to;

            response.start = new StationResponse(
                    startStation.lat,
                    startStation.lon,
                    startStation.name,
                    startStation.station_id
            );

            Station endStation = stationFinder.closestStation(request.to.lat, request.to.lon, true);
            response.end = new StationResponse(
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

}

