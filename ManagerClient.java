import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ManagerClient
{
    public static String location;
    public static String managerID;
    public static File logs;
    static Registry registry;
    static InterfaceRMI server = null;
    static String firstName, lastName, address, specialization, teacherLocation, status, fieldName, newValue;
    static double phone;
    static String[] course;
    static Date statusDate;
    static int port;
    static long baseID = 10000;
    static String recordID;
    
public ManagerClient() throws IOException {
    logs = new File("MTLlog.txt");
    if(!logs.exists()) {
        logs.createNewFile();
    }
    else
        if(logs.delete())
            logs.createNewFile(); 
}

@SuppressWarnings("resource")
public static void main(String[] args) throws ParseException, NotBoundException, IOException {
    System.out.println("Welcome");
    System.out.println("Enter manager ID : ");
    Scanner in = new Scanner(System.in);
    managerID = in.next();
 //   traceLog("Manager ID :" +managerID);
    String serverLocation = managerID.substring(0, 3).toUpperCase();
    switch (serverLocation.toUpperCase()) {
        case "MTL" :
            registry = LocateRegistry.getRegistry(1412);
            server = (InterfaceRMI)registry.lookup(serverLocation);
            break;
        case "LVL" :
            registry = LocateRegistry.getRegistry(7875);
            server = (InterfaceRMI)registry.lookup(serverLocation);
            break;
        case "DDO" :
            registry = LocateRegistry.getRegistry(7825);
            server = (InterfaceRMI)registry.lookup(serverLocation);
            break;
        default :
            System.out.println("invalid manager id");
            break;
    }
    int choice = 0;
    while(choice<5) {
    System.out.println("1.Create Teacher Record");
    System.out.println("2.Create Student Record");
    System.out.println("3.Get Record Counts");
    System.out.println("4.Edit Record");
    System.out.println("5.Exit");
    Scanner in1 = new Scanner(System.in);
    choice = in1.nextInt();
    switch(choice) {
        case 1 :
        Scanner in2 = new Scanner(System.in);
        System.out.println("Enter First Name : ");
        firstName = in2.next();
        System.out.println("Enter Last Name : ");
        lastName = in2.next();
        System.out.println("Enter Address : ");
        address = in2.next();
        System.out.println("Enter Phone Number : ");
        String ph = in2.next();
        phone = Double.parseDouble(ph);
        System.out.println("Enter Specialization : ");
        specialization = in2.next();
        System.out.println("Enter Location : ");
        teacherLocation = in2.next();
        createTRecord(firstName, lastName, address, phone, specialization, teacherLocation);
        break;
        case 2 :
            Scanner in3 = new Scanner(System.in);
            System.out.println("Enter First Name : ");
            firstName = in3.next();
            System.out.println("Enter Last Name : ");
            lastName = in3.next();
            System.out.println("Enter Course Registration : ");
            String cou = in3.next();
            course = cou.split(",");
            System.out.println("Enter Status : ");
            status = in3.next();
            System.out.println("Enter Status Date : ");
            String da = in3.next();
            DateFormat format = new SimpleDateFormat("dd/MM/yy");
            statusDate = format.parse(da);
            createSRecord(firstName, lastName, course, status, statusDate);
            break;
        case 3 :
            recordCount();
            break;
        case 4 :
            Scanner in4 = new Scanner(System.in);
            System.out.println("Enter Record ID : ");
            String recid = in4.next();
            if((recid.substring(0, 2)).equalsIgnoreCase("TR")) {
                System.out.println("Enter Field Name (Address, Specialization, Location); ");
                fieldName = in4.next();
                System.out.println("Enter new Value : ");
                newValue = in4.next();
            }
            if((recid.substring(0, 2)).equalsIgnoreCase("SR")){
            System.out.println("Enter Field Name (Course, Status, Statusdate ) : ");
            fieldName = in4.next();
            System.out.println("Enter new Value : ");
            newValue = in4.next();
            }
            else
            {
                System.out.println("Enter correct ID");
            }
            editRecord(recid, fieldName, newValue);
            break;
    }
    } 
}
private static void editRecord(String recordid, String fieldname, String newvalue) throws IOException, RemoteException, NotBoundException, ParseException
{
    // TODO Auto-generated method stub
    String strStatus = server.editRecord(recordid, fieldname, newvalue);
    System.out.println(strStatus);
    traceLog(strStatus);
    traceLog(recordid + fieldname + newvalue);
    
}
private static void recordCount() throws IOException, NotBoundException
{
    // TODO Auto-generated method stub
    String strStatus = server.recordCounts();
    System.out.println(strStatus);
    traceLog("Record Count" +strStatus);
}

public String getManagerID() {
    return managerID;
}

public static void setRecordID(String b) {
    recordID = b + baseID;
    baseID++;
}

public static String getRecordID() {
    return recordID;
}

private static void createTRecord(String fname, String lname, String address, double phone, String spec, String loc) throws RemoteException,IOException, NotBoundException {

    String strStatus = server.createTRecord(fname, lname, address, phone, spec, loc);
    traceLog(strStatus);
    traceLog(fname+ lname + address + phone + spec + loc);
    System.out.println(strStatus);
}

private static void createSRecord(String fname, String lname, String[] course, String status, Date statusDate) throws RemoteException,IOException, NotBoundException {
 
    String strStatus = server.createSRecord(fname, lname, course, status, statusDate);
    traceLog(strStatus);
    traceLog(fname + lname + course + status + statusDate);
    System.out.println(strStatus);
}

public static void traceLog(String strlog) throws IOException
{
    // TODO Auto-generated method stub
    FileWriter fw = new FileWriter(logs,true);
    Date d = new Date();
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    fw.write(df.format(d) + ":" +strlog + "\n");
    fw.flush();
    fw.close();
}

}
