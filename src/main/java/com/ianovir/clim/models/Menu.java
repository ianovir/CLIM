package com.ianovir.clim.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sebastiano Campisi (ianovir)
 * A menu represents a list of entries.
 */
public class Menu {
    private final String name;
    private final Engine engine;
    private final ArrayList<Entry> entries;
    private String description;
    boolean removeOnInvalidChoice = false;
    private boolean isRemoved;
    private String exitText = "exit";
    private String headerSeparator = "-------------";
    private boolean removeOnAction = false; // remove this menu on every action performed
    private Entry.Action exitAction;

    /**
     *
     * @param name the name of the menu
     * @param engine the {{@link Engine}} reference containing the current menu.
     */
    public Menu(String name, Engine engine) {
        this.name = name;
        entries = new ArrayList<>();
        this.engine = engine;
    }

    /**
     *
     * @param name the name of the menu
     * @param exitText the text of the exit/cancel label
     * @param engine the {{@link Engine}} reference containing the current menu.
     */
    public Menu(String name, String exitText, Engine engine) {
        this(name, engine);
        this.exitText = exitText;
    }

    /**
     * Add a new entry at the end of the entries list.
     * @param newEntry the new entry
     */
    public void addEntry(Entry newEntry){
        entries.add(newEntry);
    }

    /**
     * Adds a new Entry at the end of entries list
     * @param eName Name of the new Entry
     * @param eAction Action to be performed by the new entry
     * @return the new added entry
     */
    public Entry addEntry(String eName, Entry.Action eAction){
        Entry result = new Entry(eName, eAction);
        addEntry(result);
        return result;
    }

    /**
     * Add a new menu as sub entry with a custom entry text (different from menu's name)
     * @param submenu The new menu to be added
     * @param entryText the custom entry text if different from menu's name
     */
    public void addSubMenu(Menu submenu, String entryText){
        entries.add(new Entry(entryText, ()-> {
                submenu.isRemoved = false;
                engine.addOnTop(submenu);
            }
        ));
    }

    /**
     * Add a new menu as sub entry
     * @param submenu The new menu to be added
     */
    public void addSubMenu( Menu submenu){
        addSubMenu(submenu, submenu.name);
    }

    /**
     * Forces the call to the action corresponding to the chosen entry
     * @param entry the index of the entry in the menu
     * @return True if an action has been properly triggered, False otherwise
     */
    public boolean onChoice(int entry){

        if(isInvalidEntryChoice(entry)){
            if(isExitChoice(entry) && exitAction!=null) exitAction.doJob();
            isRemoved = removeOnInvalidChoice || isExitChoice(entry);
            return false;
        }

        Entry cEntry = getVisibleEntries().get(entry);
        if(cEntry !=null){
            engine.print(cEntry.getName());
            cEntry.onAction();
            isRemoved = removeOnAction;
            return true;
        }

        return false;
    }

    private boolean isInvalidEntryChoice(int entryIndex) {
        return entryIndex <0 || entryIndex >= getVisibleEntries().size();
    }

    private boolean isExitChoice(int entry) {
        return entry== getVisibleEntries().size();
    }

    /**
     *
     * @return True if the current menu has been removed from the engine, False otherwise
     */
    public boolean isRemoved(){
        return isRemoved;
    }

    /**
     * Retrieves the HUT of the current menu
     * @return the HUT of the menu
     */
    public String getHUT(){
        StringBuilder sb = new StringBuilder();
        if(headerSeparator != null) sb.append("\n").append(headerSeparator).append("\n");

        sb.append(name.toUpperCase()).append("\n");

        if(description!=null && !description.equals("")) sb.append(description).append("\n");

        int mac = 0; //starting index
        for(Entry ma : getVisibleEntries()){
            sb.append(" ").append(mac++).append(". ").append(ma.getName()).append("\n");
        }
        sb.append(" ").append(mac).append(". ").append(exitText).append("\n\n>>");

        return sb.toString();
    }

    private List<Entry> getVisibleEntries() {
        return entries.stream().filter(Entry::isVisible).collect(Collectors.toList());
    }

    public String getHeaderSeparator() {
        return headerSeparator;
    }

    /**
     * Sets a custom Header separator in the menu HUT
     * @param headerSeparator the new separator
     */
    public void setHeaderSeparator(String headerSeparator) {
        this.headerSeparator = headerSeparator;
    }

    @Deprecated
    public int getActionsCount(){
        return getEntriesCount();
    }

    /**
     *
     * @return The count of visible entries in the menu, including the exit entry.
     */
    public int getEntriesCount(){
        return getVisibleEntries().size()+1;
    }

    /**
     *
     * @return The count of entries (visible and not) in the menu, including the exit entry.
     */
    public int getTotalEntriesCount(){
        return entries.size()+1;
    }

    public boolean isRemoveOnAction() {
        return removeOnAction;
    }

    /**
     * Sets the property 'removeOnAction'. If the removeOnAction is True, the current menu will be removed automatically
     * once an entry is chosen.
     * @param removeOnAction True to remove the menu once an entry is chosen, False otherwise
     */
    public void removeOnAction(boolean removeOnAction) {
        this.removeOnAction = removeOnAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     *
     * @param exitAction The action to be performed on exit choice
     */
    public void setExitAction(Entry.Action exitAction){
        this.exitAction = exitAction;
    }

}
