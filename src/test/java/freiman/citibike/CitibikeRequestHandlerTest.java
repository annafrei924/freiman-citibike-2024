package freiman.citibike;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import freiman.citibike.aws.CitibikeRequestHandler;
import freiman.citibike.aws.CitibikeResponse;
import freiman.citibike.aws.StationCache;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CitibikeRequestHandlerTest {


    @Test
    void handleRequest() {
            //given
            String body = """
                {
                  "from": {
                    "lat": 40.8211,
                    "lon": -73.9359
                  },
                  "to": {
                    "lat": 40.7190,
                    "lon": -73.9585
                  }
                }
                """;
            Context context = mock(Context.class);
            APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
            when(event.getBody()).thenReturn(body);

            //when
            CitibikeRequestHandler handler = new CitibikeRequestHandler();
            CitibikeResponse citibikeResponse = handler.handleRequest(event, context);

            //then
            assertEquals(citibikeResponse.start.name, "Lenox Ave & W 146 St");
            assertEquals(citibikeResponse.end.name, "Berry St & N 8 St");

    }

}



