/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2005-10 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
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
