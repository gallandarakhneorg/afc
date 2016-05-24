/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.ui.swing.undo;

import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;

import javax.swing.Icon;
import javax.swing.undo.UndoManager;

import org.arakhne.afc.ui.swing.StandardAction;
import org.arakhne.afc.vmutil.ReflectionUtil;

/** Implementation of an action that is creating and 
 * adding an UndoableEdit into the given undo manager.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class UndoableAction extends StandardAction {

	private static final long serialVersionUID = 7640859658516394571L;
	
	private final UndoManager undoManager;
	private final Class<? extends AbstractCallableUndoableEdit> type;
	private final Object[] constructorParameters;
	
	/**
	 * @param label
	 * @param tooltip
	 * @param icon
	 * @param manager
	 * @param type
	 * @param constructorParameters
	 */
	public UndoableAction(
			String label,
			String tooltip,
			Icon icon,
			UndoManager manager,
			Class<? extends AbstractCallableUndoableEdit> type,
			Object... constructorParameters) {
		this.undoManager = manager;
		this.type = type;
		this.constructorParameters = constructorParameters;
		setText(label);
		setToolTipText(tooltip);
		setIcon(icon);
	}
	
	/**
	 * @param manager
	 * @param type
	 * @param constructorParameters
	 */
	public UndoableAction(
			UndoManager manager,
			Class<? extends AbstractCallableUndoableEdit> type,
			Object... constructorParameters) {
		this(null, null, null, manager, type, constructorParameters);
	}

	/**
	 * @param label
	 * @param icon
	 * @param manager
	 * @param type
	 * @param constructorParameters
	 */
	public UndoableAction(
			String label,
			Icon icon,
			UndoManager manager,
			Class<? extends AbstractCallableUndoableEdit> type,
			Object... constructorParameters) {
		this(label, null, icon, manager, type, constructorParameters);
	}

	/**
	 * @param label
	 * @param manager
	 * @param type
	 * @param constructorParameters
	 */
	public UndoableAction(
			String label,
			UndoManager manager,
			Class<? extends AbstractCallableUndoableEdit> type,
			Object... constructorParameters) {
		this(label, null, null, manager, type, constructorParameters);
	}

	/**
	 * @param icon
	 * @param manager
	 * @param type
	 * @param constructorParameters
	 */
	public UndoableAction(
			Icon icon,
			UndoManager manager,
			Class<? extends AbstractCallableUndoableEdit> type,
			Object... constructorParameters) {
		this(null, null, icon, manager, type, constructorParameters);
	}

	@Override
	public final void actionPerformed(ActionEvent e) {
		AbstractCallableUndoableEdit undo;
		try {
			undo = newEditInstance(this.type, this.constructorParameters);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		undo.doEdit();
		this.undoManager.addEdit(undo);
	}
	
	/** Invoked to create an instance of undoable edit.
	 * By default, this function invokes the constructor by
	 * reflection.
	 * This function may be overridden to provide an other mean to
	 * create the edit instance.
	 * 
	 * @param type is the type of edit to create.
	 * @param constructorParameters are the parameters to pass to the constructor.
	 * @return the edit instance, never <code>null</code>.
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	protected AbstractCallableUndoableEdit newEditInstance(Class<? extends AbstractCallableUndoableEdit> type, Object[] constructorParameters) throws Exception {
		for(Constructor<?> cons : type.getDeclaredConstructors()) {
			if (matches(cons.getParameterTypes(), constructorParameters)) {
				boolean b = cons.isAccessible();
				try {
					cons.setAccessible(true);
					return type.cast(cons.newInstance(constructorParameters));
				}
				catch(Throwable e) {
					//
				}
				finally {
					cons.setAccessible(b);
				}
			}
		}
		throw new InstantiationException();
	}
	
	/** Test if the instances are matching the types.
	 * 
	 * @param types
	 * @param instances
	 * @return <code>true</code> if the instances are matching the types;
	 * otherwise <code>false</code>.
	 */
	protected static boolean matches(Class<?>[] types, Object[] instances) {
		if (types==null) return instances==null || instances.length==0;
		assert(types!=null);
		if (instances==null) return types.length==0;
		if (types.length==instances.length) {
			for(int i=0; i<types.length; ++i) {
				// Use the reflection util that supports the base types.
				if (!ReflectionUtil.isInstance(types[i],instances[i]))
					return false;
			}
			return true;
		}
		return false;
	}

}
