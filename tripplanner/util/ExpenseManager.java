package tripplanner.util;

import java.util.ArrayList;
import java.util.List;
import tripplanner.model.Expense;

public class ExpenseManager {

    private List<Expense> expenses = new ArrayList<>();
    private String tripName;

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripName() {
        return tripName;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public double getTotal() {
        double sum = 0;
        for (Expense e : expenses) {
            sum += e.getAmount();
        }
        return sum;
    }

    public void clearExpenses() {
        expenses.clear();
    }
}



