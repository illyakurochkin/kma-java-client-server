package protocol;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import static java.nio.ByteBuffer.wrap;
import static java.util.Arrays.copyOfRange;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.getInstance;
import static protocol.Package.builder;
import static utils.CRC16.generateCrc16;

class PackageParser {

    public Package parse(byte[] packet, SecretKey secretKey) throws Exception {
        Cipher cipher = getInstance("AES");
        cipher.init(DECRYPT_MODE, secretKey);

        byte bMagic = packet[0];
        byte bSrc = packet[1];
        long bPktId = wrap(copyOfRange(packet, 2, 10)).getLong();
        int wLen = wrap(copyOfRange(packet, 10, 14)).getInt();
        short packageCrc16H = wrap(copyOfRange(packet, 14, 16)).getShort();
        int cType = wrap(copyOfRange(packet, 16, 20)).getInt();
        int bUserId = wrap(copyOfRange(packet, 20, 24)).getInt();
        byte[] decryptedBytes = cipher.doFinal(copyOfRange(packet, 24, packet.length - 2));
        String decryptedMessage = new String(decryptedBytes);
        short packageCrc16B = wrap(copyOfRange(packet, packet.length - 2, packet.length)).getShort();

        short headerCrc = (short) generateCrc16(copyOfRange(packet, 0, 14));
        short bodyCrc = (short) generateCrc16(copyOfRange(packet, 0, packet.length - 2));

        if (headerCrc != packageCrc16H) {
            throw new RuntimeException("Header CRC16 ("
                    + headerCrc + ") is not matching header data (" + packageCrc16B + ')');
        }

        if (bodyCrc != packageCrc16B) {
            throw new RuntimeException("Body CRC16 ("
                    + bodyCrc + ") is not matching body data (" + packageCrc16B + ')');
        }

        return builder()
                .withBMagic(bMagic)
                .withBSrc(bSrc)
                .withBPktId(bPktId)
                .withWLen(wLen)
                .withPackageCrc16H(packageCrc16H)
                .withCType(cType)
                .withBUserId(bUserId)
                .withMessage(decryptedMessage)
                .withPackageCrc16B(packageCrc16B)
                .build();
    }
}
