package com.ianovir.clim;

import com.ianovir.clim.models.Engine;
import com.ianovir.clim.models.Menu;
import com.ianovir.clim.models.Entry;

import java.util.LinkedList;

public class Demo {

    public static void main(String[] args) {

        Engine engine = new Engine("CLIM DEMO");

        //EXAMPLE: handle a list
        LinkedList<String> values = new LinkedList<>();

        // Building menus (nested)
        Menu mainMenu = engine.buildMenu("Main menu");

        mainMenu.addEntry(new Entry("List values ") {
            @Override
            public void onAction() {
                int v = 0;
                engine.print("Values:");
                for(String s : values) engine.print(v++  + ": " +s);
                engine.print("--FINISH--\n");
            }
        });

        mainMenu.addEntry(new Entry("Add value") {
            @Override
            public void onAction() {
                engine.print("Type new value:");
                String newVal = engine.forceRead();

                Menu addMenu = engine.buildMenu("Add option", "back");
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
        //secondMenu.removeOnAction(true);
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

        // Assigning main menu to engine
        engine.addOnTop(mainMenu);

        engine.start();

        while(engine.isRunning()){}

    }
}
