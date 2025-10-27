package tripplanner.main;

import javax.swing.SwingUtilities;
import tripplanner.ui.AppGUI;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI());
    }
}

