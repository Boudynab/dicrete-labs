import java.util.Scanner;
interface Expression { 
    String getRepresentation(); 
    void setRepresentation(String representation); 
}
interface LogicalExpressionSolver { 
    boolean evaluateExpression(Expression expression); 
}
class LogicalExpression implements Expression {
    private String representation;
    @Override
    public String getRepresentation() {
        return representation;
    }
    @Override
    public void setRepresentation(String representation) {
        this.representation = representation;
    }
}
public class Main {
    public static void main(String[] args) {
        LogicalExpression expression = new LogicalExpression();
        LogicalExpressionSolver solver = new LogicalExpressionEvaluator();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a logical expression to evaluate: ");
        String inputExpression = scanner.nextLine();
        expression.setRepresentation(inputExpression);
        System.out.println("Evaluating expression: " + inputExpression);
        try {
            boolean result = solver.evaluateExpression(expression);
            System.out.println("Result: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid expression: " + e.getMessage());
        }
    }
}