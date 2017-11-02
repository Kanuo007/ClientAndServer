package client;

import bsdsass2testdata.RFIDLiftData;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class General{
    protected static final String hostName = "ec2-54-213-240-135.us-west-2.compute.amazonaws.com";
    protected static final String postEndPoint = "/DIS_HW2_war/rest/load";
    protected static final String getEndPoint = "/DIS_HW2_war/rest/myvert";
//    protected static final String hostName = "127.0.0.1";
//    protected static final String postEndPoint = "/rest/load";
//    protected static final String getEndPoint = "/rest/myvert";
    protected static final String port = "8080";
    protected static final int userAmount = 40000;
    protected static final int Getthreads = 50;
    protected static final int Postthreads = 10;
    protected static final String DAY_ONE_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW2/BSDSAssignment2Day1.ser";
    protected static final String DAY_TWO_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW2/BSDSAssignment2Day2.ser";

    private List<Long> threadlatencyList;
    private List<Long>threadrequestBeginList;
    General() {
        this.threadlatencyList = new ArrayList<Long>();
        this.threadrequestBeginList = new ArrayList<Long>();
    }
    List<Long> getThreadlatencyList() { return threadlatencyList; }
    List<Long> getThreadrequestBeginList() { return threadrequestBeginList; }

    protected static ArrayList<RFIDLiftData> readData(String fileName){
        // read from file
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList <RFIDLiftData> RFIDDataIn = new ArrayList<RFIDLiftData>();

        try
        {
            // TO DO change file path for your system
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);

            // read data from serialized file
            System.out.println("===Reading array list");
            RFIDDataIn = (ArrayList) ois.readObject();

            // output contents to console
            int count = 0;
            System.out.println("===Array List contents");
            for(RFIDLiftData tmp: RFIDDataIn){
                count++;
            }
            System.out.println("Rec Count = " + count);

            ois.close();
            fis.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return RFIDDataIn;
    }

    protected static Long meanLatencies(List<Long> latencies)
    {   if (latencies.size() == 0) return 0L;
        long sum = 0;
        for (Long mark : latencies) {
            sum += mark;
        }
        return latencies.isEmpty()? 0: sum/latencies.size();
    }

    protected static Long medianLatencies(List<Long> latencies)
    {
        if (latencies.size() == 0) return 0L;
        Collections.sort(latencies);
        Long median = (latencies.get(latencies.size()/2 - 1) + latencies.get(latencies.size() / 2)) / 2;
        return median;
    }

    protected static Long percentileLatencise(List<Long> latencies, double percentile)
    {
        if (latencies.size() == 0) return 0L;
        Collections.sort(latencies);
        int index = (int)Math.ceil(((percentile / (double)100) * (double)latencies.size()));
        return latencies.get(index - 1);
    }

    public static void writeCSV(List<Long> latencies, List<Long> startTime, String fileName) {

        try {
            FileWriter writer = new FileWriter(fileName);
            CSVWriter.writeLine(writer, Arrays.asList("Latency", "RequestBeginTime"),',');
            for(int i = 0; i < latencies.size(); i++) {
                CSVWriter.writeLine(writer, Arrays.asList(String.valueOf(latencies.get(i)), String.valueOf(startTime.get(i))), ',');
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
