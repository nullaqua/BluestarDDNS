# BluestarDDNS
一个我的世界插件,通过通过阿里云的API,已经jsonip.com获取的IP地址,达到动态dns的目的

使用说明:

  下载最新的.jar文件,放进plugins文件夹(支持spigot,paper及其衍生服务端)
  
  原生版本为1.18.X 更低版本没有测试过,不过理论支持全版本
  
  首先需要获取阿里云AccessKey
  
  获取方法:
        ![image](https://user-images.githubusercontent.com/90564167/148645519-e2647818-ca5e-413d-9db9-f80cd52f0594.png)
        
        将鼠标放到右上角账号头像上,点 AccessKey管理 进去之后按提示获取一个AccessKeyID 以及AccessKeySecret
        
  确认服务器已加载插件后,使用 /dns set 设置相关配置,即可使用.设置方法使用方法可通过 /dns help 查询
  
其他说明:

  指令 dns,ddns,bluestarddns,bsddns 是完全等价的
  
  使用此插件的前提是你使用阿里云购买了一个域名
  
  此插件的目的是解决某些情况下,IP地址变动的问题的,比如使用代理,或是网络运营商不太行等情况.不能解决你的服务器没有公网IP,没有域名等问题
  
