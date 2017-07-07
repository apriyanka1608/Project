
public class TeacherRecord extends Records
{
    public String address;
    public double phone;
    public String specialization;
    public String location;
    public String recordID;
    
    public TeacherRecord() {
        
    }
    
    public TeacherRecord(String fname, String lname, String address, double phone, String spec, String loc, String id) {
        super(fname, lname);
        this.address = address;
        this.phone = phone;
        this.specialization = spec;
        this.location = loc;
        this.recordID = id;
    }
    
    public void setRecordID(String id)
    {
        recordID = id;
    }

    public String getRecordID(){
        return recordID;
    }

    public void setAddress(String add) {
        this.address = add;
    }

    public String getAddress() {
        return address;
    }

    public void setPhone(double ph) {
        this.phone = ph;
    }

    public double getPhone() {
        return phone;
    }

    public void setSpecialization(String spec) {
        this.specialization = spec;
    }

    public String getSpecialization() {
        return specialization;
    }
    
    public void setLocation(String loc) {
        this.location = loc;
    }
    
    public String getLocation() {
        return location;
    }
}
