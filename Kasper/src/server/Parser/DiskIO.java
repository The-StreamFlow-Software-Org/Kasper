package server.Parser;

import com.kasper.commons.authenticator.KasperAccessAuthenticator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.zip.DataFormatException;

import com.kasper.commons.Parser.ByteCompression;
import com.kasper.commons.Parser.KasperDocument;
import com.kasper.commons.authenticator.Meta;

public class DiskIO {

    private static SecretKey secretKey;

    static {
        try {
            secretKey = EncryptionModule.generateKeyFromInt(KasperAccessAuthenticator.getHashedKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeConfig () throws IOException {
        File file = new File("data", "kasper.init");
        if (file.exists()) {
            try (var reader = new BufferedReader(new FileReader("data/kasper.init"))){
                var str = reader.readLine();
                if (str.equals("server_mode=true")) {
                    Meta.serverMode = true;
                    str = reader.readLine();
                } else str = reader.readLine();
                var timeout = str.split("=");
                Meta.snapshotTimeout = Integer.parseInt(timeout[1]);
                var port = reader.readLine().split("=");
                Meta.port = Integer.parseInt(port[1]);
                var recursionDepth = reader.readLine().split("=");
                Meta.maxRecursionDepth = new BigInteger(recursionDepth[1]);
                for (int i=0; i<5; i++){
                    Meta.concurrentSockets.add(Integer.parseInt(reader.readLine().split("=")[1]));
                }
            } catch (IOException ignored){
                Files.createDirectories(Path.of(Meta.folder));
                try (BufferedWriter write = new BufferedWriter(new FileWriter("data/kasper.init"))){
                    Files.createDirectories(Path.of(Meta.folder));
                    writeMeta(write);
                }catch (IOException ignored2){}
            }

        } else {
            Files.createDirectories(Path.of(Meta.folder));
            try (BufferedWriter write = new BufferedWriter(new FileWriter("data/kasper.init"))){
                writeMeta(write);
            }catch (IOException ignored){}
            writeConfig();

        }
    }

    private static void writeMeta(BufferedWriter write) throws IOException {
        write.write("kasper_driver_instance=true\n");
        write.write("snapshot_time_buffer_ms=24000\n");
        write.write("connect_with_port=53182\n");
        write.write("max_recursion_depth=" + Meta.maxRecursionDepth + "\n");
        write.write("concurrent_socket_1=53183\n");
        write.write("concurrent_socket_2=53184\n");
        write.write("concurrent_socket_3=53185\n");
        write.write("concurrent_socket_4=53186\n");
        write.write("concurrent_socket_5=53187\n");
    }

    public static void writeDocument(KasperDocument document) throws Exception {

        byte[] resolvedBytes = EncryptionModule.encrypt(document.toString(), secretKey);
        byte[] compressedBytes = ByteCompression.compress(resolvedBytes);

        try (DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Meta.getPath())))) {
            writer.writeInt(compressedBytes.length);
            writer.write(compressedBytes);
        }
    }

    public static void writeDocument(byte[] document) throws Exception {
        Files.createDirectories(Path.of(Meta.folder));


        try (DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Meta.getPath())))) {
           writer.write(document);
           writer.flush();
        }
    }

    public static void writeBackup(byte[] document) throws Exception {
        Files.createDirectories(Path.of(Meta.backup));
        try (DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Meta.backupCounter())))) {
            writer.write(document);
            writer.flush();
        } document = null;
    }

    private static byte[] encryptedBuffer() throws IOException, DataFormatException {
        try (DataInputStream read = new DataInputStream(new FileInputStream( Meta.getPath()))) {
            int size = read.readInt();
            byte[] buffer = new byte[size];
            read.readFully(buffer);
            return buffer;
        }
    }

    public static String documentStringRetrieval() throws Exception {
        byte[] compressedBytes = encryptedBuffer();
        byte[] resolvedBytes = ByteCompression.decompress(compressedBytes);
        return EncryptionModule.decrypt(resolvedBytes, secretKey);
    }

    public static byte[] getSerialized () throws IOException {
        File file = new File(Meta.getPath());
        System.out.println("Kasper:> [Persistence] The persistent data is: " + (file.length() / 1000000.00) + " megabytes.");
        try (var reader = new BufferedInputStream(new DataInputStream(new FileInputStream(Meta.getPath())))){
            return reader.readAllBytes();
        }
    }

    public static class EncryptionModule {
        private static final String ALGORITHM = "AES";

        public static byte[] encrypt(String plaintext, SecretKey secretKey) throws Exception {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            return cipher.doFinal(pad(plaintextBytes));
        }

        public static String decrypt(byte[] ciphertext, SecretKey secretKey) throws Exception {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            byte[] unpaddedBytes = unpad(decryptedBytes);
            return new String(unpaddedBytes, StandardCharsets.UTF_8);
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
            byte[] derivedKey = new byte[keyLength];
            System.arraycopy(pseudoRandomKey, 0, derivedKey, 0, keyLength);

            return new SecretKeySpec(derivedKey, "AES");
        }
    }
}
