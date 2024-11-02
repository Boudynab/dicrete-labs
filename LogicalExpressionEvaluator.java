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
        boolean lastWasOperator = false;
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (Character.isLetter(c)) {
                postfix.append(c);
                lastWasOperator = false;
            } else if (c == '(') {
                operators.push(c);
                lastWasOperator = false;
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                if (operators.isEmpty() || operators.pop() != '(') {
                    return null; // mismatched parentheses
                }
                lastWasOperator = false;
            } else if (c == '~') {
                int negationCount = 0;
                while (i < infix.length() && infix.charAt(i) == '~') {
                    negationCount++;
                    i++;
                }
                i--; 
                if (negationCount % 2 != 0) {
                    while (!operators.isEmpty() && precedence.get(operators.peek()) != null && precedence.get('~') < precedence.get(operators.peek())) {postfix.append(operators.pop());
                    }
                    operators.push('~');
                }
                lastWasOperator = true;
            } else if (precedence.containsKey(c)) {
                if (lastWasOperator) {
                    return null; 
                }
                while (!operators.isEmpty() && precedence.get(operators.peek()) != null 
                        && precedence.get(c) <= precedence.get(operators.peek())) {
                    postfix.append(operators.pop());
                }
                operators.push(c);
                lastWasOperator = true;
            } else {
                return null; // invalid character
            }
        }
        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '(' || op == ')') {
                return null; // mismatched parentheses
            }
            postfix.append(op);
        }
    
        return postfix.toString();
    }    
    private boolean evaluatePostfix(String postfix) {
        Stack<Boolean> stack = new Stack<>();
        Map<Character, Boolean> variableValues = getVariableValues(postfix);
        boolean lastWasOperator = false;
        for (char c : postfix.toCharArray()) {
            if (Character.isLetter(c)) {
                stack.push(variableValues.get(c));
                lastWasOperator = false;
            } else if (precedence.containsKey(c)) { // Check if `c` is an operator
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
                lastWasOperator = true;
            } else {
                throw new IllegalArgumentException("Unexpected character: " + c);
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
