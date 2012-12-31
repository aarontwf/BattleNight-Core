package me.limebyte.battlenight.core.old;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.listeners.SignListener;
import me.limebyte.battlenight.core.util.ArrowUtil;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.SafeTeleporter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class Battle {

    private int redTeam = 0;
    private int blueTeam = 0;
    private boolean inLounge = false;
    private boolean inProgress = false;
    private boolean ending = false;
    public boolean redTeamIronClicked = false;
    public boolean blueTeamIronClicked = false;

    public final Map<String, Team> usersTeam = new HashMap<String, Team>();
    public final Set<String> spectators = new HashSet<String>();

    public void addPlayer(Player player) {
        if (Util.preparePlayer(player)) {
            String name = player.getName();
            Team team;

            if (blueTeam > redTeam) {
                team = Team.RED;
                redTeam++;
                SafeTeleporter.tp(player, Waypoint.RED_LOUNGE);
            } else {
                team = Team.BLUE;
                blueTeam++;
                SafeTeleporter.tp(player, Waypoint.BLUE_LOUNGE);
            }

            usersTeam.put(name, team);
            Messenger.tell(player, Message.JOINED_TEAMED_BATTLE, team);
            Messenger.tellEveryoneExcept(player, true, Message.PLAYER_JOINED_TEAMED_BATTLE, player, team);

            Util.setNames(player);
            inLounge = true;
        } else {
            Messenger.tell(player, Message.INVENTORY_NOT_EMPTY);
        }
    }

    public void removePlayer(Player player, boolean death, String msg1, String msg2) {
        String name = player.getName();

        if (usersTeam.containsKey(name)) {
            Team team = usersTeam.get(name);
            boolean sendMsg1 = msg1 != null;

            if (team.equals(Team.RED)) {
                redTeam--;
            } else if (team.equals(Team.BLUE)) {
                blueTeam--;
            }

            if (sendMsg1) {
                Messenger.tellEveryoneExcept(player, team.getColour() + name + ChatColor.WHITE + " " + msg1, true);
            }

            if (msg2 != null) {
                Messenger.tell(player, msg2);
            }

            // If red or blue won
            if (redTeam == 0 || blueTeam == 0) {

                ending = true;

                // If the battle started
                if (!inLounge) {
                    // If red won
                    if (redTeam > 0) {
                        Messenger.tellEveryone(true, Message.TEAM_WON, Team.RED.getColour() + Team.RED.getName());
                        // If blue won
                    } else if (blueTeam > 0) {
                        Messenger.tellEveryone(true, Message.TEAM_WON, Team.BLUE.getColour() + Team.BLUE.getName());
                        // If neither team won
                    } else {
                        Messenger.tellEveryone(true, Message.DRAW);
                    }
                }

                Iterator<String> it = usersTeam.keySet().iterator();
                while (it.hasNext()) {
                    String currentName = it.next();
                    if (Bukkit.getPlayerExact(currentName) != null) {
                        Player currentPlayer = Bukkit.getPlayerExact(currentName);
                        if (currentPlayer != player) {
                            resetPlayer(currentPlayer, true, it, false);
                        }
                    }
                }

                resetBattle();
            }

            if (!death) {
                resetPlayer(player, true, null, false);
            }
        } else {
            Messenger.log(Level.WARNING, "Failed to remove player '" + name + "' from the Battle as they are not in it.");
        }
    }

    public void resetPlayer(Player player, boolean teleport, Iterator<String> it, boolean keepData) {
        PlayerData.reset(player);
        PlayerData.restore(player, teleport, keepData);
        SignListener.cleanSigns(player);
        Metadata.remove(player, "class");

        if (it != null) {
            it.remove();
        } else {
            usersTeam.remove(player.getName());
        }

        try {
            TagAPI.refreshPlayer(player);
        } catch (Exception e) {
        }
    }

    private void resetBattle() {
        removeAllSpectators();
        SignListener.cleanSigns();
        inProgress = false;
        inLounge = false;
        ending = false;
        redTeamIronClicked = false;
        blueTeamIronClicked = false;
        spectators.clear();
        usersTeam.clear();
        redTeam = 0;
        blueTeam = 0;
    }

    public void start() {
        inProgress = true;
        inLounge = false;
        Messenger.tellEveryone(true, Message.BATTLE_STARTED);
        Util.teleportAllToSpawn();
        SignListener.cleanSigns();
    }

    public void stop() {
        if (!inLounge) {
            if (blueTeam > redTeam) {
                Messenger.tellEveryone(true, Message.TEAM_WON, Team.BLUE.getColour() + Team.BLUE.getName());
            } else if (redTeam > blueTeam) {
                Messenger.tellEveryone(true, Message.TEAM_WON, Team.RED.getColour() + Team.RED.getName());
            } else {
                Messenger.tellEveryone(true, Message.DRAW);
            }
        }

        Iterator<String> it = usersTeam.keySet().iterator();
        while (it.hasNext()) {
            String currentName = it.next();
            if (Bukkit.getPlayerExact(currentName) != null) {
                Player currentPlayer = Bukkit.getPlayerExact(currentName);
                resetPlayer(currentPlayer, true, it, false);
            }
        }

        resetBattle();
    }

    public boolean isInLounge() {
        return inLounge;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public boolean isEnding() {
        return ending;
    }

    public void addSpectator(Player player, String type) {
        if (type.equals("death")) {
            Messenger.tell(player, Message.WELCOME_SPECTATOR_DEATH);
        } else {
            SafeTeleporter.tp(player, Waypoint.SPECTATOR);
            Messenger.tell(player, Message.WELCOME_SPECTATOR);
        }

        if (!PlayerData.storageContains(player)) {
            PlayerData.store(player);
        }

        PlayerData.reset(player);
        player.setGameMode(GameMode.ADVENTURE);
        ArrowUtil.equipArrows(player);
        player.setAllowFlight(true);

        for (String n : usersTeam.keySet()) {
            if (Bukkit.getPlayerExact(n) != null) {
                Bukkit.getPlayerExact(n).hidePlayer(player);
            }
        }

        spectators.add(player.getName());
    }

    public void removeSpectator(Player player, Iterator<String> it) {
        PlayerData.reset(player);
        PlayerData.restore(player, true, false);
        Messenger.tell(player, Message.GOODBYE_SPECTATOR);

        if (it != null) {
            it.remove();
        } else {
            spectators.remove(player.getName());
        }
    }

    public void removeAllSpectators() {
        Iterator<String> it = spectators.iterator();
        while (it.hasNext()) {
            String currentName = it.next();
            if (Bukkit.getPlayerExact(currentName) != null) {
                Player currentPlayer = Bukkit.getPlayerExact(currentName);
                removeSpectator(currentPlayer, it);
            }
        }
    }
}
