package online.smyhw.localnet.command.sysCmd;

import online.smyhw.localnet.helper;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.message;

public class SYScmd_send {
    public static void cmd(String cmd) {
        if (CommandFJ.js(cmd) < 3) {
            message.show("参数不足(send <终端ID> <消息>)");
            return;
        }
        int re = helper.send_to_someone(CommandFJ.fj(cmd, 1), CommandFJ.fj(cmd, 2));
        if (re == 1) {
            message.show("终端<" + CommandFJ.fj(cmd, 1) + ">不存在，消息发送失败");
        }
    }
}
