import java.util.Date;

public class StudentRecord extends Records
{
    
public String[] courseRegistered;
public String status;
public Date statusDate;
public String recordID;

public StudentRecord(String firstName, String lastName, String[] courseRegistered, String status, Date statusDate, String recordID)
{
 super(firstName, lastName);
  this.courseRegistered = courseRegistered;
  this.status = status;
  this.statusDate = statusDate;
  this.recordID = recordID;
}

public void setRecordID(String id)
{
    recordID = id;
}

public String getRecordID(){
    return recordID;
}

public void setCourse(String[] course) {
    this.courseRegistered = course;
}

public String[] getCourse() {
    return courseRegistered;
}

public void setStatus(String status) {
    this.status = status;
}

public String getStatus() {
    return status;
}

public void setStatusDate(Date date) {
    this.statusDate = date;
}

public Date getStatusDate() {
    return statusDate;
}
}
