package com.example;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;


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

    static class ChunkProcessor implements Runnable {
        private final String filePath;
        private final long start;
        private final long end;
        private final Map<String, Stats> result = new HashMap<>(16_384);

        public ChunkProcessor(String filePath, long start, long end) {
            this.filePath = filePath;
            this.start = start;
            this.end = end;
        }

        Map<String, Stats> getResult() {
            return result;
        }

        @Override
        public void run() {
            try (FileChannel channel = new FileInputStream(filePath).getChannel()) {
                channel.position(start);

                ByteBuffer buffer = ByteBuffer.allocateDirect(1 << 16);
                byte[] carry = new byte[256];
                int carryLen = 0;
                long position = start;

                while (position < end) {
                    int bytesRead = channel.read(buffer);
                    if (bytesRead == -1) break;

                    buffer.flip();
                    int limit = buffer.limit();

                    int startIdx = 0;
                    for (int i = 0; i < limit; i++) {
                        if (buffer.get(i) == '\n') {
                            int lineLen = carryLen + (i - startIdx);
                            byte[] line = new byte[lineLen];

                            if (carryLen > 0) {
                                buffer.position(startIdx);
                                buffer.get(line, carryLen, i - startIdx);
                                System.arraycopy(carry, 0, line, 0, carryLen);
                            } else {
                                buffer.position(startIdx);
                                buffer.get(line, 0, i - startIdx);
                            }

                            parseLine(line, 0, lineLen, result);

                            carryLen = 0;
                            startIdx = i + 1;
                        }
                    }

                    if (startIdx < limit) {
                        int remaining = limit - startIdx;
                        buffer.position(startIdx);
                        buffer.get(carry, 0, remaining);
                        carryLen = remaining;
                    }

                    position += bytesRead;
                    buffer.clear();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static long [] chunkStartOffsets(FileChannel channel, int chunks, long fileSize) throws IOException {

        long[] offsets = new long[chunks];
        offsets[0] = 0;
        ByteBuffer oneByte = ByteBuffer.allocate(1);
        for(int i = 1;i<chunks;i++){
            long pos = fileSize*i/chunks;
            channel.position(pos);
            while(true){
                oneByte.clear();
                if(channel.read(oneByte) == -1){
                    break;
                }
                oneByte.flip();
                if (oneByte.get() == '\n') {
                    offsets[i] = channel.position();
                    break;
                }
            }
        }
        return offsets;
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

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("Started calculating");
        FileChannel channelBuffer = new FileInputStream("/Users/salescode/projects/1BRC/src/main/java/com/example/measurements.txt").getChannel();
        int cores = Runtime.getRuntime().availableProcessors();
        long fileSize = channelBuffer.size();
        long[] offsets = chunkStartOffsets(channelBuffer, cores, fileSize);
        ChunkProcessor[] workers = new ChunkProcessor[cores];
        Thread [] threads = new Thread[cores];
        for(int i = 0;i<cores;i++){
            long start = offsets[i];
            long end = (i+1 < cores)?offsets[i+1]:fileSize;
            workers[i] = new ChunkProcessor("/Users/salescode/projects/1BRC/src/main/java/com/example/measurements.txt",start,end);
            threads[i] = new Thread(workers[i]);
            threads[i].start();
        }
        for(Thread t : threads){
            t.join();
        }

        Map<String,Stats> finalMap = new HashMap<>(32_768);
        long endTime = System.currentTimeMillis();
        for (ChunkProcessor worker : workers) {
            for(var entry:worker.getResult().entrySet()){
                finalMap.merge(entry.getKey(),entry.getValue(),(a,b)->{
                    a.min = Math.min(a.min, b.min);
                    a.max = Math.max(a.max, b.max);
                    a.sum += b.sum;
                    a.count += b.count;
                    return a;
                });
            }
        }
        System.out.println("Total time taken to sort:"+(endTime-startTime));
        List<String> cities = new ArrayList<>(finalMap.keySet());
        Collections.sort(cities);
        for (String city : cities) {
            Stats s = finalMap.get(city);
            System.out.printf(
                    "%s=%.1f/%.1f/%.1f%n",
                    city,
                    s.min,
                    s.avg(),
                    s.max
            );
        }


    }


}
