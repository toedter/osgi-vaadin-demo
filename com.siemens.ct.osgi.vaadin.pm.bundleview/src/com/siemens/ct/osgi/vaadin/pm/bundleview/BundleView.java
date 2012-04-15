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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import com.siemens.ct.osgi.vaadin.pm.main.service.IViewContribution;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class BundleView implements IViewContribution {

	private Component view;
	private Table table;

	@Override
	public String getIcon() {
		return "icons/package.png";
	}

	@Override
	public String getName() {
		return "Bundle View";
	}

	@SuppressWarnings("serial")
	@Override
	public Component getView(Application application) {
		if (view == null) {
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setMargin(true);
			verticalLayout.setSpacing(true);

			table = new Table();
			table.addContainerProperty("Bundle Symbolic Name", String.class,
			      new ThemeResource("icons/bundle.png"));
			table.addContainerProperty("Version", String.class, null);
			table.addContainerProperty("State", String.class, null);
			table.addContainerProperty("Active", CheckBox.class, null);
			table.setWidth("100%");
			table.setPageLength(8);
			table.setSortContainerPropertyId("Bundle Symbolic Name");
			table.setSortAscending(true);
			table.setImmediate(true);

			refreshTable();
			verticalLayout.addComponent(table);

			HorizontalLayout horizontalLayout = new HorizontalLayout();
			horizontalLayout.setSpacing(true);

			Button refreshButton = new Button("Refresh Table");
			refreshButton.addListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					refreshTable();
				}
			});
			horizontalLayout.addComponent(refreshButton);

			Button selectAllButton = new Button("Select All");
			selectAllButton.addListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					Bundle[] bundles = Activator.getBundles();
					for (Bundle bundle : bundles) {
						try {
							bundle.start();
						} catch (BundleException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						refreshTable();
					}
				}
			});
			horizontalLayout.addComponent(selectAllButton);

			Button deselectAllButton = new Button("Deselect All");
			deselectAllButton.addListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					Bundle[] bundles = Activator.getBundles();
					for (Bundle bundle : bundles) {
						try {
							bundle.stop();
						} catch (BundleException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						refreshTable();
					}
				}
			});
			horizontalLayout.addComponent(deselectAllButton);

			verticalLayout.addComponent(horizontalLayout);
			view = verticalLayout;
		}
		return view;
	}

	private void refreshTable() {
		Bundle[] bundles = Activator.getBundles();
		table.removeAllItems();

		int i = 1;
		for (Bundle bundle : bundles) {
			final Bundle selectedBundle = bundle;
			CheckBox checkBox = new CheckBox();
			checkBox.setImmediate(true);
			checkBox.setValue(bundle.getState() == Bundle.ACTIVE);
			checkBox.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(
				      com.vaadin.data.Property.ValueChangeEvent event) {
					if (selectedBundle.getState() == Bundle.ACTIVE) {
						try {
							selectedBundle.stop();
							refreshTable();
						} catch (BundleException e1) {
							e1.printStackTrace();
						}
					} else if (selectedBundle.getState() == Bundle.RESOLVED) {
						try {
							selectedBundle.start();
							refreshTable();
						} catch (BundleException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			table.addItem(
			      new Object[] { bundle.getSymbolicName(), bundle.getVersion(),
			            getStateString(bundle), checkBox }, i++);
		}
		table.sort();
	}

	String getStateString(Bundle bundle) {
		switch (bundle.getState()) {
		case Bundle.ACTIVE:
			return "ACTIVE";
		case Bundle.INSTALLED:
			return "INSTALLED";
		case Bundle.RESOLVED:
			return "RESOLVED";
		case Bundle.UNINSTALLED:
			return "UNINSTALLED";
		default:
			return "UNKNOWN";
		}
	}
}
