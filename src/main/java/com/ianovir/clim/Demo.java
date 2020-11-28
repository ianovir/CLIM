package com.ianovir.clim;

import com.ianovir.clim.models.Engine;
import com.ianovir.clim.models.Menu;
import com.ianovir.clim.models.Entry;

import java.util.LinkedList;

public class Demo {

    /**
     * Sample demo showing how to handle a list of strings with CLIM
     */
    public static void main(String[] args) {

        Engine engine = new Engine("CLIM DEMO");

        LinkedList<String> values = new LinkedList<>();

        Menu mainMenu = engine.buildMenu("Main menu");
        mainMenu.setDescription("List of Strings example...");

        mainMenu.addEntry(new Entry("List values ") {
            @Override
            public void onAction() {
                if(!values.isEmpty()){
                    engine.print("Values:");
                    int v = 0;
                    for(String s : values) engine.print(v++  + ": " +s);
                    engine.print("--end of list--");
                }else{
                    engine.print("--list empty--");
                }
            }
        });

        mainMenu.addEntry(new Entry("Add value") {
            @Override
            public void onAction() {
                engine.print("Type new value:");
                String newVal = engine.forceRead();

                //building a nested menu:
                Menu addMenu = engine.buildMenu("'Add' option", "back");
                addMenu.setDescription("Choose insert position:");

                addMenu.addEntry(new Entry("Add First") {
                    @Override
                    public void onAction() {
                        values.addFirst(newVal);
                    }
                });

                addMenu.addEntry(new Entry("Add Last") {
                    @Override
                    public void onAction() {
                        values.addLast(newVal);
                    }
                });

                addMenu.addEntry(new Entry("Add at index...") {
                    @Override
                    public void onAction() {
                        try{
                            engine.print("Add index: ");
                            int index = Integer.parseInt(engine.forceRead());
                            values.add(index, newVal);
                        }catch(Exception e){engine.print("Failed!");}
                    }
                });

                addMenu.removeOnAction(true);
                engine.addOnTop(addMenu);
            }
        });

        mainMenu.addEntry(new Entry("Remove value") {
            @Override
            public void onAction() {
                try{
                    engine.print("Remove index: ");
                    int index = Integer.parseInt(engine.forceRead());
                    values.remove(index);
                }catch(Exception e){engine.print("Failed!");}
            }
        });

        Menu secondMenu = engine.buildMenu("Second menu", "cancel");

        secondMenu.addEntry(new Entry("Pop this menu") {
            @Override
            public void onAction() {
                engine.popMenu();
            }
        });

        secondMenu.addEntry(new Entry("Another action") {
            @Override
            public void onAction() {
                //do nothing
            }
        });

        mainMenu.addSubMenu(secondMenu);
        engine.addOnTop(mainMenu);
        engine.start();

        //blocking:
        while(engine.isRunning()){}

    }
}
