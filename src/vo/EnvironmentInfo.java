package vo;

/**
 * @ClassName: EnvironmentInfo
 * @Description:
 * @Author: qiuwei
 * @Date: 2020-04-20 15:09
 **/
public class EnvironmentInfo {

    private int net;

    private String wifiMac;

    private String ssid;

    private int isEncrypt;

    private int ir;

    private String ov;

    private int isSafe;

    private int firewall;

    public int getNet() {
        return net;
    }

    public void setNet(int net) {
        this.net = net;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public int getIr() {
        return ir;
    }

    public void setIr(int ir) {
        this.ir = ir;
    }

    public String getOv() {
        return ov;
    }

    public void setOv(String ov) {
        this.ov = ov;
    }

    public int getIsSafe() {
        return isSafe;
    }

    public void setIsSafe(int isSafe) {
        this.isSafe = isSafe;
    }

    public int getFirewall() {
        return firewall;
    }

    public void setFirewall(int firewall) {
        this.firewall = firewall;
    }
}
