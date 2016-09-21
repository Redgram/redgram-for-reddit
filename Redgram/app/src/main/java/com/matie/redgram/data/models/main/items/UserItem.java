package com.matie.redgram.data.models.main.items;

/**
 * Created by matie on 17/01/15.
 * Navigation Drawer Item object.
 */
public class UserItem {

    private String userName;
    private boolean isSelected = false;

    public UserItem() {
    }

    public UserItem(String userName) {
        this.userName = userName;
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
