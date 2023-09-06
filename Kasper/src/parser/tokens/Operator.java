package parser.tokens;

import parser.exceptions.Throw;

import java.util.HashMap;

public class Operator extends Token{
    public final int OperatorType;

    public static HashMap<String, Integer> operatorMap = null;
    public static final int EQUAL = 1;
    public static final int GREATER_THAN = 2;
    public static final int LESS_THAN = 3;
    public static final int GREATER_THAN_EQUAL = 4;
    public static final int LESS_THAN_EQUAL = 5;
    public static final int NOT_EQUAL = 6;
    public static final int LIKE = 7;


    private static void initOperators () {
        operatorMap = new HashMap<>();
        operatorMap.put("=", 1);
        operatorMap.put(">", 2);
        operatorMap.put("<", 3);
        operatorMap.put(">=", 4);
        operatorMap.put("<=", 5);
        operatorMap.put("!=", 6);
        operatorMap.put("LIKE", 7);
    }
    protected Operator (String type) {
        type = type.toUpperCase();
        this.name = type;
        if (operatorMap == null)
            initOperators();
        Integer contains = operatorMap.get(type);
        if (contains == null) {
            throw Throw.raw("Cannot find token '" + type + "'. Please use a valid identifier.");
        } OperatorType = contains;
        this.tokenType = TokenType.OPERATOR;

    }

    public static Operator newOperator(String operator) {
        return new Operator(operator);
    }

    boolean isOperator (String test) {
        return operatorMap.containsKey(test);
    }
}
