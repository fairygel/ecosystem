package me.fairygel.controller;

import me.fairygel.utils.ScannerUtil;

public class MainMenuController implements MenuController {
    private final MenuController ecosystemController;
    private final MenuController organismController;

    private final ScannerUtil scanner = new ScannerUtil();

    public MainMenuController(MenuController ecosystemController, MenuController organismController) {
        this.ecosystemController = ecosystemController;
        this.organismController = organismController;
    }

    @Override
    public void showMenu() {
        System.out.println("welcome to Ecosystem Simulator!\n");

        boolean end = false;
        int choice;

        while (!end) {
            System.out.println("choose your action:");
            System.out.println("1. manage ecosystems.");
            System.out.println("2. manage organisms.");
            System.out.println("3. exit.");
            System.out.print("> ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ecosystemController.showMenu();
                    break;
                case 2:
                    organismController.showMenu();
                    break;
                case 3:
                    System.out.println("have a nice day!");
                    end = true;
                    break;
                default:
                    System.out.println("Unknown action");
            }
        }
    }
}
