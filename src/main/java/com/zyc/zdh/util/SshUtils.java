package com.zyc.zdh.util;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Thread.sleep;
import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.apache.commons.lang3.StringUtils.trim;

import com.google.common.collect.ImmutableMap;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

public final class SshUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SshUtils.class);
    private static final String SSH = "ssh";
    private static final String FILE = "file";

    /**  登录用户名*/
    private String username;
    /**  登录密码*/
    private String password;
    /** 私钥 */
    private String privateKey;
    /**  服务器地址IP地址*/
    private String host;
    /** 端口*/
    private int port;
    SessionHolder<ChannelExec> session;
    public SshUtils(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        String connectUri=String.format("ssh://%s:%s@%s",username,password,host);
        session = new SessionHolder<>("exec", URI.create(connectUri));
    }

    private SshUtils() {
    }

    public void close(){
        session.close();
    }

    /**
     * <pre>
     * <code>
     * sftp("file:/C:/home/file.txt", "ssh://user:pass@host/home");
     * sftp("ssh://user:pass@host/home/file.txt", "file:/C:/home");
     * </code>
     *
     * <pre>
     *
     * @param fromUri
     *            file
     * @param toUri
     *            directory
     */
    public static void sftp(String fromUri, String toUri) {
        URI from = URI.create(fromUri);
        URI to = URI.create(toUri);

        if (SSH.equals(to.getScheme()) && FILE.equals(from.getScheme()))
            upload(from, to);
        else if (SSH.equals(from.getScheme()) && FILE.equals(to.getScheme()))
            download(from, to);
        else
            throw new IllegalArgumentException();
    }

    private static void upload(URI from, URI to) {
        try (SessionHolder<ChannelSftp> session = new SessionHolder<>("sftp", to);
                FileInputStream fis = new FileInputStream(new File(from))) {

            LOG.info("Uploading {} --> {}", from, session.getMaskedUri());
            ChannelSftp channel = session.getChannel();
            channel.connect();
            channel.cd(to.getPath());
            channel.put(fis, getName(from.getPath()));

        } catch (Exception e) {
            throw new RuntimeException("Cannot upload file", e);
        }
    }

    private static void download(URI from, URI to) {
        File out = new File(new File(to), getName(from.getPath()));
        try (SessionHolder<ChannelSftp> session = new SessionHolder<>("sftp", from);
                OutputStream os = new FileOutputStream(out);
                BufferedOutputStream bos = new BufferedOutputStream(os)) {

            LOG.info("Downloading {} --> {}", session.getMaskedUri(), to);
            ChannelSftp channel = session.getChannel();
            channel.connect();
            channel.cd(getFullPath(from.getPath()));
            channel.get(getName(from.getPath()), bos);

        } catch (Exception e) {
            throw new RuntimeException("Cannot download file", e);
        }
    }

    /**
     * <pre>
     * <code>
     * shell("ssh://user:pass@host", System.in, System.out);
     * </code>
     * </pre>
     */
    public static void shell(String connectUri, InputStream is, OutputStream os) {
        try (SessionHolder<ChannelShell> session = new SessionHolder<>("shell", URI.create(connectUri))) {
            shell(session, is, os);
        }
    }

    /**
     * <pre>
     * <code>
     * String remoteOutput = shell("ssh://user:pass@host/work/dir/path", "ls")
     * </code>
     * </pre>
     */
    public static String shell(String connectUri, String command) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            shell(connectUri, command, baos);
            return baos.toString();
        } catch (RuntimeException e) {
            LOG.warn(baos.toString());
            throw e;
        }
    }

    /**
     * <pre>
     * <code>
     * shell("ssh://user:pass@host/work/dir/path", "ls", System.out)
     * </code>
     * </pre>
     */
    public static void shell(String connectUri, String script, OutputStream out) {
        try (SessionHolder<ChannelShell> session = new SessionHolder<>("shell", URI.create(connectUri));
                PipedOutputStream pipe = new PipedOutputStream();
                PipedInputStream in = new PipedInputStream(pipe);
                PrintWriter pw = new PrintWriter(pipe)) {

            if (session.getWorkDir() != null)
                pw.println("cd " + session.getWorkDir());
            pw.println(script);
            pw.println("exit");
            pw.flush();

            shell(session, in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void shell(SessionHolder<ChannelShell> session, InputStream is, OutputStream os) {
        try {
            ChannelShell channel = session.getChannel();
            channel.setInputStream(is, true);
            channel.setOutputStream(os, true);

            LOG.info("Starting shell for " + session.getMaskedUri());
            session.execute();
            session.assertExitStatus("Check shell output for error details.");
        } catch (InterruptedException | JSchException e) {
            throw new RuntimeException("Cannot execute script", e);
        }
    }

    /**
     * <pre>
     * <code>
     * System.out.println(exec("ssh://user:pass@host/work/dir/path", "ls -t | head -n1"));
     * </code>
     * 
     * <pre>
     * 
     * @param connectUri
     * @param command
     * @return
     */
    public static String exec(String connectUri, String command) {
        try (SessionHolder<ChannelExec> session = new SessionHolder<>("exec", URI.create(connectUri))) {
            String scriptToExecute = session.getWorkDir() == null
                    ? command
                    : "cd " + session.getWorkDir() + "\n" + command;
            return exec(session, scriptToExecute);
        }
    }


    private static String exec(SessionHolder<ChannelExec> session, String command) {
        try (PipedOutputStream errPipe = new PipedOutputStream();
                PipedInputStream errIs = new PipedInputStream(errPipe);
                InputStream is = session.getChannel().getInputStream()) {

            ChannelExec channel = session.getChannel();
            channel.setInputStream(null);
            channel.setErrStream(errPipe);
            channel.setCommand(command);

            LOG.info("Starting exec for " + session.getMaskedUri());
            session.execute();
            String output = IOUtils.toString(is);
            session.assertExitStatus(IOUtils.toString(errIs));

            return trim(output);
        } catch (InterruptedException | JSchException | IOException e) {
            throw new RuntimeException("Cannot execute command", e);
        }
    }

    public static String kill(String connectUri, String command) {
        try (SessionHolder<ChannelExec> session = new SessionHolder<>("exec", URI.create(connectUri))) {
            String scriptToExecute = session.getWorkDir() == null
                    ? command
                    : "cd " + session.getWorkDir() + "\n" + command;
            return kill(session, scriptToExecute);
        }
    }

    private static String kill(SessionHolder<ChannelExec> session, String command) {
        try (PipedOutputStream errPipe = new PipedOutputStream();
             PipedInputStream errIs = new PipedInputStream(errPipe);
             InputStream is = session.getChannel().getInputStream()) {

            ChannelExec channel = session.getChannel();
            channel.setInputStream(null);
            channel.setErrStream(errPipe);
            channel.setCommand(command);

            LOG.info("Starting exec for " + session.getMaskedUri());
            session.execute();
            String output = IOUtils.toString(is);
            //session.assertExitStatus(IOUtils.toString(errIs));

            return trim(output);
        } catch (InterruptedException | JSchException | IOException e) {
            throw new RuntimeException("Cannot execute command", e);
        }
    }

    public static class SessionHolder<C extends Channel> implements Closeable {

        private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
        private static final int DEFAULT_PORT = 22;
        private static final int TERMINAL_HEIGHT = 1000;
        private static final int TERMINAL_WIDTH = 1000;
        private static final int TERMINAL_WIDTH_IN_PIXELS = 1000;
        private static final int TERMINAL_HEIGHT_IN_PIXELS = 1000;
        private static final int DEFAULT_WAIT_TIMEOUT = 100;

        private String channelType;
        private URI uri;
        private Session session;
        private C channel;

        public SessionHolder(String channelType, URI uri) {
            this(channelType, uri, ImmutableMap.of("StrictHostKeyChecking", "no"));
        }

        public SessionHolder(String channelType, URI uri, Map<String, String> props) {
            this.channelType = channelType;
            this.uri = uri;
            this.session = newSession(props);
            this.channel = newChannel(session);
        }

        private Session newSession(Map<String, String> props) {
            try {
                Properties config = new Properties();
                config.putAll(props);

                JSch jsch = new JSch();
                Session newSession = jsch.getSession(getUser(), uri.getHost(), getPort());
                newSession.setPassword(getPass());
                newSession.setUserInfo(new User(getUser(), getPass()));
                newSession.setDaemonThread(true);
                newSession.setConfig(config);
                newSession.connect(DEFAULT_CONNECT_TIMEOUT);
                return newSession;
            } catch (JSchException e) {
                throw new RuntimeException("Cannot create session for " + getMaskedUri(), e);
            }
        }

        @SuppressWarnings("unchecked")
        private C newChannel(Session session) {
            try {
                Channel newChannel = session.openChannel(channelType);
                if (newChannel instanceof ChannelShell) {
                    ChannelShell channelShell = (ChannelShell) newChannel;
                    channelShell.setPtyType("ANSI", TERMINAL_WIDTH, TERMINAL_HEIGHT, TERMINAL_WIDTH_IN_PIXELS, TERMINAL_HEIGHT_IN_PIXELS);
                }
                return (C) newChannel;
            } catch (JSchException e) {
                throw new RuntimeException("Cannot create " + channelType + " channel for " + getMaskedUri(), e);
            }
        }

        public void assertExitStatus(String failMessage) {
            checkState(channel.getExitStatus() == 0 , "Exit status %s for %s\n%s", channel.getExitStatus(), getMaskedUri(), failMessage);
        }

        public void execute() throws JSchException, InterruptedException {
            channel.connect();
            channel.start();
            while (!channel.isEOF())
                sleep(DEFAULT_WAIT_TIMEOUT);
        }

        public Session getSession() {
            return session;
        }

        public C getChannel() {
            return channel;
        }

        @Override
        public void close() {
            if (channel != null)
                channel.disconnect();
            if (session != null)
                session.disconnect();
        }

        public String getMaskedUri() {
            return uri.toString().replaceFirst(":[^:]*?@", "@");
        }

        public int getPort() {
            return uri.getPort() < 0 ? DEFAULT_PORT : uri.getPort();
        }

        public String getUser() {
            return uri.getUserInfo().split(":")[0];
        }

        public String getPass() {
            return uri.getUserInfo().split(":")[1];
        }

        public String getWorkDir() {
            return uri.getPath();
        }
    }

    private static class User implements UserInfo, UIKeyboardInteractive {

        private String user;
        private String pass;

        public User(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        @Override
        public String getPassword() {
            return pass;
        }

        @Override
        public boolean promptYesNo(String str) {
            return false;
        }

        @Override
        public String getPassphrase() {
            return user;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return true;
        }

        @Override
        public boolean promptPassword(String message) {
            return true;
        }

        @Override
        public void showMessage(String message) {
            // do nothing
        }

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            return null;
        }
    }
}