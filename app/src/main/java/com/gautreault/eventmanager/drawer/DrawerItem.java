package com.gautreault.eventmanager.drawer;

/**
 * Simple item to display an icon followed with a string name.
 */
public class DrawerItem {

    /**
     * The icon of the item.
     */
    private final int icon;

    /**
     * The name of the item.
     */
    private final String name;

    /**
     * Constructor, custom item with a label icon + name
     * @param icon : the icon of the item
     * @param name : the name of the item
     */
    public DrawerItem(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    /**
     * Getter for the icon.
     * @return the icon of the item
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Getter for the name.
     * @return the name of the item
     */
    public String getName() {
        return name;
    }
}