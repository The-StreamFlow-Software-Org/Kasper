package parser.tokens;

import com.kasper.commons.datastructures.KasperList;
import com.kasper.commons.datastructures.KasperObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class FunctionToken extends Token{
    private LinkedList<KasperObject> arguments;
    private KasperFunction functionTask;

    private FunctionToken (String name) {
        this.tokenType = TokenType.FUNCTION;
        this.name = name;
        arguments = new LinkedList<>();
    }

    public static FunctionToken newFunction (String name) {
        return new FunctionToken(name);
    }

    public void decorate(KasperFunction function){
        functionTask = function;
    }

    public void performTask(){
        functionTask.performTask(arguments);
    }

    public void injectArguments(KasperList arguments){
        this.arguments = arguments.toList().getInternalArray();
    }

    public String toString () {
        return "FunctionToken: " + name + " with arguments: " + arguments.toString();
    }
}
