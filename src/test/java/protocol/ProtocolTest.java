package protocol;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class ProtocolTest {
    private static PackageCreator creator = new PackageCreator();
    private static PackageParser parser = new PackageParser();

    private SecretKey secretKey;

    @Before
    public void generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator key = KeyGenerator.getInstance("AES");
        key.init(256);
        secretKey = key.generateKey();
    }

    @Test
    public void shouldCreateAndParsePackage() throws Exception {
        byte byteSrc = 1;
        int cType = 0;
        int bUserId = 1;
        String message = "hello world";

        byte[] byteArray = creator.create(byteSrc, cType, bUserId, message, secretKey);

        Package packageInstance = parser.parse(byteArray, secretKey);

        assertEquals(byteSrc, packageInstance.getbSrc());
        assertEquals(cType, packageInstance.getcType());
        assertEquals(bUserId, packageInstance.getbUserId());
        assertEquals(message, packageInstance.getMessage());
    }
}
