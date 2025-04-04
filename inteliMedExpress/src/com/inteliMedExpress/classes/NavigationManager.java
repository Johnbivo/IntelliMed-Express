package com.inteliMedExpress.classes;

import javafx.scene.control.Button;
import java.util.List;

public class NavigationManager {
    private List<Button> navigationButtons;

    public NavigationManager(List<Button> navigationButtons) {
        this.navigationButtons = navigationButtons;
    }

    public void selectButton(Button selectedButton) {
        // Reset all buttons
        for (Button button : navigationButtons) {
            UIHelper.styleNavigationButton(button, false);
        }

        // Style the selected button
        UIHelper.styleNavigationButton(selectedButton, true);
    }
}