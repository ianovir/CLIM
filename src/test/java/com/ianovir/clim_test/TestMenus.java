package com.ianovir.clim_test;

import com.ianovir.clim.models.Engine;
import com.ianovir.clim.models.Menu;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestMenus {

    @Test
    public void testAddMenuByEntry(){
        Engine engine = new Engine("engine name");
        Menu menu1 =  new Menu("menu1", engine);
        Menu menu2 =  new Menu("menu2", engine);

        menu1.addEntry("add menu2 on top" , ()-> engine.addOnTop(menu2));
        engine.addOnTop(menu1);

        engine.start();
        engine.onChoice(0);
        engine.stop();

        assertEquals(2, engine.getMenuCount());
    }

    @Test
    public void testRemoveMenuByEntry(){
        Engine engine = new Engine("engine name");
        Menu menu1 =  new Menu("menu1", engine);
        Menu menu2 =  new Menu("menu2", engine);

        menu1.addEntry("pop menu on top" , engine::popMenu);

        engine.addOnTop(menu1);
        engine.addOnTop(menu2);

        engine.start();
        engine.onChoice(0);
        engine.stop();

        assertEquals(1, engine.getMenuCount());
    }

    @Test
    public void testPersistenceMenuOnAction(){
        Engine engine = new Engine("engine name");
        Menu menu1 =  new Menu("menu1", engine);
        Menu subMenu =  new Menu("sub menu", engine);

        subMenu.addEntry("placeholder action" , ()-> { });

        menu1.addSubMenu(subMenu);

        engine.addOnTop(menu1);

        engine.start();
        engine.onChoice(0);//choose sub menu (auto-entry)
        engine.onChoice(0);//choose sub menu's entry 0
        engine.stop();

        //submenu won't be removed
        assertEquals(2, engine.getMenuCount());
    }

    @Test
    public void testRemoveMenuOnAction(){
        Engine engine = new Engine("engine name");
        Menu menu1 =  new Menu("menu1", engine);
        Menu subMenu =  new Menu("sub menu", engine);

        subMenu.removeOnAction(true);
        subMenu.addEntry("placeholder action" , ()-> { });

        menu1.addSubMenu(subMenu);

        engine.addOnTop(menu1);

        engine.start();
        engine.onChoice(0);//choose sub menu (auto-entry)
        engine.onChoice(0);//choose sub menu's entry 0
        engine.stop();

        assertEquals(1, engine.getMenuCount());
    }

}
