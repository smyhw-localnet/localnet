package online.smyhw.localnet.lib.Exception;

public class WebAPI_Exception extends Exception {
    private static final long serialVersionUID = 7;
    public int type;
    public Exception upEXP;
    String url = "";

    public WebAPI_Exception(int type, String url) {


    }
}
