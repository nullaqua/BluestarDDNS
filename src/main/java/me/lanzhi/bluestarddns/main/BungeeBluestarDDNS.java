package me.lanzhi.bluestarddns.main;

import me.lanzhi.bluestarddns.RUN.Ddns;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.ChatColor;

public class BungeeBluestarDDNS extends Plugin
{
    @Override
    public void onEnable()
    {
        System.out.println(ChatColor.AQUA+"动态dns已加载!");
        Ddns.test();
    }

    @Override
    public void onDisable()
    {
        System.out.println(ChatColor.RED+"动态dns已卸载!");
    }
}
