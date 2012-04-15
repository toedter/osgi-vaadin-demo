/*******************************************************************************
 * Copyright (c) 2010 Kai Tödter, Siemens AG and others
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kai Toedter - initial API and implementation
 *******************************************************************************/

package com.siemens.ct.pm.model.minimal;

import com.siemens.ct.pm.model.IPerson;

public class Person implements IPerson {

	public Person(String firstName, String lastName, String company) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
	}

	private String firstName;
	private String lastName;
	private String company;

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getCompany() {
		return company;
	}

	@Override
	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

}
