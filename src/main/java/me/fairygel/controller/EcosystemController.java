package me.fairygel.controller;

import me.fairygel.entity.Ecosystem;
import me.fairygel.entity.Soil;
import me.fairygel.entity.Weather;
import me.fairygel.entity.WeatherConditions;
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
        System.out.print("enter name of ecosystem: ");
        String name = scanner.nextLine();

        System.out.println("creating weather for ecosystem...");
        System.out.print("enter temperature: ");
        int temperature = scanner.nextInt();
        WeatherConditions weatherConditions = getWeatherConditions();

        Weather weather = new Weather();
        weather.setTemperature(temperature);
        weather.setConditions(weatherConditions);

        System.out.println("creating soil for ecosystem...");
        System.out.print("enter area of soil: ");
        int area = scanner.nextInt();
        int humidity = getHumidity();

        Soil soil = new Soil();
        soil.setArea(area);
        soil.setHumidity(humidity);

        ecosystemManager.createEcosystem(name, weather, soil);
    }

    private int getHumidity() {
        System.out.println("enter humidity(in percents): ");
        int humidity = scanner.nextInt();

        if (humidity >= 100 || humidity < 0) {
            System.out.println("humidity must be between 0 and 100");
            return getHumidity();
        }

        return humidity;
    }

    private WeatherConditions getWeatherConditions() {
        System.out.println("choose weather conditions: ");
        System.out.println("1. sunny");
        System.out.println("2. rainy");
        System.out.println("3. cloudy");
        System.out.println("4. stormy");
        System.out.print("> ");

        int choice = scanner.nextInt();

        return switch (choice) {
            case 1 -> WeatherConditions.SUNNY;
            case 2 -> WeatherConditions.RAINY;
            case 3 -> WeatherConditions.CLOUDY;
            case 4 -> WeatherConditions.STORMY;
            default -> {
                System.out.println("unknown weather.");
                yield getWeatherConditions();
            }
        };
    }

    private void readEcosystem() {
        listEcosystems();
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
        long id = getId();
        Ecosystem ecosystem = ecosystemManager.readEcosystemById(id);

        String name = ecosystem.getName();
        Weather weather = ecosystem.getCurrentWeather();
        Soil soil = ecosystem.getSoil();

        boolean continueUpdating = true;

        while (continueUpdating) {
            System.out.println("choose what you want to update:");
            System.out.println("1. ecosystem name");
            System.out.println("2. weather");
            System.out.println("3. soil");
            System.out.println("4. finish updating");
            System.out.print("your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("enter new ecosystem name: ");
                    scanner.nextLine();
                    name = scanner.nextLine();
                    break;

                case 2:
                    System.out.println("updating weather...");
                    System.out.print("enter new temperature: ");
                    int temperature = scanner.nextInt();
                    WeatherConditions weatherConditions = getWeatherConditions();

                    weather = new Weather();
                    weather.setTemperature(temperature);
                    weather.setConditions(weatherConditions);
                    break;

                case 3:
                    System.out.println("updating soil...");
                    System.out.print("enter new soil area: ");
                    int area = scanner.nextInt();
                    int humidity = getHumidity();

                    soil = new Soil();
                    soil.setArea(area);
                    soil.setHumidity(humidity);
                    break;

                case 4:
                    continueUpdating = false;
                    break;

                default:
                    System.out.println("invalid choice. please select 1 to 4.");
            }
        }

        ecosystemManager.updateEcosystem(id, name, weather, soil);
        System.out.printf("ecosystem with id=%s updated.%n", id);
    }


    private void deleteEcosystem() {
        long id = getId();

        ecosystemManager.deleteEcosystem(id);
        System.out.printf("ecosystem with id=%s deleted.%n", id);
    }


    private long getId() {
        listEcosystems();

        System.out.print("enter id: ");
        long id = scanner.nextLong();

        // if organism with id not exist, ask again
        if (ecosystemManager.readEcosystemById(id) == null) {
            System.out.printf("there is no ecosystem with id=%s.%n", id);
            return getId();
        }

        return id;
    }

    private void listEcosystems() {
        System.out.println("-------------ECOSYSTEMS-------------");
        System.out.println(ecosystemManager.getEcosystems());
    }
}
