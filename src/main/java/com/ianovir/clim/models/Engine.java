/*
 * Copyright (c) 2020 Sebastiano Campisi - ianovir.com
 */

package com.ianovir.clim.models;

import com.ianovir.clim.models.streams.InputStream;
import com.ianovir.clim.models.streams.OutputStream;
import com.ianovir.clim.models.streams.ScannerInputStream;
import com.ianovir.clim.models.streams.SystemOutputStream;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * @author Sebastiano Campisi (ianovir)
 * The choreographer of the menus' stack.
 * By default, input and output streams are the {{@link ScannerInputStream}} an {{@link SystemOutputStream}} respectively
 */
public class Engine {

    private String name;
    private Menu currentMenu;
    private Stack<Menu> menus;
    private InputStream inStream;
    private OutputStream outStream;
    private boolean running = false;

    /**
     * Menus' choreographer
     * @param name the name of the main engine
     */
    public Engine(String name) {
        this.name = name;
        menus = new Stack<>();
        inStream = new ScannerInputStream(this);
        outStream = new SystemOutputStream();
    }

    /**
     * Adds a menu on the top of the stack
     * @param newMenu the new menu to be added
     */
    public void addOnTop(Menu newMenu){
        menus.push(newMenu);
        currentMenu = menus.peek();
    }

    /**
     * Removes the menu on top
     */
    public void popMenu(){
        if(menus.size()>1){
            menus.pop();
        }
        currentMenu = menus.peek();
    }

    /**
     * Forces the call to the action corresponding to the chosen entry
     * @param entry the index of the entry in the current menu
     */
    public void onChoice(int entry){
        currentMenu.onChoice(entry);
        if(currentMenu.isRemoved()){
            menus.pop();
            currentMenu = menus.empty() ? null: menus.peek();
        }
        //engine stops if no more menus
        if(currentMenu==null)
            stop();
        else
            printHUT();
    }

    /**
     *
     * @return the current menu on top
     */
    public Menu getCurrentMenu() {
        return currentMenu;
    }

    /**
     * Creates a new menu referencing the current engine; Note that the menu won't be added to the stack automatically.
     * @param name the name of the new menu
     * @return the new built menu
     */
    public Menu buildMenu(String name){
        return new Menu(name, this);
    }

    /**
     * Creates a new menu referencing the current engine; Note that the menu won't be added to the stack automatically.
     * @param name the name of the new menu
     * @param exitText the text for the exit text
     * @return the new built menu
     */
    public Menu buildMenu(String name, String exitText){
        return new Menu(name, exitText, this);
    }

    /**
     * Starts the current engine
     */
    public synchronized void start() {
        running = true;
        inStream.open();
        outStream.open();
        present();
        printHUT();
    }

    /**
     * Stops the current engine.
     */
    public synchronized void stop(){
        running = false;
        inStream.close();
        outStream.close();
    }

    /**
     *
     * @return True if the engine is running, False otherwise.
     */
    public synchronized boolean isRunning(){
        return running;
    }

    /**
     *
     * @return The name of the engine.
     */
    public String getName() {
        return name;
    }

    /**
     * Forces the input stream to be read
     * @return the read line
     */
    public String forceRead() {
        return inStream.forceRead();
    }

    /**
     * Sends a message to the output stream
     * @param msg the content to show.
     */
    public void print(String msg) {
        outStream.onOutput(msg);
    }

    /**
     * Sets the input stream
     * @param inStream the new input stream
     */
    public void setInputStream(InputStream inStream) {
        this.inStream = inStream;
    }

    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    private void present() {
        outStream.onOutput(name);
    }

    private void printHUT() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outStream.onOutput(getCurrentMenu().getHUT());
    }
}
