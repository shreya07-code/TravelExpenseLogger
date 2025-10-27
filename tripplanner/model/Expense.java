package tripplanner.model;

public class Expense {
    private String category;
    private String description;
    private double amount;
    private String date;

    public Expense(String category, String description, double amount, String date) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
}


