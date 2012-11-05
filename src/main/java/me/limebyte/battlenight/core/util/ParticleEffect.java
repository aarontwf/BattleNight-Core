package me.limebyte.battlenight.core.util;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;

public class ParticleEffect {

    private final static int SPIRAL_PARTICLE_COUNT = 30;

    public static void spiral(Player player) {
        for (double i = 0; i < SPIRAL_PARTICLE_COUNT; i++) {
            double deg = i / SPIRAL_PARTICLE_COUNT * 360;
            double diffX = Math.rint(10 * (Math.sin(deg))) / 10;
            double diffZ = Math.rint(10 * (Math.cos(deg))) / 10;

            Location loc = player.getLocation();
            loc.setX(loc.getBlockX() + 0.5 + diffX);
            loc.setY(Math.floor(loc.getY()) + (i / SPIRAL_PARTICLE_COUNT) * 2);
            loc.setZ(loc.getBlockZ() + 0.5 + diffZ);

            playOrangeEffect(loc);
        }
    }

    private static void playSmokeEffect(Location location, Direction direction) {
        location.getWorld().playEffect(location, Effect.SMOKE, direction.getValue());
    }

    private static void playOrangeEffect(Location location) {
        location.getWorld().playEffect(location, Effect.POTION_BREAK, PotionType.FIRE_RESISTANCE.getDamageValue());
    }

    private enum Direction {
        SOUTH_EAST(0),
        SOUTH(1),
        SOUTH_WEST(2),
        EAST(3),
        UP(4),
        WEST(5),
        NORTH_EAST(6),
        NORTH(7),
        NORTH_WEST(8);

        private int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
