package server;

import protocol.Package;

import java.util.Map;
import java.util.Random;

public class FakeProcessor implements Processor {

    private final Map<String, Integer> database;
    private Sender sender;

    public FakeProcessor(Sender sender, Map<String, Integer> database) {
        this.sender = sender;
        this.database = database;
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

            String productName;
            int amount;
            switch (pack.getcType()) {
                case CommandTypes.REMOVE:
                    productName = pack.getMessage().split(" ")[0].toLowerCase();
                    amount = Integer.parseInt(pack.getMessage().split(" ")[1]);
                    database.put(productName, database.getOrDefault(productName, 0) + -amount);
                    break;
                case CommandTypes.PUT:
                    productName = pack.getMessage().split(" ")[0].toLowerCase();
                    amount = Integer.parseInt(pack.getMessage().split(" ")[1]);
                    database.put(productName, database.getOrDefault(productName, 0) + amount);
                    break;
                case CommandTypes.CHECK:
                    productName = pack.getMessage().split(" ")[0].toLowerCase();
                    amount = database.getOrDefault(productName, 0);
                    sender.send((productName + ":" + amount).getBytes());
                    return;
            }
            sender.send("OK".getBytes());
        }).start();
    }
}
