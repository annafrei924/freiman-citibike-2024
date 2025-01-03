package freiman.citibike.aws.s3;

import com.google.gson.Gson;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import freiman.citibike.json.Stations;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;


public class StationsCache {
    private Stations stationInfo;
    //CHECKSTYLE:OFF
    private final String BUCKET_NAME = "freiman.citibike1";
    private final String KEY_NAME = "stationInfo.json";
    //CHECKSTYLE:ON
    private final S3Client s3Client;
    private final StationService service;
    private Instant lastModified;


    private boolean s3LastModified() {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(KEY_NAME)
                    .build();
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            lastModified = headObjectResponse.lastModified();
            return Duration.between(lastModified, Instant.now()).toHours() >= 1;
        } catch (Exception e) {
            return false;
        }
    }

    public StationsCache() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        this.service = new StationServiceFactory().getService();
    }

    public Stations getStations() {
        boolean overOneHour = timeSinceLastModified();
        if (stationInfo != null && lastModified != null && !overOneHour) {
            return stationInfo;
        } else if (overOneHour) {
            stationInfo = service.stationInfo().blockingGet();
            writeToS3();
            lastModified = Instant.now();
        }
        else if (stationInfo == null && !overOneHour) {
            readFromS3();
        }
        return stationInfo;

    }


    public void writeToS3() {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(KEY_NAME)
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(stationInfo);
        s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
    }


    public void readFromS3() {

        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(BUCKET_NAME)
                .key(KEY_NAME)
                .build();

        InputStream in = s3Client.getObject(getObjectRequest);
        Gson gson = new Gson();
        stationInfo = gson.fromJson(new InputStreamReader(in), Stations.class);
    }

    private Instant getLastModified() {
        return lastModified;
    }

    private Stations getStationsFromService() {
        return service.stationInfo().blockingGet();
    }

    public boolean timeSinceLastModified() {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(KEY_NAME)
                    .build();
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            lastModified = headObjectResponse.lastModified();
            return Duration.between(lastModified, Instant.now()).toHours() >= 1;
        } catch (Exception e) {
            return false;
        }
    }


}


