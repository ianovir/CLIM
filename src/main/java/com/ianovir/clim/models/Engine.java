/*
 * Copyright (c) 2020 Sebastiano Campisi - ianovir.com
 */

package com.ianovir.clim.models;

import com.ianovir.clim.models.streams.InputStream;
import com.ianovir.clim.models.streams.OutputStream;
import com.ianovir.clim.models.streams.ScannerInputStream;
import com.ianovir.clim.models.streams.SystemOutputStream;

import java.util.Stack;
import java.util.concurrent.CountDownLatch;

/**
 * @author Sebastiano Campisi (ianovir)
 * The choreographer of the menus' stack.
 * By default, input and output streams are the {{@link ScannerInputStream}} an {{@link SystemOutputStream}} respectively
 */
public class Engine {

    private static final long WAIT_LOOP_MS = 500;
    private final String name;
    private final Stack<Menu> menus;
    private Menu currentMenu;
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
        manageMenuRemoval();
        stopOrContinue();
    }

    private void stopOrContinue() {
        if(currentMenu==null){
            stop();
        }
        else{
            printHUT();
        }
    }

    private void manageMenuRemoval() {
        if(currentMenu.isRemoved()){
            menus.pop();
            currentMenu = menus.empty() ? null: menus.peek();
        }
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
    @Deprecated
    public Menu buildMenu(String name){
        return new Menu(name, this);
    }

    /**
     * Creates a new menu referencing the current engine; Note that the menu won't be added to the stack automatically.
     * @param name the name of the new menu
     * @param exitText the text for the exit text
     * @return the new built menu
     */
    @Deprecated
    public Menu buildMenu(String name, String exitText){
        return new Menu(name, exitText, this);
    }

    /**
     * Creates a new menu referencing the current engine; new menu will be added to the top of stack automatically.
     * @param name the name of the new menu
     * @return the new built menu
     */
    public Menu buildMenuOnTop(String name){
        Menu m = new Menu(name, this);
        addOnTop(m);
        return m;
    }

    public Menu buildMenuOnTop(String name, String exitText){
        Menu m = new Menu(name, exitText, this);
        addOnTop(m);
        return m;
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
     * Forces the input stream to be read
     * @param msg Message to print before read
     * @return the read line
     */
    public String forceRead(String msg) {
        outStream.put(msg);
        return inStream.forceRead();
    }

    /**
     * Sends a message to the output stream
     * @param msg the content to show.
     */
    public void print(String msg) {
        outStream.put(msg);
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
        outStream.put(name);
    }

    private void printHUT() {
        if(currentMenu!=null){
            outStream.put(currentMenu.getHUT());
        }
    }

    public int getMenuCount(){
        return menus.size();
    }

    /**
     * Waits for the engine to complete its execution (blocking)
     * @throws InterruptedException
     */
    public void await() throws InterruptedException {
        while(isRunning()){
            Thread.sleep(WAIT_LOOP_MS);
        }
    }

}
