package data;

import java.util.Iterator;

public class BasicClass {
    public static void main(String[] args) {
        RBT<Integer, String> tree = new RBT<>();

        for(int i = 0; i < 15; i++) {
            tree.Add(i, "" + i);
        }
        System.out.println(tree.toVisualizedString(""));

        RBT<Integer, String> temp = new RBT<>();
        System.out.println(temp.toVisualizedString(""));
        for(int k = 0; k < 20; k++) {
            temp.Add(k, "" + k);
        }
        System.out.println(temp.toVisualizedString(""));
        for(int i = 0; i < 20; i++) {
            System.out.println(i);
            temp.Remove(i);
            System.out.println("Is balanced - " + temp.IsBalanced());
            System.out.println(temp.toVisualizedString(""));
        }
        System.out.println(temp.toVisualizedString(""));
        temp.Add(5,"5");
        temp.Add(4,"4");
        temp.Add(6,"6");
        temp.Add(7,"7");
        System.out.println(temp.toVisualizedString(""));

//        for(int i = 0; i < 15; i++) {
//            System.out.println("Deleting: " + i);
//            tree.Remove(i);
//            tree.Add(i, "" + i);
//        }
//        Iterator<String> iterator = tree.iterator();
//        while(iterator.hasNext()) {
//            String one = iterator.next();
//            System.out.println(one);
//        }

    }
}
