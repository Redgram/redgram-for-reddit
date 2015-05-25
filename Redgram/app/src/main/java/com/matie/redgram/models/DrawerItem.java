package com.matie.redgram.models;

/**
 * Created by matie on 17/01/15.
 * Navigation Drawer Item object.
 */
public class DrawerItem {
    private String itemName;

    private int itemIcon;

    private boolean mainItem;

    private boolean selected;

    public DrawerItem(String itemName, int itemIcon, boolean mainItem) {
        this.itemName = itemName;
        this.itemIcon = itemIcon;
        this.mainItem = mainItem;
    }

    public DrawerItem(String itemName, boolean mainItem) {
        this(itemName, 0, mainItem);
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public boolean isMainItem() {
        return mainItem;
    }

    public void setMainItem(boolean mainItem) {
        this.mainItem = mainItem;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
