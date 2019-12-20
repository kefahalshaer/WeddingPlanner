package amr.com.weedingplanner.Objects;

public class ToDoItem {

    private String Name;
    private String Description;
    private String LastDate;
    private boolean Done;


    public ToDoItem(String name, String description, String lastDate, boolean done) {
        Name = name;
        Description = description;
        LastDate = lastDate;
        Done = done;
    }

    public ToDoItem(String description, String lastDate, boolean done) {
        Description = description;
        LastDate = lastDate;
        Done = done;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }

    public boolean isDone() {
        return Done;
    }

    public void setDone(boolean done) {
        Done = done;
    }
}
