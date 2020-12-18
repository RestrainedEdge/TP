package data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {

    private static final String ID_CODE = "TA";
    private static int serNr = 100;

    private Queue<String> keys;
    private Queue<String> value;

    public void generateShuffleIdsAndCars(int setSize) {
        keys = generateShuffleKeys(setSize);
        value = generateShuffleValues(setSize);
    }

    /**
     * Gražinamas vienas elementas (Automobilis) iš sugeneruotos Automobilių eilės.
     * Kai elementai baigiasi sugeneruojama nuosava situacija ir išmetamas pranešimas.
     * Šis metodas naudojamas grafinėje sąsajoje
     *
     * @return
     */
    public String getValue() {
        if (value == null) {
            System.out.println("carsNotGenerated");
        }
        if (value.isEmpty()) {
            System.out.println("allSetStoredToMap");
        }

        return value.poll();
    }

    public String getKey() {
        if (keys == null) {
            System.out.println("carsIdsNotGenerated");
        }
        if (keys.isEmpty()) {
            System.out.println("allKeysStoredToMap");
        }
        return keys.poll();
    }

    public static Queue<String> generateShuffleValues(int size) {
        LinkedList<String> values = IntStream.range(0, size)
                .mapToObj(i -> generateKey())
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(values);
        return values;
    }

    public static Queue<String> generateShuffleKeys(int size) {
        LinkedList<String> keys = IntStream.range(0, size)
                .mapToObj(i -> generateKey())
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(keys);
        return keys;
    }

    public static String generateKey() {
        return ID_CODE + (serNr++);
    }
}
