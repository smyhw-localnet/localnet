package online.smyhw.localnet.lib;

import online.smyhw.localnet.message;

public class CommandFJ {

    /**
     * 分解指令\n
     * 0字段表示指令本体
     *
     * @param command
     * @param num
     * @return
     */
    public static String fj(String command, int num) {
        String re = " ";
        int sy = 0;//索引，即现在检查到了哪个字符，以0开头
        int dl = -1;//记录现在检查到了第几个参数，以1开头
        int s = 0, e = -1;//当前参数开始处索引以及结束处索引(e=结束处索引+1)
        boolean y_mode = false; //是否处于引号中
        command = command.trim();//删除前后的空白
        if (command.isEmpty()) {
            message.info("[命令解释器]:空指令！");
            return " ";
        }
        if (js(command) < num) {
            message.info("[命令解释器]: 没有那么长的指令！");
            return " ";
        }
        command = command + " ";//尾部加空格，防止单节指令无法读到空格而出错
        //添加特殊功能
        if (num == -1)//砍掉指令第一段
        {
            return command.substring(command.indexOf(' ') + 1);
        }
        //一般分解过程
        while (true) {
            if (command.length() == sy) {
                break;
            }
            if ((command.charAt(sy) == '"') && (command.length() > (sy + 1)) && (command.charAt(sy + 1) == ' ')) {
                y_mode = false;
                sy++;
            }
            if ((command.charAt(sy) == ' ') && (y_mode == false)) {
                if ((command.charAt(sy) == ' ') && (command.length() > (sy + 1)) && (command.charAt(sy + 1) == '"')) {
                    y_mode = true;
                }
                s = e + 1;
                e = sy;
                dl++;
                if (dl == num)//返回字符串
                {
                    try {
                        re = command.substring(s, e);
                        if (re.charAt(0) == '"' && re.charAt(re.length() - 1) == '"') {
                            re = re.substring(1, re.length() - 1);
                        }
                    } catch (IndexOutOfBoundsException ee) {
                        return "error";
                    }
                    return re;
                }
            }
            sy++;
        }
        return " ";
    }

    /**
     * 根据给定的指令,返回这个指令有几个参数</br>
     * 注意!这个数值包含指令本身!</br>
     * 例如:</br>
     * 指令:connect 127.0.0.1 443<br>
     * 返回:3</br>
     * 再注意!本方法无法对参数中的两个或多个空格做正确的处理!</br>
     * 例如:</br>
     * connect  hhz</br>
     * 因为两个单词中有两个或更多空格,这时本方法会返回错误值!</br>
     *
     * @param cmd 输入的指令
     * @return 指令的参数个数
     */
    public static int js(String cmd) {
        cmd = cmd.trim();
        char[] arr = cmd.toCharArray();
        int re = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ' ') {
                re++;
            }
        }
        return re + 1;
    }

}
