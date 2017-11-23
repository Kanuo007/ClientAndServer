package client.Utils;

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

    public static final String DAY_ONE_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW3/BSDSAssignment2Day1.ser";
    public static final String DAY_TWO_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW3/BSDSAssignment2Day2.ser";
    public static final String DAY_THREE_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW3/BSDSAssignment2Day3.ser";
    public static final String DAY_FOUR_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW3/BSDSAssignment2Day4.ser";
    public static final String DAY_FIVE_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW3/BSDSAssignment2Day5.ser";
    public static final String DAY_999_FILE = "/Users/iris/Desktop/DistributedSystem/DIS_HW3/BSDSAssignment2Day999.ser";

    public static final String serve_1_hostName = "ec2-54-213-9-70.us-west-2.compute.amazonaws.com";
    public static final String serve_2_hostName = "ec2-54-191-104-220.us-west-2.compute.amazonaws.com";
    public static final String serve_3_hostName = "ec2-54-186-190-185.us-west-2.compute.amazonaws.com";
    public static final String loadBalancer_hostName = "disLB-48246579.us-west-2.elb.amazonaws.com";
    private static final String aws_postEndPoint = "/DIS_HW3_war/rest/load";
    private static final String aws_getEndPoint = "/DIS_HW3_war/rest/myvert";

    private static final String local_hostName = "127.0.0.1";
    public static final String local_postEndPoint = "/rest/load";
    public static final String local_getEndPoint = "/rest/myvert";

    public static final String local_DB_IP = "Wens-MacBook-Pro.local";
    public static final String DB_Server1_IP = "ip-172-31-22-159";
    public static final String DB_Server2_IP = "ip-172-31-26-76";
    public static final String DB_Server3_IP = "ip-172-31-26-220";

    private static final String Query_DB_Time = "QueryDBTime";
    public static final String Request_Response_Time = "ResponseTime";
    private static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";

    public static final String port = "8080";
    public static final String postEndPoint = aws_postEndPoint;
    public static final String getEndPoint = aws_getEndPoint;
    private List<Long> threadlatencyList;
    private List<Long>threadrequestBeginList;

    // ******* config each time **********
    public static final String hostName = loadBalancer_hostName;
    public static final int dayNum = 5;
    public static final String dayFile = DAY_FIVE_FILE;
    public static final String requestType = POST_REQUEST;
    public static final String latencyType = Request_Response_Time;
    public static final int userAmount = 10000;
    public static final int GETTHREADS = 100;
    public static final int POSTTHREADS = 50;


    General() {
        this.threadlatencyList = new ArrayList<Long>();
        this.threadrequestBeginList = new ArrayList<Long>();
    }
    List<Long> getThreadlatencyList() { return threadlatencyList; }
    List<Long> getThreadrequestBeginList() { return threadrequestBeginList; }

    public static ArrayList<RFIDLiftData> readData(String fileName){
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

    public static Long meanLatencies(List<Long> latencies)
    {   if (latencies.size() == 0) return 0L;
        long sum = 0;
        for (Long mark : latencies) {
            sum += mark;
        }
        return latencies.isEmpty()? 0: sum/latencies.size();
    }

    public static Long medianLatencies(List<Long> latencies)
    {
        if (latencies.size() == 0) return 0L;
        Collections.sort(latencies);
        Long median = (latencies.get(latencies.size()/2 - 1) + latencies.get(latencies.size() / 2)) / 2;
        return median;
    }

    public static Long percentileLatencise(List<Long> latencies, double percentile)
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
