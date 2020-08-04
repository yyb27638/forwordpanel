package com.leeroy.forwordpanel.forwordpanel.common.util.remotessh;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class KeyLoginUserInfo implements UserInfo, UIKeyboardInteractive {

    @Override

    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
        return null;
    }

    @Override
    public String getPassphrase() {
        return null;
    }


    @Override

    public String getPassword() {


        return null;

    }


    @Override

    public boolean promptPassword(String message) {

// TODO Auto-generated method stub

        return true;

    }


    @Override

    public boolean promptPassphrase(String message) {

// TODO Auto-generated method stub

        return true;

    }


    @Override

    public boolean promptYesNo(String message) {
        return true;
    }


    @Override

    public void showMessage(String message) {
    }
}
