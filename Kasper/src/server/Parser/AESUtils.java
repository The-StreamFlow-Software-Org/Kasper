package server.Parser;

import com.kasper.commons.authenticator.KasperAccessAuthenticator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

public class AESUtils {
    private static final String ALGORITHM = "AES";

    public static byte[] encrypt(byte[] plaintext) throws Exception {
        SecretKey secretKey = generateKeyFromInt(KasperAccessAuthenticator.getHashedKey());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] paddedPlaintext = pad(plaintext);
        return cipher.doFinal(paddedPlaintext);
    }

    public static byte[] decrypt(byte[] ciphertext) throws Exception {
        SecretKey secretKey = generateKeyFromInt(KasperAccessAuthenticator.getHashedKey());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] paddedCiphertext = cipher.doFinal(ciphertext);
        return unpad(paddedCiphertext);
    }


    private static byte[] pad(byte[] input) {
        int paddingLength = 16 - (input.length % 16);
        byte[] paddedInput = new byte[input.length + paddingLength];
        System.arraycopy(input, 0, paddedInput, 0, input.length);
        for (int i = input.length; i < paddedInput.length; i++) {
            paddedInput[i] = (byte) paddingLength;
        }
        return paddedInput;
    }

    private static byte[] unpad(byte[] input) {
        int paddingLength = input[input.length - 1];
        byte[] unpaddedInput = new byte[input.length - paddingLength];
        System.arraycopy(input, 0, unpaddedInput, 0, unpaddedInput.length);
        return unpaddedInput;
    }

    private static final String HASH_ALGORITHM = "SHA-256";

    private static SecretKey generateKeyFromInt(int value) throws Exception {
        // Convert the integer to a byte array
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.putInt(value);
        byte[] inputBytes = byteBuffer.array();

        // Apply HKDF to generate the secret key
        SecretKeySpec secretKeySpec = hkdfExtractAndExpand(inputBytes, null, 16);
        return secretKeySpec;
    }

    static SecretKeySpec hkdfExtractAndExpand(byte[] inputKeyMaterial, byte[] salt, int keyLength) throws Exception {
        SecretKey secretKey = new SecretKeySpec(inputKeyMaterial, "HKDF");
        MessageDigest hashFunction = MessageDigest.getInstance(HASH_ALGORITHM);

        // HKDF Extract
        if (salt == null) {
            salt = new byte[hashFunction.getDigestLength()];
        }
        hashFunction.update(salt);
        byte[] pseudoRandomKey = hashFunction.digest(inputKeyMaterial);

        // HKDF Expand
        byte[] derivedKey = Arrays.copyOf(pseudoRandomKey, keyLength);

        return new SecretKeySpec(derivedKey, "AES");
    }
}
