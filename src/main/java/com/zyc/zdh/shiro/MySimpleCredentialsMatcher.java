package com.zyc.zdh.shiro;

import com.zyc.zdh.util.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class MySimpleCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
        Object tokenCredentials = null;
        try {
            tokenCredentials = Encrypt.AESencrypt(new String(authcToken.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object accountCredentials = getCredentials(info);
        return accountCredentials.equals(tokenCredentials);

    }
}
