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

package com.siemens.ct.osgi.vaadin.pm.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.ct.osgi.vaadin.pm.main.service.IActionContribution;
import com.siemens.ct.osgi.vaadin.pm.main.service.IViewContribution;
import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class MainApplication extends Application {

	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(MainApplication.class);

	private final List<IViewContribution> viewContributions = Collections
	      .synchronizedList(new ArrayList<IViewContribution>());
	private final List<IActionContribution> actionContributions = Collections
	      .synchronizedList(new ArrayList<IActionContribution>());
	private final Map<IActionContribution, Button> buttonActionMap = Collections
	      .synchronizedMap(new HashMap<IActionContribution, Button>());
	private final Map<IActionContribution, MenuItem> menuActionMap = Collections
	      .synchronizedMap(new HashMap<IActionContribution, MenuItem>());

	private Window main;
	private VerticalLayout mainLayout;
	private TabSheet tabSheet;
	private Window aboutWindow;

	private volatile boolean initialized = false;

	private HorizontalLayout toolbar;

	private MenuBar.MenuItem actionMenu;

	@Override
	public void init() {
		logger.info("Dynamic Vaadin OSGi demo init...");
		setTheme(Reindeer.THEME_NAME);
		// setTheme(Runo.THEME_NAME);
		// setTheme("demo");
		main = new Window("Dynamic Vaadin OSGi Demo");
		mainLayout = (VerticalLayout) main.getContent();
		mainLayout.setMargin(false);
		mainLayout.setStyleName("blue");
		setMainWindow(main);

		mainLayout.setSizeFull();
		mainLayout.addComponent(getMenu());

		HorizontalLayout header = new HorizontalLayout();

		header.addComponent(getHeader());
		header.addComponent(getToolbar());
		mainLayout.addComponent(header);

		CssLayout margin = new CssLayout();
		margin.setMargin(false, true, true, true);
		margin.setSizeFull();
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		margin.addComponent(tabSheet);
		mainLayout.addComponent(margin);
		mainLayout.setExpandRatio(margin, 1);

		for (IViewContribution viewContribution : viewContributions) {
			tabSheet.addTab(viewContribution.getView(this),
			      viewContribution.getName(),
			      new ThemeResource(viewContribution.getIcon()));
		}

		for (IActionContribution actionContribution : actionContributions) {
			addActionContribution(actionContribution);
		}

		initialized = true;
	}

	@SuppressWarnings("serial")
	private MenuBar getMenu() {
		MenuBar menubar = new MenuBar();
		menubar.setWidth("100%");
		actionMenu = menubar.addItem("Action", null);

		actionMenu.addItem("Built-in Action...", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				main.showNotification("Built-in Action executed!");
			}
		});
		actionMenu.addSeparator();

		final MenuBar.MenuItem viewMenu = menubar.addItem("Help", null);
		viewMenu.addItem("About...", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				main.addWindow(getAboutDialog());
			}
		});

		return menubar;
	}

	private Layout getHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setMargin(true);
		header.setSpacing(true);
		// header.setStyleName(Reindeer.LAYOUT_BLACK);

		CssLayout titleLayout = new CssLayout();
		H2 title = new H2("Dynamic Vaadin OSGi Demo");
		titleLayout.addComponent(title);
		SmallText description = new SmallText(
		      "Select the \"Bundle View\" tab and activate/stop OSGi bundles dynamically.");
		description.setSizeUndefined();
		titleLayout.addComponent(description);

		header.addComponent(titleLayout);

		return header;
	}

	private Layout getToolbar() {
		if (toolbar == null) {
			toolbar = new HorizontalLayout();
			toolbar.setMargin(true);
			toolbar.setSpacing(true);
		}
		return toolbar;
	}

	private Window getAboutDialog() {
		if (aboutWindow == null) {
			aboutWindow = new Window("About...");
			aboutWindow.setModal(true);
			aboutWindow.setWidth("400px");

			VerticalLayout layout = (VerticalLayout) aboutWindow.getContent();
			layout.setMargin(true);
			layout.setSpacing(true);
			layout.setStyleName("blue");

			CssLayout titleLayout = new CssLayout();
			H2 title = new H2("Dynamic Vaadin OSGi Demo");
			titleLayout.addComponent(title);
			SmallText description = new SmallText(
			      "<br>Copyright (c) Siemens AG, Kai Tödter and others.<br>"
			            + "Licensed under Eclipse Public License (EPL).<br><br>"
			            + "This software contains modules licenced under<br>"
			            + " the Apache Software Foundation 2.0 license (ASF) and EPL<br><br>"
			            + "Many thanks to Chris Brind, Neil Bartlett and<br>"
			            + " Petter Holmström for their OSGi and Vaadin<br>"
			            + " related work, blogs, and bundles.<br><br>"
			            + "The icons are from the Silk icon set by Mark James<br>"
			            + "<a href=\"http://www.famfamfam.com/lab/icons/silk/\">http://www.famfamfam.com/lab/icons/silk/</a>");
			description.setSizeUndefined();
			description.setContentMode(Label.CONTENT_XHTML);

			titleLayout.addComponent(description);
			aboutWindow.addComponent(titleLayout);

			@SuppressWarnings("serial")
			Button close = new Button("Close", new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					(aboutWindow.getParent()).removeWindow(aboutWindow);
				}

			});
			layout.addComponent(close);
			layout.setComponentAlignment(close, "right");
		}
		return aboutWindow;
	}

	public void bindViewContribution(IViewContribution viewContribution) {
		logger.debug("bindViewContribution()");
		viewContributions.add(viewContribution);
		if (initialized) {
			tabSheet.addTab(viewContribution.getView(this),
			      viewContribution.getName(),
			      new ThemeResource(viewContribution.getIcon()));
		}
	}

	public void unbindViewContribution(IViewContribution viewContribution) {
		logger.debug("unbindViewContribution()");
		viewContributions.remove(viewContribution);
		if (initialized) {
			tabSheet.removeComponent(viewContribution.getView(this));
		}
	}

	public void bindActionContribution(
	      final IActionContribution actionContribution) {
		logger.debug("bindActionContribution()");
		if (initialized) {
			addActionContribution(actionContribution);
		}
		actionContributions.add(actionContribution);
	}

	private void addActionContribution(
	      final IActionContribution actionContribution) {
		final Application application = this;
		Button button = new Button(actionContribution.getText());
		button.setIcon(new ThemeResource(actionContribution.getIcon()));
		button.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				actionContribution.execute(application);
			}
		});
		getToolbar().addComponent(button);
		buttonActionMap.put(actionContribution, button);

		@SuppressWarnings("serial")
		MenuItem menuItem = actionMenu.addItem(actionContribution.getText(),
		      new Command() {
			      @Override
			      public void menuSelected(MenuItem selectedItem) {
				      actionContribution.execute(application);
			      }
		      });
		menuItem.setIcon(new ThemeResource(actionContribution.getIcon()));
		menuActionMap.put(actionContribution, menuItem);
	}

	public void unbindActionContribution(IActionContribution actionContribution) {
		logger.debug("unbindActionContribution()");
		Button button = buttonActionMap.get(actionContribution);
		toolbar.removeComponent(button);
		buttonActionMap.remove(actionContribution);

		MenuItem menuItem = menuActionMap.get(actionContribution);
		actionMenu.removeChild(menuItem);
		menuActionMap.remove(actionContribution);

		actionContributions.remove(actionContribution);
	}

	@SuppressWarnings("serial")
	class H1 extends Label {
		public H1(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H1);
		}
	}

	@SuppressWarnings("serial")
	class H2 extends Label {
		public H2(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H2);
		}
	}

	@SuppressWarnings("serial")
	class SmallText extends Label {
		public SmallText(String caption) {
			super(caption);
			setStyleName(Reindeer.LABEL_SMALL);
		}
	}

}