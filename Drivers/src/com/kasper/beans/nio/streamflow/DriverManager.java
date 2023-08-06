package com.kasper.beans.nio.streamflow;

import com.kasper.commons.exceptions.StreamFlowException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverManager {

    public static DriverInstance getConnection (String connectionUrl) throws StreamFlowException {
        TokenData tokens = extractTokens(connectionUrl);
        if (tokens.protocolName.equals("kasper")) {
            return new KasperStandardDriver(tokens.host, tokens.username, tokens.password, tokens.port);
        } else {
            throw new StreamFlowException("The protocol name '" + tokens.protocolName + "' is not supported.");
        }
    }

    public static TokenData extractTokens(String inputString) throws StreamFlowException {
        String pattern = "^(\\w+):\\/\\/([^:]+):(\\d+)\\/([^/]+)/([^/]+)$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(inputString);

        if (matcher.find()) {
            String protocolName = matcher.group(1);
            String host = matcher.group(2);
            int port = Integer.parseInt(matcher.group(3));
            String username = matcher.group(4);
            String password = matcher.group(5);
            return new TokenData(protocolName, host, port, username, password);
        } else {
            throw new StreamFlowException("The connection url '" + inputString + "' is not valid.\nThe valid format is:\n\t[protocol://host:port/username/password]");
        }
    }

    private static class TokenData {
        String protocolName;
        String host;
        int port;
        String username;
        String password;

        public TokenData(String protocolName, String host, int port, String username, String password) {
            this.protocolName = protocolName;
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
        }
    }
}
