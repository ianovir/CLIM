package com.ianovir.clim.models.streams;

/**
 * @author Sebastiano Campisi (ianovir)
 */
public interface OutputStream extends  Stream{
    void onOutput(String out);
}
