**这是一个致力于连接和处理各个信息频道的项目**  
通过插件可以将各个信息频道连接在一起  
*目前，我们的插件支持有QQ，minecraft服务器,pigeon网页聊天室等*  
***  
#####使用localnet  
* **获取localnet.jar本体**  
你可以在本项目的Releases中下载编译好的版本  
或者拉取源代码自己编译(localnet本体没有任何依赖,*~~你甚至可以拿javac直接构建~~*)  
<br>
* **首次运行并生成配置文件**  
使用命令`java -jar localnet.jar`即可启动localnet  
首次启动时,localnet并不会完全启动,而是在当前目录下生成必要文件后退出
<br>
* **更改配置文件(可选)**
如有需要,你可以更改[配置文件](https://github.com/smyhw-localnet/localnet/blob/master/src/data/example_config/LN.config)
<br>
* **再次启动localnet**
当出现
`
[09:40:38][info]localnet初始化完成!  
[09:40:38][show]+==========smyhw==========+  
[09:40:38][show]|   version:1.0           |  
[09:40:38][show]|                         |  
[09:40:38][show]|                         |  
[09:40:38][show]+=========localnet========+  
test@test>  
`
#####目录结构:  
./plugins   #存放插件，需要加载的插件就全部塞到这里（注意！插件文件不能改名）  
./logs   #日志文件  
./configs   #插件的配置文件目录  
./LN.config   #localnet核心配置文件
