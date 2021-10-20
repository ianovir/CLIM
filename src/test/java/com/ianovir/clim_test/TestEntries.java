package com.ianovir.clim_test;

import com.ianovir.clim.models.Engine;
import com.ianovir.clim.models.Menu;
import com.ianovir.clim.models.streams.InputStream;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestEntries {

    @Test
    public void testAddSubtract(){
        final int[] sum = {0};
        Engine engine = new Engine("engine name");
        Menu sMenu =  engine.buildMenuOnTop("sum menu");
        sMenu.addEntry("add 4" , ()-> sum[0] +=4);
        sMenu.addEntry("sub 2" , ()-> sum[0] -=2);

        engine.start();
        engine.onChoice(0);
        engine.onChoice(1);
        engine.stop();

        assertEquals(2, sum[0]);
    }

}
