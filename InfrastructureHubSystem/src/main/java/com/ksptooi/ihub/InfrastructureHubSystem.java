package com.ksptooi.ihub;


import com.ksptooi.Application;
import com.ksptooi.ihub.serviceunit.UnitA;
import com.ksptooi.psm.subsystem.SubSystem;
import com.ksptooi.psm.subsystem.SubSystemEntry;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SubSystemEntry(name = "InfrastructureHubSystem",version = "1.0A")
public class InfrastructureHubSystem extends SubSystem {

    private final Logger log = LoggerFactory.getLogger(InfrastructureHubSystem.class);

    @Inject
    private UnitA ua;

    @Override
    public void onActivated() {

        System.out.println("""
                ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓███████▓▒░\s
                ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░
                ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░
                ░▒▓█▓▒░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓███████▓▒░\s
                ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░
                ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░
                ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░░▒▓███████▓▒░\s""");

        ua.test();

    }


}
