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
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDNSManager extends BukkitRunnable
{
    private final BluestarDDNS plugin;
    boolean IsNormal=true;
    private IAcsClient client;

    public DDNSManager(BluestarDDNS plugin)
    {
        this.plugin=plugin;
    }

    public String getDnsIp()
    {
        if (plugin.getConfig().getString("AccessKeySecret").isEmpty()||plugin.getConfig().getString("AccessKeyID").isEmpty()||plugin.getConfig().getString("name").isEmpty()||plugin.getConfig().getString("rR").isEmpty()||plugin.getConfig().getString("region").isEmpty())
        {
            return null;
        }
        DefaultProfile profile=DefaultProfile.getProfile(plugin.getConfig().getString("region"),plugin.getConfig().getString("AccessKeyID"),plugin.getConfig().getString("AccessKeySecret"));
        IAcsClient client=new DefaultAcsClient(profile);
        DescribeDomainRecordsRequest describeDomainRecordsRequest=new DescribeDomainRecordsRequest();
        describeDomainRecordsRequest.setDomainName(plugin.getConfig().getString("name"));
        describeDomainRecordsRequest.setRRKeyWord(plugin.getConfig().getString("rR"));
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
            System.out.println(ChatColor.RED+"出现错误!可能是配置设置错误!请检查");
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
            System.out.println(ChatColor.RED+"出现错误!获取当前IP地址失败");
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
                System.out.println(ChatColor.RED+"出现错误!未知原因");
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
            System.out.println(ChatColor.RED+"出现错误!可能是配置设置错误!请检查");
        }
        return null;
    }

    @Override
    public void run()
    {
        if (plugin.getConfig().getString("AccessKeySecret").isEmpty()||plugin.getConfig().getString("AccessKeyID").isEmpty()||plugin.getConfig().getString("name").isEmpty()||plugin.getConfig().getString("rR").isEmpty()||plugin.getConfig().getString("region").isEmpty())
        {
            return;
        }
        DefaultProfile profile=DefaultProfile.getProfile(plugin.getConfig().getString("region"),plugin.getConfig().getString("AccessKeyID"),plugin.getConfig().getString("AccessKeySecret"));
        client=new com.aliyuncs.DefaultAcsClient(profile);
        DescribeDomainRecordsRequest describeDomainRecordsRequest=new DescribeDomainRecordsRequest();
        describeDomainRecordsRequest.setDomainName(plugin.getConfig().getString("name"));
        describeDomainRecordsRequest.setRRKeyWord(plugin.getConfig().getString("rR"));
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
                System.out.println(ChatColor.RED+"检测到解析IP与实际不符,已进行调整!");
                UpdateDomainRecordRequest updateDomainRecordRequest=new UpdateDomainRecordRequest();
                updateDomainRecordRequest.setRR(plugin.getConfig().getString("rR"));
                updateDomainRecordRequest.setRecordId(recordId);
                updateDomainRecordRequest.setValue(currentHostIP);
                updateDomainRecordRequest.setType("A");
                UpdateDomainRecordResponse updateDomainRecordResponse=updateDomainRecord(updateDomainRecordRequest,client);
            }
            else if (!plugin.getConfig().getBoolean("quiet"))
            {
                System.out.println("解析IP为实际IP,持续监控中!");
            }
        }
        else
        {
            System.out.println(ChatColor.RED+"您的主机记录中没有"+plugin.getConfig().getString("rR")+"请先手动添加!");
        }
    }
}
