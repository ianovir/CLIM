package com.ianovir.clim.models;


/**
 * @author Sebastiano Campisi (ianovir)
 * An Entry is the basic element composing a menu.
 */
public  class Entry {
    private final Action action;
    private final String name;
    private boolean isVisible;

    @Deprecated
    public Entry(String name) {
        this(name, null);
    }

    /**
     *
     * @param name the text listed on the menu the entry belongs to.
     * @param action action to be performed when the entry is chosen
     */
    public Entry(String name, Action action) {
        this.name = name;
        isVisible = true;
        this.action = action;
    }

    public void onAction(){
        if(action!=null) action.doJob();
    }

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

    @FunctionalInterface
    public interface Action{
        void doJob();
    }

}
