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

import java.awt.event.MouseEvent;
import java.util.EventObject;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.event.PointerEvent;

/** Swing implementation of a pointer event.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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
