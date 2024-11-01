package me.fairygel.controller;

import me.fairygel.manager.EcosystemManager;
import me.fairygel.utils.ScannerUtil;

public class EcosystemController implements MenuController {
    private final EcosystemManager ecosystemManager;

    private final ScannerUtil scanner = new ScannerUtil();

    public EcosystemController() {
        ecosystemManager = new EcosystemManager();

        ecosystemManager.start();
    }

    @Override
    public void showMenu() {

    }
}
