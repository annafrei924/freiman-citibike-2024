package freiman.citibike.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import freiman.citibike.StationFinder;
import freiman.citibike.json.Station;
import java.util.Map;

public class CitibikeRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, CitibikeResponse> {

    @Override
    public CitibikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitibikeRequest request = gson.fromJson(body, CitibikeRequest.class);

        StationServiceFactory factory = new StationServiceFactory();
        StationService service = factory.getService();
        Map<String, Station> stations = factory.merge(service);
        StationFinder stationFinder = new StationFinder(stations);

        CitibikeResponse response = new CitibikeResponse();
        response.from = request.from;
        response.to = request.to;

        response.start = stationFinder.closestStation(request.from.lat, request.from.lon, false);

        response.end = stationFinder.closestStation(request.to.lat, request.to.lon, true);

        return response;
    }
}

