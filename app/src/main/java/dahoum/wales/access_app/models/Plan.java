package dahoum.wales.access_app.models;

public class Plan {
    private String hourFrom;
    private String hourTo;
    private String placeName;
    private String priorityText;
    int viewType;

    public Plan(String hourFrom, String hourTo, String placeName, String priorityText, int viewType) {
        this.hourFrom = hourFrom;
        this.hourTo = hourTo;
        this.placeName = placeName;
        this.priorityText = priorityText;
        this.viewType = viewType;
    }

    public String getHourFrom() {
        return hourFrom;
    }

    public void setHourFrom(String hourFrom) {
        this.hourFrom = hourFrom;
    }

    public String getHourTo() {
        return hourTo;
    }

    public void setHourTo(String hourTo) {
        this.hourTo = hourTo;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPriorityText() {
        return priorityText;
    }

    public void setPriorityText(String priorityText) {
        this.priorityText = priorityText;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
