package dahoum.wales.access_app.models;

public class Slot {
    private String hourFromTo;
    private String priorityText;
    private String capacity;
    private String shortDate;
    private String date;
    int viewType;

    public Slot(String hourFromTo, String priorityText, String capacity, String shortDate, String date, int viewType) {
        this.hourFromTo = hourFromTo;
        this.priorityText = priorityText;
        this.capacity = capacity;
        this.shortDate = shortDate;
        this.date = date;
        this.viewType = viewType;
    }

    public String getHourFromTo() {
        return hourFromTo;
    }

    public void setHourFromTo(String hourFromTo) {
        this.hourFromTo = hourFromTo;
    }

    public String getPriorityText() {
        return priorityText;
    }

    public void setPriorityText(String priorityText) {
        this.priorityText = priorityText;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getShortDate() {
        return shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
