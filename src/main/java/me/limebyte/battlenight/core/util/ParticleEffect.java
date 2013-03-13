package me.limebyte.battlenight.core.util;

import java.util.logging.Level;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleEffect {

    private enum Direction {
        SOUTH_EAST(0), SOUTH(1), SOUTH_WEST(2), EAST(3), UP(4), WEST(5), NORTH_EAST(6), NORTH(7), NORTH_WEST(8);

        private int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final static int SPIRAL_PARTICLE_COUNT = 30;

    public static void classSelect(Player player, String type) {
        if (type.equalsIgnoreCase("ender")) {
            playEnderEffect(player.getLocation(), Direction.UP);
        } else if (type.equalsIgnoreCase("smoke")) {
            playSmokeEffect(player.getLocation());
        } else {
            Messenger.debug(Level.INFO, "Invalid or no particle type.");
            return;
        }
    }

    private static void playEnderEffect(Location location, Direction direction) {
        Location loc;
        for (double h = 0.0; h < 1.8; h += 0.2) {
            loc = location.clone().add(0, h, 0);
            loc.getWorld().playEffect(location, Effect.ENDER_SIGNAL, direction.getValue());
            loc.getWorld().playEffect(location, Effect.ENDER_SIGNAL, direction.getValue());
        }
    }

    private static void playSmokeEffect(Location location) {
        spiral(location, Effect.SMOKE);
    }

    private static void spiral(Location location, Effect effect) {
        for (double i = 0; i < SPIRAL_PARTICLE_COUNT; i++) {
            double deg = i / SPIRAL_PARTICLE_COUNT * 360;
            double diffX = Math.rint(10 * Math.sin(deg)) / 10;
            double diffZ = Math.rint(10 * Math.cos(deg)) / 10;

            Location loc = location.clone();
            loc.setX(loc.getBlockX() + 0.5 + diffX);
            loc.setY(Math.floor(loc.getY()) + i / SPIRAL_PARTICLE_COUNT * 1.8);
            loc.setZ(loc.getBlockZ() + 0.5 + diffZ);

            loc.getWorld().playEffect(loc, effect, Direction.UP.getValue());
        }
    }
}
