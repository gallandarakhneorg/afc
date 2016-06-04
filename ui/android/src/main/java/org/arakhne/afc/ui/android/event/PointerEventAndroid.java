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

package org.arakhne.afc.ui.android.event ;

import java.util.EventObject;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.event.PointerEvent;

import android.view.KeyEvent;
import android.view.MotionEvent;

/** Android implementation of a pointer event.
 *  
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class PointerEventAndroid extends EventObject implements PointerEvent {

	private static final long serialVersionUID = -1281311602355739859L;

	private final MotionEvent motionEvent;
	private long when;
	private float x;
	private float y;
	private boolean consumed = false;
	
	/**
	 * @param source
	 * @param x
	 * @param y
	 * @param me
	 */
	public PointerEventAndroid(Object source, float x, float y, MotionEvent me) {
		super(source);
		this.motionEvent = me;
		this.when = me.getEventTime();
		this.x = x;
		this.y = y;
	}

	@Override
	public int getDeviceId() {
		return this.motionEvent.getDeviceId();
	}
	
	@Override
	public long when() {
		return this.when;
	}
	
	/** Change the timestamp of the event.
	 * 
	 * @param when is the new timestamp.
	 */
	public void setWhen(long when) {
		this.when = when;
	}

	@Override
	public boolean isConsumed() {
		return this.consumed;
	}

	@Override
	public void consume() {
		this.consumed = true;		
	}
	
	/** Mark this event as not consumed.
	 */
	public void unconsume() {
		this.consumed = false;
	}

	@Override
	public boolean isShiftDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_SHIFT_ON)!=0;
	}

	@Override
	public boolean isControlDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_CTRL_ON)!=0;
	}

	@Override
	public boolean isMetaDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_META_ON)!=0;
	}

	@Override
	public boolean isAltDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_ALT_ON)!=0;
	}

	@Override
	public boolean isAltGraphDown() {
		return false;
	}

	@Override
	public boolean isContextualActionTriggered() {
		return false;
	}
	
	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public int getButton() {
		return this.motionEvent.getButtonState();
	}

	@Override
	public int getClickCount() {
		return 1;
	}

	@Override
	public float getOrientation() {
		return this.motionEvent.getOrientation();
	}
	
	/** Set the position of the event.
	 * 
	 * @param x
	 * @param y
	 */
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "device="+getDeviceId()+ 
				"\nx="+getX()+ 
				"\ny="+getY()+ 
				"\nbutton="+getButton()+ 
				"\nkeys=0x"+Integer.toHexString(this.motionEvent.getMetaState())+ 
				"\norientation="+getOrientation()+ 
				"\nconsumed="+isConsumed(); 
	}

	@Override
	public float getXPrecision() {
		return this.motionEvent.getXPrecision();
	}

	@Override
	public float getYPrecision() {
		return this.motionEvent.getYPrecision();
	}

	@Override
	public int getPointerCount() {
		return this.motionEvent.getPointerCount();
	}

	@Override
	public Shape2f getToolArea(int pointerIndex) {
		float x = this.motionEvent.getX(pointerIndex);
		float y = this.motionEvent.getY(pointerIndex);
		float major = this.motionEvent.getToolMajor(pointerIndex);
		float minor = this.motionEvent.getToolMinor(pointerIndex);
		// The circle has a minimal radius of 1 pixel.
		return new Circle2f(x, y,
				Math.max(
						Math.max(major, minor),
						MINIMAL_TOOL_SIZE));
	}

	@Override
	public ToolType getToolType(int pointerIndex) {
		switch(this.motionEvent.getToolType(pointerIndex)) {
		case MotionEvent.TOOL_TYPE_FINGER:
			return ToolType.FINGER;
		case MotionEvent.TOOL_TYPE_STYLUS:
			return ToolType.STYLUS;
		case MotionEvent.TOOL_TYPE_MOUSE:
			return ToolType.MOUSE;
		case MotionEvent.TOOL_TYPE_ERASER:
			return ToolType.ERASER;
		default:
		}
		return ToolType.UNKNOW;
	}

	@Override
	public boolean isToolAreaSupported() {
		return true;
	}
	
	@Override
	public boolean intersects(Shape2f s) {
		for(int i=0; i<getPointerCount(); ++i) {
			if (s.intersects(getToolArea(i).getPathIterator())) {
				return true;
			}
		}
		return false;
	}
	
}
