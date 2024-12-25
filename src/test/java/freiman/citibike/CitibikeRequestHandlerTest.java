package freiman.citibike;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import freiman.citibike.aws.CitibikeRequest;
import freiman.citibike.aws.CitibikeRequestHandler;
import freiman.citibike.aws.CitibikeResponse;
import freiman.citibike.aws.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CitibikeRequestHandlerTest {


    @Test
    void handleRequest() {
        String body = "{\n" + "\n" + "  \"from\": {\n" + "\n" + "    \"lat\": 40.8211,\n" +
                "\n" + "    \"lon\": -73.9359\n" + "\n" + "  },\n" + "\n" + "  \"to\": {\n" +
                "\n" + "    \"lat\": 40.7190,\n" + "\n" + "    \"lon\": -73.9585\n" +
                "\n" + "  }\n" + "\n" + "}";

        Context context = mock(Context.class);
        APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
        when(event.getBody()).thenReturn(body);
        CitibikeRequestHandler handler = new CitibikeRequestHandler();

        //when
        CitibikeResponse citibikeResponse = handler.handleRequest(event, context);

        //then
        assertEquals(citibikeResponse.start.name, "Lenox Ave & W 146 St");
        assertEquals(citibikeResponse.end.name, "Berry St & N 8 St");
    }
}



