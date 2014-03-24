package org.battlenight.core.game.type;

import org.battlenight.api.game.type.GameType;

public class CaptureTheFlag extends GameType {

    public String getDisplayName() {
        return "Capture the Flag";
    }

    @Override
    public boolean hasObjective() {
        return true;
    }

    @Override
    public boolean onDamage() {
        return true;
    }

    @Override
    public void onKill() {

    }

    @Override
    public boolean onDeath() {
        return true;
    }

    @Override
    public void onRespawn() {

    }

}
