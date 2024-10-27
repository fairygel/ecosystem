package me.fairygel;

import java.util.Scanner;

public class EcosystemController {
    EcosystemSimulator ecosystemSimulator;

    public EcosystemController() {
        ecosystemSimulator = new EcosystemSimulator();
        ecosystemSimulator.start();
    }

    private void showMenu() {
        System.out.println("welcome to Ecosystem Simulator!");

        boolean end = false;
        Scanner scanner = new Scanner(System.in);

        while (!end) {
            System.out.println("choose your action:");
            System.out.println("1. see all ecosystems.");
            System.out.println("2. create ecosystem.");
            System.out.println("3. get ecosystem details.");
            System.out.println("4. update ecosystem.");
            System.out.println("5. delete ecosystem.");
            System.out.println("6. exit.");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println(ecosystemSimulator.getEcosystems());
                    break;
                case 2:
                    System.out.println(ecosystemSimulator.createEcosystem());
                    break;
                case 3:
                    System.out.println(ecosystemSimulator.getEcosystems());
                    System.out.print("Enter ecosystem ID: ");
                    long id = scanner.nextLong();

                    System.out.println(ecosystemSimulator.loadEcosystem(id));
                    break;
                case 4:
                    System.out.println(ecosystemSimulator.editEcosystem());
                    break;
                case 5:
                    System.out.println(ecosystemSimulator.deleteEcosystem());
                    break;
                case 6:
                    end = true;
                    break;
                default:
                    System.out.println("Unknown action");

            }

            System.out.println();
        }
    }

    public void start() {
        showMenu();
    }
}
