/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.progress;

import java.util.EventObject;

/**
 * Task progression event.
 *
 * @author $Author: sgalland$
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

	private final double factor;

	private final String comment;

	private final boolean isIndeterminate;

	/** Constructor.
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
	 *     otherwise <code>false</code>.
	 */
	public boolean isRoot() {
		return this.isRoot;
	}

	/** Replies if the associated task was marked as finished,
	 * ie the current value is greater or equal to the maximum
	 * value AND the associated task progression object is a root
	 * task.
	 *
	 * @return <code>true</code> if the task was finished,
	 *     otherwise <code>false</code>.
	 */
	public boolean isFinished() {
		return this.isRoot && this.value >= this.max;
	}

	/** Replies the task progression which generate this event.
	 *
	 * @return the model.
	 */
	public Progression getProgression() {
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
	@SuppressWarnings("checkstyle:magicnumber")
	public double getPercent() {
		return this.factor * 100.;
	}

	/**
	 * Returns the model's current value in percent of pregression.
	 *
	 * @return  the model's value between 0 and 1
	 * @see     #getValue()
	 * @see     #getPercent()
	 */
	public double getProgressionFactor() {
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
