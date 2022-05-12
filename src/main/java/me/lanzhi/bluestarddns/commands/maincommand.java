package me.lanzhi.bluestarddns.commands;

import me.lanzhi.bluestarddns.main.BluestarDDNS;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

public class maincommand implements CommandExecutor, TabExecutor
{
    private final BluestarDDNS plugin;
    String MessageHead=ChatColor.GOLD+"["+ChatColor.AQUA+"BluestarDDNS"+ChatColor.GOLD+"]";

    public maincommand(BluestarDDNS plugin)
    {
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args)
    {
        if (args[0].equals("reload"))
        {
            plugin.getDdnsManager().reload();
            sender.sendMessage(MessageHead+"已重新加载");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args)
    {
        return Collections.singletonList("reload");
    }
}
