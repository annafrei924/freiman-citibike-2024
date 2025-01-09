package freiman.citibike.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import freiman.citibike.json.StationServiceFactory;
import freiman.citibike.StationFinder;
import freiman.citibike.json.Station;


public class CitibikeRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, CitibikeResponse> {
    private final StationCache cache = new StationCache();
    @Override
    public CitibikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitibikeRequest request = gson.fromJson(body, CitibikeRequest.class);
        StationServiceFactory factory = new StationServiceFactory();
        StationFinder stationFinder = new StationFinder(factory.merge(cache));

        Station start = stationFinder.closestStation(request.from.lat, request.from.lon, false);
        Station end = stationFinder.closestStation(request.to.lat, request.to.lon, true);

        return new CitibikeResponse(request.from, request.to, start, end);
    }
}