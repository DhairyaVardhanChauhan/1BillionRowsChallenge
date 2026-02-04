package com.example;

import java.awt.desktop.SystemSleepEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static class Stats {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        int count = 0;

        void add(double temp) {
            min = Math.min(min, temp);
            max = Math.max(max, temp);
            sum += temp;
            count++;
        }

        double avg() {
            return sum / count;
        }
    }

    public static void main(String[] args) throws Exception {

        Map<String,Stats> mp = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/salescode/projects/1BRC/src/main/java/com/example/measurements.txt"));
        String line;
        System.out.println("Starting reading: ");
        long statTime = System.currentTimeMillis();
        while((line = bufferedReader.readLine()) != null){
            int splitIndex = line.indexOf(";");
            String city = line.substring(0,splitIndex);
            double temp = fastParseDouble(line,splitIndex+1);
            mp.computeIfAbsent(city, k -> new Stats()).add(temp);
        }
        System.out.println("Reading completed in : " + (System.currentTimeMillis() - statTime));
        List<String> cities = new ArrayList<>(mp.keySet());
        for(String city:cities){
            Stats s = mp.get(city);
            System.out.printf(
                    "%s=%.1f/%.1f/%.1f%n",
                    city,
                    s.min,
                    s.avg(),
                    s.max
            );
        }
    }

    static double fastParseDouble(String str,int start){
        boolean neg = false;
        if(str.charAt(start) == '-'){
            neg = true;
            start++;
        }
        int intPart = 0;
        int fracPart = 0;
        int fracDiv = 1;
        boolean fraction = false;

        for (int i = start; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.') {
                fraction = true;
                continue;
            }
            int digit = c - '0';
            if (!fraction) {
                intPart = intPart * 10 + digit;
            }
            else {
                fracPart = fracPart * 10 + digit;
                fracDiv *= 10;
            }
        }
        double val = intPart + (double) fracPart / fracDiv;
        return neg?-val:val;
    }
}
