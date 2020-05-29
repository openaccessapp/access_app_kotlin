package dahoum.wales.access_app.models;

public class Place {
    private String name;
    private String capacity;
    private int currentCount;
    private String date;
    private String hours;
    private String priority;

    public Place(String name, String capacity, int currentCount, String date, String hours, String priority) {
        this.name = name;
        this.capacity = capacity;
        this.currentCount = currentCount;
        this.date = date;
        this.hours = hours;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
