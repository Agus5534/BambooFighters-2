package io.github.agus5534.bamboofightersv2.utils.location;


import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Deprecated(forRemoval = true)
public class OvniAnimation implements Listener {
    int taskID = 0;
    private final Player pl;
    private final GameTeam team;
    private final PlayerSelection selection;
    public OvniAnimation(Player pl, GameTeam team, PlayerSelection selection) {
        this.pl = pl;
        this.team = team;
        this.selection = selection;

        Bukkit.getPluginManager().registerEvents(this, BambooFighters.instance);
        this.execute();
    }
    private void execute() {
        pl.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10000, 1, false, false, false));

        var loc = LocationUtil.of(
                "Panda,-1.46,31,-5.52,89.37,-1.95","Panda,-1.46,32.04,-5.52,92.52,-4.2","Panda,-1.46,32.78,-5.52,102.12,-2.85","Panda,-1.46,33.15,-5.52,106.77,-3.6","Panda,-1.46,33.9,-5.52,113.52,-3.6","Panda,-1.46,34.64,-5.52,119.82,-3.9","Panda,-1.46,35.01,-5.52,126.57,-6","Panda,-1.46,35.75,-5.52,133.62,-4.5","Panda,-1.46,36.5,-5.52,145.32,-4.35","Panda,-1.46,37.24,-5.52,155.97,-2.4","Panda,-1.46,38.36,-5.52,164.82,-2.85","Panda,-1.46,39.48,-5.52,170.67,-3","Panda,-1.46,40.59,-5.52,176.97,-2.55","Panda,-1.46,41.34,-5.52,-174.48,-3","Panda,-1.46,42.08,-5.52,-167.88,-4.65","Panda,-1.46,43.2,-5.52,-158.88,-4.8","Panda,-1.46,43.94,-5.52,-147.33,-3.45","Panda,-1.46,44.69,-5.52,-140.73,-4.5","Panda,-1.46,45.8,-5.52,-130.38,-3.15","Panda,-1.46,46.92,-5.52,-116.88,-0.45","Panda,-1.46,48.42,-5.52,-104.73,-0.15","Panda,-1.46,49.91,-5.52,-93.93,-0.9","Panda,-1.46,51.03,-5.52,-81.63,1.5","Panda,-1.46,52.15,-5.52,-63.78,1.95","Panda,-1.46,52.89,-5.52,-55.08,-4.65","Panda,-1.46,54.01,-5.52,-52.08,-6.15","Panda,-1.46,55.5,-5.52,-43.23,-5.25","Panda,-1.46,56.62,-5.52,-34.38,-6.45","Panda,-1.46,58.12,-5.52,-27.33,-6.6","Panda,-1.46,59.61,-5.52,-19.98,-7.35","Panda,-1.46,61.11,-5.52,-9.03,-8.25","Panda,-1.46,62.6,-5.52,0.27,-8.7","Panda,-1.46,64.1,-5.52,14.97,-7.65","Panda,-1.46,65.21,-5.52,28.17,-8.25","Panda,-1.46,67.08,-5.52,30.57,-6.6","Panda,-1.46,68.58,-5.52,36.12,-7.35","Panda,-1.46,70.45,-5.52,44.37,-7.35","Panda,-1.46,71.94,-5.52,50.37,-8.1","Panda,-1.46,73.81,-5.52,54.87,-10.8","Panda,-1.46,74.93,-5.52,60.87,-11.1","Panda,-1.46,76.8,-5.52,66.72,-10.8","Panda,-1.46,78.67,-5.52,75.12,-10.8","Panda,-1.46,80.17,-5.52,82.17,-10.8","Panda,-1.46,81.66,-5.52,85.47,-9.45","Panda,-1.46,82.41,-5.52,93.72,-13.35","Panda,-1.46,83.9,-5.52,97.62,-13.35","Panda,-1.46,85.4,-5.52,107.97,-16.35","Panda,-1.46,87.27,-5.52,116.22,-16.95","Panda,-1.46,88.38,-5.52,126.42,-17.85","Panda,-1.46,89.88,-5.52,130.47,-18.3","Panda,-1.46,91.37,-5.52,133.02,-18.9","Panda,-1.46,92.49,-5.52,141.57,-18.9","Panda,-1.46,94.36,-5.52,147.87,-19.05","Panda,-1.46,95.86,-5.52,154.32,-20.1","Panda,-1.46,98.09,-5.52,163.17,-18","Panda,-1.46,99.59,-5.52,176.52,-15.6","Panda,-1.46,101.08,-5.52,-171.63,-14.25","Panda,-1.46,102.2,-5.52,-147.48,-10.5","Panda,-1.46,104.07,-5.52,-119.13,-6.75","Panda,-1.46,105.94,-5.52,-103.98,-3.6","Panda,-1.46,107.06,-5.52,-93.03,0.3","Panda,-1.46,107.2,-5.52,-80.88,3.3"
        );

        var instance = BambooFighters.instance;

        instance.onAnimationPlayer.add(pl);
        var iterator = loc.iterator();

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, ()-> {
            if(!iterator.hasNext()) {
                Bukkit.getScheduler().cancelTask(taskID);

                pl.removePotionEffect(PotionEffectType.GLOWING);

                Bukkit.broadcast(TranslatableText.basicTranslate("team.selection.player_selected",team.getOwner().getName(), pl.getName()));
                team.addMember(pl);
                selection.setHasSelected(true);

                HandlerList.unregisterAll(this);

                return;
            }

            pl.teleport(iterator.next());
        },5L, 4L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getPlayer().getUniqueId() == pl.getUniqueId()) {
            event.setCancelled(true);
        }
    }
}
