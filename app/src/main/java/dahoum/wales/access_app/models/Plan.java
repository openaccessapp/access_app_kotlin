package dahoum.wales.access_app.models;

public class Plan {
    private String placeName;
    private String priorityText;
    int viewType;

    public Plan(String placeName, String priorityText, int viewType) {
        this.placeName = placeName;
        this.priorityText = priorityText;
        this.viewType = viewType;
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
