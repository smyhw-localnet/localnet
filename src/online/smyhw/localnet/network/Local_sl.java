package online.smyhw.localnet.network;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.message;

public class Local_sl extends Client_sl {
    public Local_sl() {
        super("local", null);
        this.remoteID = LN.ID;
    }

    /**
     * 发送数据包信息
     *
     * @param input
     */
    public void sendData(DataPack msg) {
        String type = (String) msg.getValue("type");
        switch (type) {
            case "note":
                String NoteType = (String) msg.getValue("NoteType");
                String NoteText = (String) msg.getValue("NoteText");
                message.warning("警告>{" + NoteType + "}:" + NoteText);
                return;
            case "message":
                String message = (String) msg.getValue("message");
                online.smyhw.localnet.message.show(">" + message);
                return;
            default:
                String tmp = msg.getStr();
                online.smyhw.localnet.message.show("本地虚拟终端>" + tmp);
                return;
        }
    }
}
