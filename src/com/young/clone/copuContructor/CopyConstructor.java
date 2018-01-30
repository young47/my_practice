package com.young.clone.copuContructor;

import java.lang.reflect.Constructor;

/**
 * Created by young on 18/1/2.
 */
public class CopyConstructor {
    public static void ripen(Tomato t) {
// Use the "copy constructor":
        t = new Tomato(t);
        System.out.println("In ripen, t is a " + t.getClass().getName());
    }

    public static void slice(Fruit f) {
        f = new Fruit(f); // Hmmm... will this work?
        System.out.println("In slice, f is a " + f.getClass().getName());
    }

    public static void ripen2(Tomato t) {
        try {
            Class c = t.getClass();
// Use the "copy constructor":
            Constructor ct = c.getConstructor(new Class[]{c});
            Object obj = ct.newInstance(new Object[]{t});
            System.out.println("In ripen2, t is a " + obj.getClass().getName());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void slice2(Fruit f) {
        try {
            Class c = f.getClass();
            Constructor ct = c.getConstructor(new Class[]{c});
            Object obj = ct.newInstance(new Object[]{f});
            System.out.println("In slice2, f is a " + obj.getClass().getName());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Tomato tomato = new Tomato();
        ripen(tomato); // OK
        slice(tomato); // OOPS!
        ripen2(tomato); // OK
        slice2(tomato); // OK
        System.out.println("");
        GreenZebra g = new GreenZebra();
        ripen(g); // OOPS!
        slice(g); // OOPS!
        ripen2(g); // OK
        slice2(g); // OK
        g.evaluate();
    }
}

class FruitQualities {
    private int weight;
    private int color;
    private int firmness;
    private int ripeness;
    private int smell;

    // etc.
    public FruitQualities() { // Default constructor
// Do something meaningful...
    }

    public FruitQualities(FruitQualities f) {
        weight = f.weight;
        color = f.color;
        firmness = f.firmness;
        ripeness = f.ripeness;
        smell = f.smell;
// etc.
    }
}

class Seed {
    // Members...
    public Seed() { /* Default constructor */ }

    public Seed(Seed s) { /* Copy constructor */ }
}

class Fruit {
    private FruitQualities fq;
    private int seeds;
    private Seed[] s;

    public Fruit(FruitQualities q, int seedCount) {
        fq = q;
        seeds = seedCount;
        s = new Seed[seeds];
        for (int i = 0; i < seeds; i++)
            s[i] = new Seed();
    }

    public Fruit(Fruit f) {
        fq = new FruitQualities(f.fq);

        seeds = f.seeds;
        s = new Seed[seeds];
// Call all Seed copy-constructors:
        for (int i = 0; i < seeds; i++)
            s[i] = new Seed(f.s[i]);
// Other copy-construction activities...
    }

    // To allow derived constructors (or other
// methods) to put in different qualities:
    protected void addQualities(FruitQualities q) {
        fq = q;
    }

    protected FruitQualities getQualities() {
        return fq;
    }
}

class ZebraQualities extends FruitQualities {
    private int stripedness;

    public ZebraQualities() { // Default constructor
        super();
// do something meaningful...
    }

    public ZebraQualities(ZebraQualities z) {
        super(z);
        stripedness = z.stripedness;
    }
}

class Tomato extends Fruit {
    public Tomato() {
        super(new FruitQualities(), 100);
    }

    public Tomato(Tomato t) { // Copy-constructor super(t); // Upcast for base copy-constructor // Other copy-construction activities...
        super(t);
    }
}


class GreenZebra extends Tomato {
    public GreenZebra() {
        addQualities(new ZebraQualities());
    }

    public GreenZebra(GreenZebra g) {
        super(g); // Calls Tomato(Tomato) // Restore the right qualities: addQualities(new ZebraQualities());
    }

    public void evaluate() {
        ZebraQualities zq = (ZebraQualities) getQualities();
// Do something with the qualities // ...
    }
}

