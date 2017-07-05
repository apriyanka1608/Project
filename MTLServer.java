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
import java.util.Date;

@SuppressWarnings("serial")
public class MTLServer extends UnicastRemoteObject implements InterfaceRMI {

    private File logs = null;
    private int recordCount =0;
    private int baseID = 10000;
    private String recordID;
    private int LVLport = 7875;
    private int DDOPort = 7825;
    private String managerID;
    
    public MTLServer() throws IOException {
        logs = new File("MTLlog.txt");
        if(!logs.exists()) {
            logs.createNewFile();
        }
        else
            if(logs.delete())
                logs.createNewFile();
    }


    @Override
    public String createTRecord(String firstName, String lastName, String address, double phoneNumber,
            String specialization, String location) throws RemoteException, IOException
    {
        // TODO Auto-generated method stub
        if(location.equalsIgnoreCase("MTL"))
                recordCount++;
        if(location.equalsIgnoreCase("LVL")) {
            
        }
        else {
            
        }
        
        try {
            recordID = "TR" + baseID;
            baseID++;
            this.traceLog("Teacher Record Created Successfully");
            return "Teacher Record Created Succesfully" +recordID;
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
        // TODO Auto-generated method stub
        return null;
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
            DatagramPacket request = new DatagramPacket(message, message.length, aHost, LVLport);
            aSocket.send(request);
            this.traceLog("Sent request to LVL Server");
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            this.traceLog("Received response from LVL Server");
            response = new String(reply.getData());
            aSocket.close();
            this.traceLog("Connection is closed with LVL Server");
            aSocket = new DatagramSocket();
            request = new DatagramPacket(message, message.length, aHost, DDOPort);
            aSocket.send(request);
            this.traceLog("Sent request to DDO Server");
            byte[] buffer1 = new byte[1000];
            DatagramPacket reply1 = new DatagramPacket(buffer1, buffer1.length);
            aSocket.receive(reply1);
            response = "MTL " +recordCount+ ",LVL " +response+ ",DDO" +reply1.getData();
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


    @Override
    public String editRecord(String recordID, String fieldName, String newValue) throws RemoteException, IOException,
                                                                                 ParseException
    {
        // TODO Auto-generated method stub
        return null;
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
       MTLServer mtl;
    try
    {
        mtl = new MTLServer();
        Registry registry = LocateRegistry.createRegistry(1412);
        registry.bind("MTLServer" , mtl);
        mtl.traceLog("MTL server started");
        while(true) {
            DatagramSocket socket = new DatagramSocket(1412);
            byte[] buffer = new byte[1000];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);
            String rep = "MTL " +mtl.recordCount+ ",";
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
