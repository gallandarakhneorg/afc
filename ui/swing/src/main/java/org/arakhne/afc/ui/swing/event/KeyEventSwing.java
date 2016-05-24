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

package org.arakhne.afc.ui.swing.event;

import java.util.EventObject;

import org.arakhne.afc.ui.event.KeyEvent;

/** Swing implementation of a key event.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class KeyEventSwing extends EventObject implements KeyEvent {

	private static final long serialVersionUID = 5413599206960834822L;
	
	private final java.awt.event.KeyEvent event;
	
	/**
	 * @param event
	 */
	public KeyEventSwing(java.awt.event.KeyEvent event) {
		super(event.getSource());
		this.event = event;
	}

	@Override
	public int getDeviceId() {
		return System.identityHashCode(this.event.getComponent());
	}

	@Override
	public boolean isConsumed() {
		return this.event.isConsumed();
	}

	@Override
	public void consume() {
		this.event.consume();
	}

	@Override
	public boolean isShiftDown() {
		return this.event.isShiftDown();
	}

	@Override
	public boolean isControlDown() {
		return this.event.isControlDown();
	}

	@Override
	public boolean isMetaDown() {
		return this.event.isMetaDown();
	}

	@Override
	public boolean isAltDown() {
		return this.event.isAltDown();
	}

	@Override
	public boolean isAltGraphDown() {
		return this.event.isAltGraphDown();
	}

	@Override
	public boolean isContextualActionTriggered() {
		return this.event.getKeyCode()==java.awt.event.KeyEvent.VK_CONTEXT_MENU;
	}

	@Override
	public int getKeyCode() {
		return this.event.getKeyCode();
	}

	@Override
	public char getKeyChar() {
		return this.event.getKeyChar();
	}
	
}
