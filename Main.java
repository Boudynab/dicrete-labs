interface Expression { 
    String getRepresentation(); 
    void setRepresentation(String representation); 
 } 
    interface LogicalExpressionSolver { 
    boolean evaluateExpression(Expression expression); 
}
public class Main {
    public static void main(String[] args) {
        LogicalExpression expression = new LogicalExpression();
        String[] testExpressions = {"(P ^ Q) v ~R"};
        for (String expr : testExpressions) {
            expression.setRepresentation(expr);
            System.out.println("Evaluating expression: " + expr);
            LogicalExpressionSolver solver = new LogicalExpressionEvaluator();
            try {
                boolean result = solver.evaluateExpression(expression);
                System.out.println("Result: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid expression: " + e.getMessage());
            }
            System.out.println(); 
        }
    }
}
