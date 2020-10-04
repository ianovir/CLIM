package com.ianovir.clim.models.streams;

/**
 * @author Sebastiano Campisi (ianovir)
 * The default stream printing lines to stdout.
 */
public class SystemOutputStream implements  OutputStream{

    @Override
    public void onOutput(String out) {
        System.out.println(out);
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
