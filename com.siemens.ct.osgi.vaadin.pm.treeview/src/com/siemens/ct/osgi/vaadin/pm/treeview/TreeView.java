package com.siemens.ct.osgi.vaadin.pm.treeview;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.ct.osgi.vaadin.pm.main.service.IViewContribution;
import com.siemens.ct.pm.model.IPerson;
import com.siemens.ct.pm.model.IPersonManager;
import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class TreeView implements IViewContribution {

	protected Logger logger = LoggerFactory.getLogger(TreeView.class);

	protected IPersonManager personManager;
	private Component view;

	private Tree tree;

	@Override
	public String getIcon() {
		return "icons/folder.png";
	}

	@Override
	public String getName() {
		return "Tree View";
	}

	@Override
	public Component getView(Application application) {
		if (view == null) {
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setMargin(true);

			tree = new Tree("Compamies and Employees");
			verticalLayout.addComponent(tree);
			synchronized (this) {
				if (personManager != null) {
					refreshTree();
				}
			}

			view = verticalLayout;
		}
		return view;
	}

	public void removePersonManager(final IPersonManager personManager) {
		logger.debug("removePersonManager");
		if (this.personManager == personManager) {
			this.personManager = null;
			tree.removeAllItems();
		}
	}

	public void setPersonManager(final IPersonManager personManager) {
		logger.debug("setPersonManager");

		synchronized (this) {
			this.personManager = personManager;
			if (tree != null) {
				refreshTree();
			}
		}
	}

	void refreshTree() {
		List<IPerson> persons = personManager.getPersons();
		tree.removeAllItems();

		for (IPerson person : persons) {
			String company = person.getCompany();
			String name = person.getFirstName() + " " + person.getLastName();
			tree.addItem(company);
			tree.addItem(name);
			tree.setChildrenAllowed(name, false);
			tree.setParent(name, company);
			if (company.contains("Siemens")) {
				tree.expandItem(company);
			}
		}

	}
}
