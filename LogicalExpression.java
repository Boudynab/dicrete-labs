public class LogicalExpression implements Expression {
    
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
