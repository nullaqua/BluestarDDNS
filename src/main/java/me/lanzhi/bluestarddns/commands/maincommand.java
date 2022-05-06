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
        if (args.length==3&&args[0].equals("set"))
        {
            if (args[1].equals("accessKeySecret"))
            {
                plugin.getConfig().set("AccessKeySecret",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                plugin.saveConfig();

                return true;
            }
            else if (args[1].equals("accessKeyID"))
            {
                plugin.getConfig().set("AccessKeyID",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                plugin.saveConfig();

                return true;
            }
            else if (args[1].equals("name"))
            {
                plugin.getConfig().set("name",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                plugin.saveConfig();

                return true;
            }
            else if (args[1].equals("rR"))
            {
                plugin.getConfig().set("rR",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                plugin.saveConfig();

                return true;
            }
            else if (args[1].equals("time"))
            {
                try
                {
                    long t=Long.parseLong(args[2]);
                    plugin.getConfig().set("time",t);
                    sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                    plugin.saveConfig();
                    return true;
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(MessageHead+ChatColor.RED+"错误!");
                    return true;
                }
            }
            else if (args[1].equals("region"))
            {
                plugin.getConfig().set("region",args[2]);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                plugin.saveConfig();

                return true;
            }
            else if (args[1].equals("quiet"))
            {
                boolean j;
                try
                {
                    j=Boolean.parseBoolean(args[2]);
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(MessageHead+ChatColor.RED+"错误!");
                    return true;
                }
                plugin.getConfig().set("quiet",j);
                sender.sendMessage(MessageHead+ChatColor.GREEN+"设置成功!");
                plugin.saveConfig();

                return true;
            }
            else
            {
                sender.sendMessage(MessageHead+ChatColor.RED+"错误! 请使用 /bsddns ? 获取帮助");
                return true;
            }
        }
        else if (args.length>0&&args[0].equals("add"))
        {
            sender.sendMessage(MessageHead+ChatColor.RED+"此功能开发中!");
            return true;
        }
        else if (args.length==1&&args[0].equals("get"))
        {
            sender.sendMessage(MessageHead+ChatColor.BLUE+"当前IP:"+plugin.getDdnsManager().getCurrentHostIP());
            if (!(Objects.requireNonNull(plugin.getConfig().getString("AccessKeySecret")).isEmpty())||plugin.getConfig().getString("AccessKeyID").isEmpty()||plugin.getConfig().getString("name").isEmpty()||plugin.getConfig().getString("rR").isEmpty()||plugin.getConfig().getString("region").isEmpty())
            {
                sender.sendMessage(MessageHead+ChatColor.BLUE+"解析IP:"+plugin.getDdnsManager().getDnsIp());
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
            sender.sendMessage(MessageHead+ChatColor.WHITE+"    quiet                   是否仅在出错时输出日志,默认为true,后台消息太多,太吵可设为true");
            sender.sendMessage(MessageHead+ChatColor.WHITE+"bsddns get 获取当前IP及解析IP");
            return true;
        }
        else
        {
            sender.sendMessage(MessageHead+ChatColor.RED+"错误! 请使用 /bsddns ? 获取帮助");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args)
    {
        if (args.length==1&&sender.hasPermission("bsddns.use"))
        {
            return Arrays.asList("set","get","add");
        }
        else if (args.length==2&&args[0].equals("set"))
        {
            return Arrays.asList("accessKeySecret","accessKeyID","name","rR","time","region","quiet");
        }
        else if (args.length==3&&args[1].equals("quiet"))
        {
            return Arrays.asList("true","false");
        }
        else if (args.length==2&&args[0].equals("add"))
        {
            return Collections.singletonList("主域名");
        }
        else if (args.length==3&&args[0].equals("add"))
        {
            return Collections.singletonList("rR主机记录");
        }
        else if (args.length==4&&args[0].equals("add"))
        {
            return Collections.singletonList("TTL");
        }
        else
        {
            return Collections.emptyList();
        }
    }
}
