package me.fairygel;

import me.fairygel.controller.EcosystemController;
import me.fairygel.controller.MainMenuController;
import me.fairygel.controller.MenuController;
import me.fairygel.controller.OrganismController;

public class EcosystemSimulator {
    private final MenuController mainMenuController;

    public EcosystemSimulator() {
        MenuController organismController = new OrganismController();
        MenuController ecosystemController = new EcosystemController();

        mainMenuController = new MainMenuController(ecosystemController, organismController);
    }

    public void start() {
        mainMenuController.showMenu();
    }
}
