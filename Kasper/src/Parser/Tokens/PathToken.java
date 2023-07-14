package Parser.Tokens;

import Parser.ParserExceptions.Throw;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class PathToken extends Token {
    public ArrayList<String> path;

    protected PathToken (String pathToken) {
        path = new ArrayList<>();
        this.tokenType = TokenType.PATH;
        StringBuilder currentPath = new StringBuilder();
        for (int i=0; i<pathToken.length(); i++) {
            if (pathToken.charAt(i) == '\\') {
                i++;
                char escape = pathToken.charAt(i);
                switch (escape){
                    case '.' -> currentPath.append('.');
                    case '\\' -> currentPath.append('\\');
                    default -> throw Throw.invalidEscape("\\"+escape, "path", pathToken);
                }
            } else if (pathToken.charAt(i) == '.') {
                if (currentPath.isEmpty()) throw Throw.raw("Path '" + pathToken + "' contains an empty key. Keys cannot be empty.");
                else {
                    path.add(currentPath.toString());
                    currentPath.setLength(0);
                }
            } else {
                currentPath.append(pathToken.charAt(i));}
        }
    }

    public static PathToken newPath (String path) {
        return new PathToken(path);
    }

}
