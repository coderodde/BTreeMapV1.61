import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;
import net.coderodde.util.BTreeMap;

public class Demo {

    public static void main(String[] args) {
        final int MINIMUM_DEGREE = 16;
        final int UNIVERSE_SIZE = 100_000;
        final int LOAD_SIZE = 1_000_000;
        final int QUERY_SIZE = 1_000_000;
        final int DELETE_SIZE = 500_000;

        Map<Integer, Integer> tree1 = new BTreeMap<>(MINIMUM_DEGREE);
        Map<Integer, Integer> tree2 = new TreeMap<>(); 

        Random random = new Random();

        // Warmup:
        for (int i = 0; i < LOAD_SIZE; ++i) {
            int key = random.nextInt(UNIVERSE_SIZE);
            tree1.put(key, key);
            tree2.put(key, key);
        }

        for (int i = 0; i < QUERY_SIZE; ++i) {
            int key = random.nextInt(UNIVERSE_SIZE);

            if (!Objects.equals(tree1.get(key), tree2.get(key))) {
                throw new IllegalStateException(
                        "Trees do not agree during warmup.");
            }
        }

        for (int i = 0; i < DELETE_SIZE; ++i) {
            int key = random.nextInt(UNIVERSE_SIZE);

            if (!Objects.equals(tree1.remove(key), tree2.remove(key))) {
                throw new IllegalStateException(
                        "Trees do not agree during warmup.");
            }
        }

        if (tree1.size() != tree2.size()) {
            throw new IllegalStateException("Size mismatch after warmup.");
        }

        // Benchmark:
        long seed = System.currentTimeMillis();
        System.out.println("Seed = " + seed);

        Random random1 = new Random(seed);
        Random random2 = new Random(seed);

        long totalTime1 = 0L; 
        long totalTime2 = 0L;

        long startTime = System.currentTimeMillis();

        tree1 = new BTreeMap<>(MINIMUM_DEGREE);

        for (int i = 0; i < LOAD_SIZE; ++i) {
            int key = random1.nextInt(UNIVERSE_SIZE);
            tree1.put(key, 3 * key);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("BTreeMap.put in " + 
                (endTime - startTime) + " milliseconds.");

        totalTime1 += endTime - startTime;

        startTime = System.currentTimeMillis();

        for (Integer i : tree1.keySet()) {

        }

        endTime = System.currentTimeMillis();

        System.out.println("Iteration of BTreeMap in " + 
                (endTime - startTime) + " milliseconds.");

        totalTime1 += endTime - startTime;

        startTime = System.currentTimeMillis();

        for (int i = 0; i < QUERY_SIZE; ++i) {
            int key = random1.nextInt(UNIVERSE_SIZE);
            tree1.get(key);
        }

        endTime = System.currentTimeMillis();

        System.out.println("BTreeMap.get in " +
                (endTime - startTime) + " milliseconds.");

        totalTime1 += endTime - startTime;

        startTime = System.currentTimeMillis();

        for (int i = 0; i < DELETE_SIZE; ++i) {
            int key = random1.nextInt(LOAD_SIZE);
            tree1.remove(key);
        }

        endTime = System.currentTimeMillis();

        System.out.println("BTreeMap.remove in " +
                (endTime - startTime) + " milliseconds.");

        totalTime1 += endTime - startTime;

        System.out.println("BTreeMap total time: " + totalTime1 +
                " milliseconds.");
        System.out.println();

        ///// TreeMap /////
        startTime = System.currentTimeMillis();

        tree2 = new TreeMap<>();

        for (int i = 0; i < LOAD_SIZE; ++i) {
            int key = random2.nextInt(UNIVERSE_SIZE);
            tree2.put(key, 3 * key);
        }

        endTime = System.currentTimeMillis();

        System.out.println("TreeMap.put in " + 
                (endTime - startTime) + " milliseconds.");

        totalTime2 += endTime - startTime;

        startTime = System.currentTimeMillis();

        for (Integer i : tree2.keySet()) {

        }

        endTime = System.currentTimeMillis();

        System.out.println("Iteration of TreeMap in " + 
                (endTime - startTime) + " milliseconds.");

        totalTime2 += endTime - startTime;

        startTime = System.currentTimeMillis();

        for (int i = 0; i < QUERY_SIZE; ++i) {
            int key = random2.nextInt(UNIVERSE_SIZE);
            tree2.get(key);
        }

        endTime = System.currentTimeMillis();

        System.out.println("TreeMap.get in " +
                (endTime - startTime) + " milliseconds.");

        totalTime2 += endTime - startTime;

        startTime = System.currentTimeMillis();

        for (int i = 0; i < DELETE_SIZE; ++i) {
            int key = random2.nextInt(LOAD_SIZE);
            tree2.remove(key);
        }

        endTime = System.currentTimeMillis();

        System.out.println("TreeMap.remove in " +
                (endTime - startTime) + " milliseconds.");

        totalTime2 += endTime - startTime;

        System.out.println("TreeMap total time: " + totalTime2 +
                " milliseconds.");

        tree1.clear();
        tree2.clear();

        for (int i = 0; i < 100_000; ++i) {
            int key = random1.nextInt();
            tree1.put(key, null);
            tree2.put(key, null);
        }

        Iterator<Integer> iter1 = tree1.keySet().iterator();
        Iterator<Integer> iter2 = tree2.keySet().iterator();

        while (iter1.hasNext()) {
            if (!iter1.next().equals(iter2.next())) {
                throw new IllegalStateException("Iterators disagree!");
            }
        }
    }
}
