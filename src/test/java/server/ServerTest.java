package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;

public class ServerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Sender sender = new FakeSender();
    private Processor processor = new FakeProcessor(sender);

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test(timeout = 0)
    public void shouldSendMessageAndReceiveOK() throws InterruptedException {

        new FakeReceiver(processor, 10);
        String expectedAnswer = range(0, 10).mapToObj(i -> "OK").collect(joining());
        sleep(2000);
        StringBuilder actualAnswer = new StringBuilder();
        String data = outContent.toString();
        for (int i = 0; i < data.length() - 1; i++) {
            if ("OK".equals(data.substring(i, i + 2))) {
                actualAnswer.append("OK");
            }
        }
        assertEquals(expectedAnswer, actualAnswer.toString());
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.out.println(outContent.toString());
    }
}
