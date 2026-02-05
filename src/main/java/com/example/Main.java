package com.example;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
        BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream("/Users/salescode/projects/1BRC/src/main/java/com/example/measurements.txt"), 1 << 16);
        byte[] buffer = new byte[1<<16];
        int len;

        byte[]carry = new byte[256];
        int carryLen = 0;
        System.out.println("Starting reading: ");
        long statTime = System.currentTimeMillis();

        while ((len = bufferedReader.read(buffer)) != -1) {
            int start = 0;
            for (int i = 0; i < len; i++) {
                if (buffer[i] == '\n') {
                    int lineLen = carryLen + (i - start);
                    byte[] line = new byte[lineLen];
                    System.arraycopy(carry,0,line,0,carryLen);
                    System.arraycopy(buffer,start,line,carryLen,i-start);
                    parseLine(line, 0, lineLen, mp);
                    carryLen = 0;
                    start = i + 1;
                }
            }

            if(start < len){  // no /n found
                carryLen = len - start;
                System.arraycopy(buffer, start, carry, 0, carryLen);
            }
        }
        bufferedReader.close();
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

    static double fastParseDouble(byte[] str,int start,int end){
        boolean neg = false;
        if(str[start] == '-'){
            neg = true;
            start++;
        }
        int intPart = 0;
        int fracPart = 0;
        int fracDiv = 1;
        boolean fraction = false;

        for (int i = start; i < end; i++) {
            byte c = str[i];
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

    static void parseLine(byte[] buffer,int start,int end,Map<String,Stats>mp){
        int seperatorIndex = start;
        while(buffer[seperatorIndex] != ';'){
            seperatorIndex++;
        }
        String city = new String(buffer, start, seperatorIndex - start);
        double temperature = fastParseDouble(buffer,seperatorIndex+1,end);
        mp.computeIfAbsent(city,k->new Stats()).add(temperature);
    }
}
