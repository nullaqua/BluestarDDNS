package me.lanzhi.bluestarddns.main;

import me.lanzhi.bluestarddns.DDNSManager;
import me.lanzhi.bluestarddns.commands.maincommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class BluestarDDNS extends JavaPlugin
{
    private DDNSManager ddnsManager;
    private BukkitTask task;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        if (getCommand("dns")!=null)
        {
            getCommand("dns").setExecutor(new maincommand(this));
        }
        ddnsManager=new DDNSManager(this);
        long time=getConfig().getLong("time");
        task=ddnsManager.runTaskTimerAsynchronously(this,0,time==0?12000:time*20);
        System.out.println(ChatColor.AQUA+"动态dns已加载!");
    }

    @Override
    public void onDisable()
    {
        task.cancel();
        System.out.println(ChatColor.RED+"动态dns已卸载!");
    }

    public DDNSManager getDdnsManager()
    {
        return ddnsManager;
    }
}
