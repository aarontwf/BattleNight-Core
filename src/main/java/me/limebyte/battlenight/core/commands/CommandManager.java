package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private static List<BNCommand> commands = new ArrayList<BNCommand>();
    
    public static void register(BNCommand command) {
        commands.add(command);
    }
    
    public static List<BNCommand> getCommands() {
        return commands;
    }
    
}
