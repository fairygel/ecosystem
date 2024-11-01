package me.fairygel.controller;

import me.fairygel.entity.Ecosystem;
import me.fairygel.manager.EcosystemManager;
import me.fairygel.utils.ScannerUtil;

public class EcosystemController implements MenuController {
    private final EcosystemManager ecosystemManager;

    private final ScannerUtil scanner = new ScannerUtil();

    public EcosystemController() {
        ecosystemManager = new EcosystemManager();
    }

    @Override
    public void showMenu() {
        boolean end = false;

        while (!end) {
            System.out.println("choose your action:");
            System.out.println("1. get all ecosystem's.");
            System.out.println("2. create ecosystem.");
            System.out.println("3. get info about ecosystem.");
            System.out.println("4. update ecosystem.");
            System.out.println("5. delete ecosystem.");
            System.out.println("6. go back.");
            System.out.print("> ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    listEcosystems();
                    break;
                case 2:
                    createEcosystem();
                    break;
                case 3:
                    readEcosystem();
                    break;
                case 4:
                    updateEcosystem();
                    break;
                case 5:
                    deleteEcosystem();
                    break;
                case 6:
                    end = true;
                    break;
                default:
                    System.out.println("Unknown action");
            }
        }
    }

    private void createEcosystem() {

    }

    private void readEcosystem() {
        System.out.print("enter id of ecosystem: ");

        long id = scanner.nextLong();
        Ecosystem ecosystem = ecosystemManager.readEcosystemById(id);

        if (ecosystem == null) {
            System.out.printf("there is no ecosystem with id=%s.%n", id);
            return;
        }

        System.out.println(ecosystem);
    }

    private void updateEcosystem() {
    }

    private void deleteEcosystem() {
    }

    private void listEcosystems() {
        System.out.println("-------------ECOSYSTEMS-------------");
        System.out.println(ecosystemManager.getEcosystems());
    }
}
