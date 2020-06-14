package dahoum.wales.access_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Visit {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("visitors")
    @Expose
    private Integer visitors;
    @SerializedName("occupiedSlots")
    @Expose
    private Integer occupiedSlots;
    @SerializedName("maxSlots")
    @Expose
    private Integer maxSlots;
    @SerializedName("slotId")
    @Expose
    private String slotId;
    @SerializedName("placeId")
    @Expose
    private String placeId;
    private int viewType;

    public Visit(String startTime, int viewType) {
        this.startTime = startTime;
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Integer getOccupiedSlots() {
        return occupiedSlots;
    }

    public Integer getMaxSlots() {
        return maxSlots;
    }

    public int getViewType() {
        return viewType;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getPlaceId() {
        return placeId;
    }
}
