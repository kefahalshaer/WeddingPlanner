package amr.com.weedingplanner.Objects;


public class user {

    private  String Name;
    private  String Email;
    private  String Type;
    private  String PhoneNumber;

    public user(String name, String email, String Type) {
        Name = name;
        Email = email;
        this.Type = Type;
    }

   public user(String name, String email, String Type,String PhoneNumber) {
        Name = name;
        Email = email;
       this.Type = Type;
        this.PhoneNumber =PhoneNumber;
   }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

}