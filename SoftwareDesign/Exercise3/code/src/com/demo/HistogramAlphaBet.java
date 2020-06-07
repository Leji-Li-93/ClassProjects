package com.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class HistogramAlphaBet {

    /**
     * take out all non-letter characters in a string
     * @param txt the string that needs to handle
     * @return a string that contains letter only
     */
    private static String cleanText(String txt){
        return txt.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    /**
     * rounding a digit in to given decimal place
     * @param origin
     * @param decimalPlace
     * @return
     */
    public static double roundOff(double origin, int decimalPlace){
        double rounding = Math.pow(10, decimalPlace);
        return ((double)Math.round(origin * rounding)) / rounding;
    }

    /**
     * count the number of the characters in alphabet
     * @param file the file provides that text context
     * @return an unsorted map that records that count of each characters in alphabet
     */
    public static Map<Character, Integer> readText(File file){
        Map<Character, Integer> map = new HashMap<Character, Integer>(26);
        for(char c = 'a'; c <= 'z'; c++){
            map.put(c, 0);
        }
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()){
                String line = cleanText(reader.nextLine());
                //System.out.println(line);
                for (int i = 0; i < line.length(); i++) {
                    map.put(line.charAt(i), (map.get(line.charAt(i)) +1 ));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Something wrong when reading the file.");
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * calculate the frequency of each entries in a map
     * @param map the map provides the source data, they type of the value should be
     * @param <K> the type of key of the map
     * @return an unsorted map that contains the frequency data of the source map
     */
    public static<K> Map<K, Double> getFrequency(Map<K, ? extends Number> map){
        if(null == map){
            return null;
        }
        Map<K, Double> frequency = new HashMap<K, Double>(map.size());
        double sum = 0;
        for(Map.Entry<K, ? extends Number> entry: map.entrySet()){
            sum += entry.getValue().doubleValue();
        }
        for(Map.Entry<K, ? extends Number> entry: map.entrySet()){
            frequency.put(entry.getKey(), roundOff(entry.getValue().doubleValue() / sum, 5));
        }
        return frequency;
    }

    /**
     * sort the map in descending order
     * @param map source map
     * @param <K> type of the key
     * @param <V> type of the value. This type must be comparable
     * @return a sorted map in descending order, according to the value
     */
    public static<K, V extends Comparable<? super V>> Map<K, V> sortMap(Map<K, V> map){
        if(null == map){
            return null;
        }
        return map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.<K, V>comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }


    /**
     *
     * @param map
     * @param width
     * @param height
     * @param n
     * @return
     */
    public static Canvas getPieChartCanvas(Map<Character, Double> map, double width, double height, int n){
        Canvas canvas = new Canvas(width, height);
        double r = 0.6 * Math.min(width, height) / 2;
        MyPieChart chart = new MyPieChart(width/2, height/2, r);
        chart.setFrequency(map);
        chart.setDisplayCount(n);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        chart.draw(gc);
        return canvas;
    }
}
