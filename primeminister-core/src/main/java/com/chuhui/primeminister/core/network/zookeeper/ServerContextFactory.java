package com.chuhui.primeminister.core.network.zookeeper;

import com.chuhui.primeminister.core.PrimeMinisterServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.JMException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerContextFactory
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
public abstract class ServerContextFactory {


    // Tells whether SSL is enabled on this ServerCnxnFactory
    protected boolean secure;

    /**
     * The buffer will cause the connection to be close when we do a send.
     */
    static final ByteBuffer closeConn = ByteBuffer.allocate(0);

    public abstract int getLocalPort();

    public abstract Iterable<ServerContext> getConnections();

    public int getNumAliveConnections() {
        return cnxns.size();
    }


    /**
     * @return true if the cnxn that contains the sessionId exists in this ServerCnxnFactory
     *         and it's closed. Otherwise false.
     */
    public abstract boolean closeSession(long sessionId);

    public void configure(InetSocketAddress addr, int maxcc) throws IOException {
        configure(addr, maxcc, false);
    }

    public abstract void configure(InetSocketAddress addr, int maxcc, boolean secure)
            throws IOException;

    public abstract void reconfigure(InetSocketAddress addr);

//    protected SaslServerCallbackHandler saslServerCallbackHandler;
//    public Login login;

    /** Maximum number of connections allowed from particular host (ip) */
    public abstract int getMaxClientCnxnsPerHost();

    /** Maximum number of connections allowed from particular host (ip) */
    public abstract void setMaxClientCnxnsPerHost(int max);

    public boolean isSecure() {
        return secure;
    }

    public void startup(PrimeMinisterServer zkServer) throws IOException, InterruptedException {
        startup(zkServer, true);
    }

    // This method is to maintain compatiblity of startup(zks) and enable sharing of zks
    // when we add secureCnxnFactory.
    public abstract void startup(PrimeMinisterServer zkServer, boolean startServer)
            throws IOException, InterruptedException;

    public abstract void join() throws InterruptedException;

    public abstract void shutdown();

    public abstract void start();

    @Autowired
    protected PrimeMinisterServer pmServer;


    public abstract void closeAll();





    public abstract InetSocketAddress getLocalAddress();

    public abstract void resetAllConnectionStats();

    public abstract Iterable<Map<String, Object>> getAllConnectionInfo(boolean brief);

    private final ConcurrentHashMap<ServerContext, ConnectionBean> connectionBeans = new ConcurrentHashMap<>();

    // Connection set is relied on heavily by four letter commands
    // Construct a ConcurrentHashSet using a ConcurrentHashMap
    protected final Set<ServerContext> cnxns = Collections.newSetFromMap(
            new ConcurrentHashMap<ServerContext, Boolean>());
    public void unregisterConnection(ServerContext serverCnxn) {
        ConnectionBean jmxConnectionBean = connectionBeans.remove(serverCnxn);
        if (jmxConnectionBean != null){
          //  MBeanRegistry.getInstance().unregister(jmxConnectionBean);
        }
    }

    public void registerConnection(ServerContext serverContext) {
        if (pmServer != null) {
            ConnectionBean jmxConnectionBean = new ConnectionBean(serverContext, pmServer);
//            try {
//                MBeanRegistry.getInstance().register(jmxConnectionBean, zkServer.jmxServerBean);
//                connectionBeans.put(serverCnxn, jmxConnectionBean);
//            } catch (JMException e) {
//                LOG.warn("Could not register connection", e);
//            }
        }

    }

    /**
     *
     * 配置Sasl?不知道是什么...
     *
     * 如果指定,初始化服务器SASL.
     *
     * If the user has specified a "ZooKeeperServer.LOGIN_CONTEXT_NAME_KEY"
     * or a jaas.conf using "java.security.auth.login.config"
     * the authentication is required and an exception is raised.
     *
     * 否则,不会配置任何身份验证,也不会引发任何异常.
     * Otherwise no authentication is configured and no exception is raised.
     *
     * @throws IOException if jaas.conf is missing or there's an error in it.
     */
//    protected void configureSaslLogin() throws IOException {
//        String serverSection = System.getProperty(ZooKeeperSaslServer.LOGIN_CONTEXT_NAME_KEY,
//                ZooKeeperSaslServer.DEFAULT_LOGIN_CONTEXT_NAME);
//
//        // Note that 'Configuration' here refers to javax.security.auth.login.Configuration.
//        AppConfigurationEntry entries[] = null;
//        SecurityException securityException = null;
//        try {
//            entries = Configuration.getConfiguration().getAppConfigurationEntry(serverSection);
//        } catch (SecurityException e) {
//            // handle below: might be harmless if the user doesn't intend to use JAAS authentication.
//            securityException = e;
//        }
//
//        // No entries in jaas.conf
//        // If there's a configuration exception fetching the jaas section and
//        // the user has required sasl by specifying a LOGIN_CONTEXT_NAME_KEY or a jaas file
//        // we throw an exception otherwise we continue without authentication.
//        if (entries == null) {
//            String jaasFile = System.getProperty(Environment.JAAS_CONF_KEY);
//            String loginContextName = System.getProperty(ZooKeeperSaslServer.LOGIN_CONTEXT_NAME_KEY);
//            if (securityException != null && (loginContextName != null || jaasFile != null)) {
//                String errorMessage = "No JAAS configuration section named '" + serverSection +  "' was found";
//                if (jaasFile != null) {
//                    errorMessage += " in '" + jaasFile + "'.";
//                }
//                if (loginContextName != null) {
//                    errorMessage += " But " + ZooKeeperSaslServer.LOGIN_CONTEXT_NAME_KEY + " was set.";
//                }
//                LOG.error(errorMessage);
//                throw new IOException(errorMessage);
//            }
//            return;
//        }
//
//        // jaas.conf entry available
//        try {
//            saslServerCallbackHandler = new SaslServerCallbackHandler(Configuration.getConfiguration());
//            login = new Login(serverSection, saslServerCallbackHandler, new ZKConfig() );
//            login.startThreadIfNeeded();
//        } catch (LoginException e) {
//            throw new IOException("Could not configure server because SASL configuration did not allow the "
//                    + " ZooKeeper server to authenticate itself properly: " + e);
//        }
//    }


}
