import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.mathematic.Mathematics;

public class Www {
    public static void main(String[] args) {
        try {
//            KeyPair pair = Crypto.ecKeyPair(521);
//            System.out.println(PreSharedCipherEncode.encodeEcPrivate((ECPrivateKey) pair.getPrivate(), false));
//            System.out.println(PreSharedCipherEncode.encodeEcPublic((ECPublicKey) pair.getPublic(), false));
            Crypto.decodeEcPubkey(Mathematics.toBytes(
                    "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vnobd6b4d2kz1lqhz5hs7j0ledngfd1s7zobjb31fkhpsyu8fje35hk5ott7y0qc14k0qqgo5fe8c828gxnhlfx21pxtmaug75xam51q7u2ps7oik6e7h6zc6waggzt9jx366jsg56c9vmux0mmwqdkhxydgdtm3ytlgmn6qlo8y11btd91rw8l76a3pycebz97y9zg6gbtxau",
                    36
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }

        x(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public static void x(Runnable xxx) {

    }
}
