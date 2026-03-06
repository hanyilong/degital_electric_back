package com.device.manage.entity;

public class DeviceStatic {
    int totalCount;
    int onlineCount;
    int offlineCount;
    int repairingCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getOfflineCount() {
        return offlineCount;
    }

    public void setOfflineCount(int offlineCount) {
        this.offlineCount = offlineCount;
    }

    public int getRepairingCount() {
        return repairingCount;
    }

    public void setRepairingCount(int repairingCount) {
        this.repairingCount = repairingCount;
    }
}
