package online.smyhw.localnet.command.sysCmd;

import online.smyhw.localnet.data.DocManager;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.message;

public class SYScmd_doc {
    public static void cmd(String cmd) {
        String doc_path;
        if (CommandFJ.js(cmd) < 2) {
            doc_path = "/data/doc/index";
        } else {
            doc_path = CommandFJ.fj(cmd, 1);
        }
        String re = DocManager.getDoc(doc_path);
        message.show(re);
    }
}
