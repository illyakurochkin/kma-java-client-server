package protocol;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

import static java.nio.ByteBuffer.allocate;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static javax.crypto.Cipher.getInstance;
import static utils.ByteArraysUtils.toArray;
import static utils.ByteArraysUtils.toList;
import static utils.CRC16.generateCrc16;

public class PackageCreator {
    private static long counter = 1;

    byte[] create(byte bSrc, int cType, int bUserId, String message, SecretKey secretKey) throws Exception {
        Cipher cipher = getInstance("AES");
        cipher.init(ENCRYPT_MODE, secretKey);

        List<Byte> bMsq = new ArrayList<>();
        List<Byte> resultList = new ArrayList<>();
        byte bMagic = 0x13;

        byte[] byteDataToEncrypt = message.getBytes();
        byte[] encrypted = cipher.doFinal(byteDataToEncrypt);

        bMsq.addAll(toList(allocate(4).putInt(cType).array()));
        bMsq.addAll(toList(allocate(4).putInt(bUserId).array()));
        bMsq.addAll(toList(encrypted));
        int wLen = bMsq.size();

        resultList.add(bMagic);
        resultList.add(bSrc);
        resultList.addAll(toList(allocate(8).putLong(counter).array()));
        resultList.addAll(toList(allocate(4).putInt(wLen).array()));

        short wCrc16H = (short) generateCrc16(toArray(resultList));
        resultList.addAll(toList(allocate(2).putShort(wCrc16H).array()));

        resultList.addAll(bMsq);

        short wCrc16B = (short) generateCrc16(toArray(resultList));
        resultList.addAll(toList(allocate(2).putShort(wCrc16B).array()));

        counter += 1;
        return toArray(resultList);
    }
}
