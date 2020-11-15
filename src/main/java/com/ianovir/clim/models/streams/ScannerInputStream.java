package com.ianovir.clim.models.streams;

import com.ianovir.clim.models.Engine;

import java.util.Scanner;

/**
 * @author Sebastiano Campisi (ianovir)
 * The default input stream getting inputs from the standard system input.
 */
public class ScannerInputStream extends InputStream {
    private Scanner sc;

    public ScannerInputStream(Engine subscriber) {
        super(subscriber);
        sc  = new Scanner(System.in);
    }

    @Override
    public String forceRead() {
        return sc.nextLine();
    }

}
