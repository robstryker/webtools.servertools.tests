package org.eclipse.wst.server.core.tests.integration.model;

import java.util.Calendar;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.model.ModuleFactoryDelegate;
import org.eclipse.wst.server.core.util.ModuleFile;

public class ModuleFactory extends ModuleFactoryDelegate {

	
	private IModule[] arr = new IModule[3];
	public ModuleFactory() {
		arr[0] = createModule("one", "one", "test.module", "1.0", null);
		arr[1] = createModule("two", "two", "test.module", "1.0", null);
		arr[2] = createModule("three", "three", "test.module", "1.0", null);
		
	}

	@Override
	public ModuleDelegate getModuleDelegate(IModule module) {
		return new PrivateModuleDelegate(module);
	}

	private class PrivateModuleDelegate extends ModuleDelegate{
		public PrivateModuleDelegate(IModule mod) {
		}

		@Override
		public IStatus validate() {
			return Status.OK_STATUS;
		}

		@Override
		public IModule[] getChildModules() {
			return new IModule[0];
		}

		@Override
		public IModuleResource[] members() throws CoreException {
			return new IModuleResource[] { 
					new ModuleFile("File1", ResourcesPlugin.getWorkspace().getRoot().getLocation(), Calendar.getInstance().getTime().getTime()),	
					new ModuleFile("File2", ResourcesPlugin.getWorkspace().getRoot().getLocation(), Calendar.getInstance().getTime().getTime()),	
					new ModuleFile("File3", ResourcesPlugin.getWorkspace().getRoot().getLocation(), Calendar.getInstance().getTime().getTime()),	
			};
		}
		
	}
	
	@Override
	public IModule[] getModules() {
		return arr;
	}

}
