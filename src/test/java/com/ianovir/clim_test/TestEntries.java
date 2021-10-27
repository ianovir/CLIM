package com.ianovir.clim_test;

import com.ianovir.clim.models.Engine;
import com.ianovir.clim.models.Entry;
import com.ianovir.clim.models.Menu;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    public void testNullActionEntry(){
        final int[] sum = {0};
        Engine engine = new Engine("engine name");
        Menu sMenu =  engine.buildMenuOnTop("menu");
        sMenu.addEntry("entry1" , null);

        engine.start();
        try {
            engine.onChoice(0);
            Assert.assertTrue(true);
        } catch(NullPointerException ex) {
            Assert.fail(ex.getMessage());
        }

        engine.stop();
    }

    @Test
    public void testCancelActionAfterHidingEntry(){
        Engine engine = new Engine("engine name");
        Menu sMenu =  engine.buildMenuOnTop("menu");
        Entry hiding = sMenu.addEntry("Entry1" , null);
        sMenu.addEntry("Hide Entry1" , ()->hiding.setVisible(false));

        engine.start();
        try {
            engine.onChoice(1);//hiding entry1
            engine.onChoice(1);//exiting
            Assert.assertTrue(true);
        } catch(IndexOutOfBoundsException ex) {
            Assert.fail(ex.getMessage());
        }

        engine.stop();
    }


    @Test
    public void testCheckMenuWithHiddenEntry(){
        Engine engine = new Engine("engine name");
        Menu sMenu =  engine.buildMenuOnTop("menu");

        Entry hiddenEntry = new Entry("Hidden entry", ()->{});
        hiddenEntry.setVisible(false);
        sMenu.addEntry(hiddenEntry);

        sMenu.addEntry("Another entry" , ()-> {});

        engine.start();
        int noe = sMenu.getEntriesCount();
        engine.stop();

        assertEquals(2, noe);//another entry and exit one
    }

    @Test
    public void testCheckHideEntryWithAnotherAction(){
        Engine engine = new Engine("engine name");
        Menu sMenu =  engine.buildMenuOnTop("menu");

        Entry hiddenEntry = new Entry("Hidden entry", ()->{});
        Entry guiltyEntry = new Entry("Guilty entry", ()->hiddenEntry.setVisible(false));

        sMenu.addEntry(guiltyEntry);
        sMenu.addEntry(hiddenEntry);

        engine.start();
        int noe_before = sMenu.getEntriesCount();
        engine.onChoice(0);
        int noe_after = sMenu.getEntriesCount();
        engine.stop();

        assertEquals(3, noe_before);
        assertEquals(2, noe_after);
    }

    @Test
    public void testCheckShowAndExecuteEntryWithAnotherAction(){
        Engine engine = new Engine("engine name");
        Menu sMenu =  engine.buildMenuOnTop("menu");

        AtomicInteger flag = new AtomicInteger(-1);
        Entry hiddenEntry = new Entry("Hidden entry", ()->flag.set(1));
        hiddenEntry.setVisible(false);
        Entry guiltyEntry = new Entry("Guilty entry", ()->hiddenEntry.setVisible(true));

        sMenu.addEntry(hiddenEntry);
        sMenu.addEntry(guiltyEntry);

        engine.start();
        engine.onChoice(0);//show the hiddenEntry (first)
        engine.onChoice(0);//execute the hiddenEntry
        engine.stop();

        assertEquals(1, flag.get());
    }
}
