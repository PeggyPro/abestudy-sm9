package org.yy;

import org.bouncycastle.util.encoders.Hex;
import org.yy.gm.SM9Utils;
import org.yy.gm.params.SM9CurveParameters;
import org.yy.gm.params.SM9Parameters;
import org.yy.gm.structs.SM9Cipher;
import org.yy.gm.structs.SM9KeyPackage;
import org.yy.gm.structs.SM9MasterSecretKey;
import org.yy.gm.structs.SM9PublicKey;
import org.yy.gm.structs.SM9SecretKey;

import it.unisa.dia.gas.jpbc.PairingParameters;

/**
 * SM9 测试时使用的日志工具。
 * <p>
 * 输出日志时，十六进制的数据默认不分割，以便能整体复制到其他工具上进行验证。
 *
 * @author yaoyuan
 * @since 2023/3/10 0:06
 */
public class SM9LogUtils {
    public static boolean showHexWrap = false;
    public static boolean showHexUppercase = false;

    public static void showMsg(String msg) {
        System.out.println(msg);
    }

    public static void showMsg() {
        showMsg("");
    }

    public static void showSM9Curve(SM9Parameters parameters) {
        PairingParameters pairingParameters = parameters.pairing.getPairingParameters();

        showMsg("----------------------------------------------------------------------");
        showMsg("SM9 curve parameters:");

        showMsg("b:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(pairingParameters.getBigInteger("b"))));

        showMsg("t:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(pairingParameters.getBigInteger("t"))));

        showMsg("q:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(pairingParameters.getBigInteger("q"))));

        showMsg("N:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(pairingParameters.getBigInteger("r"))));

        showMsg("beta:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(pairingParameters.getBigInteger("beta"))));

        showMsg("alpha0:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(pairingParameters.getBigInteger("alpha0"))));

        showMsg("alpha1:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(pairingParameters.getBigInteger("alpha1"))));

        showMsg("P1:");
        showMsg(toHexString(SM9Utils.G1ElementToByte(parameters.P1)));

        showMsg("P2:");
        showMsg(toHexString(SM9Utils.G2ElementToByte(parameters.P2)));

        showMsg("----------------------------------------------------------------------");
    }

    public static void showMasterSecretKey(SM9MasterSecretKey masterSecretKey) {
        showMsg("encrypt master private key:");
        showMsg(toHexString(SM9Utils.bigIntegerToBytes(masterSecretKey.getKey(), SM9CurveParameters.LEN_N)));
    }

    public static void showPublicKey(SM9PublicKey publicKey) {
        showMsg("encrypt master public key:");
        showMsg(toHexString(SM9Utils.G1ElementToByte(publicKey.Q)));
        showMsg();
    }

    public static void showSecretKey(SM9SecretKey secretKey) {
        showMsg("encrypt private key:");
        showMsg(toHexString(SM9Utils.G2ElementToByte(secretKey.d)));
        showMsg();
    }

    public static void showKeyPackage(SM9KeyPackage keyPackage) {
        showMsg("SM9 key package:");
        showMsg("C: ");
        showMsg(toHexString(SM9Utils.G1ElementToByte(keyPackage.C)));
        showMsg("K: ");
        showMsg(toHexString(keyPackage.K));
        showMsg();
    }

    public static void showCipherText(SM9Cipher cipherText) {
        showMsg("SM9 result ciphertext:");
        showMsg(toHexString(cipherText.toByteArray()));
        showMsg("EnType: " + (cipherText.enType==0?"XOR":cipherText.enType));
        showMsg("C1: ");
        showMsg(toHexString(SM9Utils.G1ElementToByte(cipherText.C1)));
        showMsg("C2: ");
        showMsg(toHexString(cipherText.C2));
        showMsg("C3: ");
        showMsg(toHexString(cipherText.C3));
        showMsg();
    }

    public static String toHexString(byte[] data) {
        String hex = toHexString0(data);
        return showHexUppercase ? hex.toUpperCase() : hex;
    }

    public static String toHexString0(byte[] data) {
        String hexData = Hex.toHexString(data);
        if (showHexWrap)
            return showString(hexData);
        else
            return hexData;
    }

    public static String showString(String data) {
        if (data.length() < 2)
            return data + "\n";

        StringBuffer sb = new StringBuffer();
        String line = "";
        for (int i = 0; i < data.length(); i += 2) {
            line += data.substring(i, i + 2);

            if ((i + 2) % 64 == 0) {
                sb.append(line);
                sb.append("\n");
                line = "";
            } else if ((i + 2) % 8 == 0)
                line += " ";
        }

        if (!line.isEmpty()) {
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }
}
