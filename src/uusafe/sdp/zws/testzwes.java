package uusafe.sdp.zws;
/**
 * @ClassName: testzwes
 * @Description: 让Jmeter测试工具调用一个方法sdp_Jmeterbody()
 * @Author: zhuws
 * @Date: 2021-12-17
 **/


import com.uusafe.sdp.common.util.Md5Util;
import com.uusafe.sdp.common.util.sm2.GmECDH;
import com.uusafe.sdp.common.util.sm3.SM3Utils;
import com.uusafe.sdp.common.util.sm4.SM4Util;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.SocketUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import vo.EnvironmentInfo;
import vo.LoginMessageBean;
import com.uusafe.sdp.common.util.*;
import com.uusafe.framework.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;


import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;


public class testzwes  extends AbstractJavaSamplerClient {
    private int sdp_port=8888;
    private String host_name="";
    private String enhanceType="0";
    private String GMType="0";
    private String user_name="";
    private String req_message;

    public static void main(String[] args) throws Exception{
        sdp_Jmeterbody("wokaozws000200","0","0","172.16.30.67",8888);
//        byte[] sdpstr = sdp_enhancedAuth("zws","0","1");
//        String test_str = Base64.encodeBytes(sdpstr);
        System.out.println("Do not use this class directly");
    }
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("user_name", "");
        params.addArgument("enhanceType", "");
        params.addArgument("GMType", "");
        params.addArgument("host_name", "");
        params.addArgument("sdp_port", "");
        return params;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();

