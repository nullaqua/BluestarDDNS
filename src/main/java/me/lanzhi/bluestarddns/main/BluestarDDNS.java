package me.lanzhi.bluestarddns.main;

import me.lanzhi.bluestarddns.RUN.Ddns;
import me.lanzhi.bluestarddns.commands.maincommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class BluestarDDNS extends JavaPlugin
{
    public static Plugin config;
    @Override
    public void onEnable()
    {
        System.out.println(ChatColor.AQUA+"动态dns已加载!");
        saveDefaultConfig();
        config = me.lanzhi.bluestarddns.main.BluestarDDNS.getProvidingPlugin(me.lanzhi.bluestarddns.main.BluestarDDNS.class);
        //  bsddns
        //  ddns
        //  dns
        //  bluestarddns
        if (getCommand("dns")!=null)
        {
            getCommand("dns").setExecutor(new maincommand());
        }
        Ddns.test();
    }

    @Override
    public void onDisable()
    {
        System.out.println(ChatColor.RED+"动态dns已卸载!");
    }
}
