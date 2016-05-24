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

package org.arakhne.afc.ui.event ;

import org.arakhne.afc.math.continous.object2d.Shape2f;


/** Describe the event related to the pointer (mouse or finger).
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public interface PointerEvent extends InputEvent {

	/** Replies the minimal size (in pixels) of the touch area.
	 * The implementation classes must use this
	 * variable, when possible, to initiale the size
	 * of the touch areas.
	 */
	public static final float MINIMAL_TOOL_SIZE = 8;
	
	/** Replies the timestamp of when this event occurred.
	 * 
	 * @return the timestamp of when this event occurred.
	 */
	public long when();

	/** Replies the X position of the pointer.
	 * 
	 * @return the X position of the pointer.
	 */
	public float getX();
	
	/** Replies the Y position of the pointer.
	 * 
	 * @return the Y position of the pointer.
	 */
	public float getY();

	/**
     * Returns the states of the buttons or the number
     * of the button that is the cause of the event.
     * <p>
     * The semantic if the replied value depends on
     * the background platform. For Swing, it
     * is the number of the button. For Android,
     * it is the states of the buttons.
     *
     * @return the number of the button, or {@code 0} for none.
     */
    public int getButton();
    
    /** Replies the number of clicks.
     * 
     * @return the number of clicks.
     */
    public int getClickCount();
    
	/**
     * Returns the orientation of the pointer.
     * Returns the orientation of the touch area and 
     * tool area in radians clockwise from vertical 
     * for the pointer. An angle of 0 radians 
     * indicates that the major axis of contact is oriented 
     * upwards, is perfectly circular or is of unknown 
     * orientation. A positive angle indicates that the 
     * major axis of contact is oriented to the right. 
     * A negative angle indicates that the major axis 
     * of contact is oriented to the left. The full 
     * range is from -PI/2 radians (finger pointing fully left) 
     * to PI/2 radians (finger pointing fully right).
     *
     * @return the orientation (in radians) of the pointer.
     */
    public float getOrientation();
    
	/** Replies the precision of the X position of the pointer.
	 * <p>
	 * On several devices, the X position is approximate (on
	 * tactil devices for example). This precision permits to
	 * estimate the hardware X coordinate.
	 * 
	 * @return the precision X position of the pointer.
	 */
	public float getXPrecision();
	
	/** Replies the precision of the Y position of the pointer.
	 * <p>
	 * On several devices, the Y position is approximate (on
	 * tactil devices for example). This precision permits to
	 * estimate the hardware Y coordinate.
	 * 
	 * @return the Y position of the pointer.
	 */
	public float getYPrecision();
	
	/** Replies the number of pointers concerned by this event.
	 * 
	 * @return the number of pointers.
	 */
	public int getPointerCount();
	
	/** Returns a shape that describes the size of the approaching 
	 * tool for the given pointer index. The tool area represents 
	 * the estimated size of the finger or pen that is touching the 
	 * device independent of its actual touch area at the point of contact.
	 * 
	 * @param pointerIndex is the index of the pointer for which to retreive
	 * the tool area.
	 * @return the area covered by the tool.
	 * @see #isToolAreaSupported()
	 */
	public Shape2f getToolArea(int pointerIndex);

	/** Returns if the background API is able to reply the
	 * area covered by the pointer mean (finger, pen...).
	 * This function permits to determine if the value replied
	 * by {@link #getToolArea(int)} has a valid meaning or
	 * is only a work-around.
	 * 
	 * @return <code>true</code> if the background API could compute
	 * the area covered by the pointer, <code>false</code> otherwise.
	 */
	public boolean isToolAreaSupported();

	/** Returns the type of tool detected for the given pointer.
	 * 
	 * @param pointerIndex is the index of the pointer for which to retreive
	 * the tool type.
	 * @return the type of tool.
	 */
	public ToolType getToolType(int pointerIndex);

	/** Replies if the pointer tool area is intersecting
	 * the given shape.
	 * 
	 * @param s is the shape to test
	 * @return <code>true</code> if the pointer area intersect
	 * the given shape; <code>false</code> otherwise.
	 * @see #getToolArea(int)
	 */
	public boolean intersects(Shape2f s);
	
	/** Types of tools.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum ToolType {
		
		/** Unknown. */
		UNKNOW,
		/** Finger. */
		FINGER,
		/** Stylus. */
		STYLUS,
		/** Eraser. */
		ERASER,
		/** Mouse. */
		MOUSE;
		
	}
	
}
