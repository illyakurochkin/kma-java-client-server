package protocol;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import static java.nio.ByteBuffer.wrap;
import static java.util.Arrays.copyOfRange;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.getInstance;
import static protocol.Package.builder;
import static utils.CRC16.generateCrc16;

public class PackageParser {

    public Package parse(byte[] byteArray, SecretKey secretKey) {
        try {
            Cipher cipher = getInstance("AES");
            cipher.init(DECRYPT_MODE, secretKey);

            byte bMagic = byteArray[0];
            byte bSrc = byteArray[1];
            long bPktId = wrap(copyOfRange(byteArray, 2, 10)).getLong();
            int wLen = wrap(copyOfRange(byteArray, 10, 14)).getInt();
            short packageCrc16H = wrap(copyOfRange(byteArray, 14, 16)).getShort();
            int cType = wrap(copyOfRange(byteArray, 16, 20)).getInt();
            int bUserId = wrap(copyOfRange(byteArray, 20, 24)).getInt();
            byte[] decryptedBytes = cipher.doFinal(copyOfRange(byteArray, 24, byteArray.length - 2));
            String decryptedMessage = new String(decryptedBytes);
            short packageCrc16B = wrap(copyOfRange(byteArray, byteArray.length - 2, byteArray.length)).getShort();

            short headerCrc = (short) generateCrc16(copyOfRange(byteArray, 0, 14));
            short bodyCrc = (short) generateCrc16(copyOfRange(byteArray, 0, byteArray.length - 2));


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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
