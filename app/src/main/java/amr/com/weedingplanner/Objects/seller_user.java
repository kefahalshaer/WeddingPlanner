package amr.com.weedingplanner.Objects;

import java.io.Serializable;

public class seller_user  implements Serializable {

  private  String Name;
  private  String Email;
  private  String Password;

  public seller_user(String name, String email, String password) {
    Name = name;
    Email = email;
    Password = password;
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

  public String getPassword() {
    return Password;
  }

  public void setPassword(String password) {
    Password = password;
  }
}
