package me.fairygel;

import java.util.Scanner;

public class EcosystemController {
    private final EcosystemSimulator ecosystemSimulator;
    private final OrganismManager organismManager;

    private final Scanner scanner = new Scanner(System.in);

    public EcosystemController() {
        ecosystemSimulator = new EcosystemSimulator();
        organismManager = new OrganismManager();

        ecosystemSimulator.start();
    }

    private void showStartMenu() {
        System.out.println("welcome to Ecosystem Simulator!");

        boolean end = false;

        while (!end) {
            System.out.println("choose your action:");
            System.out.println("1. manage simulations.");
            System.out.println("2. manage organisms.");
            System.out.println("3. exit.");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showSimulationsMenu();
                    break;
                case 2:
                    showOrganismsMenu();
                    break;
                case 3:
                    end = true;
                    organismManager.end();
                    break;
                default:
                    System.out.println("Unknown action");
            }
        }
    }

    private boolean chooseOrganism() {
        boolean end = false;
        boolean isAnimal = false;

        while (!end) {
            System.out.println("who you need?");
            System.out.println("1. animal");
            System.out.println("2. plant");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    isAnimal = true;
                    end = true;
                    break;
                case 2:
                    end = true;
                    break;
                default:
                    System.out.println("unknown organism");
            }
        }
        return isAnimal;
    }

    private void showOrganismsMenu() {
        boolean end = false;
        boolean isAnimal = chooseOrganism();

        while (!end) {
            System.out.println("choose your action:");
            System.out.println("1. list organisms");
            System.out.println("2. create organism.");
            System.out.println("3. get info about organism.");
            System.out.println("4. update organism.");
            System.out.println("5. delete organism.");
            System.out.println("6. go back.");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listOrganisms(isAnimal);
                    break;
                case 2:
                    createOrganisms(isAnimal);
                    break;
                case 3:
                    getByIdOrganism(isAnimal);
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    end = true;
                    break;
                default:
                    System.out.println("Unknown action");
            }
        }
    }

    private void getByIdOrganism(boolean isAnimal) {
        System.out.println("enter id of organism: ");

        boolean end = false;
        while (!end) {
            try {
                long id = scanner.nextInt();
                System.out.println(organismManager.getByIdOrganism(id, isAnimal));
                end = true;
            } catch (Exception e) {
                System.out.println("invalid input. please enter a correct number.");
                scanner.nextLine();
            }
        }
    }

    private void createOrganisms(boolean isAnimal) {
        System.out.println("enter name of organism: ");

        String name = scanner.nextLine();

        System.out.println("enter enter energy of organism: ");
        boolean end = false;
        while (!end) {
            try {
                int energy = scanner.nextInt();
                organismManager.createOrganism(name, energy, isAnimal);
                end = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

    }

    private void listOrganisms(boolean isAnimal) {
        System.out.println(organismManager.getOrganisms(isAnimal));
    }

    private void showSimulationsMenu() {
    }

    public void start() {
        showStartMenu();
    }
}
