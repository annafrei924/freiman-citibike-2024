//package freiman.citibike;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import freiman.citibike.aws.CitibikeRequestHandler;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//
//public class CitibikeRequestHandlerTest {
//
//    @Test
//    void handleRequest() {
//        //given
//        CitibikeRequestHandler handler = new CitibikeRequestHandler();
//        CitibikeRequestHandler.CitibikeRequest.Coordinate fromCoordinate = new  CitibikeRequestHandler.CitibikeRequest.Coordinate();
//        fromCoordinate.lat = 40.8211;
//        fromCoordinate.lon = -73.9359;
//
//        CitibikeRequestHandler.CitibikeRequest.Coordinate toCoordinate = new CitibikeRequestHandler.CitibikeRequest.Coordinate();
//        toCoordinate.lat = 40.7190;
//        toCoordinate.lon = -73.9585;
//
//        CitibikeRequestHandler.CitibikeRequest request = new CitibikeRequestHandler.CitibikeRequest();
//        request.from = fromCoordinate;
//        request.to = toCoordinate;
//        Context context = mock(Context.class);
//
//        //when
//        CitibikeRequestHandler.CitibikeResponse citibikeResponse = handler.handleRequest(request, context);
//
//        //then
//        assertEquals(citibikeResponse.start.name, "Lenox Ave & W 146 St");
//        assertEquals(citibikeResponse.end.name, "Berry St & N 8 St");
//    }
//}
//
//
//
