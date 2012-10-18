package me.limebyte.battlenight.core.util;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SmokeEffect {

    private final static int SPIRAL_SMOKE_COUNT = 16;

    public static void play(final Player player) {
        smokeSpiral(player);
    }

    private static void smokeSpiral(Player player) {
        for (double i = 0; i < SPIRAL_SMOKE_COUNT; i++) {
            double deg = i / SPIRAL_SMOKE_COUNT * 360;
            double diffX = Math.rint(10 * (Math.sin(deg))) / 10;
            double diffZ = Math.rint(10 * (Math.cos(deg))) / 10;

            Location loc = player.getLocation();
            loc.setX(loc.getBlockX() + 0.5 + diffX * 1.5);
            loc.setY(loc.getY() + i / SPIRAL_SMOKE_COUNT);
            loc.setZ(loc.getBlockZ() + 0.5 + diffZ * 1.5);

            playSmokeEffect(loc, Direction.UP);
        }
    }

    private static void playSmokeEffect(Location location, Direction direction) {
        location.getWorld().playEffect(location, Effect.SMOKE, direction.getValue());
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
