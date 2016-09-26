package com.matie.redgram.data.models.main.items;

/**
 * Created by matie on 17/01/15.
 * Navigation Drawer Item object.
 */
public class UserItem {

    private String id;
    private String userName;
    private boolean isSelected = false;

    public UserItem(String id, String userName) {
        this.id =id;
        this.userName = userName;
    }

    public UserItem(String userName) {
        this.userName = userName;
    }


    public UserItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
