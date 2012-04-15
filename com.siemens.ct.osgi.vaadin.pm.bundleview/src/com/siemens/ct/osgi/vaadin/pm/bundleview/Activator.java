/*******************************************************************************
 * Copyright (c) 2010 Kai Toedter and Siemens AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kai Toedter - initial API and implementation
 *******************************************************************************/

package com.siemens.ct.osgi.vaadin.pm.bundleview;

import java.util.ArrayList;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

public class Activator implements BundleActivator, SynchronousBundleListener {

	private static Bundle[] bundles;

	public static synchronized Bundle[] getBundles() {
		return bundles;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Bundle[] allBundles = context.getBundles();
		ArrayList<Bundle> bundleList = new ArrayList<Bundle>();

		// Hack for adding only our relevant bundles to the list
		for (Bundle bundle : allBundles) {
			String symbolicName = bundle.getSymbolicName();
			if ((symbolicName.startsWith("com.siemens.ct.osgi.vaadin.pm") || symbolicName
			      .startsWith("com.siemens.ct.pm.model."))
			      && !((symbolicName.contains("main")
			            || symbolicName.contains("theme")
			            || symbolicName.contains("bundleview") || symbolicName
			            .contains("logback")))) {
				bundleList.add(bundle);
			}
		}
		bundles = bundleList.toArray(new Bundle[] {});
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		if (BundleEvent.STARTED == event.getType()) {

		} else if (BundleEvent.STOPPED == event.getType()) {

		}
	}

}
