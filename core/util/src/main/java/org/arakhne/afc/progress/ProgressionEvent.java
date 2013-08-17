/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
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
package org.arakhne.afc.progress;

import java.util.EventObject;

/**
 * Task progression event.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProgressionEvent extends EventObject {

	private static final long serialVersionUID = 4840275907048148943L;

	private final boolean isRoot;
	
	private final int min;

	private final int max;

	private final int value;
	
	private final float factor;

	private final String comment;

	private final boolean isIndeterminate;

	/**
	 * @param source is the model that was thrown the event.
	 * @param isRoot indicates if this event was fired by a root task progression source.
	 */
	ProgressionEvent(Progression source, boolean isRoot) {
		super(source);
		this.isRoot = isRoot;
		this.min = source.getMinimum();
		this.max = source.getMaximum();
		this.value = source.getValue();
		this.factor = source.getProgressionFactor();
		this.comment = source.getComment();
		this.isIndeterminate = source.isIndeterminate();
	}

	/** Replies if this event was fired by an task progression source
	 * which is a root source.
	 * 
	 * @return <code>true</code> if the task progression is a root,
	 * otherwise <code>false</code>.
	 */
	public boolean isRootTaskProgression() {
		return this.isRoot;
	}

	/** Replies if the associated task was marked as finished,
	 * ie the current value is greater or equal to the maximum
	 * value AND the associated task progression object is a root
	 * task.
	 * 
	 * @return <code>true</code> if the task was finished,
	 * otherwise <code>false</code>.
	 */
	public boolean isTaskFinished() {
		return this.isRoot && this.value>=this.max;
	}

	/** Replies the task progression which generate this event.
	 * 
	 * @return the model.
	 */
	public Progression getTaskProgression() {
		return (Progression) getSource();
	}

	/** Returns the minimum acceptable value.
	 * 
	 * @return the minimal value.
	 */
	public int getMinimum() {
		return this.min;
	}

	/**
	 * Returns the model's maximum.
	 * 
	 * @return the maximal value.
	 */
	public int getMaximum() {
		return this.max;
	}

	/**
	 * Returns the model's current value.  Note that the upper
	 * limit on the model's value is <code>maximum</code> 
	 * and the lower limit is <code>minimum</code>.
	 * 
	 * @return the current value.
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Returns the model's current value in percent of pregression.
	 *
	 * @return  the model's value between 0 and 100
	 * @see     #getValue()
	 * @see     #getProgressionFactor()
	 */
	public float getPercent() {
		return this.factor * 100f;
	}

	/**
	 * Returns the model's current value in percent of pregression.
	 *
	 * @return  the model's value between 0 and 1
	 * @see     #getValue()
	 * @see     #getPercent()
	 */
	public float getProgressionFactor() {
		return this.factor;
	}

	/**
	 * Returns the model's current comment.
	 * 
	 * @return the current comment or <code>null</code>.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * Returns the value of the <code>indeterminate</code> property.
	 *
	 * @return the value of the <code>indeterminate</code> property
	 */
	public boolean isIndeterminate() {
		return this.isIndeterminate;
	}
	
}
