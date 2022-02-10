package me.lanzhi.bluestarddns.commands;

import me.lanzhi.bluestarddns.RUN.Ddns;
import me.lanzhi.bluestarddns.main.BluestarDDNS;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.lanzhi.bluestarddns.main.BluestarDDNS.config;

public class maincommand implements CommandExecutor, TabExecutor
{
    String MessageHead=ChatColor.AQUA+"[BSDDNS]";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!sender.hasPermission("bsddns.use"))
        {
            sender.sendMessage(MessageHead+ChatColor.RED+"你没有权限使用此指令!");
            return false;
        }
        if (args.length==3&&args[0].equals("set"))
        {
            if (args[1].equals("accessKeySecret"))
            {
                config.getConfig().set("AccessKeySecret",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                config.saveConfig();
                config.reloadConfig();
                return true;
            }
            else if (args[1].equals("accessKeyID"))
            {
                config.getConfig().set("AccessKeyID",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                config.saveConfig();
                config.reloadConfig();
                return true;
            }
            else if (args[1].equals("name"))
            {
                config.getConfig().set("name",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                config.saveConfig();
                config.reloadConfig();
                return true;
            }
            else if (args[1].equals("rR"))
            {
                config.getConfig().set("rR",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                config.saveConfig();
                config.reloadConfig();
                return true;
            }
            else if(args[1].equals("time"))
            {
                try
                {
                    long t = Integer.parseInt(args[2]);
                    config.getConfig().set("time",t);
                    sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                    config.saveConfig();
                    config.reloadConfig();
                    return true;
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(MessageHead+ChatColor.RED+"错误!");
                    return false;
                }
            }
            else  if(args[1].equals("region"))
            {
                config.getConfig().set("region",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                config.saveConfig();
                config.reloadConfig();
                return true;
            }
            else if(args[1].equals("quiet"))
            {
                boolean j;
                try
                {
                    j = Boolean.parseBoolean(args[2]);
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(MessageHead+ChatColor.RED+"错误!");
                    return false;
                }
                config.getConfig().set("quiet",j);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                config.saveConfig();
                config.reloadConfig();
                return true;
            }
            else
            {
                sender.sendMessage(MessageHead+ChatColor.RED+"错误! 请使用 /bsddns ? 获取帮助");
                return false;
            }
        }
        else if (args.length>0&&args[0].equals("add"))
        {
            sender.sendMessage(MessageHead+ChatColor.RED+"此功能开发中!请谅解");
            return false;
        }
        else if (args.length==1&&args[0].equals("get"))
        {
            sender.sendMessage(MessageHead+ChatColor.BLUE+"当前IP:"+Ddns.getip());
            if(!(Objects.requireNonNull(config.getConfig().getString("AccessKeySecret")).isEmpty())||
                    config.getConfig().getString("AccessKeyID").isEmpty()||
                    config.getConfig().getString("name").isEmpty()||
                    config.getConfig().getString("rR").isEmpty()||
                    config.getConfig().getString("region").isEmpty())
            {
                sender.sendMessage(MessageHead+ChatColor.BLUE+"解析IP:"+Ddns.getjsip());
            }
            return true;
        }
        else if (args.length>0&&(args[0].equals("?")||args[0].equals("？")||args[0].equals("help")))
        {
            sender.sendMessage(MessageHead+ChatColor.WHITE+"bsddns set :");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    accessKeySecret         设置阿里云AccessKeySecret");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    accessKeyID             设置阿里云AccessKeyID");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    name                    设置你的主域名");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    rR                      设置主机记录(二级域名)");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    region                  设置地域ID(可以不设置,则默认为北京地区)");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    time                    设置每多少秒检查dns(不设置则默认为5分钟)");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    justPutLogWhenError     是否仅在出错时输出日志,默认为false,后台消息太多,太吵可设为true");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"bsddns get 获取当前IP及解析IP");
            return true;
        }
        else
        {
            sender.sendMessage(MessageHead+ChatColor.RED+"错误! 请使用 /bsddns ? 获取帮助");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length==1&&sender.hasPermission("bsddns.use"))
        {
            List<String> tablist=new ArrayList<>();
            tablist.add("add");
            tablist.add("get");
            tablist.add("set");
            return tablist;
        }
        else if(args.length==2&&sender.hasPermission("bsddns.use")&&args[0].equals("set"))
        {
            List<String> tablist=new ArrayList<>();
            tablist.add("accessKeySecret");
            tablist.add("accessKeyID");
            tablist.add("name");
            tablist.add("rR");
            tablist.add("time");
            tablist.add("region");
            tablist.add("quiet");
            return tablist;
        }
        else if(args.length==3&&sender.hasPermission("bsddns.use")&&args[1].equals("quiet"))
        {
            List<String> tablist=new ArrayList<>();
            tablist.add("true");
            tablist.add("false");
            return tablist;
        }
        else if(args.length==2&&sender.hasPermission("bsddns.use")&&args[0].equals("add"))
        {
            List<String> tablist=new ArrayList<>();
            tablist.add("主域名");
            return tablist;
        }
        else if(args.length==3&&sender.hasPermission("bsddns.use")&&args[0].equals("add"))
        {
            List<String> tablist=new ArrayList<>();
            tablist.add("rR主机记录");
            return tablist;
        }
        else if(args.length==4&&sender.hasPermission("bsddns.use")&&args[0].equals("add"))
        {
            List<String> tablist=new ArrayList<>();
            tablist.add("TTL");
            return tablist;
        }
        else
        {
            List<String> tablist=new ArrayList<>();
            return tablist;
        }
    }
}
