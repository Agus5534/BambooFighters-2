package io.github.agus5534.bamboofightersv2.utils.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationUtil {

    public static Location of(String s) {
        String[] coords = s.split(",");

        if(coords.length < 2) { return null; }

        return new Location(Bukkit.getWorld(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3]), Float.parseFloat(coords[4]), Float.parseFloat(coords[5]));
    }

    public static List<Location> of(String... s) {
        List<Location> locs = new ArrayList<>();

        Arrays.stream(s).forEach(s1 -> {
            String[] coords = s1.split(",");

            if(coords.length < 2) { return; }

            var w = Bukkit.getWorld(coords[0]);

            if(w == null) { w = Bukkit.getWorlds().get(0); }

            var loc = new Location(w, Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3]), Float.parseFloat(coords[4]), Float.parseFloat(coords[5]));
            locs.add(loc);
        });

        return locs;
    }

    public static String deserialize(Location location) {
        return String.format("%s,%s,%s,%s,%s,%s", location.getWorld().getName(), LocationUtil.r(location.getX()), LocationUtil.r(location.getY()), LocationUtil.r(location.getZ()), LocationUtil.r(location.getYaw()), LocationUtil.r(location.getPitch()));
    }

    private static String r(Double d) {
        return new DecimalFormat("#.##").format(d);
    }

    private static String r(Float f) {
        return new DecimalFormat("#.##").format(f);
    }
}
