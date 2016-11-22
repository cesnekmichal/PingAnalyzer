/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinganalyzer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Michal
 */
public class PingAnalyzer {

    public static void main(String[] args) throws InterruptedException {
        ArrayList<String> lines = tracert("seznam.cz");
        ArrayList<Tuple<String,String>> ipNODES = getIpNODES(lines);
        for (int i = 0; i < 1000; i++) {
            System.out.println("--S-T-A-R-T--");
            for (Tuple<String,String> ipNODE : ipNODES) {
                Integer latency = getLatency(ping(ipNODE.first,5000));
                String latencyStr = latency!=null ? latency.toString() : "?";
                System.out.println(latencyStr + "ms "+ipNODE);
            }
            System.out.println("----E-N-D----");
            Thread.sleep(1000);
        }
    }
    
    public static void main1(String[] args) {
        System.out.println(getHostName("193.165.148.129"));
    }
    
    public static void sleep(long milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ex) {
        }
    }
    
    public static String getHostName(String ipAddress){
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            String hostName = inetAddress.getHostName();
            if (!ipAddress.equals(hostName)) {
                return hostName;
            } else {
                return null;
            }
        } catch (UnknownHostException ex) {
            return null;
        }
    }
    
    public static Integer getLatency(ArrayList<String> lines){
        for (String line : lines) {
            if(line.startsWith("Reply from")){
                int start = line.indexOf("time")+5;
                int end   = line.indexOf("ms");
                try {
                    String latency_str = line.substring(start, end);
                    Integer latency = Integer.valueOf(latency_str);
                    if(latency instanceof Integer){
                        return latency;
                    }
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }
    
    public static ArrayList<Tuple<String,String>> getIpNODES(ArrayList<String> lines){
        ArrayList<Tuple<String,String>> result = new ArrayList<>();
        for (String line : lines) {
            System.out.println(line);
        }
        for (String line : lines) {
            if(line.startsWith("Tracing")) continue;
            String ip = getIP(line);
            if(ip!=null){
                result.add(new Tuple<>(ip,line.substring(32)));
            }
        }
        return result;
    }
    
    public static String getIP(String text){
        String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
    
    public static ArrayList<String> tracert(String ipAddressOrHostname) {
        return executeCommand("tracert "+ipAddressOrHostname);
    }
    
    public static ArrayList<String> ping(String ipAddressOrHostname,Integer timeout) {
        return executeCommand("ping -w "+timeout+" -n 1 "+ipAddressOrHostname);
    }
    
    private static ArrayList<String> executeCommand(String command) {
    ArrayList<String> lines = new ArrayList<>();
    Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;

    }    
 
    /**
     * Metoda deserializuje objekt z bajtoveho pole
     * @param byteArray Bajtove pole
     * @return Deserializovany objekt
     */
    public static Object deSerializeObject(byte[] byteArray){
        Object result = null;
        if ((byteArray!=null) && (byteArray.length>0)){
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            Object deSerializeObject = deSerializeObject(bais);
            try { 
                bais.close();
            } catch (IOException ex) {
                return null;
            }
            return deSerializeObject;
        }
        return result;
    }
    
    public static Object deSerializeObject(InputStream  is){
        Object result = null;
        try(ObjectInputStream ois = new ObjectInputStream(is)){
            result = ois.readObject();
            return result;
        } catch (Exception ex) {
            return result;
        } 
    }
    
    /**
     * Metoda serializuje objekt na bajtove pole
     * @param o Serializovatelny objek
     * @return Bajtove pole
     */
    public static byte[] serializeObject(Object o){
        if(o==null) return new byte[]{};
        byte[] result = null;
        try {
            ByteArrayOutputStream  baos = new ByteArrayOutputStream();
            serializeObject(o, baos);
            result = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            return null;
        }
        return result;        
    }
    
    /**
     * Metoda serializuje objekt na bajtove pole
     * @param o Serializovatelny objek
     * @param os stream do ktereho se serializovany objekt zapise
     */
    public static void serializeObject(Object o, OutputStream os){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            try {
                oos.writeObject(o);
            } finally {
                oos.close();
            }
        } catch (IOException ex) {
            return;
        }
    }
    
    
}
