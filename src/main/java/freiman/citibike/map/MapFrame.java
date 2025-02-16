package freiman.citibike.map;

import javax.swing.*;
import java.awt.*;

public class MapFrame extends JFrame {

    public MapFrame() {

        // Set up the frame
        setTitle("Citibike Bike Map");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        MapController mapController = new MapController();
        add(mapController.createMap(), BorderLayout.CENTER);

        // Create the coordinates panel
        JPanel coordinates = new JPanel();
        JTextField start = new JTextField(30);
        JTextField end = new JTextField(30);
        coordinates.add(new JLabel("Start:"));
        coordinates.add(start);
        coordinates.add(new JLabel("End:"));
        coordinates.add(end);

        // Create the buttons panel
        JPanel buttons = new JPanel();
        JButton map = new JButton("Map");
        JButton clear = new JButton("Clear");
        buttons.add(map);
        buttons.add(clear);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(coordinates, BorderLayout.NORTH);
        southPanel.add(buttons, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Set up MapController listeners to update text fields
        mapController.setOnStartPointSelected((lat, lon) -> {
            start.setText(String.format("%.6f, %.6f", lat, lon));
        });

        mapController.setOnEndPointSelected((lat, lon) -> {
            end.setText(String.format("%.6f, %.6f", lat, lon));
        });

        // Add button functionality
        map.addActionListener(e -> {
            mapController.getLambda();
            repaint();
        });
        clear.addActionListener(e -> {
            start.setText("");
            end.setText("");
            mapController.clear();
        });
    }
}