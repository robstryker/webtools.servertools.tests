/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.server.tomcat.core.tests;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.server.tomcat.core.internal.ITomcatRuntime;
import org.eclipse.jst.server.tomcat.core.internal.ITomcatRuntimeWorkingCopy;
import org.eclipse.wst.server.core.*;

import junit.framework.TestCase;

public class TomcatRuntimeTestCase extends TestCase {
	private static final String RUNTIME_TYPE_ID_32 = "org.eclipse.jst.server.tomcat.runtime.32";
	private static final String RUNTIME_TYPE_ID_40 = "org.eclipse.jst.server.tomcat.runtime.40";
	private static final String RUNTIME_TYPE_ID_41 = "org.eclipse.jst.server.tomcat.runtime.41";
	private static final String RUNTIME_TYPE_ID_50 = "org.eclipse.jst.server.tomcat.runtime.50";
	private static final String RUNTIME_TYPE_ID_55 = "org.eclipse.jst.server.tomcat.runtime.55";

	protected static IRuntime runtime;
	protected static ITomcatRuntime tomcatRuntime;

	protected IRuntimeWorkingCopy createRuntime(String runtimeTypeId) throws Exception {
		IRuntimeType rt = ServerCore.findRuntimeType(runtimeTypeId);
		IRuntimeWorkingCopy wc = rt.createRuntime("a", null);
		wc.setLocation(new Path("c://test"));
		return wc;
	}

	public void test00CreateRuntime() throws Exception {
		runtime = createRuntime(RUNTIME_TYPE_ID_32).save(false, null);
		assertTrue(!runtime.isWorkingCopy());
	}
	
	protected void validateRuntime() throws Exception {
		IStatus status = runtime.validate(null);
		assertTrue(!status.isOK());
	}
	
	protected void adaptRuntime() throws Exception {
		tomcatRuntime = (ITomcatRuntime) runtime.loadAdapter(ITomcatRuntime.class, null);
		assertNotNull(tomcatRuntime);
		assertNotNull(tomcatRuntime.getVMInstall());
		assertNotNull(tomcatRuntime.getRuntimeClasspath(null));
	}
	
	protected void modifyRuntime() throws Exception {
		IRuntimeWorkingCopy wc = runtime.createWorkingCopy();
		ITomcatRuntimeWorkingCopy trwc = (ITomcatRuntimeWorkingCopy) wc.loadAdapter(ITomcatRuntimeWorkingCopy.class, null);
		trwc.setVMInstall(null);
		wc.save(true, null);
		tomcatRuntime = (ITomcatRuntime) runtime.loadAdapter(ITomcatRuntime.class, null);
		assertNotNull(tomcatRuntime.getVMInstall());
	}
	
	protected void deleteRuntime() throws Exception {
		runtime.delete();
		runtime = null;
		tomcatRuntime = null;
	}

	public void test01ValidateRuntime() throws Exception {
		validateRuntime();
	}
	
	public void test02AdaptRuntime() throws Exception {
		adaptRuntime();
	}
	
	public void test03ModifyRuntime() throws Exception {
		modifyRuntime();
	}

	public void test04DeleteRuntime() throws Exception {
		deleteRuntime();
	}
	
	public void test10CreateRuntime() throws Exception {
		runtime = createRuntime(RUNTIME_TYPE_ID_40).save(false, null);
		assertTrue(!runtime.isWorkingCopy());
	}

	public void test11ValidateRuntime() throws Exception {
		validateRuntime();
	}
	
	public void test12AdaptRuntime() throws Exception {
		adaptRuntime();
	}
	
	public void test13ModifyRuntime() throws Exception {
		modifyRuntime();
	}

	public void test14DeleteRuntime() throws Exception {
		deleteRuntime();
	}
	
	public void test20CreateRuntime() throws Exception {
		runtime = createRuntime(RUNTIME_TYPE_ID_41).save(false, null);
		assertTrue(!runtime.isWorkingCopy());
	}

	public void test21ValidateRuntime() throws Exception {
		validateRuntime();
	}
	
	public void test22AdaptRuntime() throws Exception {
		adaptRuntime();
	}
	
	public void test23ModifyRuntime() throws Exception {
		modifyRuntime();
	}

	public void test24DeleteRuntime() throws Exception {
		deleteRuntime();
	}
	
	public void test30CreateRuntime() throws Exception {
		runtime = createRuntime(RUNTIME_TYPE_ID_50).save(false, null);
		assertTrue(!runtime.isWorkingCopy());
	}

	public void test31ValidateRuntime() throws Exception {
		validateRuntime();
	}
	
	public void test32AdaptRuntime() throws Exception {
		adaptRuntime();
	}
	
	public void test33ModifyRuntime() throws Exception {
		modifyRuntime();
	}

	public void test34DeleteRuntime() throws Exception {
		deleteRuntime();
	}
	
	public void test40CreateRuntime() throws Exception {
		runtime = createRuntime(RUNTIME_TYPE_ID_55).save(false, null);
		assertTrue(!runtime.isWorkingCopy());
	}

	public void test41ValidateRuntime() throws Exception {
		validateRuntime();
	}
	
	public void test42AdaptRuntime() throws Exception {
		adaptRuntime();
	}
	
	public void test43ModifyRuntime() throws Exception {
		modifyRuntime();
	}

	public void test44DeleteRuntime() throws Exception {
		deleteRuntime();
	}
}
