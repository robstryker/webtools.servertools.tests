package org.eclipse.wst.server.core.tests.integration.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.Server;

public class StartServerLaunchConfiguration implements ILaunchConfigurationDelegate2, ILaunchConfigurationDelegate {

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		IServer s = getServer(configuration);
		DataModel.getInstance().setProperty(s, DataModel.START_TIME, System.currentTimeMillis());
		final ServerBehaviorClass beh = (ServerBehaviorClass)s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor());
		beh.setServerState2(IServer.STATE_STARTING);
		// Simulate a delayed start
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch(InterruptedException ie) {
					// ignore
				}
				beh.setServerState2(IServer.STATE_STARTED);
			}
		}.start();
	}
	
	public static IServer getServer(ILaunchConfiguration configuration) throws CoreException {
		String serverId = configuration.getAttribute(Server.ATTR_SERVER_ID, (String) null);
		if (serverId != null)
			return ServerCore.findServer(serverId);
		return null;
	}

	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub
		return true;
	}

}
