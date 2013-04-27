/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2005-10 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.awt.event.MouseEvent;
import java.util.EventObject;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.event.PointerEvent;

/** Swing implementation of a pointer event.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PointerEventSwing extends EventObject implements PointerEvent {

	private static final long serialVersionUID = -4265280802869990270L;
	
	private final MouseEvent event;
	
	/**
	 * @param event
	 */
	public PointerEventSwing(MouseEvent event) {
		super(event.getSource());
		this.event = event;
	}
	
	@Override
	public long when() {
		return this.event.getWhen();
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
		return this.event.isPopupTrigger();
	}

	@Override
	public float getX() {
		return this.event.getX();
	}

	@Override
	public float getY() {
		return this.event.getY();
	}

	@Override
	public int getButton() {
		return this.event.getButton();
	}

	@Override
	public float getOrientation() {
		return 0;
	}

	@Override
	public int getClickCount() {
		return this.event.getClickCount();
	}

	@Override
	public float getXPrecision() {
		return 0;
	}

	@Override
	public float getYPrecision() {
		return 0;
	}

	@Override
	public int getPointerCount() {
		return 1;
	}

	@Override
	public Circle2f getToolArea(int pointerIndex) {
		// Replies a circle with 1 pixel of radius
		return new Circle2f(
				this.event.getX(),
				this.event.getY(),
				MINIMAL_TOOL_SIZE / 2f);
	}

	@Override
	public ToolType getToolType(int pointerIndex) {
		return ToolType.MOUSE;
	}

	@Override
	public boolean isToolAreaSupported() {
		return false;
	}
	
	@Override
	public boolean intersects(Shape2f s) {
		for(int i=0; i<getPointerCount(); ++i) {
			if (s.intersects(getToolArea(i))) {
				return true;
			}
		}
		return false;
	}

}
