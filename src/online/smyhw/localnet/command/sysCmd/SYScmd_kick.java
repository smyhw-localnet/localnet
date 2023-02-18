package online.smyhw.localnet.command.sysCmd;

import online.smyhw.localnet.helper;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.message;
import online.smyhw.localnet.network.Client_sl;

public class SYScmd_kick {
    public static void cmd(String cmd) {
        if (CommandFJ.js(cmd) < 2) {
            message.show("参数错误!请指定需要断开的终端ID");
            return;
        }
        String ID = CommandFJ.fj(cmd, 1);
        Client_sl tUS = helper.Find_Client(ID);
        if (tUS == null) {
            message.show("指定的终端ID不存在!");
            return;
        }
        tUS.Disconnect("被指令踢出");
        return;
    }
}
