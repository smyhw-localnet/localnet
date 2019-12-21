package online.smyhw.localnet;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class plugin 
{
	/**
	 * @author smyhw
	 * 仅在启动时运行一次，请勿调用！
	 * 
	 */
	public static void start()
	{
		message.info("localnet插件系统V1.0");
		File plugin_dir_t = new File("./plugins");
//		if(plugin_dir.exists()) {message.show("good");}
		File plugin_dir = new File(plugin_dir_t.getAbsolutePath());
//		message.show("插件文件夹路径：");
		File[] plugins_list = plugin_dir.listFiles();
		message.info("插件总数："+plugins_list.length);
		int temp=0;
		while(true)
		{
			if(temp==plugins_list.length) {break;}
			load("file:///"+plugin_dir_t.getAbsolutePath()+"/"+plugins_list[temp].getName(),plugins_list[temp].getName().substring(0,plugins_list[temp].getName().length()-4)+".lnp");
//			message.show("file:///"+plugin_dir_t.getAbsolutePath()+"/"+plugins_list[temp].getName());
			temp++;
		}
	}

	/**
	 * @author smyhw
	 * @see 加载的同时会调用插件的初始化(plugin_loaded)方法
	 * @param url 需要加载的类的路径
	 */
	public static void load(String url,String murl)
	{
		try 
		{
			ClassLoader cloader = new URLClassLoader(new URL[] { new URL(url) });
			Class<?> cl = cloader.loadClass(murl);
//			Class<?> cl = Class.forName(murl);
			cl.getMethod("plugin_loaded").invoke(null);
		} 
		catch (Exception e) 
		{
			message.info("出现错误,一个插件可能无法被加载："+url);
			e.printStackTrace();
		}
	}
	
}
