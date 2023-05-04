package io.github.agus5534.bamboofightersv2.utils.location;

import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Region {
    private final List<Location> locationList;
    public Region(Location... locations) {
        locationList = Arrays.stream(locations).toList();
    }

    public Region(List<Location> locations) {
        this.locationList = locations;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public Location getRandomLocation() {
        return locationList.get(new Random().nextInt(locationList.size()));
    }
}
