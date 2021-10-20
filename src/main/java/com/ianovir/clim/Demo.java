package com.ianovir.clim;

import com.ianovir.clim.models.Engine;
import com.ianovir.clim.models.Menu;
import com.ianovir.clim.models.Entry;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class Demo {

    private static CountDownLatch latch;

    /**
     * Sample demo showing how to handle a list of strings with CLIM
     */
    public static void main(String[] args) {

        initLatch();

        Engine engine = new Engine("CLIM DEMO");

        LinkedList<String> values = new LinkedList<>();
        Menu mainMenu = engine.buildMenuOnTop("Main menu");
        mainMenu.setDescription("List of Strings example...");

        //create new entry by using its constructor...
        Entry entry1 = new Entry("List values ",
            ()-> {
                if(!values.isEmpty()){
                    engine.print("Values:");
                    int v = 0;
                    for(String s : values) engine.print(v++  + ": " +s);
                    engine.print("--end of list--");
                }else{
                    engine.print("--list empty--");
                }
            }
        );

        mainMenu.addEntry(entry1);

        //... or by using the wrapped way provided by Menu
        mainMenu.addEntry("Add value", ()-> {
                engine.print("Type new value:");
                String newVal = engine.forceRead();

                //building a nested menu:
                Menu addMenu = engine.buildMenuOnTop("'Add' option", "back");
                addMenu.setDescription("Choose insert position:");

                addMenu.addEntry("Add First", ()-> values.addFirst(newVal));

                addMenu.addEntry("Add Last", () -> values.addLast(newVal));

                addMenu.addEntry("Add at index...", ()-> {
                        try{
                            engine.print("Add index: ");
                            int index = Integer.parseInt(engine.forceRead());
                            values.add(index, newVal);
                        }catch(Exception e){engine.print("Failed!");}
                    }
                );

                addMenu.removeOnAction(true);
                engine.addOnTop(addMenu);
                    });

        mainMenu.addEntry("Remove value", ()-> {
                try{
                    engine.print("Remove index: ");
                    int index = Integer.parseInt(engine.forceRead());
                    values.remove(index);
                }catch(Exception e){engine.print("Failed!");}
            }
        );

        Menu secondMenu = engine.buildMenuOnTop("Second menu", "cancel");
        secondMenu.addEntry("Pop this menu", engine::popMenu);
        secondMenu.addEntry("Another action", ()->{/*do nothing*/});

        mainMenu.addSubMenu(secondMenu);
        mainMenu.setExitAction(()->latch.countDown());//releasing latch (count=1)

        engine.start();


        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void initLatch() {
        //using cd latch to block the thread while the engine is running
        //to avoid "while(engine.isRunning()){}"
        latch = new CountDownLatch(1);
    }
}
