package ac.ke.retro.encryption;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RSA extends AppCompatActivity {

    public RSA(encrypt encrypt) {

    }
        public static class RSAKeyPairGenerator {

            private static PublicKey publicKey;
            private PrivateKey privateKey;
            // private PublicKey publicKey;

            public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(1024);
                KeyPair pair = keyGen.generateKeyPair();
                this.privateKey = pair.getPrivate();
                this.publicKey = pair.getPublic();
            }

            public void writeToFile(String path, byte[] key) throws IOException {
                File f = new File(path);
                f.getParentFile().mkdirs();

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(key);
                fos.flush();
                fos.close();
            }

            public PrivateKey getPrivateKey() {
                return privateKey;
            }

            public static PublicKey getPublicKey() {
                return publicKey;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
                RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
                keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
                keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
                System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
                System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            public static PublicKey getPublicKey(String base64PublicKey) {
                PublicKey publicKey = null;
                try {
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    publicKey = keyFactory.generatePublic(keySpec);
                    return publicKey;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                return publicKey;
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            public static byte[] encrypt(String data, String publicKey) throws
                    BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
                return cipher.doFinal(data.getBytes());

            }


            public static void poro() throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException {
                String data = "data was here ";
                String publicKey = RSAKeyPairGenerator.getPublicKey().toString();
                byte[] output = encrypt(data, publicKey);
                //System.out.println(output);
            }
        }
}



