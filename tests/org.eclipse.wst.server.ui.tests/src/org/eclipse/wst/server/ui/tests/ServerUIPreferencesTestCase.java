/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.server.ui.tests;

import junit.framework.TestCase;

import org.eclipse.wst.server.ui.internal.ServerUIPlugin;
import org.eclipse.wst.server.ui.internal.ServerUIPreferences;

public class ServerUIPreferencesTestCase extends TestCase {
	protected static ServerUIPreferences prefs;

	public void test00GetProperties() throws Exception {
		prefs = ServerUIPlugin.getPreferences();
	}

	public void test02GetPref() throws Exception {
		prefs.getSaveEditors();
	}

	public void test05SetPref() throws Exception {
		prefs.setSaveEditors(ServerUIPreferences.SAVE_EDITORS_ALWAYS);
		assertEquals(prefs.getSaveEditors(), ServerUIPreferences.SAVE_EDITORS_ALWAYS);
	}

	public void test06SetPref() throws Exception {
		prefs.setSaveEditors(ServerUIPreferences.SAVE_EDITORS_NEVER);
		assertEquals(prefs.getSaveEditors(), ServerUIPreferences.SAVE_EDITORS_NEVER);
	}

	public void test07SetPref() throws Exception {
		prefs.setSaveEditors(ServerUIPreferences.SAVE_EDITORS_PROMPT);
		assertEquals(prefs.getSaveEditors(), ServerUIPreferences.SAVE_EDITORS_PROMPT);
	}

	public void test08SetPref() throws Exception {
		prefs.setLaunchMode(ServerUIPreferences.LAUNCH_MODE_RESTART);
		assertEquals(prefs.getLaunchMode(), ServerUIPreferences.LAUNCH_MODE_RESTART);
	}

	public void test09SetPref() throws Exception {
		prefs.setLaunchMode(ServerUIPreferences.LAUNCH_MODE_CONTINUE);
		assertEquals(prefs.getLaunchMode(), ServerUIPreferences.LAUNCH_MODE_CONTINUE);
	}

	public void test10SetPref() throws Exception {
		prefs.setLaunchMode(ServerUIPreferences.LAUNCH_MODE_PROMPT);
		assertEquals(prefs.getLaunchMode(), ServerUIPreferences.LAUNCH_MODE_PROMPT);
	}

	public void test11SetPref() throws Exception {
		prefs.setLaunchMode2(ServerUIPreferences.LAUNCH_MODE2_RESTART);
		assertEquals(prefs.getLaunchMode2(), ServerUIPreferences.LAUNCH_MODE2_RESTART);
	}

	public void test12SetPref() throws Exception {
		prefs.setLaunchMode2(ServerUIPreferences.LAUNCH_MODE2_DISABLE_BREAKPOINTS);
		assertEquals(prefs.getLaunchMode2(), ServerUIPreferences.LAUNCH_MODE2_DISABLE_BREAKPOINTS);
	}

	public void test13SetPref() throws Exception {
		prefs.setLaunchMode2(ServerUIPreferences.LAUNCH_MODE2_CONTINUE);
		assertEquals(prefs.getLaunchMode2(), ServerUIPreferences.LAUNCH_MODE2_CONTINUE);
	}

	public void test14SetPref() throws Exception {
		prefs.setLaunchMode2(ServerUIPreferences.LAUNCH_MODE2_PROMPT);
		assertEquals(prefs.getLaunchMode2(), ServerUIPreferences.LAUNCH_MODE2_PROMPT);
	}

	public void test15SetPref() throws Exception {
		prefs.setEnableBreakpoints(ServerUIPreferences.ENABLE_BREAKPOINTS_ALWAYS);
		assertEquals(prefs.getEnableBreakpoints(), ServerUIPreferences.ENABLE_BREAKPOINTS_ALWAYS);
	}

	public void test16SetPref() throws Exception {
		prefs.setEnableBreakpoints(ServerUIPreferences.ENABLE_BREAKPOINTS_NEVER);
		assertEquals(prefs.getEnableBreakpoints(), ServerUIPreferences.ENABLE_BREAKPOINTS_NEVER);
	}

	public void test17SetPref() throws Exception {
		prefs.setEnableBreakpoints(ServerUIPreferences.ENABLE_BREAKPOINTS_PROMPT);
		assertEquals(prefs.getEnableBreakpoints(), ServerUIPreferences.ENABLE_BREAKPOINTS_PROMPT);
	}

	public void test19DefaultPref() throws Exception {
		prefs.setSaveEditors(prefs.getDefaultSaveEditors());
		assertEquals(prefs.getSaveEditors(), prefs.getDefaultSaveEditors());
	}
}