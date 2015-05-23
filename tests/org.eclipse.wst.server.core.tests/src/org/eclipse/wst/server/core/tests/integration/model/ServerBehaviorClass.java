package org.eclipse.wst.server.core.tests.integration.model;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;

public class ServerBehaviorClass extends ServerBehaviourDelegate {

	 private boolean failPublish = false;
	
	public ServerBehaviorClass() {
		// TODO Auto-generated constructor stub
	}

	public void publish(int kind, List<IModule[]> modules, IProgressMonitor monitor, IAdaptable info) throws CoreException {
		DataModel.getInstance().setProperty(getServer(), DataModel.PUBLISH_TIME, System.currentTimeMillis());
		try {
			Thread.sleep(500);
		} catch(InterruptedException ie) {
			
		}
		if( failPublish ) {
			throw new CoreException(new Status(IStatus.ERROR, "Failed to publish", null));
		}
	}
	
	@Override
	public void stop(boolean force) {
		super.setServerState(IServer.STATE_STOPPED);
	}
	
	
	public void setPublishToFail(boolean b) {
		failPublish = b;
	}

	
	public void setModulePublishState2(IModule[] m, int i) {
		super.setModulePublishState(m, i);
	}
	public void setModuleRestartState2(IModule[] m, boolean b) {
		super.setModuleRestartState(m, b);
	}
	
	public void setModuleState2(IModule[] m, int i) {
		super.setModuleState(m, i);
	}
	public void setModuleStatus2(IModule[] m, IStatus s) {
		super.setModuleStatus(m, s);
	}
	public void setServerPublishState2(int i) {
		super.setServerPublishState(i);
	}
	public void setServerRestartState2(boolean b) {
		super.setServerRestartState(b);
	}
	public void setServerState2(int i) {
		super.setServerState(i);
	}
	public void setServerStatus2(IStatus s) {
		super.setServerStatus(s);
	}

}
