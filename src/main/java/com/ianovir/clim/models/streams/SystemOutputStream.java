package com.ianovir.clim.models.streams;

/**
 * @author Sebastiano Campisi (ianovir)
 * The default stream printing lines to stdout.
 */
public class SystemOutputStream implements  OutputStream{

    @Override
    public void put(String output) {
        System.out.println(output);
    }

    @Override
    public void open() {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }
}
