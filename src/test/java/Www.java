import com.github.cao.awa.apricot.identifier.RandomIdentifier;
import com.github.cao.awa.apricot.util.encryption.Crypto;

import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class Www {
    public static void main(String[] args) {
        try {
            KeyPair pair = Crypto.ecKeyPair(521);

            byte[] source = RandomIdentifier.create(10240)
                                            .getBytes();

            byte[] signed = Crypto.ecSign(source,
                                          (ECPrivateKey) pair.getPrivate()
            );

            System.out.println(signed.length);

            System.out.println(new String(signed));

            System.out.println(Crypto.ecVerify(source,
                                               signed,
                                               (ECPublicKey) pair.getPublic()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
