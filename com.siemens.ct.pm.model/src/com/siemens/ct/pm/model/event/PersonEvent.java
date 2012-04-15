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

package com.siemens.ct.pm.model.event;

import java.util.EventObject;

import com.siemens.ct.pm.model.IPerson;

public class PersonEvent extends EventObject {

    private static final long serialVersionUID = 5641252708254799778L;

    private final IPerson person;

    public static enum Type {
        DELETE, CREATE, UPDATE
    };

    private final Type type;

    public PersonEvent(Object source, Type type, IPerson person) {
        super(source);
        this.type = type;
        this.person = person;
    }

    public synchronized Type getType() {
        return type;
    }

    public synchronized IPerson getPerson() {
        return person;
    }
}
