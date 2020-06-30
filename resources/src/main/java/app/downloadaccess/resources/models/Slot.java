package app.downloadaccess.resources.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slot {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("occupiedSlots")
    @Expose
    private Integer occupiedSlots;
    @SerializedName("maxSlots")
    @Expose
    private Integer maxSlots;
    @SerializedName("isPlanned")
    @Expose
    private Boolean isPlanned;
    @SerializedName("friends")
    @Expose
    private Integer friends;

    public Slot(String from, int viewType) {
        this.from = from;
        this.viewType = viewType;
    }

    private int viewType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getOccupiedSlots() {
        return occupiedSlots;
    }

    public void setOccupiedSlots(Integer occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
    }

    public Integer getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(Integer maxSlots) {
        this.maxSlots = maxSlots;
    }

    public Boolean getIsPlanned() {
        return isPlanned;
    }

    public void setIsPlanned(Boolean isPlanned) {
        this.isPlanned = isPlanned;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Boolean getPlanned() {
        return isPlanned;
    }

    public void setPlanned(Boolean planned) {
        isPlanned = planned;
    }

    public Integer getFriends() {
        return friends;
    }

    public void setFriends(Integer friends) {
        this.friends = friends;
    }

}
