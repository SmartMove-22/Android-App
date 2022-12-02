package pt.ua.hackaton.smartmove.recyclers.utils;

import java.time.LocalDateTime;

public class DayOfWeekRecyclerItem {

    private final LocalDateTime localDateTime;
    private boolean isActive;
    private final int activeColorResourceId;
    private final int inactiveColorResourceId;

    public DayOfWeekRecyclerItem(LocalDateTime localDateTime, boolean isActive, int activeColorResourceId, int inactiveColorResourceId) {
        this.localDateTime = localDateTime;
        this.isActive = isActive;
        this.activeColorResourceId = activeColorResourceId;
        this.inactiveColorResourceId = inactiveColorResourceId;
    }

    public int getActiveColorResourceId() {
        return activeColorResourceId;
    }

    public int getInactiveColorResourceId() {
        return inactiveColorResourceId;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public boolean isActive() {
        return isActive;
    }

}
