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
        byte src = 1;
        int type = 0;
        int userId = 1;
        String message = "hello world";

        System.out.println("--- EXPECTED ---");
        System.out.println("src - " + src);
        System.out.println("type - " + type);
        System.out.println("userId - " + userId);
        System.out.println("message - " + message);
        System.out.println();

        byte[] byteArray = creator.create(src, type, userId, message, secretKey);

        Package packageInstance = parser.parse(byteArray, secretKey);

        System.out.println("--- ACTUAL ---");
        System.out.println(packageInstance);

        assertEquals(src, packageInstance.getbSrc());
        assertEquals(type, packageInstance.getcType());
        assertEquals(userId, packageInstance.getbUserId());
        assertEquals(message, packageInstance.getMessage());
    }
}
