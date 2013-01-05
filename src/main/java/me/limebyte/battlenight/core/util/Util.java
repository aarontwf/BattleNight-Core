package me.limebyte.battlenight.core.util;

import java.util.List;
import java.util.Random;

public class Util {

    private static final Random random = new Random();

    private Util() {

    }

    public static Object getRandom(List<?> objects) {
        return objects.get(random.nextInt(objects.size()));
    }

}
