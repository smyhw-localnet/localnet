package online.smyhw.localnet.command.sysCmd;

import online.smyhw.localnet.command.cmdManager;
import online.smyhw.localnet.message;

public class SYScmd_help {
    public static void cmd(String cmd) {
        String re = cmdManager.cmd_list.toString();
        re = "\n".concat(re);
        re = re.replace('=', '\n');
        re = re.replace(',', '\n');
        message.show(re);
        return;
    }
}
