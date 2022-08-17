package vo;

/**
 * @ClassName: LoginMessageBean
 * @Description:
 * @Author: qiuwei
 * @Date: 2020-03-18 14:25
 **/
public class LoginMessageBean {
    /**
     * 客户端链路层公钥
     */
    private String cpk;
    /**
     * 登录账号
     */
    private String un;
    /**
     * 鉴权密码
     */
    private String pw;
    /**
     * 登录类型 0-账号密码登录
     */
    private int t;
    /**
     * 租户编码
     */
    private String oc;
    /**
     * 客户端唯一标示
     */
    private String cid;
    /**
     * 客户端名称
     */
    private String dn;

    /**
     * env
     */
    private EnvironmentInfo env;
    /**
     * 平台 0-android 1-ios 2-windows 3-macos 4-未知
     */
    private int pf;

    /**
     * 客户端版本
     */
    private String vc;

    /**
     * sdk版本
     */
    private String sdkvc;

    public String getVc() {
        return vc;
    }

    public void setVc(String vc) {
        this.vc = vc;
    }

    public String getSdkvc() {
        return sdkvc;
    }

    public void setSdkvc(String sdkvc) {
        this.sdkvc = sdkvc;
    }

    public String getCpk() {
        return cpk;
    }

    public void setCpk(String cpk) {
        this.cpk = cpk;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String getOc() {
        return oc;
    }

    public void setOc(String oc) {
        this.oc = oc;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public EnvironmentInfo getEnv() {
        return env;
    }

    public void setEnv(EnvironmentInfo env) {
        this.env = env;
    }

    public int getPf() {
        return pf;
    }

    public void setPf(int pf) {
        this.pf = pf;
    }
}
