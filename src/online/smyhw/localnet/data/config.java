package online.smyhw.localnet.data;

import online.smyhw.localnet.message;

import java.util.HashMap;

public class config implements java.io.Serializable {
    private static final long serialVersionUID = 7;
    protected HashMap<String, String> main_data = new HashMap<String, String>();

    /**
     * 线程安全的方法,对该实例的数据表进行设置维护<br>
     * 可以将value置为null来删除该key<br>
     *
     * @param key 需要设置的键
     * @param value 需要设置的内容
     */
    protected synchronized void p_set(String key, String value) {
        if (value == null) {
            main_data.remove(key);
        }
        main_data.put(key, value);
    }

    public void set(String key, Object value) {
        p_set(key, value.toString());
    }

    /**
     * 从该实例的数据表中读取数据<br>
     * 参数不用我解释了吧...
     *
     * @param key 需要获取的键
     */
    protected String get(String key) {
        return main_data.get(key);
    }

    /**
     * 从给定的key中读出int类型数据<br>
     * 如果给定的key不存在,则返回0<br>
     * 请不要使用这个方法去读取其他类型的数据,鬼知道会返回什么
     *
     * @param key 需要获取的键
     * @return 所指定的键的内容
     */
    @Deprecated
    public int get_int(String key) {
        String sre = get(key);
        if (sre == null) {
            message.info("config类get_int方法:给定的key查询为null,返回0");
            return 0;
        }
        return Integer.parseInt(sre);
    }

    /**
     * @see config#get_int
     */
    @Deprecated
    public double get_double(String key) {
        String sre = get(key);
        if (sre == null) {
            message.info("config类get_douoble方法:给定的key查询为null,返回0");
            return 0;
        }
        return Double.parseDouble(sre);
    }

    /**
     * @see config#get_int
     */
    @Deprecated
    public String get_String(String key) {
        String re = get(key);
        if (re == null) {
            message.info("config类get_String方法:给定的key查询为null,返回空字符串");
            return "";
        }
        return re;
    }

    /**
     * @see config#get_int
     */
    public boolean get_boolean(String key) {
        String sre = get(key);
        if (sre == null) {
            return false;
        }
        return sre.equalsIgnoreCase("true");

    }

    /**
     * 在参考方法的基础上，如果查询到的值为null，则返回传入的第二个参数
     * @see config#get_int
     */
    public int get_int(String key, int if_null) {
        String sre = get(key);
        if (sre == null) {
            return if_null;
        }
        return Integer.parseInt(sre);
    }

    /**
     * @see config#get_int(String, int)
     */
    public String get_String(String key, String if_null) {
        String re = get(key);
        if (re == null) {
            return if_null;
        }
        return re;

    }

    /**
     * @see config#get_int(String, int)
     */
    public boolean get_boolean(String key, boolean if_null) {
        String sre = get(key);
        if (sre == null) {
            return if_null;
        }
        return sre.equalsIgnoreCase("true");

    }

    /**
     * @see config#get_int(String, int)
     */
    public double get_double(String key, double if_null) {
        String sre = get(key);
        if (sre == null) {
            return if_null;
        }
        return Double.parseDouble(sre);
    }
}
