package org.eclipse.wst.server.core.tests.integration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServer.IOperationListener;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.ServerType;
import org.eclipse.wst.server.core.tests.integration.model.DataModel;
import org.eclipse.wst.server.core.tests.integration.model.ServerBehaviorClass;

import junit.framework.TestCase;

public class StartTwiceDefectTestCase extends TestCase {
	public void testStartBeforeSync1() throws Exception {
		_testSynchronousStartAPI("servertools.test.startBeforeSynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartBeforeSync2() throws Exception {
		_testStartOperationListenerAPI("servertools.test.startBeforeSynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartBeforeSync3() throws Exception {
		_testGenericStartAPI("servertools.test.startBeforeSynch");
		deleteAllServersAndRuntimes();
	}

	
	
	public void testStartAfterSync1() throws Exception {
		_testSynchronousStartAPI("servertools.test.startAfterSynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartAfterSync2() throws Exception {
		_testStartOperationListenerAPI("servertools.test.startAfterSynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartAfterSync3() throws Exception {
		_testGenericStartAPI("servertools.test.startAfterSynch");
		deleteAllServersAndRuntimes();
	}

	
	
	public void testStartBeforeAsync1() throws Exception {
		_testSynchronousStartAPI("servertools.test.startBeforeAsynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartBeforeAsync2() throws Exception {
		_testStartOperationListenerAPI("servertools.test.startBeforeAsynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartBeforeAsync3() throws Exception {
		_testGenericStartAPI("servertools.test.startBeforeAsynch");
		deleteAllServersAndRuntimes();
	}

	
	public void testStartAfterAsync1() throws Exception {
		_testSynchronousStartAPI("servertools.test.startAfterAsynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartAfterAsync2() throws Exception {
		_testStartOperationListenerAPI("servertools.test.startAfterAsynch");
		deleteAllServersAndRuntimes();
	}
	public void testStartAfterAsync3() throws Exception {
		_testGenericStartAPI("servertools.test.startAfterAsynch");
		deleteAllServersAndRuntimes();
	}
	
	private void _testSynchronousStartAPI(String sttString) throws Exception {
		IServerType st = ServerCore.findServerType(sttString);
		IRuntimeType rtt = st.getRuntimeType();
		IRuntimeWorkingCopy rttwc = rtt.createRuntime("Test", new NullProgressMonitor());
		IRuntime rt = rttwc.save(true, new NullProgressMonitor());
		IServerWorkingCopy swc = st.createServer("Server1", null, new NullProgressMonitor());
		IServer s = swc.save(true, new NullProgressMonitor());
		boolean startBeforePublish = ((ServerType)st).startBeforePublish();
		
		// start it using deprecated API, with no publish required
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_NONE);
		s.synchronousStart("run", new NullProgressMonitor());
		Long publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		Long startTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		DataModel.getInstance().clearProperties(s);
		
		assertNotNull(startTime);
		assertNull(publishTime);
		
		// Just change state to requiring a publish, then try again
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_INCREMENTAL);
		s.synchronousStart("run", new NullProgressMonitor());
		publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		startTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		DataModel.getInstance().clearProperties(s);
		assertNotNull(startTime);  // start occurred
		assertNotNull(publishTime); // publish occurred
		if( startBeforePublish) {
			assertTrue(startTime.longValue() < publishTime.longValue());
		} else {
			assertTrue(publishTime.longValue() < startTime.longValue());
		}
		
		
		// Run a third time to ensure a failed publish will not allow the start to run

		// Just change state to requiring a publish, then try again
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_INCREMENTAL);
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setPublishToFail(true);

		s.synchronousStart("run", new NullProgressMonitor());
		publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		startTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setPublishToFail(false);
		DataModel.getInstance().clearProperties(s);
		if(!startBeforePublish) {
			assertNotNull(publishTime);
			assertNull(startTime);
		} else {
			assertNotNull(startTime);  // start occurred
			assertNotNull(publishTime); // publish occurred
		}
	}

	private static class CustomOperationListener implements IOperationListener {
		private IStatus res = null;
		public void done(IStatus result) {
			res = result;
		}
		public IStatus getResult() {
			return res;
		}
	}
	
	private void waitForOpListener(CustomOperationListener opl) throws Exception {
		// Wait maximum of 10 seconds
		long start = System.currentTimeMillis();
		while(opl.getResult() == null && System.currentTimeMillis() < (start + 10000)) {
			try {
				Thread.sleep(100);
			} catch(InterruptedException ie) {
			}
		}
		if( opl.getResult() == null ) {
			throw new Exception("IOperationListener never informed of a finish");
		}
	}
	
	// This may be synchronous or asynch
	private void _testStartOperationListenerAPI(String sttString) throws Exception {
		IServerType st = ServerCore.findServerType(sttString);
		IRuntimeType rtt = st.getRuntimeType();
		IRuntimeWorkingCopy rttwc = rtt.createRuntime("Test", new NullProgressMonitor());
		IRuntime rt = rttwc.save(true, new NullProgressMonitor());
		IServerWorkingCopy swc = st.createServer("Server1", null, new NullProgressMonitor());
		IServer s = swc.save(true, new NullProgressMonitor());
		boolean synchronousType = ((ServerType)st).synchronousStart();
		
		CustomOperationListener opl = new CustomOperationListener();
		
		// start it using deprecated API, with no publish required
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_NONE);
		s.start("run", opl);
		Long startCallReturnTime = System.currentTimeMillis();
		waitForOpListener(opl);
		Long publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		Long startJobStartTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		DataModel.getInstance().clearProperties(s);
		assertNotNull(startCallReturnTime);
		assertNotNull(startJobStartTime);
		assertNull(publishTime);
		if( synchronousType) {
			// Start Job was entered before start() call returned
			assertTrue(startCallReturnTime > startJobStartTime);
		} else {
			// Start Job was entered after start() call returned
			assertTrue(startJobStartTime > startCallReturnTime);
		}
		
		opl = new CustomOperationListener();
		// Just change state to requiring a publish, then try again
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_INCREMENTAL);
		s.start("run", opl);
		startCallReturnTime = System.currentTimeMillis();
		waitForOpListener(opl);
		publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		startJobStartTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		DataModel.getInstance().clearProperties(s);
		assertNotNull(startCallReturnTime);
		assertNotNull(startJobStartTime);  // start occurred
		assertNotNull(publishTime); // publish occurred
		if( ((ServerType)st).startBeforePublish()) {
			assertTrue(startJobStartTime.longValue() < publishTime.longValue());
		} else {
			assertTrue(publishTime.longValue() < startJobStartTime.longValue());
		}
		if( synchronousType) {
			// Start Job was entered before start() call returned
			assertTrue(startCallReturnTime > startJobStartTime);
		} else {
			// Start Job was entered after start() call returned
			assertTrue(startJobStartTime >= startCallReturnTime);
		}

		
		// Run a third time to ensure a failed publish will not allow the start to run
		// when the server is publishBeforeStart 

		// Just change state to requiring a publish, then try again
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_INCREMENTAL);
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setPublishToFail(true);
		opl = new CustomOperationListener();
		s.start("run",  opl);
		startCallReturnTime = System.currentTimeMillis();
		waitForOpListener(opl);
		publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		startJobStartTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		DataModel.getInstance().clearProperties(s);
		assertNotNull(startCallReturnTime);
		if(!((ServerType)st).startBeforePublish()) {
			assertNotNull(publishTime);
			assertNull(startJobStartTime);
		} else {
			assertNotNull(publishTime);
			assertNotNull(startJobStartTime);
		}
		if( synchronousType) {
			if( ((ServerType)st).startBeforePublish()) {
				// Start Job was entered before start() call returned
				assertTrue(startCallReturnTime > startJobStartTime);
			} else {
				// Publish Job was entered before start() call returned
				assertTrue(startCallReturnTime > publishTime);
			}
		} else { // async
			if( ((ServerType)st).startBeforePublish()) {
				// Start Job was entered at or after start() call returned
				assertTrue(startJobStartTime >= startCallReturnTime);
			} else {
				// async publish before start
				// Publish Job was entered at or after start() call returned
				assertTrue(publishTime >= startCallReturnTime);
			}
		}
		
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setPublishToFail(false);
	}
	
	private void waitForJobsToComplete() {
		try {
			Thread.sleep(500);
		} catch(InterruptedException ie) {}
		waitForIdle(300, 10000);
	}
	
	private void _testGenericStartAPI(String sttString) throws Exception {
		IServerType st = ServerCore.findServerType(sttString);
		IRuntimeType rtt = st.getRuntimeType();
		IRuntimeWorkingCopy rttwc = rtt.createRuntime("Test", new NullProgressMonitor());
		IRuntime rt = rttwc.save(true, new NullProgressMonitor());
		IServerWorkingCopy swc = st.createServer("Server1", null, new NullProgressMonitor());
		IServer s = swc.save(true, new NullProgressMonitor());
		boolean synchronousType = ((ServerType)st).synchronousStart();

		// start it using deprecated API, with no publish required
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_NONE);
		s.start("run", new NullProgressMonitor());
		Long startCallReturnTime = System.currentTimeMillis();
		try {
			Thread.sleep(500);
		} catch(InterruptedException ie) {}
		waitForJobsToComplete();
		Long publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		Long startJobStartTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		DataModel.getInstance().clearProperties(s);
		assertNotNull(startCallReturnTime);
		assertNotNull(startJobStartTime);
		assertNull(publishTime);
		if( synchronousType) {
			// Start Job was entered before start() call returned
			assertTrue(startCallReturnTime > startJobStartTime);
		} else {
			// Start Job was entered after start() call returned
			assertTrue(startJobStartTime >= startCallReturnTime);
		}

		
		// Just change state to requiring a publish, then try again
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_INCREMENTAL);
		s.start("run", new NullProgressMonitor());
		startCallReturnTime = System.currentTimeMillis();
		waitForJobsToComplete();
		publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		startJobStartTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		DataModel.getInstance().clearProperties(s);
		assertNotNull(startJobStartTime);  // start occurred
		assertNotNull(publishTime); // publish occurred
		if( ((ServerType)st).startBeforePublish()) {
			assertTrue(startJobStartTime.longValue() < publishTime.longValue());
		} else {
			assertTrue(publishTime.longValue() < startJobStartTime.longValue());
		}
		if( synchronousType) {
			// Start Job was entered before start() call returned
			assertTrue(startCallReturnTime > startJobStartTime);
		} else {
			// Start Job was entered after start() call returned
			assertTrue(startJobStartTime >= startCallReturnTime);
		}

		
		// Run a third time to ensure a failed publish will not allow the start to run
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setServerPublishState2(IServer.PUBLISH_STATE_INCREMENTAL);
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setPublishToFail(true);
		s.start("run",  new NullProgressMonitor());
		startCallReturnTime = System.currentTimeMillis();
		waitForJobsToComplete();
		publishTime = DataModel.getInstance().getProperty(s, DataModel.PUBLISH_TIME, Long.class);
		startJobStartTime = DataModel.getInstance().getProperty(s, DataModel.START_TIME, Long.class);
		s.stop(true);
		((ServerBehaviorClass)(s.loadAdapter(ServerBehaviorClass.class, new NullProgressMonitor()))).setPublishToFail(false);
		DataModel.getInstance().clearProperties(s);
		assertNotNull(startCallReturnTime);
		if(!((ServerType)st).startBeforePublish()) {
			assertNotNull(publishTime);
			assertNull(startJobStartTime);
		} else {
			assertNotNull(publishTime);
			assertNotNull(startJobStartTime);
		}
		if( synchronousType) {
			if( ((ServerType)st).startBeforePublish()) {
				// Start Job was entered before start() call returned
				assertTrue(startCallReturnTime > startJobStartTime);
			} else {
				// Publish Job was entered before start() call returned
				assertTrue(startCallReturnTime > publishTime);
			}
		} else { // async
			if( ((ServerType)st).startBeforePublish()) {
				// Start Job was entered after start() call returned
				assertTrue(startJobStartTime >= startCallReturnTime);
			} else {
				// async publish before start
				// Publish Job was entered after start() call returned
				assertTrue(publishTime >= startCallReturnTime);
			}
		}
	}

	
	
	
	/*
	 * Utility methods
	 */
	
	public static void deleteAllServersAndRuntimes() throws Exception {
		deleteAllServers();
		deleteAllRuntimes();
	}
	
	public static void deleteAllServers() throws CoreException {
		IServer[] servers = ServerCore.getServers();
		for( int i = 0; i < servers.length; i++ ) {
			servers[i].delete();
		}
	}

	public static void deleteAllRuntimes() throws CoreException {
		IRuntime[] runtimes = ServerCore.getRuntimes();
		for( int i = 0; i < runtimes.length; i++ ) {
			runtimes[i].delete();
		}
	}
	

	private static final long MAX_IDLE = 20 * 60 * 1000L;
	private static final long DEFAULT_DELAY = 500;

	public static void waitForIdle() {
		waitForIdle(DEFAULT_DELAY);
	}

	public static void waitForIdle(long delay) {
		waitForIdle(delay, MAX_IDLE);
	}

	public static void waitForIdle(long delay, long maxIdle) {
		long start = System.currentTimeMillis();
		while (!isIdle()) {
			try {
				Thread.sleep(delay);
			} catch(InterruptedException ie) {
				
			}
			if ((System.currentTimeMillis() - start) > maxIdle) {
				Job[] jobs = Job.getJobManager().find(null);
				StringBuffer str = new StringBuffer();
				for (Job job : jobs) {
					if (job.getThread() != null && !shouldIgnoreJob(job)) {
						str.append("\n").append(job.getName()).append(" (")
								.append(job.getClass()).append(")");
					}
				}
				if (str.length() > 0)
					throw new RuntimeException(
							"Long running tasks detected:" + str.toString()); //$NON-NLS-1$
			}
		}
	}
	
	private static boolean isIdle() {
		boolean isIdle = Job.getJobManager().isIdle();
		if (!isIdle) {
			Job[] jobs = Job.getJobManager().find(null);
			for (Job job : jobs) {
				if (job.getThread() != null && !shouldIgnoreJob(job)) {
					return false;
				}
			}
		}
		return true;
	}

	// The list of non-build related long running jobs
	private static final String[] IGNORE_JOBS_NAMES = new String[] {
		"workbench auto-save job"
	};
	
	/**
	 * A workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=405456 in Eclipse 4.3.0M7
	 * (since -Dorg.eclipse.ui.testsDisableWorkbenchAutoSave=true option is not yet implemented in M7)
	 *
	 * @param job
	 * @return
	 */
	private static boolean shouldIgnoreJob(Job job) {
		for (String name : IGNORE_JOBS_NAMES) {
			if (name != null && job != null && job.getName() != null && 
					name.equalsIgnoreCase(job.getName().trim())) {
				System.out.println("Ignoring the non-build long running job: " + job.getName());
				return true;
			}
		}
		return false;
	}	
}
