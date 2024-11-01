package me.fairygel.controller;

import me.fairygel.entity.organism.Organism;
import me.fairygel.manager.OrganismManager;
import me.fairygel.utils.ScannerUtil;

public class OrganismController implements MenuController {
    private String organismType = "";
    private boolean isAnimal = false;

    private final OrganismManager organismManager;

    private final ScannerUtil scanner = new ScannerUtil();

    public OrganismController() {
        organismManager = new OrganismManager();
    }

    @Override
    public void showMenu() {
        boolean end = false;
        chooseOrganism();

        while (!end) {
            System.out.println("choose your action:");
            System.out.printf("1. get all %s's.%n", organismType);
            System.out.printf("2. create %s.%n", organismType);
            System.out.printf("3. get info about %s.%n", organismType);
            System.out.printf("4. update %s.%n", organismType);
            System.out.printf("5. delete %s.%n", organismType);
            System.out.println("6. go back.");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    listOrganisms();
                    break;
                case 2:
                    createOrganisms();
                    break;
                case 3:
                    readOrganism();
                    break;
                case 4:
                    updateOrganism();
                    break;
                case 5:
                    deleteOrganism();
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

    private void chooseOrganism() {
        boolean end = false;

        while (!end) {
            System.out.println("who you need?");
            System.out.println("1. animal");
            System.out.println("2. plant");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    isAnimal = true;
                    organismType = "animal";
                    end = true;
                    break;
                case 2:
                    isAnimal = false;
                    organismType = "plant";
                    end = true;
                    break;
                default:
                    System.out.println("unknown organism");
            }
        }

        organismManager.start(isAnimal);
    }

    private long getId() {
        listOrganisms();

        System.out.println("enter id: ");
        long id = scanner.nextLong();

        // if organism with id not exist, ask again
        if (organismManager.readOrganismById(id) == null) {
            System.out.printf("there is no organism with id=%s.%n", id);
            return getId();
        }

        return id;
    }

    // -----------------------CRUD OPERATIONS--------------------------

    private void createOrganisms() {
        System.out.println("enter name of organism: ");
        String name = scanner.nextLine();

        System.out.println("enter enter energy of organism: ");
        int energy = scanner.nextInt();

        organismManager.createOrganism(name, energy);
    }

    // can read not existing organisms
    private void readOrganism() {
        System.out.println("enter id of organism: ");

        long id = scanner.nextLong();
        Organism organism = organismManager.readOrganismById(id);

        if (organism == null) {
            System.out.printf("there is no organism with id=%s.%n", id);
        } else {
            String type = isAnimal? "animal" : "plant";
            String name = organism.getName();
            int energy = organism.getEnergy();

            System.out.printf("%s with id = %s.%n name = %s, energy = %s.%n", type, id, name, energy);
        }
    }

    private void updateOrganism() {
        long id = getId();

        System.out.println("enter name of organism: ");
        String name = scanner.nextLine();

        System.out.println("enter enter energy of organism: ");
        int energy = scanner.nextInt();

        organismManager.updateOrganism(id, name, energy);
    }

    private void deleteOrganism() {
        long id = getId();

        organismManager.deleteOrganism(id, isAnimal);
    }

    private void listOrganisms() {
        String type = isAnimal? "ANIMALS" : "PLANTS";

        System.out.printf("-------------%s-------------%n", type);
        System.out.println(organismManager.getOrganisms());
    }
}