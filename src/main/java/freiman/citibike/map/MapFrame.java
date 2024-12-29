package freiman.citibike.map;

import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.awt.*;

public class MapFrame extends JFrame {
    private MapController mapController;
    public MapFrame(MapController mapController) {
        this.mapController = mapController;
        JFrame frame = new JFrame("Citibike Bike Map");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JPanel mapPanel = new JPanel();
        //mapPanel.add(mapController.createMap());
        //add(frame.getContentPane().add(mapController.createMap()), BorderLayout.CENTER);
        frame.getContentPane().add(mapController.createMap());

        JPanel coordinates = new JPanel();
        JTextField start = new JTextField();
        JTextField end = new JTextField();
        coordinates.add(start);
        coordinates.add(end);
        add(coordinates, BorderLayout.SOUTH);

        JPanel buttons = new JPanel();
        JButton map = new JButton("Map");
        JButton clear = new JButton("Clear");
        buttons.add(map);
        buttons.add(clear);
        add(buttons, BorderLayout.SOUTH);


        frame.setVisible(true);
    }

}
