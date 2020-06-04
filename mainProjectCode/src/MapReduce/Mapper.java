package MapReduce;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Abstract implementation of a threadable MAP-capable object. Built off the implementation by Ari Trachtenberg in EC 504
 */

public abstract class Mapper implements Callable<Mapper.MapperEmission> {

    // default constructor
    protected Mapper() { }

    public void init(String theValue) {
        value = theValue;
    }

    public static class MapperEmission extends EmissionTemplate {
        public MapperEmission(String Content, List<String> Links) {
            super(Content, Links);
        }
    }

    @Override
    abstract public MapperEmission call();

    // ... STATIC METHODS
    public static void addTask(Mapper task) {
        Mapper.tasks.add(task);
    }

    // STATIC FIELDS
    public static final List<Mapper> tasks = new ArrayList<>();                  // all the tasks to be executed
    public static List<Future<MapperEmission>> futures = new ArrayList<>();  // results of task executions

    // FIELDS
    protected String value = null;  // The value provided when constructing this object.
}
