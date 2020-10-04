package com.ianovir.clim.models;


/**
 * @author Sebastiano Campisi (ianovir)
 * An Entry is the basic element composing a menu.
 */
public abstract class Entry {
    private String name;
    private boolean isVisible;

    /**
     *
     * @param name The name of the entry, that is the text displayed on the menu the entry belongs to.
     */
    public Entry(String name) {
        this.name = name;
    }

    public abstract void onAction();

    public String getName() {
        return name;
    }

    /**
     *
     * @return True (Default) if the entry is visible on the menu, False otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     *
     * @param visible True (Default) if the entry must be visible on the menu, False otherwise
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
