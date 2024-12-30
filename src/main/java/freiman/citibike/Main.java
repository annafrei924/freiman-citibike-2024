package freiman.citibike;

import freiman.citibike.json.Station;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import freiman.citibike.json.Stations;
import freiman.citibike.map.MapController;
import freiman.citibike.map.MapFrame;
import io.reactivex.rxjava3.core.Single;
import org.jxmapviewer.JXMapViewer;


public class Main {
    public static void main(String[] args) {
        MapFrame frame = new MapFrame();
        frame.setVisible(true);
    }

}
