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

    public CitibikeRequestHandler(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public CitibikeResponse handleRequest(CitibikeRequest request, Context context) {

        StationServiceFactory factory = new StationServiceFactory();
        StationService service = factory.getService();
        Map<String, Station> stations = factory.merge(service);
        StationFinder stationFinder = new StationFinder(stations);
        Station startStation = stationFinder.closestStation(request.from.lat, request.from.lon, false);

        // Step 5: Build the response
        CitibikeResponse response = new CitibikeResponse();
        response.from = request.from;
        response.to = request.to;

        response.start = startStation;

        Station endStation = stationFinder.closestStation(request.to.lat, request.to.lon, true);
        response.end = endStation;

        return response;


    }

}

