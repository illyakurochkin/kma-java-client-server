package server;

import protocol.Package;
import protocol.PackageParser;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Random;

public class FakeReceiver implements Receiver {

    private SecretKey secretKey;
    private Processor processor;

    public FakeReceiver(SecretKey secretKey, Processor processor, List<byte[]> bytes) {
        this.secretKey = secretKey;
        this.processor = processor;
        bytes.forEach(this::receive);
    }

    @Override
    public void receive(byte[] byteArray) {
        new Thread(() -> {
            try {
                Thread.sleep(new Random().nextInt(400) + 20);
                System.out.println("receive");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            PackageParser parser = new PackageParser();
            Package pack = parser.parse(byteArray, secretKey);
            processor.process(pack);
        }).start();
    }
}
