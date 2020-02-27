import java.util.HashMap;
import java.util.Vector;

public class Main {

    public static void main(String[] args)
    {
        Vector<String> a = new Vector<>();
        a.add("this is our first sentence");
        a.add( "here is another sentence");
        HashMap<String,Integer> hm = new HashMap<>();
        hm.put("this", 0);
        hm.put("is", 1);
        hm.put("our", 2);
        hm.put("first", 3);
        hm.put("sentence", 4);
        hm.put("here", 5);
        hm.put("another", 6);

        DB myDB = new DB(hm, a);
        System.out.println("Done");

        Checker myCheck = new Checker(hm, myDB.relationships);
        System.out.println(myCheck.check("this is our first sentence"));
        System.out.println(myCheck.check("is this suspicious"));
        System.out.println(myCheck.check("i bought a pineapple"));
    }

}


