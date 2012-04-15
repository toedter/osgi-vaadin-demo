/*******************************************************************************
 * Copyright (c) 2009 Siemens AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kai Toedter - initial API and implementation
 *******************************************************************************/

package com.siemens.ct.pm.model.basic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.ct.pm.model.IPerson;
import com.siemens.ct.pm.model.IPersonListener;
import com.siemens.ct.pm.model.IPersonManager;
import com.siemens.ct.pm.model.event.PersonEvent;

public class PersonManager implements IPersonManager {

	private final List<IPerson> persons;
	private final List<IPersonListener> personListeners;
	private final Logger logger = LoggerFactory.getLogger(PersonManager.class);

	public PersonManager() {
		personListeners = new ArrayList<IPersonListener>();

		persons = new ArrayList<IPerson>();
		persons.add(new Person("Kai", "Tödter", "Siemens AG"));
		persons.add(new Person("Bernd", "Kolb", "Itemis"));
		persons.add(new Person("Gerd", "Wütherich", "Freelancer"));
		persons.add(new Person("Heiko", "Seeberger", "Weigle Wilczek GmbH"));
		persons.add(new Person("Martin", "Lippert", "Tasktop"));
		persons.add(new Person("Muscalla", "Benjamin", "Tasktop"));
		persons.add(new Person("Peter", "Kriens", "aQute"));
		persons.add(new Person("Nicole ", "Wengatz", "Siemens AG"));
		persons.add(new Person("Frank", "Buschmann", "Siemens AG"));
		persons.add(new Person("Michael", "Stal", "Siemens AG"));
		persons.add(new Person("Alexander", "Thurow", "Weigle Wilczek GmbH"));
		persons.add(new Person("Matthias", "Lübken", "akquinet it-agile GmbH"));
		persons.add(new Person("Henning", "Wolf", "akquinet it-agile GmbH"));

		logger.info("initialized");
	}

	@Override
	public List<IPerson> getPersons() {
		return persons;
	}

	@Override
	public void deletePerson(IPerson selectedPerson) {
		persons.remove(selectedPerson);
		PersonEvent personEvent = new PersonEvent(this,
				PersonEvent.Type.DELETE, selectedPerson);
		for (IPersonListener personlistener : personListeners) {
			personlistener.handleEvent(personEvent);
		}
	}

	public synchronized void addPersonListener(IPersonListener personListener) {
		logger.info("add personListener: " + personListener);
		personListeners.add(personListener);
	}

	public synchronized void removePersonListener(IPersonListener personListener) {
		logger.info("remove personListener: " + personListener);
		personListeners.remove(personListener);
	}
}
