package server;

import java.util.Random;

public class FakeSender implements Sender {
    @Override
    public void send(byte[] byteArray) {
        System.out.println("send");
        new Thread(() -> {
            try {
                Thread.sleep(new Random().nextInt(400) + 20);
                // send message
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(new String(byteArray));
        }).start();
    }
}
