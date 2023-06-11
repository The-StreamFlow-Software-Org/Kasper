package KasperCommons.Network;

import KasperCommons.Exceptions.KasperIOException;

import java.io.*;
import java.net.Socket;

public class NetworkPackage {
    public Socket socket;
    public InputStream rawInput;
    public DataInputStream bufferedInput;
    public OutputStream rawOutput;
    public DataOutputStream bufferedOutput;

    public NetworkPackage (Socket socket) throws KasperIOException {
        try {
            this.socket = socket;
            rawInput = socket.getInputStream();
            bufferedInput = new DataInputStream(new BufferedInputStream(rawInput));
            rawOutput = socket.getOutputStream();
            bufferedOutput = new DataOutputStream(rawOutput);
        } catch (IOException e) {
            throw new KasperIOException(e.toString());
        }

    }
}
