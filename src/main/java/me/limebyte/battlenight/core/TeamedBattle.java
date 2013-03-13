package me.limebyte.battlenight.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.limebyte.battlenight.api.tosort.Waypoint;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.SafeTeleporter;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class TeamedBattle extends SimpleBattle {

    private List<Team> teams = new ArrayList<Team>();

    public TeamedBattle(int duration, int minPlayers, int maxPlayers) {
        super(duration, minPlayers, maxPlayers);
    }

    @Override
    public void addDeath(Player player) {
        super.addDeath(player);

        Team team = getTeam(player);
        if (team != null) {
            team.addDeath();
        }
    }

    @Override
    public void addKill(Player player) {
        super.addKill(player);

        Team team = getTeam(player);
        if (team != null) {
            team.addKill();
        }
    }

    @Override
    public boolean addPlayer(Player player) {
        if (!assignTeam(player)) return false;
        boolean worked = super.addPlayer(player);
        if (worked) {
            Team team = getTeam(player);
            Messenger.tell(player, Message.JOINED_TEAM, team);
            Messenger.tellEveryoneExcept(player, true, Message.PLAYER_JOINED_TEAM, player, team);
        }
        return worked;
    }

    public boolean addTeam(Team team) {
        String name = team.getName();
        for (Team t : teams) {
            if (t.getName().equals(name)) return false;
        }
        if (!teams.add(team)) return false;
        setMinPlayers(teams.size());
        return true;
    }

    public boolean areEnemies(Player player1, Player player2) {
        return !getTeam(player1).getName().equals(getTeam(player2).getName());
    }

    private boolean assignTeam(Player player) {
        if (teams.size() < 2) return false;

        Team smallest = null;
        for (Team team : teams) {
            if (smallest == null || team.getSize() < smallest.getSize()) {
                smallest = team;
            }
        }

        setTeam(player, smallest);
        return true;
    }

    public List<Team> getLeadingTeams() {
        if (teams.size() == 0) return null;

        List<Team> leading = new ArrayList<Team>();
        for (Team team : teams) {
            if (leading.isEmpty()) {
                leading.add(team);
                continue;
            }

            Team inLead = leading.get(0);
            double leadKD = inLead.getKDR();
            double teamKD = team.getKDR();

            if (leadKD > teamKD) {
                continue;
            }
            if (leadKD < teamKD) {
                leading.clear();
            }

            leading.add(team);
        }

        return leading;
    }

    public Team getTeam(Player player) {
        String teamName = Metadata.getString(player, "team");
        if (teamName != null) {
            for (Team team : teams) {
                if (team.getName().equals(teamName)) return team;
            }
        }
        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    protected String getWinMessage() {
        String message;
        List<Team> leading = getLeadingTeams();

        if (leading.isEmpty() || leading.size() == getTeams().size()) {
            message = Message.DRAW.getMessage();
        } else if (leading.size() == 1) {
            message = Messenger.format(Message.TEAM_WON, leading.get(0));
        } else {
            message = Messenger.format(Message.TEAM_WON, leading);
        }

        return message;
    }

    @Override
    public boolean removePlayer(Player player) {
        setTeam(player, null);
        return super.removePlayer(player);
    }

    public boolean removeTeam(Team team) {
        if (!teams.remove(team)) return false;
        setMinPlayers(teams.size());
        return true;
    }

    public void setTeam(Player player, Team team) {
        String prevTeam = Metadata.getString(player, "team");
        if (prevTeam != null) {
            for (Team t : teams) {
                if (t.getName().equals(prevTeam)) {
                    t.decrementSize();
                    break;
                }
            }
        }

        if (team != null) {
            Metadata.set(player, "team", team.getName());
            team.incrementSize();
        } else {
            Metadata.remove(player, "team");
        }
    }

    @Override
    public boolean shouldEnd() {
        if (!isInProgress()) return false;
        for (Team team : getTeams()) {
            if (team.getSize() < 1) return true;
        }
        return false;
    }

    @Override
    public boolean stop() {
        for (String name : getPlayers()) {
            Player player = toPlayer(name);
            if (player == null) {
                continue;
            }
            setTeam(player, null);
        }

        boolean result = super.stop();

        for (Team team : teams) {
            if (team == null) {
                continue;
            }
            team.reset(this);
        }

        return result;
    }

    @Override
    protected void teleportAllToSpawn() {
        @SuppressWarnings("unchecked")
        List<Waypoint> waypoints = (ArrayList<Waypoint>) getArena().getSpawnPoints().clone();
        List<Waypoint> free = waypoints;
        Random random = new Random();

        HashMap<String, Waypoint> spawns = new HashMap<String, Waypoint>();

        for (Team team : getTeams()) {
            if (free.size() <= 0) {
                free = waypoints;
            }
            int id = random.nextInt(free.size());
            spawns.put(team.getName(), free.get(id));
            free.remove(id);
        }

        for (String name : getPlayers()) {
            Player player = toPlayer(name);
            if (player == null || !player.isOnline()) {
                continue;
            }
            SafeTeleporter.tp(player, spawns.get(Metadata.getString(player, "team")).getLocation());
        }
    }

    @Override
    public Location toSpectator(Player player, boolean death) {
        if (!containsPlayer(player)) return null;
        setTeam(player, null);
        return super.toSpectator(player, death);
    }
}
