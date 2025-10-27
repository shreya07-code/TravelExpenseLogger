package tripplanner.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import tripplanner.model.Expense;

public class FileHandler {

    private static final String FOLDER_PATH = "data/trips";

    static {
        File f = new File(FOLDER_PATH);
        if (!f.exists()) f.mkdirs();
    }

    public static void saveTripToFile(String tripName, ExpenseManager manager) throws IOException {
        if (tripName == null || tripName.trim().isEmpty()) {
            tripName = "Trip_" + System.currentTimeMillis();
        }

        String fileName = tripName.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        File file = new File(FOLDER_PATH + "/" + fileName);

        FileWriter fw = new FileWriter(file);
        fw.write("Trip Name: " + tripName + "\n\n");
        fw.write(String.format("%-15s %-20s %-10s %-12s%n", "Category", "Description", "Amount", "Date"));
        fw.write("---------------------------------------------------------------\n");

        double total = 0;
        for (Expense e : manager.getExpenses()) {
            fw.write(String.format("%-15s %-20s %-10.2f %-12s%n",
                    e.getCategory(), e.getDescription(), e.getAmount(), e.getDate()));
            total += e.getAmount();
        }

        fw.write("\nTotal Expense: " + total);
        fw.close();
    }

    public static List<String> listSavedTrips() throws IOException {
        List<String> list = new ArrayList<>();
        File folder = new File(FOLDER_PATH);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File f : files) {
                list.add(f.getName());
            }
        }
        return list;
    }

    public static String readTripFile(String fileName) throws IOException {
        File file = new File(FOLDER_PATH + "/" + fileName);

        StringBuilder sb = new StringBuilder();
        java.util.Scanner sc = new java.util.Scanner(file);
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        sc.close();
        return sb.toString();
    }
}




