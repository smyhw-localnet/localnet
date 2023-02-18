package online.smyhw.localnet.plugins;

import online.smyhw.localnet.message;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class PluginsManager {

    public static ArrayList<String> PluginsList = new ArrayList<String>();//实际加载的插件列表
    public static ClassLoader cloader;

    /**
     * @author smyhw
     * 仅在启动时运行一次，请勿调用！
     */
    public static void start() {
        message.info("[插件管理器]localnet插件系统V1.0");
        File plugin_dir_t = new File("./plugins");
//		if(plugin_dir.exists()) {message.show("good");}
        File plugin_dir = new File(plugin_dir_t.getAbsolutePath());
//		message.show("插件文件夹路径：");
        File[] plugins_list = plugin_dir.listFiles();
        message.show("[插件管理器]插件总数：" + plugins_list.length);
        try {
            URL[] PluginsURL = new URL[plugins_list.length];
            for (int temp = 0; temp < plugins_list.length; temp++) {
                PluginsURL[temp] = new URL("file:///" + plugin_dir_t.getAbsolutePath() + "/" + plugins_list[temp].getName());
            }
            cloader = new URLClassLoader(PluginsURL);
            for (int temp = 0; temp < PluginsURL.length; temp++) {
                message.info("[插件管理器]加载插件:" + plugins_list[temp].getName());
                String murl = plugins_list[temp].getName().substring(0, plugins_list[temp].getName().length() - 4) + ".lnp";
                Class<?> cl = cloader.loadClass(murl);
                cl.getMethod("plugin_loaded").invoke(null);
                PluginsList.add(murl.substring(0, murl.length() - 4));
            }
        } catch (Exception e) {
            message.warning("[插件管理器]插件加载出错!请检查发生此错误时正在加载的插件!", e);
        }
    }

    /**
     * 查找指定插件是否存在/已被加载
     *
     * @param input
     * @return
     */
    public static boolean find(String input) {
        return PluginsList.contains(input);
    }

}
