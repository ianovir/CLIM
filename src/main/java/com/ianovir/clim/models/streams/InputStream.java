package com.ianovir.clim.models.streams;

import com.ianovir.clim.models.Engine;

import java.util.Scanner;


/**
 * @author Sebastiano Campisi (ianovir)
 * Input Stream for the @{@link Engine}
 */
public abstract class InputStream implements Stream {

    protected final Engine engine;
    Boolean run = false;
    Thread t ;

    /**
     *
     * @param subscriber the main subscriber to the current input stream
     */
    public InputStream(Engine subscriber){
        this.engine = subscriber;
        t= new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    onInput(forceRead());
                }
            }
        });
        t.setDaemon(true);
    }

    /**
     * Forces the choice of a menu entry
     * @param entryIndex the index of the chosen index (must be a numeric)
     */
    public void onInput(String entryIndex){
        if(entryIndex==null  || entryIndex.isEmpty()) return;

        int ch = -1;
        try{
            ch = Integer.parseInt(entryIndex);
        }catch(NumberFormatException ex){
            ex.printStackTrace();
        }
        onInput(ch);
    }

    /**
     * Forces the choise of a menu entry
     * @param entryIndex the index of the chosen index
     */
    public void onInput(Integer entryIndex){
        engine.onChoice(entryIndex);
    }

    /**
     * Forces the read from the stream. Please, consider that this call will be called on separate thread. This call
     * can be supposed to be blocking.
     * @return the
     */
    public abstract String forceRead();

    /**
     * Opens the current stream
     */
    @Override
    public void open() {
        if(t.isAlive()) return;
        run = true;
        t.start();
    }

    /**
     * Closes the current stream
     */
    @Override
    public void close() {
        run = false;
    }
}
