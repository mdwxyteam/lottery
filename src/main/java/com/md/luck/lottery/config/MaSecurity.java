package com.md.luck.lottery.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.md.luck.lottery.common.util.MaObjUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MaSecurity {
    private Log log = LogFactory.getLog(this.getClass());
    @Value("${public.key}")
    private String fakePublicKey;
    private String[] ch = {"h", "m", "n", "o", "p", "q", "a", "b", "c", "d", "g", "u", "v", "w", "e", "f", "i", "j", "k", "l", "x", "y", "z"};
    private String publicKey = "";
    private byte[] key = null;

    public void getPublicKey() {
        fakePublicKey = fakePublicKey.trim();
        StringBuilder strb = new StringBuilder("mdtongyz");
        if (fakePublicKey != null && !"".equals(fakePublicKey)) {
            for (int i = 0; i < fakePublicKey.length(); i++) {
                String str = String.valueOf(fakePublicKey.charAt(i));
                if (isDigit(str)) {
                    int j = Integer.valueOf(str);
                    strb.append(ch[j]);
                }
            }
        }
        publicKey = strb.toString();
    }

    // 判断一个字符串是否都为数字
    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return 加密后的结果
     */
    public String encrypt(String content) {
        AES aes = creatAes();
        // 加密
        String se = aes.encryptHex(content);
        log.info("encryptStr:" + se);
        return se;
    }

    private AES creatAes() {
        if (null == key) {
            if (MaObjUtil.isEmpty(publicKey)) {
                getPublicKey();
            }
            key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), publicKey.getBytes()).getEncoded();
        }
        AES aes = SecureUtil.aes(key);
        return aes;
    }

    public String decrypt(String content) {
        AES aes = creatAes();
        // 解密为字符串
        String decryptStr = aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        log.info("decryptStr:" + decryptStr);
        return decryptStr;
    }

    public static void main(String[] args) {
        MaSecurity maSecurity = new MaSecurity();
        String sstr = "uhhjhijhjkoioj";
        String ssrt = maSecurity.decrypt(sstr);
        System.out.println(ssrt);
    }
}
