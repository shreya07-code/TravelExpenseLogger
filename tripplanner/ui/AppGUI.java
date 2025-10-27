package tripplanner.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.List;
import tripplanner.util.FileHandler;
import tripplanner.util.ExpenseManager;
import tripplanner.model.Expense;

public class AppGUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel container;

    private JTextField tripNameField;
    private JComboBox<String> categoryBox;
    private JTextField descriptionField;
    private JTextField amountField;

    private ExpenseManager manager;
    private DefaultTableModel tableModel;
    private JTable expenseTable;

    public AppGUI() {
        manager = new ExpenseManager();
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Travel Expense Logger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 650);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(createHomePanel(), "home");
        container.add(createNewTripPanel(), "newtrip");
        container.add(createViewExpensesPanel(), "viewExpenses");
        container.add(createViewPreviousPanel(), "prevTrips");

        frame.setContentPane(container);
        frame.setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(235, 230, 255));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Travel Expense Logger");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 40, 0));
        title.setForeground(new Color(70, 30, 140));

        JButton newTripBtn = new JButton("Start a New Trip");
        JButton viewPrevBtn = new JButton("View Previous Summaries");

        styleButton(newTripBtn);
        styleButton(viewPrevBtn);

        newTripBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewPrevBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        newTripBtn.setMaximumSize(new Dimension(250, 45));
        viewPrevBtn.setMaximumSize(new Dimension(250, 45));

        newTripBtn.addActionListener(e -> cardLayout.show(container, "newtrip"));
        viewPrevBtn.addActionListener(e -> loadPreviousTrips());

        panel.add(title);
        panel.add(newTripBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(viewPrevBtn);

        return panel;
    }

    private JPanel createNewTripPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 235, 255));

        JLabel title = new JLabel("New Trip - Enter Expenses");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(40, 20, 400, 40);
        panel.add(title);

        JLabel tripLabel = new JLabel("Trip Name:");
        tripLabel.setBounds(40, 80, 150, 30);
        panel.add(tripLabel);

        tripNameField = new JTextField();
        tripNameField.setBounds(150, 80, 800, 30);
        panel.add(tripNameField);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 125, 150, 30);
        panel.add(categoryLabel);

        String[] categories = {"Food", "Travel", "Stay", "Shopping", "Miscellaneous"};
        categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(150, 125, 200, 30);
        panel.add(categoryBox);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(40, 170, 150, 30);
        panel.add(descLabel);

        descriptionField = new JTextField();
        descriptionField.setBounds(150, 170, 800, 30);
        panel.add(descriptionField);

        JLabel amountLabel = new JLabel("Amount (INR):");
        amountLabel.setBounds(40, 215, 150, 30);
        panel.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(150, 215, 200, 30);
        panel.add(amountField);

        JButton addBtn = new JButton("Add Expense");
        addBtn.setBounds(40, 270, 170, 35);
        styleButton(addBtn);

        JButton viewBtn = new JButton("View Entered Expenses");
        viewBtn.setBounds(240, 270, 220, 35);
        styleButton(viewBtn);

        JButton saveBtn = new JButton("Save Trip Summary");
        saveBtn.setBounds(480, 270, 220, 35);
        styleButton(saveBtn);

        JButton homeBtn = new JButton("Back to Home");
        homeBtn.setBounds(720, 270, 200, 35);
        styleButton(homeBtn);

        addBtn.addActionListener(this::onAddExpense);
        viewBtn.addActionListener(e -> cardLayout.show(container, "viewExpenses"));
        saveBtn.addActionListener(e -> onSaveTrip());
        homeBtn.addActionListener(e -> cardLayout.show(container, "home"));

        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(saveBtn);
        panel.add(homeBtn);

        return panel;
    }

    private JPanel createViewExpensesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 235, 255));

        tableModel = new DefaultTableModel(new Object[]{"Category", "Description", "Amount", "Date"}, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(24);
        expenseTable.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        JButton backBtn = new JButton("Back");
        styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(container, "newtrip"));
        panel.add(backBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createViewPreviousPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(235, 230, 255));

        JLabel label = new JLabel("Previous Trip Summaries");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setOpaque(true);
        label.setBackground(new Color(210, 190, 255));
        label.setForeground(new Color(60, 30, 120));
        panel.add(label, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> previousList = new JList<>(listModel);
        previousList.setFont(new Font("Consolas", Font.PLAIN, 14));

        panel.add(new JScrollPane(previousList), BorderLayout.WEST);

        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        previousList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String fn = previousList.getSelectedValue();
                if (fn != null) {
                    try {
                        summaryArea.setText(FileHandler.readTripFile(fn));
                    } catch (IOException ex) {
                        summaryArea.setText("Error reading selected file!");
                    }
                }
            }
        });

        JButton backBtn = new JButton("Back to Home");
        styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(container, "home"));
        panel.add(backBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void loadPreviousTrips() {
        try {
            List<String> files = FileHandler.listSavedTrips();

            JPanel prevPanel = (JPanel) container.getComponent(3);
            JScrollPane scroll = (JScrollPane) prevPanel.getComponent(1);
            JList<String> list = (JList<String>) scroll.getViewport().getView();

            list.setListData(files.toArray(new String[0]));
            cardLayout.show(container, "prevTrips");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading saved trips!");
        }
    }

    private void onAddExpense(ActionEvent e) {
        try {
            String category = categoryBox.getSelectedItem().toString();
            String desc = descriptionField.getText();
            double amt = Double.parseDouble(amountField.getText());
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            Expense exp = new Expense(category, desc, amt, date);
            manager.addExpense(exp);
            tableModel.addRow(new Object[]{category, desc, amt, date});

            descriptionField.setText("");
            amountField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Enter valid amount!");
        }
    }

    private void onSaveTrip() {
        try {
            String trip = tripNameField.getText();
            FileHandler.saveTripToFile(trip, manager);

            JOptionPane.showMessageDialog(frame, "Trip saved!");
            manager.clearExpenses();
            tableModel.setRowCount(0);

            cardLayout.show(container, "home");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error saving trip!");
        }
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(200, 180, 255));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(new Color(120, 85, 200)));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(new Color(60, 30, 120));
    }
}





