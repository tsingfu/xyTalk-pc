package xysoft.im.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.iqprivate.PrivateDataManager;
import xysoft.im.app.Launcher;
import xysoft.im.utils.DebugUtil;

public final class SessionManager implements ConnectionListener {
    private AbstractXMPPConnection connection;
	private String username;   //用于断开连接后的重新登陆
    private String password;//用于断开连接后的重新登陆
	private String JID;
	private String userBareAddress;
    
    private PrivateDataManager personalDataManager;
    private List<PresenceListener> presenceListeners = new ArrayList<>();
    private DiscoverItems discoverItems;

    public SessionManager() {
    }


    public void initializeSession(AbstractXMPPConnection conn) {
    	connection = conn;
        personalDataManager = PrivateDataManager.getInstanceFor(conn);

        discoverItems();
    
        List<DiscoverItems.Item> it = discoverItems.getItems();//

	    for (DiscoverItems.Item item : it) {	     	
	    	DebugUtil.debug(item.getEntityID()+item.getName());
	     }
    }

    /**
     * 初始化服务发现service discovery.
     */
    private void discoverItems() {
        ServiceDiscoveryManager disco = ServiceDiscoveryManager.getInstanceFor(Launcher.connection);
        try {
            try {
				discoverItems = disco.discoverItems(Launcher.connection.getServiceName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        catch (XMPPException | SmackException e) {
            discoverItems = new DiscoverItems();
        }
    }

    public AbstractXMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(AbstractXMPPConnection connection) {
		this.connection = connection;
	}
	
    public String getJID() {
		return JID;
	}

	public void setJID(String jID) {
		JID = jID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserBareAddress() {
		return userBareAddress;
	}

	public void setUserBareAddress(String userBareAddress) {
		this.userBareAddress = userBareAddress;
	}

    public PrivateDataManager getPersonalDataManager() {
        return personalDataManager;
    }

    @Override
    public void connected( XMPPConnection xmppConnection )
    {
    	DebugUtil.debug("connected");
    }

    @Override
    public void authenticated( XMPPConnection xmppConnection, boolean b )
    {
    	DebugUtil.debug("authenticated");
    }

    public void connectionClosed() {
    	DebugUtil.debug("connectionClosed");

    }

    public void addPresenceListener(PresenceListener listener) {
        presenceListeners.add(listener);
    }

    public void removePresenceListener(PresenceListener listener) {
        presenceListeners.remove(listener);
    }

    public DiscoverItems getDiscoveredItems() {
        return discoverItems;
    }

    public void reconnectingIn(int i) {
    	DebugUtil.debug("reconnecting...");
    	//自动断线重连
		try {
			Launcher.connection.connect();
			Launcher.connection.login();
		} catch (SmackException | IOException | XMPPException |InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			 
    }


    public void reconnectionFailed(Exception exception) {
    	DebugUtil.debug("reconnectionFailed");
    }

	@Override
	public void connectionClosedOnError(Exception e) {
    	DebugUtil.debug("connectionClosedOnError");
	}

	@Override
	public void reconnectionSuccessful() {
		DebugUtil.debug("reconnectionSuccessful");
	}

}
