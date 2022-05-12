# BluestarDDNS
一个我的世界插件,通过通过阿里云的API,已经jsonip.com获取的IP地址,达到动态dns的目的

使用说明:

  下载最新的.jar文件,放进plugins文件夹(支持spigot,paper及其衍生服务端)
  
  1.18.X-1.8.X
  
  阿里云需要获取阿里云AccessKey
  
   ![屏幕截图 2022-01-08 210047](https://user-images.githubusercontent.com/90564167/148645860-afb8895a-0eb2-42bd-9fc8-afee012f4ab8.png)
        
   将鼠标放到右上角账号头像上,点 AccessKey管理 进去之后按提示获取一个AccessKeyID 以及AccessKeySecret
   
  namecheap需要添加一个A+解析,并获取密码
   
   ![fbdebd701aada0f410c0dc6ffe079b0](https://user-images.githubusercontent.com/90564167/167988142-300e7d7e-b673-40fb-a828-3d200653786e.png)
        
  确认服务器已加载插件后,打开插件配置文件,里面会有提示,输入刚才获取的信息,保存后使用/dns reload即可
  
其他说明:

  指令 dns,ddns,bluestarddns,bsddns 是完全等价的
  
  使用此插件的前提是你使用阿里云购买了一个域名
  
  此插件的目的是解决某些情况下,IP地址变动的问题的,比如使用代理,或是网络运营商不太行等情况.不能解决你的服务器没有公网IP,没有域名等问题
  
截图:

  ![image](https://user-images.githubusercontent.com/90564167/148647832-cafbd127-8d51-40a9-b949-86e6a3590eab.png)

