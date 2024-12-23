package freiman.citibike;

import com.amazonaws.services.lambda.runtime.Context;
import freiman.citibike.aws.CitibikeRequest;
import freiman.citibike.aws.CitibikeRequestHandler;
import freiman.citibike.aws.CitibikeResponse;
import freiman.citibike.aws.Coordinate;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CitibikeRequestHandlerTest {

    @Test
    void handleRequest() {
        //given
        S3Client mockS3Client = mock(S3Client.class);
        Context context = mock(Context.class);
        CitibikeRequestHandler handler = new CitibikeRequestHandler(mockS3Client);
        Coordinate fromCoordinate = new Coordinate();
        fromCoordinate.lat = 40.8211;
        fromCoordinate.lon = -73.9359;

        Coordinate toCoordinate = new Coordinate();
        toCoordinate.lat = 40.7190;
        toCoordinate.lon = -73.9585;

        CitibikeRequest request = new CitibikeRequest();
        request.from = fromCoordinate;
        request.to = toCoordinate;


        //when
        CitibikeResponse citibikeResponse = handler.handleRequest(request, context);

        //then
        assertEquals(citibikeResponse.start.name, "Lenox Ave & W 146 St");
        assertEquals(citibikeResponse.end.name, "Berry St & N 8 St");
    }
}