        user_name = javaSamplerContext.getParameter("user_name");
        enhanceType = javaSamplerContext.getParameter("enhanceType");
        GMType = javaSamplerContext.getParameter("GMType");
        host_name = javaSamplerContext.getParameter("host_name") ;
        sdp_port = Integer.parseInt(javaSamplerContext.getParameter("sdp_port")) ;
        sampleResult.sampleStart();// jmeter 开始统计响应时间标记
        try {
            if (!isIp(host_name))
            {
                sampleResult.setResponseMessage("123");
                sampleResult.setResponseData("ip地址不正确,无法请求",null);
                sampleResult.setSuccessful(false);
                throw new Exception("ip地址不正确");
            }
            testzwes.sdp_Jmeterbody(user_name,enhanceType,GMType,host_name,sdp_port);
            sampleResult.setSuccessful(true);
            req_message = Base64.encodeBytes(testzwes.sdp_enhancedAuth(user_name,enhanceType,GMType));
            sampleResult.setResponseData("UDP请求成功,详见SDP服务端("+host_name+")日志;当前用户名："+user_name+"\n该请求体Base64编码参考：\n"+req_message,null);
         //   System.out.println("UDP请求成功,详见SDP服务端("+host_name+")日志;当前用户名："+user_name+"\n该请求体Base64编码参考：\n"+req_message);
        } catch (Exception e) {
            e.printStackTrace();
            sampleResult.setSuccessful(false);
        }finally {
            sampleResult.sampleEnd();// jmeter 结束统计响应时间标记
        }
        return sampleResult;
    }

    public static boolean isIp(String ip) {
        boolean ip_judge = ip.matches("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
        return ip_judge;
    }

    public static void sdp_Jmeterbody(String usernames,String enhanced_type,String GM_type,String host_name,int sdp_port) throws Exception{
        byte[] message = sdp_enhancedAuth(usernames,enhanced_type,GM_type);
        run(host_name,message,sdp_port);
    }

    public static byte[] sdp_enhancedAuth(String user_names,String enhanced_type,String GM_type) throws Exception{
        byte[] result=null;
        LoginMessageBean loginMessageBean = new LoginMessageBean();
        loginMessageBean.setCid(Md5Util.encode(user_names));
        Map<String, String> tempKeys = EncryptOrDecryptUtil.generatorKey();
        String tempCliPubKey = tempKeys.get("pubKey");
        loginMessageBean.setCpk(tempCliPubKey);
        loginMessageBean.setDn("来自java测试工具");
        EnvironmentInfo environmentInfo  = new EnvironmentInfo();
        environmentInfo.setFirewall(1);
        environmentInfo.setNet(0);
        environmentInfo.setIsEncrypt(1);
        environmentInfo.setIr(0);
        environmentInfo.setOv("NJ性能");
        environmentInfo.setIsSafe(0);

        loginMessageBean.setT(0);
        loginMessageBean.setEnv(environmentInfo);
        loginMessageBean.setOc("default");
        //(数据类型)(最小值+Math.random()*(最大值-最小值+1))
        int Pf=(int)(0+Math.random()*(3-0+1));
        loginMessageBean.setPf(Pf);
        loginMessageBean.setPw("123456789");
        loginMessageBean.setUn(user_names);
        if ("1".equals(GM_type))
        {
            result = GmEnCodeMessage(loginMessageBean,"01","01");
        }
        else {
            result = testzwes.EnCodeMessage(loginMessageBean, "01", "01");
        }
        if ("1".equals(enhanced_type)) {
            result = addChecksumToBuf(result);
        }
//        String zws_txt=Base64.encodeBytes(result);
        return result;
    }

    private static byte[] EnCodeMessage(Object amb, String messageType, String version) throws Exception {
        Map<String, String> tempKeys = EncryptOrDecryptUtil.generatorKey();
        String tempCliPubKey = tempKeys.get("pubKey");
        String tempCliPriKey = tempKeys.get("priKey");

        //密钥磋商
        byte[] sharedKey = EncryptOrDecryptUtil.ecdhKey(Base64.decode("PEDTrhRmXciOMNfdC2ZAyt/hCY5N/qKPGxZ1FBOpzyQ="), Base64.decode(tempCliPriKey));
        byte[] secretContent = encodeMessage(JsonUtil.ObjectToJsonBytes(amb),sharedKey);

        long time = System.currentTimeMillis();

        byte[] content = copyByte(version.getBytes(),messageType.getBytes(),Base64.decode(tempCliPubKey),secretContent, (time+"").getBytes(), Md5Util.encode("123456").getBytes());

        byte[] signData = EncryptOrDecryptUtil.HMACSHA256(content, sharedKey);
        byte[] result = copyByte(content,signData);

        return result;
    }
    private static byte[] encodeMessage(byte[] message, byte[] sharedSecret) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] skey = new byte[16];
        System.arraycopy(sharedSecret, 0, skey, 0, 16);

        byte[] vi = new byte[16];
        System.arraycopy(sharedSecret, 16, vi, 0, 16);


        return SM4Util.encrypt_Cbc_Padding(skey, vi, message);

    }
    private static byte[] copyByte(byte[]... bytes) {
        int length = 0;
        for (byte[] bytePer : bytes) {
            length = length + bytePer.length;
        }
        int countLength = 0;
        byte[] resultByte = new byte[length];
        for (byte[] bytePer : bytes) {
            System.arraycopy(bytePer, 0, resultByte, countLength, bytePer.length);
            countLength += bytePer.length;
        }
        return resultByte;
    }
    //增强认证方法
    private static byte[] addChecksumToBuf(byte[] message) {
        byte iXorCalOut = 0;
        byte[] header = new byte[]{(byte)0xFF,(byte)0xA5};
        byte[] pCheckSum = new byte[2];
        int length = message.length;
        if (length > 50)
        {
            length = 50;
        }
        byte[] aFixFactorValue = new byte[]{0x45, (byte) 0x87, 0x23, 0x11};
        for (int i = message.length-length;i<message.length;i++)
        {
            iXorCalOut ^= message[i];
        }
        pCheckSum[0] = (byte) (iXorCalOut ^ aFixFactorValue[0] ^ aFixFactorValue[2]);
        pCheckSum[1] = (byte) (iXorCalOut ^ aFixFactorValue[1] ^ aFixFactorValue[3]);
        return copyByte(header,message,pCheckSum);
    }

    public static void run(String hostname,byte[] message,int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChineseProverbClientHandler());
            Channel ch = b.bind(0).sync().channel();

            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    message), SocketUtils.socketAddress(hostname, port))).sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    //国密方法
    private static byte[] GmEnCodeMessage(Object amb, String messageType, String version) throws Exception {
        byte[] keys = GmECDH.getInstance().wgGenerateKeyPair();
        byte[] publicKey = new byte[65];
        byte[] privateKey = new byte[32];
        System.arraycopy(keys, 0, privateKey, 0, 32);
        System.arraycopy(keys, 32, publicKey, 0, 65);

        //密钥磋商
        byte[] sharedKey = GmECDH.getInstance().wgECDH(privateKey,Base64.decode("BKHIoAUiq7qiTb+q75FV9sNZJnqkvWrkLlEHPN558duaznHGVY5lz35gyU9p1vSIOfqKyo++YIVYbQlRNjy9jQo="));
        byte[] secretContent = encodeMessage(JsonUtil.ObjectToJsonBytes(amb),sharedKey);

        long time = System.currentTimeMillis();

        byte[] content = copyByte(version.getBytes(),messageType.getBytes(),publicKey,secretContent, (time+"").getBytes(), Md5Util.encode("123456").getBytes());

        byte[] signData = SM3Utils.hmac(sharedKey,content);
        byte[] result = null;

        result = copyByte(content,signData);

        return result;
    }
}
