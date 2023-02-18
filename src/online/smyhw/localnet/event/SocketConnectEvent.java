package online.smyhw.localnet.event;

import java.net.Socket;

/**
 * 在socket连接建立后，并未实际发送任何数据前</br>
 * 可以拿它来初始化加密握手，或者检测假连接压测等</br>
 *
 * @author smyhw
 */
public class SocketConnectEvent extends LN_Event {
    public Socket enSocket;
    boolean Cancel = false;

    public SocketConnectEvent(Socket i) {
        this.enSocket = i;
        this.EventName = "SocketConnectEvent";//修改
        EventManager.DOevent(this);
    }

    public void setCancel() {
        this.Cancel = true;
    }

    public boolean getCancel() {
        return this.Cancel;
    }
}
