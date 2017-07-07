import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class LVLServer extends UnicastRemoteObject implements InterfaceRMI {

    private File logs = null;
    private int recordCount =0;
    private int baseID = 10000;
    private String recordID;
    private int MTLport = 1412;
    private int DDOPort = 7825;
    private String managerID;
    public HashMap<String, List<Records>> recordDetails;
    
    public LVLServer() throws IOException {
        logs = new File("MTLlog.txt");
        if(!logs.exists()) {
            logs.createNewFile();
        }
        else
            if(logs.delete())
                logs.createNewFile();
        recordDetails = new HashMap<String, List<Records>>();
    }


    @Override
    public String createTRecord(String firstName, String lastName, String address, double phoneNumber,
            String specialization, String location) throws RemoteException, IOException
    {
        // TODO Auto-generated method stub
        if(location.equalsIgnoreCase("LVL"))
                recordCount++;
        if(location.equalsIgnoreCase("MTL")) {
            
        }
        else {
            
        }
        
        try {
            recordID = "TR" + baseID;
            baseID++;
            TeacherRecord trec = new TeacherRecord(firstName, lastName, address, phoneNumber, specialization, location, recordID);
            String mapKey = lastName.substring(0, 1).toUpperCase();
            List<Records> lstRecords = recordDetails.get(mapKey);
            if (lstRecords == null) {
                lstRecords = new ArrayList<Records>();
                recordDetails.put(mapKey, lstRecords);
            }
            lstRecords.add(trec);
            recordCount++;
            this.traceLog("Created teacher record Succesfully +recordID+ firstName+ lastName+ address+ phoneNumber+ specialization+ location+");
            return "Created a teacher record"  +recordID;
        }
        
        catch(Exception e) {
            this.traceLog("Error while creating teacher record");
            return "Error while creating teacher record";
            
        }
    }


    @Override
    public String createSRecord(String firstName, String lastName, String[] courseRegistered, String status,
            Date statusDate) throws RemoteException, IOException
    {
        try {
            recordID = "TR" + baseID;
            baseID++;
            StudentRecord srec = new StudentRecord(firstName, lastName, courseRegistered, status, statusDate, recordID);
            String mapKey = lastName.substring(0, 1).toUpperCase();
            List<Records> lstRecords = recordDetails.get(mapKey);
            if (lstRecords == null) {
                lstRecords = new ArrayList<Records>();
                recordDetails.put(mapKey, lstRecords);
            }
            lstRecords.add(srec);
            recordCount++;
            this.traceLog("Created student record Succesfully");
            return "Created a student record"  +recordID;
            }
            
            catch(Exception e) {
              //  traceLog("failed to create student record");
                return "Error while creating student record";
            }
    }


    @Override
    public String recordCounts() throws RemoteException, IOException
    {
        // TODO Auto-generated method stub
        this.traceLog(managerID+ "is trying to get record counts");
        DatagramSocket aSocket = null;
        String response = null;
        try {
            aSocket = new DatagramSocket();
            byte[] message = "Record Count".getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(message, message.length, aHost, MTLport);
            aSocket.send(request);
            this.traceLog("Sent request to MTL Server");
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            this.traceLog("Received response from MTL Server");
            response = new String(reply.getData());
            aSocket.close();
            this.traceLog("Connection is closed with MTL Server");
            aSocket = new DatagramSocket();
            request = new DatagramPacket(message, message.length, aHost, DDOPort);
            aSocket.send(request);
            this.traceLog("Sent request to DDO Server");
            byte[] buffer1 = new byte[1000];
            DatagramPacket reply1 = new DatagramPacket(buffer1, buffer1.length);
            aSocket.receive(reply1);
            response = "MTL " +response+ ",LVL " +recordCount+ ",DDO" +reply1.getData();
            this.traceLog("Received response from DDO Server");
            aSocket.close();
            this.traceLog("Connection is closed with DDO Server");
            return response;
             }
        catch(SocketException e) {
            return "Socket:" +e.getMessage();
        }
        catch(IOException e) {
            return "IO:" +e.getMessage();
        }
        finally {
            if(aSocket!=null)
                aSocket.close();
        }
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public String editRecord(String recordID, String fieldName, String newValue) throws RemoteException, IOException,
                                                                                 ParseException
    {
        Iterator tr = recordDetails.entrySet().iterator();
        while(tr.hasNext()) {
            Entry ent = (Entry) tr.next();
            List<Records> recv = (List<Records>) ent.getValue();
                Iterator it = recv.iterator();
                while(it.hasNext()) {
                    Records rec = (Records) it.next();
                    if(fieldName.equalsIgnoreCase("address")) {
                        ((TeacherRecord) rec).setAddress(newValue);
                        return recordID+ "address is changed to" +newValue;
                    }
                    if(fieldName.equalsIgnoreCase("specialization")) {
                        ((TeacherRecord) rec).setSpecialization(newValue);
                        return recordID+ "specialization is changed to" +newValue;
                    }
                    if(fieldName.equalsIgnoreCase("location")) {
                        String c = ((TeacherRecord) rec).getLocation();
                        ((TeacherRecord) rec).setLocation(newValue);
                        if(newValue.equalsIgnoreCase("LVL")) {
                            recordCount++;
                        }
                        if(newValue.equalsIgnoreCase("MTL")) {
                            recordCount--;
                        }
                        else {
                            recordCount--;
                        }
                        return recordID+ "location is changed from " +c+ "to" +newValue;
                    }
                    if(fieldName.equalsIgnoreCase("course")) {
                        String[] val = newValue.split(",");
                        String[] a = ((StudentRecord) rec).getCourse();
                        ((StudentRecord) rec).setCourse(val);
                        return recordID+ "course registeration is changed from " +a+ "to" +newValue;
                    }
                    if(fieldName.equalsIgnoreCase("status")) {
                        ((StudentRecord) rec).setStatus(newValue);
                        String b = ((StudentRecord) rec).getStatus();
                        return recordID+ "status is changed from " +b+ " to" +newValue;
                    }
                    if(fieldName.equalsIgnoreCase("statusdate")) {
                        DateFormat format = new SimpleDateFormat("dd/MM/yy");
                        Date d = format.parse(newValue);
                        ((StudentRecord) rec).setStatusDate(d);
                        return recordID+ "status date is changed to" +newValue;
                    }
                }
        }
         return "error";
     }
    
    public void traceLog(String strlog) throws IOException
    {
        // TODO Auto-generated method stub
        FileWriter fw = new FileWriter(logs,true);
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        fw.write(df.format(d) + ":" +strlog + "\n");
        fw.flush();
        fw.close();
    }
   public static void main(String[] args) {
       LVLServer lvl;
    try
    {
        lvl = new LVLServer();
        Registry registry = LocateRegistry.createRegistry(7875);
        registry.bind("LVLServer" , lvl);
        lvl.traceLog("LVL server started");
        while(true) {
            DatagramSocket socket = new DatagramSocket(1412);
            byte[] buffer = new byte[1000];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);
            String rep = lvl.recordCount+ ",";
            byte[] buffer1 = rep.getBytes();
            DatagramPacket reply = new DatagramPacket(buffer1, buffer1.length, request.getAddress(), request.getPort());
            socket.send(reply);
            socket.close();
            
        }
    }
    catch (IOException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    catch (AlreadyBoundException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       
       
      
   }
}
