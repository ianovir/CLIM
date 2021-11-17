CLIM
=======

Command line interface menu (CLIM) engine for JVM offers a very simple system to manage UI menus via the cli.

![p1](https://github.com/ianovir/CLIM/blob/master/pics/ctrl_console.jpg)

# Motivation

CLIM may be useful when prototyping core libraries in their early stage, in absence of advanced GUI yet. For example, when you are developing core component for multi-platform environments and you need to use or distribute it in its early stage.

# Download

You can download the last compiled version of `CLIM` from the [releases](https://github.com/ianovir/CLIM/releases) page, or import it from maven bintray.

# Usage

See the `Demo.java` for a simple example.

## Overview

The main components of CLIM are:
* Engine
* Menu
* Entry
* Streams

### Engine
`Engine` is the main component organizing menus, printing to `OutputStream` and reading from `InputStream`. 
To create a new engine use its constructor:
```java
Engine engine = new Engine("CLIM DEMO");
```

To start the engine:
```java
engine.start();
```

To wait for the engine to terminate its execution (blocking call):
```java
engine.await();
```

### Menu
A `Menu` is a list of entries.

The simplest way to create a menu is by using the engine's  method `buildMenu()`:
```java
Menu mainMenu = engine.buildMenu("Main menu");
```
this will create a new menu but it won't add it on the top of the engine.

Add as many entries as you want:
```java
mainMenu.addEntry(newEntry);
```

Or add a sub-menu as entry:
```java
Menu secondMenu = new Menu("Second menu", "cancel", engine);
//...
mainMenu.addSubMenu(secondMenu);
```

finally add the menu to the engine:
```java
engine.addOnTop(mainMenu);
```

To create a menu and add it on top automatically, use:
```java
Menu myMenu = engine.buildMenuOnTop("My Menu");
```


### Entry
An `Entry` represents an option the user can choose. You need to implement the `onAction()` abstract method of an entry in order to specify its action.

To create an Entry use the constructor:
```java
Entry newEntry = new Entry("New Entry", ()->{/*do stuff...*/} );
```

or use the wrapping method from menu object:
```java
Entry newEntry = myMenu.addEntry("New Entry", ()->{/*do stuff...*/} );
```
Change Entry's visibility to hide/show it on menu list.

If you want to create an entry to show a sub-menu, just call the `addSubMenu()`:
```java
Menu subMenu = ...
myMenu.addSubMenu(subMenu);
```
an entry with the name of `subMenu` will be shown in `myMenu`.

### Streams
CLIM's `Stream` is an object used for streaming input and output data. By default, the `Engine` uses `ScannerInputStream` and `SystemOutputStream` which uses the `System.in` and `System.out` streams. You can define your custom streams by implementing the `InputStream` and `OutputStream` classes.

It is possible to assign custom streams to the engine:
```java
engine.setInputStream(customInputStream);
engine.setOutStream(customOutputStream);
```

# Copyright
Copyright(c) 2021 Sebastiano Campisi - [ianovir.com](https://ianovir.com). 
Read LICENSE file for more details.