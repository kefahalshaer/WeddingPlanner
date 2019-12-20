package amr.com.weedingplanner.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class message implements Parcelable {
    String Sender;
    String Reciver;
    String Message;
    String Date;

    public message(String sender, String reciver, String message, String date) {
        Sender = sender;
        Reciver = reciver;
        Message = message;
        Date = date;
    }

    protected message(Parcel in) {
        Sender = in.readString();
        Reciver = in.readString();
        Message = in.readString();
        Date = in.readString();
    }

    public static final Creator<message> CREATOR = new Creator<message>() {
        @Override
        public message createFromParcel(Parcel in) {
            return new message(in);
        }

        @Override
        public message[] newArray(int size) {
            return new message[size];
        }
    };

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReciver() {
        return Reciver;
    }

    public void setReciver(String reciver) {
        Reciver = reciver;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Sender);
        parcel.writeString(Reciver);
        parcel.writeString(Message);
        parcel.writeString(Date);

    }
}
