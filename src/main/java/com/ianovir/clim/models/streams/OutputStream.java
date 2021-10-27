package com.ianovir.clim.models.streams;

/**
 * @author Sebastiano Campisi (ianovir)
 * OutputStream for the @{@link com.ianovir.clim.models.Engine}
 */
public interface OutputStream extends  Stream{
    void put(String output);
}
