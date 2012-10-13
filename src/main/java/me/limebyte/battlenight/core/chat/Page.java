package me.limebyte.battlenight.core.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

public abstract class Page {

    String title, header, footer;
    String[] lines;
    
    public Page(String title, CommandSender sender) {
        this.title = title;
        this.lines = getLines(sender);
        header = getHeader();
        footer = getFooter();
    }
    
    public String[] getPage() {
        String[] page = new String[lines.length + 2];
        page[0] = header;
        for (int i = 0; i < lines.length; i++) {
            page[i + 1] = lines[i];
        }
        page[lines.length + 1] = footer;
        return page;
    }
    
    private String getHeader() {
        String formattedTitle = " " + ChatColor.WHITE + title + " ";
        int dashCount = 0;
        
        // Calculate total number of dashes
        int i = formattedTitle.length();
        while (i < ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
            i++;
            dashCount++;
        }
        
        // Create dashes String for a single side
        StringBuilder dashes = new StringBuilder();
        dashes.append(ChatColor.DARK_GRAY);
        for (i = 0; i < dashCount / 2; i++) {
            dashes.append("-");
        }
        
        return dashes.toString() + formattedTitle + dashes.toString();
    }
    
    private String getFooter() {
        StringBuilder dashes = new StringBuilder();
        dashes.append(ChatColor.DARK_GRAY);
        
        if (header != null) {
            // Create dashes String
            for (int i = 0; i < header.length(); i++) {
                dashes.append("-");
            }
        } else {
            // Create dashes String
            for (int i = 0; i < ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH; i++) {
                dashes.append("-");
            }
        }
        
        return dashes.toString();
    }
    
    public abstract String[] getLines(CommandSender sender);
}
