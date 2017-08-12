package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 11/8/17.
 */

public class DisplayEvent {
    public static final int Header_TYPE = 0;
    public static final int EVENT_TYPE = 1;
    private String heading;
    private HomeDisplay mDescription;
    private int mType;
    public DisplayEvent(String name, HomeDisplay description, int type) {
        this.heading = name;
        this.mDescription = description;
        this.mType = type;
    }
    public String getName() {
        return heading;
    }
    public void setName(String name) {
        this.heading = name;
    }
    public HomeDisplay getDescription() {
        return mDescription;
    }
    public void setDescription(HomeDisplay description) {
        this.mDescription = description;
    }
    public int getType() {
        return mType;
    }
    public void setType(int type) {
        this.mType = type;
    }
}
