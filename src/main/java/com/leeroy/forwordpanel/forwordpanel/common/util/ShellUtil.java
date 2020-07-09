package com.leeroy.forwordpanel.forwordpanel.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class ShellUtil {

    public static String execShell(String shell) {
        log.info("execute shell: {}", shell);
        try {
            String[] cmd = new String[]{"/bin/sh", "-c", shell};
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            log.info("shell result: {}", result);
            return sb.toString();
        } catch (Exception e) {
            log.error("----error-----");
            e.printStackTrace();
        }
        return null;
    }
}
