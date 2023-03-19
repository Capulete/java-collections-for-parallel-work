import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> q1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> q2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> q3 = new ArrayBlockingQueue<>(100);
    public static void main(String[] args) throws InterruptedException {
        Thread text = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                try {
                    q1.put(generateText("abc", 100000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    q2.put(generateText("abc", 100000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    q3.put(generateText("abc", 100000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        text.start();

        Thread a = new Thread(() -> {
            int maxA = maxChar(q1, 'a');
            System.out.println("Кол-во 'a' - " + maxA);
        });
        a.start();

        Thread b = new Thread(() -> {
            int maxB = maxChar(q2, 'b');
            System.out.println("Кол-во 'b' - " + maxB);
        });
        b.start();

        Thread c = new Thread(() -> {
            int maxC = maxChar(q3, 'c');
            System.out.println("Кол-во 'c' - " + maxC);
        });
        c.start();

        a.join();
        b.join();
        c.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxChar(BlockingQueue<String> q, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10000; i++) {
                text = q.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted");
            return -1;
        }
        return max;
    }
}