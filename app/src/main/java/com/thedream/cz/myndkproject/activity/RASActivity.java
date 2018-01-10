package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.data.entity.LoginInfo;
import com.thedream.cz.myndkproject.utils.Base64Util;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.RSAUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RASActivity extends AppCompatActivity {

    private String privateKey;
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDsgOoq7vCwUHTD9LKDGJMh7hSA/Y/YncEz8HVvNSGppNeM+rXjsRTGHmi+iz5WLI5sPxs0RMe9bUA5WDbNIBgCZQwApC7A5kL+IvFCUPdVEIlYyir4vOcyq2fC4WOPgtMAMShtqIipved+p+FxASCTIE/HTWzHftzZpQ2VfDGXMQIDAQAB";
    private String json;
    private String publicEncrypt;
    private String privateEncrypt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ras);
        ButterKnife.bind(this);
        LoginInfo info = new LoginInfo();
        info.setUid("123456");
        info.setUsername("陈壮");
        info.setToken("hahah");
        info.setBelongTo("test");
        json = info.toString();
        try {
            privateKey = RSAUtil.getPrivateKey(RSAUtil.genKeyPair());
            publicKey = RSAUtil.getPublicKey(RSAUtil.genKeyPair());
            PrintUtil.printCZ("privateKey：" + privateKey + "  publicKey:" + publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn_public_encrypt)
    public void publicEncrypt() {
        try {
            byte[] bytes = RSAUtil.encryptByPublicKey(json.getBytes(), publicKey);
            publicEncrypt = Base64Util.encode(bytes);
            PrintUtil.printCZ("公钥加密后:" + publicEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_private_decode)
    public void privateDecode() {
        try {
            byte[] bytes = RSAUtil.decryptByPrivateKey(Base64Util.decode(publicEncrypt), privateKey);
            String privateDecode = new String(bytes, "utf-8");
            PrintUtil.printCZ("私钥解密后:" + privateDecode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_private_encrypt)
    public void privateEncrypt() {
        try {
            byte[] bytes = RSAUtil.encryptByPrivateKey(json.getBytes(), privateKey);
            privateEncrypt = Base64Util.encode(bytes);
            PrintUtil.printCZ("私钥加密后:" + privateEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_public_decode)
    public void publicDecode() {
        String json = "ejFXnVNkWeirM5nIpQpdubCEjou+DQON41PUSjdgg5EvOx2a20r/DKPfE9IfVCmFBBNb5JrI6QQgWB1IHbZGdnNYZTp4/51kHTzlTrXMsFCBLOjVjG3qvE923ya02ySHBCPrZ7ur7yy/L9Q1OgVwXKtmWxmQJHkQm/aHtn3fubQ=";
        try {
            String publicDecode = RSAUtil.decryptByPublicKey2(Base64Util.decode(json), publicKey);
            PrintUtil.printCZ("公钥解密后:" + publicDecode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
