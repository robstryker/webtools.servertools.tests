/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
�*
 * Contributors:
 *    IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.jst.server.core.tests;

import org.eclipse.jst.server.core.internal.JavaServerPlugin;
import junit.framework.TestCase;

public class ExistenceTest extends TestCase {
	public void testPluginExists() {
		assertNotNull(JavaServerPlugin.getInstance());
	}
}