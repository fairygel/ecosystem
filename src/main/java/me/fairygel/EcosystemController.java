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

        organismManager.start();

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
                    updateOrganism(isAnimal);
                    break;
                case 5:
                    deleteOrganism(isAnimal);
                    break;
                case 6:
                    end = true;
                    organismManager.saveChanges();
                    break;
                default:
                    System.out.println("Unknown action");
            }
        }
    }

    private long getId(boolean isAnimal) {
        System.out.println("enter id: ");
        long id = 0;

        boolean end = false;

        while (!end) {
            try {
                id = scanner.nextLong();

                if (organismManager.getOrganismById(id, isAnimal) == null)
                    throw new IllegalArgumentException("wrong id");

                end = true;
            } catch (Exception e) {
                System.out.println("invalid input. please enter a correct number.");
                scanner.nextLine();
            }
        }

        return id;
    }

    private void deleteOrganism(boolean isAnimal) {
        long id = getId(isAnimal);

        organismManager.deleteOrganism(id, isAnimal);
    }

    private void updateOrganism(boolean isAnimal) {
        long id = getId(isAnimal);
        System.out.println("enter name of organism: ");

        String name = scanner.next();
        int energy = 0;

        System.out.println("enter enter energy of organism: ");

        boolean end = false;

        while (!end) {
            try {
                energy = scanner.nextInt();
                end = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        organismManager.updateOrganism(id, name, energy, isAnimal);
    }

    private void getByIdOrganism(boolean isAnimal) {
        System.out.println("enter id of organism: ");

        boolean end = false;
        while (!end) {
            try {
                long id = scanner.nextInt();
                System.out.println(organismManager.getOrganismById(id, isAnimal));
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
