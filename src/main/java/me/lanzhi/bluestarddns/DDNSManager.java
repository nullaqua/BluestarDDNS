package me.lanzhi.bluestarddns;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import me.lanzhi.bluestarddns.main.BluestarDDNS;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDNSManager
{
    private final BluestarDDNS plugin;
    public HashMap<BukkitRunnable, BukkitTask> tasks;

    public DDNSManager(BluestarDDNS plugin)
    {
        this.plugin=plugin;
    }

    public void start()
    {
        tasks=new HashMap<>();
        for (String id: plugin.getConfig().getConfigurationSection("aliyun").getKeys(false))
        {
            if (plugin.getConfig().getLong("aliyun."+id+".time",-1)<=0)
            {
                continue;
            }
            BukkitRunnable runnable=new aliyun(plugin,id);
            System.out.println("添加解析任务 aliyun."+id);
            tasks.put(runnable,runnable.runTaskTimerAsynchronously(plugin,0,plugin.getConfig().getLong("aliyun."+id+".time")*20));
        }
        for (String id: plugin.getConfig().getConfigurationSection("namecheap").getKeys(false))
        {
            if (plugin.getConfig().getLong("namecheap."+id+".time",-1)<=0)
            {
                continue;
            }
            BukkitRunnable runnable=new namecheap(plugin,id);
            System.out.println("添加解析任务 namecheap."+id);
            tasks.put(runnable,runnable.runTaskTimerAsynchronously(plugin,0,plugin.getConfig().getLong("namecheap."+id+".time")*20));
        }
    }

    public void stop()
    {
        for (BukkitTask task: tasks.values())
        {
            task.cancel();
        }
    }

    public void reload()
    {
        stop();
        plugin.reloadConfig();
        start();
    }

    private static class aliyun extends BukkitRunnable
    {
        public final ConfigurationSection configurationSection;
        private final String id;
        boolean IsNormal=true;
        private IAcsClient client;

        private aliyun(BluestarDDNS plugin,String id)
        {
            this.id=id;
            configurationSection=plugin.getConfig().getConfigurationSection("aliyun."+id);
        }

        public String getDnsIp()
        {
            if (configurationSection.getString("AccessKeySecret").isEmpty()||configurationSection.getString("AccessKeyID").isEmpty()||configurationSection.getString("name").isEmpty()||configurationSection.getString("rR").isEmpty()||configurationSection.getString("region").isEmpty())
            {
                return null;
            }
            DefaultProfile profile=DefaultProfile.getProfile(configurationSection.getString("region"),configurationSection.getString("AccessKeyID"),configurationSection.getString("AccessKeySecret"));
            IAcsClient client=new DefaultAcsClient(profile);
            DescribeDomainRecordsRequest describeDomainRecordsRequest=new DescribeDomainRecordsRequest();
            describeDomainRecordsRequest.setDomainName(configurationSection.getString("name"));
            describeDomainRecordsRequest.setRRKeyWord(configurationSection.getString("rR"));
            describeDomainRecordsRequest.setType("A");
            DescribeDomainRecordsResponse describeDomainRecordsResponse=this.describeDomainRecords(describeDomainRecordsRequest,client);
            List<DescribeDomainRecordsResponse.Record> domainRecords=describeDomainRecordsResponse.getDomainRecords();
            DescribeDomainRecordsResponse.Record record=domainRecords.get(0);
            return record.getValue();
        }

        public DescribeDomainRecordsResponse describeDomainRecords(DescribeDomainRecordsRequest request,IAcsClient client)
        {
            try
            {
                return client.getAcsResponse(request);
            }
            catch (ClientException e)
            {
                System.out.println(ChatColor.RED+"出现错误!id: aliyun."+id);
            }
            return null;
        }

        public String getCurrentHostIP()
        {
            String jsonip="https://jsonip.com/";
            String result="";
            BufferedReader in=null;
            try
            {
                URL url=new URL(jsonip);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                in=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line=in.readLine())!=null)
                {
                    result+=line;
                }
                IsNormal=true;
            }
            catch (Exception e)
            {
                System.out.println(ChatColor.RED+"出现错误!id: aliyun."+id);
                IsNormal=false;
            }
            finally
            {
                try
                {
                    if (in!=null)
                    {
                        in.close();
                    }
                }
                catch (Exception e2)
                {
                    System.out.println(ChatColor.RED+"出现错误!id: aliyun."+id);
                }

            }
            String rexp="(\\d{1,3}\\.){3}\\d{1,3}";
            Pattern pat=Pattern.compile(rexp);
            Matcher mat=pat.matcher(result);
            String res="";
            while (mat.find())
            {
                res=mat.group();
                break;
            }
            return res;
        }

        private UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest request,IAcsClient client)
        {
            try
            {
                return client.getAcsResponse(request);
            }
            catch (ClientException e)
            {
                System.out.println(ChatColor.RED+"出现错误!id: aliyun."+id);
            }
            return null;
        }

        @Override
        public void run()
        {
            if (configurationSection.getString("AccessKeySecret").isEmpty()||configurationSection.getString("AccessKeyID").isEmpty()||configurationSection.getString("name").isEmpty()||configurationSection.getString("rR").isEmpty()||configurationSection.getString("region").isEmpty())
            {
                return;
            }
            DefaultProfile profile=DefaultProfile.getProfile(configurationSection.getString("region"),configurationSection.getString("AccessKeyID"),configurationSection.getString("AccessKeySecret"));
            client=new com.aliyuncs.DefaultAcsClient(profile);
            DescribeDomainRecordsRequest describeDomainRecordsRequest=new DescribeDomainRecordsRequest();
            describeDomainRecordsRequest.setDomainName(configurationSection.getString("name"));
            describeDomainRecordsRequest.setRRKeyWord(configurationSection.getString("rR"));
            describeDomainRecordsRequest.setType("A");
            DescribeDomainRecordsResponse describeDomainRecordsResponse=describeDomainRecords(describeDomainRecordsRequest,client);
            List<DescribeDomainRecordsResponse.Record> domainRecords=describeDomainRecordsResponse.getDomainRecords();
            if (domainRecords.size()!=0)
            {
                DescribeDomainRecordsResponse.Record record=domainRecords.get(0);
                String recordId=record.getRecordId();
                String recordsValue=record.getValue();
                String currentHostIP=getCurrentHostIP();
                if (!IsNormal)
                {
                    System.out.println(ChatColor.RED+"尝试重新获取当前IP");
                    currentHostIP=getCurrentHostIP();
                    if (!IsNormal)
                    {
                        System.out.println(ChatColor.RED+"仍无法获取,请检查网络连接");
                        return;
                    }
                    else
                    {
                        System.out.println("重新获取成功");
                    }
                }
                if (!currentHostIP.equals(recordsValue))
                {
                    System.out.println(ChatColor.RED+"检测到解析IP与实际不符,已进行调整!id: aliyun."+id);
                    System.out.println(ChatColor.RED+"实际IP: "+currentHostIP);
                    System.out.println(ChatColor.RED+"解析IP: "+recordsValue);
                    UpdateDomainRecordRequest updateDomainRecordRequest=new UpdateDomainRecordRequest();
                    updateDomainRecordRequest.setRR(configurationSection.getString("rR"));
                    updateDomainRecordRequest.setRecordId(recordId);
                    updateDomainRecordRequest.setValue(currentHostIP);
                    updateDomainRecordRequest.setType("A");
                    UpdateDomainRecordResponse updateDomainRecordResponse=updateDomainRecord(updateDomainRecordRequest,client);
                }
                else if (!configurationSection.getBoolean("quiet"))
                {
                    System.out.println("解析IP为实际IP,持续监控中!id: aliyun."+id);
                }
            }
            else
            {
                System.out.println(ChatColor.RED+"您的主机记录中没有"+configurationSection.getString("rR")+"请先手动添加!id: aliyun."+id);
            }
        }
    }

    private static class namecheap extends BukkitRunnable
    {
        private final ConfigurationSection configurationSection;
        private final String id;
        String x;

        private namecheap(BluestarDDNS plugin,String id)
        {
            this.id=id;
            this.configurationSection=plugin.getConfig().getConfigurationSection("namecheap."+id);
            x="https://dynamicdns.park-your-domain.com/update?host="+configurationSection.getString("rR")+"&domain="+configurationSection.getString("name")+"&password="+configurationSection.getString("password");
        }

        @Override
        public void run()
        {
            if (!configurationSection.getBoolean("quiet"))
            {
                System.out.println("namecheap."+id+" 开始dns矫正");
            }
            try
            {
                URL url=new URL(x);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                urlConnection.getInputStream();
            }
            catch (Throwable e)
            {
                System.out.println(ChatColor.RED+"错误!进行dns更新失败,id: namecheap."+id);
            }
            if (!configurationSection.getBoolean("quiet"))
            {
                System.out.println("namecheap."+id+" dns矫正结束");
            }
        }
    }
}
