package server;

import protocol.Package;
import protocol.PackageCreator;
import protocol.PackageParser;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

public class FakeReceiver implements Receiver {

    private SecretKey secretKey;
    private Processor processor;

    public FakeReceiver(Processor processor, int commandAmount) {
        try {
            KeyGenerator key = KeyGenerator.getInstance("AES");
            key.init(256);
            secretKey = key.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        this.processor = processor;
        startFakeReceiving(commandAmount);
    }

    private void startFakeReceiving(int commandAmount) {
        PackageCreator creator = new PackageCreator();

        List<byte[]> bytes = List.of(creator.create((byte) 1, 1, 1, "first", secretKey),
                creator.create((byte) 1, 1, 1, "second", secretKey),
                creator.create((byte) 1, 1, 1, "third", secretKey));

        for (int i = 0; i < commandAmount; ) {
            for (int j = 0; j < bytes.size() && i < commandAmount; j++, i++) {
                receive(bytes.get(j));
            }
        }
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
