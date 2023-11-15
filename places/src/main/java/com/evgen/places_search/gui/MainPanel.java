package com.evgen.places_search.gui;

import com.evgen.places_search.graphhopper.Place;
import com.evgen.places_search.graphhopper.GraphhopperPlaces;
import com.evgen.places_search.interesting.InfoFinder;
import com.evgen.places_search.interesting.IntPlace;
import com.evgen.places_search.interesting.InterestingPlacesSearch;
import com.evgen.places_search.maps.MapFinder;
import com.evgen.places_search.weather.Weather;
import com.evgen.places_search.weather.WeatherSearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainPanel extends JPanel {

    private JComboBox<Object> resultsComboBox = new JComboBox<>();
    private transient Place currentPlace;
    private transient Place[] places;
    private JTextPane textPane;
    private JTextPane weatherPane;
    private JList<IntPlace> interestingPlaces;

    private final transient InfoFinder infoFinder = new InfoFinder();
    private final transient MapFinder mapFinder = new MapFinder();

    private final ImageIcon noMap = new ImageIcon(ClassLoader.getSystemResource("no_map.png"));
    private final ImageIcon loadingMap = new ImageIcon(ClassLoader.getSystemResource("loading_map.png"));


    public MainPanel(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(initSearchPanel());
        this.add(initResultsPanel());
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(initInfoPanel());
        this.add(new JSeparator(SwingConstants.HORIZONTAL));

        this.add(initDetailsPanel());
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(initMapPanel());
    }

    private JPanel initSearchPanel(){
        GraphhopperPlaces handler = new GraphhopperPlaces();
        JPanel panel = new JPanel();
        panel.setBackground(Color.ORANGE);
        JLabel placeLabel = new JLabel("Введите запрос");
        panel.add(placeLabel);
        JTextField placeTextField = new JTextField();
        placeTextField.setColumns(20);
        panel.add(placeTextField);
        JButton searchButton = new JButton("Поиск",
                new ImageIcon(ClassLoader.getSystemResource("search16.png")));
        panel.add(searchButton);
        resultsComboBox.setSelectedItem("Ждем запрос...");

        searchButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (placeTextField.getText().isEmpty()) return;

                handler.setToSearch(placeTextField.getText());
                resultsComboBox.setSelectedItem(null);

                CompletableFuture<List<Place>> future = CompletableFuture.supplyAsync( ()->{
                    handler.run();
                    return handler.getResults();
                } );

                future.thenApply( result -> {
                    if (result == null){
                        resultsComboBox.setSelectedItem("Ошибка!");
                        return null;
                    }
                    resultsComboBox.removeAllItems();
                    places = result.toArray(new Place[0]);
                    for (Place place : places){
                        resultsComboBox.addItem(place);
                    }
                    resultsComboBox.setEnabled(true);
                    return null;
                } );
            }
        });

        return panel;
    }

    public JPanel initWeatherPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Погода в выбранном месте");

        weatherPane = new JTextPane();
        weatherPane.setEditable(false);
        weatherPane.setContentType("text/html");
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(weatherPane);

        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.X_AXIS));
        weatherPanel.add(label);

        panel.add(weatherPanel);
        panel.add(scrollPane);

        return panel;
    }

    public void prepareWeather(){
        if (currentPlace == null) return;
        WeatherSearch weather = new WeatherSearch();
        weather.setCoords(currentPlace.parseCoordinates());

        CompletableFuture<Weather> future = CompletableFuture.supplyAsync( ()->{
            weatherPane.setText("<div style=\"font-family: Arial;\"><b>загрузка...</b></div>");
            weather.run();
            return weather.getResult();
        } );

        future.thenApply( result -> {
            if (result == null){
                weatherPane.setText("<div style=\"font-family: Arial;\"><b>ошибка!</b></div>");
                return null;
            }
            weatherPane.setText(result.toString());
            return null;
        } );

    }

    private JPanel initResultsPanel() {
        JPanel panel = new JPanel();

        resultsComboBox = new JComboBox<>(new String[]{"Ждите..."});
        resultsComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        resultsComboBox.setEnabled(false);

        JPanel wrapper = new JPanel();
        wrapper.add( resultsComboBox );
        panel.add(new JLabel("Результаты:"));
        panel.add( wrapper );

        resultsComboBox.addActionListener(e -> {
            textPane.setText(" ");
            currentPlace = (Place)resultsComboBox.getSelectedItem();
            if (currentPlace != null) {
                prepareWeather();
                findIntPlaces();
                redrawMap();
            }
        });

        return panel;
    }

    private JPanel initInfoPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(initWeatherPanel());
        //panel.add(wPanel.initWeatherPanel());
        panel.add(initListPanel());

        return panel;
    }

    private JPanel initListPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);

        JScrollPane pane = new JScrollPane();
        interestingPlaces = new JList<>();
        interestingPlaces.setDragEnabled(false);
        interestingPlaces.setVisibleRowCount(8);
        interestingPlaces.addListSelectionListener(e -> {
            if (!interestingPlaces.getValueIsAdjusting()) {
                findInfo(interestingPlaces.getSelectedValue());
            }
        });

        pane.createVerticalScrollBar();
        pane.setViewportView(interestingPlaces);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.add(new JLabel("Интересные места рядом"));

        panel.add(panel1);
        panel.add(pane);

        return panel;
    }

    private void findIntPlaces(){
        if (currentPlace == null) return;
        InterestingPlacesSearch ipsearch = new InterestingPlacesSearch();
        ipsearch.setCoords(currentPlace.parseCoordinates());
        interestingPlaces.setListData(new IntPlace[]{new IntPlace("загрузка...")});

        CompletableFuture<List<IntPlace>> future = CompletableFuture.supplyAsync( ()->{
            ipsearch.run();
            return ipsearch.getResults();
        } );

        future.thenApply( result -> {
            if (result == null){
                System.out.println("int places error");
                return null;
            }
            interestingPlaces.setListData(result.toArray(new IntPlace[0]));
            return null;
        } );

    }

    private JPanel initDetailsPanel(){
        JPanel panel = new JPanel();
        JScrollPane pane = new JScrollPane();
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        pane.setViewportView(textPane);
        pane.createVerticalScrollBar();
        textPane.setText("<div style=\"font-family: Arial;\">Попробуйте найти что-нибудь!</div>");
        textPane.setEditable(false);
        textPane.setPreferredSize(new Dimension(450, 200));

        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            }
        });

        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            }
        });

        panel.add(pane);
        return panel;
    }

    private void findInfo(IntPlace place){
        if (place == null) return;
        String xid = place.getXid();
        if (xid == null) return;

        CompletableFuture<String> future = CompletableFuture.supplyAsync( ()->{
            textPane.setText("<div style=\"font-family: Arial;\">Ищем подробности...</div>");
            infoFinder.setXid(xid);
            infoFinder.run();
            return infoFinder.getInfo();
        } );

        future.thenApply( result -> {
            textPane.setText("<html><div style=\"font-family: Arial;\">"+result+"</div></html>");
            return null;
        } );
    }

    JLabel map = new JLabel(noMap);

    private JPanel initMapPanel(){
        JPanel panel = new JPanel();

        panel.add(new JLabel("Карта:"));
        panel.add(map);
        return panel;
    }

    private void redrawMap(){
        if (currentPlace == null) return;

        mapFinder.setCoords(currentPlace.parseCoordinates());
        map.setIcon(loadingMap);


        CompletableFuture<BufferedImage> future = CompletableFuture.supplyAsync( ()->{
            mapFinder.run();
            return mapFinder.getMap();
        } );

        future.thenApply( result -> {
            if (result == null){
                System.out.println("map error");
                map.setIcon(noMap);
                return null;
            }
            map.setIcon(new ImageIcon(result));
            return null;
        } );
    }
}
