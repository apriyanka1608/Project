import java.io.IOException;
import java.rmi.Remote;
import java.text.ParseException;
import java.util.Date;

public interface InterfaceRMI extends Remote
{
String createTRecord(String firstName, String lastName, String address, double phoneNumber, String specialization, String location) 
                throws java.rmi.RemoteException, IOException;
String createSRecord(String firstName, String lastName, String[] courseRegistered, String status, Date statusDate)
                throws java.rmi.RemoteException, IOException;
String recordCounts() throws java.rmi.RemoteException, IOException;
String editRecord(String recordID, String fieldName, String newValue)
                throws java.rmi.RemoteException, IOException, ParseException;
}
