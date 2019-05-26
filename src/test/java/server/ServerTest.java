package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.PackageCreator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static server.CommandTypes.*;

public class ServerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private final Sender sender = new FakeSender();

    private Processor processor;
    private List<byte[]> manipulateCommands;
    private List<byte[]> checkCommands;
    private SecretKey secretKey;

    @Before
    public void init() throws NoSuchAlgorithmException {
        // init secret key
        KeyGenerator key = KeyGenerator.getInstance("AES");
        key.init(256);
        secretKey = key.generateKey();

        // init processor
        Map<String, Integer> database = new HashMap<>();
        database.put("product1", 100);
        database.put("product2", 100);
        database.put("product3", 100);
        database.put("product4", 100);

        processor = new FakeProcessor(sender, database);

        // init receivers
        PackageCreator creator = new PackageCreator();

        manipulateCommands = List.of(creator.create((byte) 1, PUT, 1, "product1 10", secretKey),
                creator.create((byte) 1, PUT, 1, "product2 10", secretKey),
                creator.create((byte) 1, PUT, 1, "product3 10", secretKey),
                creator.create((byte) 1, PUT, 1, "product4 10", secretKey),
                creator.create((byte) 1, REMOVE, 1, "product1 10", secretKey),
                creator.create((byte) 1, REMOVE, 1, "product2 10", secretKey),
                creator.create((byte) 1, REMOVE, 1, "product3 10", secretKey),
                creator.create((byte) 1, REMOVE, 1, "product4 10", secretKey));

        checkCommands = List.of(creator.create((byte) 1, CHECK, 1, "product1", secretKey),
                creator.create((byte) 1, CHECK, 1, "product2", secretKey),
                creator.create((byte) 1, CHECK, 1, "product3", secretKey),
                creator.create((byte) 1, CHECK, 1, "product4", secretKey));

        // init fake stream
        System.setOut(new PrintStream(outContent));
    }


    @Test
    public void shouldSendMessageAndReceiveOK() throws InterruptedException {
        // manipulate
        new FakeReceiver(secretKey, processor, manipulateCommands);

        String expectedAnswer = range(0, manipulateCommands.size()).mapToObj(i -> "OK").collect(joining());

        sleep(5000);

        StringBuilder actualAnswer = new StringBuilder();
        String data1 = outContent.toString();
        for (int i = 0; i < data1.length() - 1; i++) {
            if ("OK".equals(data1.substring(i, i + 2))) {
                actualAnswer.append("OK");
            }
        }

        System.out.println("[expected: " + expectedAnswer + ", actual: " + actualAnswer + "]");
        assertEquals("answers ok on every PUT or REMOVE command", expectedAnswer, actualAnswer.toString());

        // check database state
        new FakeReceiver(secretKey, processor, checkCommands);

        sleep(5000);

        String data2 = outContent.toString();

        assertTrue(data2.contains("product1:100"));
        System.out.println("[product1 - expected: 100, actual: 100]");
        assertTrue(data2.contains("product2:100"));
        System.out.println("[product2 - expected: 100, actual: 100]");
        assertTrue(data2.contains("product3:100"));
        System.out.println("[product3 - expected: 100, actual: 100]");
        assertTrue(data2.contains("product4:100"));
        System.out.println("[product4 - expected: 100, actual: 100]");
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.out.println(outContent.toString());
    }
}
