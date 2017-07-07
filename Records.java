
public class Records
{
public String firstName;
public String lastName;

public Records() {
    
}

public Records(String fname, String lname) {
    this.firstName = fname;
    this.lastName = lname;
}

public void setFirstName(String fname) {
    this.firstName = fname;
}

public String getFirstName() {
    return firstName;
}

public void setLastName(String lname) {
    this.lastName = lname;
}

public String getLastName() {
    return lastName;
}

}
