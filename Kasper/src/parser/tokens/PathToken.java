package parser.tokens;

import parser.exceptions.Throw;

import java.util.ArrayList;

public class PathToken extends Token {

    protected PathToken (String pathToken) {
       this.name = pathToken;
    }

    private void deprecatedPath (String pathToken){
        var path = new ArrayList<>();
        this.tokenType = TokenType.PATH;
        StringBuilder currentPath = new StringBuilder();
        for (int i=0; i<pathToken.length(); i++) {
            if (pathToken.charAt(i) == '\\') {
                currentPath.append(pathToken.charAt(i));
                currentPath.append(pathToken.charAt(++i));
            } else if (pathToken.charAt(i) == '.') {
                if (currentPath.isEmpty()) throw Throw.raw("Path '" + pathToken + "' contains an empty key. Keys cannot be empty.");
                else {
                    path.add(currentPath.toString());
                    currentPath.setLength(0);
                }
            } else {
                currentPath.append(pathToken.charAt(i));}
        } if (!currentPath.isEmpty()) path.add(currentPath.toString());
    }

    public static PathToken newPath (String path) {
        return new PathToken(path);
    }

}
