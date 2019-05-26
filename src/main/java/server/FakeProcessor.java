package server;

import model.Product;
import protocol.Package;

import java.util.Random;
import java.util.Set;

public class FakeProcessor implements Processor {

    private Set<Product> database;
    private Sender sender;

    public FakeProcessor(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void process(Package pack) {
        System.out.println("process");
        new Thread(() -> {
            try {
                Thread.sleep(new Random().nextInt(400) + 20);
                // make database connection
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sender.send("OK".getBytes());
        }).start();
    }
}
