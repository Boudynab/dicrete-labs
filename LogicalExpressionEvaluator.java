import java.util.*;
public class LogicalExpressionEvaluator implements LogicalExpressionSolver {
    private static final Map<Character, Integer> precedence = Map.of(
        '~', 3,
        '^', 2,
        'v', 1,
        '>', 0
    );
    @Override
    public boolean evaluateExpression(Expression expression) {
        String postfix = infixToPostfix(expression.getRepresentation());
        if (postfix == null) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return evaluatePostfix(postfix);
    }
    private String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (Character.isLetter(c)) {
                postfix.append(c);
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                if (operators.isEmpty() || operators.pop() != '(') {
                    return null; // mismatched ()
                }
            } else if (precedence.containsKey(c)) {
                while (!operators.isEmpty() && precedence.get(operators.peek()) != null && precedence.get(c) <= precedence.get(operators.peek())) {
                    postfix.append(operators.pop());
                }
                operators.push(c);
            } else {
                return null; //wrong char
            }
        }
        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '(') {
                return null; // mismatched ()
            }
            postfix.append(op);
        }
        return postfix.toString();
    }
    private boolean evaluatePostfix(String postfix) {
        Stack<Boolean> stack = new Stack<>();
        Map<Character, Boolean> variableValues = getVariableValues(postfix);
        for (char c : postfix.toCharArray()) {
            if (Character.isLetter(c)) {
                stack.push(variableValues.get(c));
            } else {
                boolean result;
                switch (c) {
                    case '~':
                        result = !stack.pop();
                        break;
                    case '^':
                        result = stack.pop() & stack.pop();
                        break;
                    case 'v':
                        result = stack.pop() | stack.pop();
                        break;
                    case '>':
                        boolean b = stack.pop();
                        boolean a = stack.pop();
                        result = !a || b;
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected character: " + c);
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }
    private Map<Character, Boolean> getVariableValues(String postfix) {
        Scanner scanner = new Scanner(System.in);
        Set<Character> variables = new HashSet<>();
        for (char c : postfix.toCharArray()) {
            if (Character.isLetter(c) && c != 'v' && c != '^' && c != '~' && c != '>') {
                variables.add(c);
            }
        }        
        Map<Character, Boolean> values = new HashMap<>();
        for (char variable : variables) {
            System.out.print("Enter value for " + variable + " (true/false): ");
            values.put(variable, scanner.nextBoolean());
        }
        return values;
    }
}
