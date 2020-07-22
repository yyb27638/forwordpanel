package com.leeroy.forwordpanel.forwordpanel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * This class provide interface to execute command on remote Linux.
 */

public class SSHCommandExecutor {
    private String ipAddress;

    private String username;

    private String password;

    public static final int DEFAULT_SSH_PORT = 34204;

    private Vector<String> stdout;

    public SSHCommandExecutor(final String ipAddress, final String username, final String password) {
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = password;
        stdout = new Vector<>();
    }

    public int execute(final String command) {
        int returnCode = 0;
        JSch jsch = new JSch();
        MyUserInfo userInfo = new MyUserInfo();

        try {
            // Create and connect session.
            Session session = jsch.getSession(username, ipAddress, DEFAULT_SSH_PORT);
            session.setPassword(password);
            session.setUserInfo(userInfo);
            session.connect();

            // Create and connect channel.
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(channel
                    .getInputStream()));

            channel.connect();
            System.out.println("The remote command is: " + command);

            // Get the output of remote command.
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();

            // Get the return code only after the channel is closed.
            if (channel.isClosed()) {
                returnCode = channel.getExitStatus();
            }

            // Disconnect the channel and session.
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnCode;
    }

    public Vector<String> getResultSet() {
        return stdout;
    }

    public String getResult() {
        return stdout.get(0);
    }

    public static void main(final String [] args) {
        SSHCommandExecutor sshExecutor = new SSHCommandExecutor("120.241.154.4", "root", "28L8CegNk9");
        sshExecutor.execute(String.format("iptables -n -v -L -t filter -x | grep %s | awk '{print $2}'", "34.92.251.167"));
        System.out.println(sshExecutor.getResult());
    }
}
