package amr.com.weedingplanner.Objects;

import java.util.ArrayList;

public class messages {
    private String ReciverId;
    private String ReciverName;
    private String Message;
    private String Date;


    public messages(String reciverId, String recivername, String message, String date) {
        ReciverId = reciverId;
        ReciverName = recivername;
        Message = message;
        Date = date;
    }


    public String getReciverId() {
        return ReciverId;
    }

    public void setReciverId(String reciverId) {
        ReciverId = reciverId;
    }

    public String getReciverName() {
        return ReciverName;
    }

    public void setReciverName(String reciverName) {
        ReciverName = reciverName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
