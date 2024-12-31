package freiman.citibike.map;

import javax.swing.*;
import java.awt.*;

public class MapFrame extends JFrame {


    public MapFrame() {
        MapController mapController = new MapController();

        // Set up the frame
        setTitle("Citibike Bike Map");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Use BorderLayout for proper positioning

        // Add the map to the center
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

        // Add the panels to the frame
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
            mapController.drawRoute();
            repaint();
        });
        clear.addActionListener(e -> {
            start.setText("");
            end.setText("");
            mapController.clear();
        });

        // Make the frame visible
        setVisible(true);
    }
}
