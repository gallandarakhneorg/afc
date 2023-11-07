/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.lang.ref.WeakReference;

/**
 * An object that permits to indicates the progression of
 * a task.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class SubProgressionModel extends DefaultProgression {

	private final int level;

	private final double minValueInParent;

	private final double maxValueInParent;

	private final double extentInParent;

	private WeakReference<DefaultProgression> parent;

	private final boolean overwriteCommentWhenDisconnect;

	/** Create a progress model with the specified <i>determinate</i> state.
	 *
	 * @param parent is the parent model
	 * @param minPValue is the value at which this sub-model is starting in its parent.
	 * @param maxPValue is the value at which this sub-model is ending in its parent.
	 * @param minValue is the minimum value of this progression model.
	 * @param maxValue is the maximum value of this progression model.
	 * @param isIndeterminate indicates if this model is under progression.
	 * @param adjusting indicates if this model is adjusting its value.
	 * @param overwriteComment indicates if the comment of this subtask may overwrite
	 *     the comment of its parent when it is disconnected.
	 */
	SubProgressionModel(
			DefaultProgression parent,
			double minPValue, double maxPValue,
			int minValue, int maxValue,
			boolean isIndeterminate, boolean adjusting,
			boolean overwriteComment) {
		super(minValue, minValue, maxValue, adjusting);
		assert parent != null;
		this.level = parent.getTaskDepth() + 1;
		double uptMaxPValue = maxPValue;
		if (minPValue > uptMaxPValue) {
			uptMaxPValue = minPValue;
		}
		this.minValueInParent = minPValue;
		this.maxValueInParent = uptMaxPValue;
		this.extentInParent = uptMaxPValue - minPValue;
		this.overwriteCommentWhenDisconnect = overwriteComment;
		this.parent = new WeakReference<>(parent);
		setIndeterminate(isIndeterminate);
	}

	/** Create a progress model with the specified <i>determinate</i> state.
	 *
	 * @param parent is the parent model
	 * @param minPValue is the value at which this sub-model is starting in its parent.
	 * @param maxPValue is the value at which this sub-model is ending in its parent.
	 * @param isIndeterminate indicates if this model is under progression.
	 * @param adjusting indicates if this model is adjusting its value.
	 * @param overwriteComment indicates if the comment of this subtask may overwrite
	 *     the comment of its parent when it is disconnected.
	 */
	SubProgressionModel(DefaultProgression parent, double minPValue, double maxPValue,
			boolean isIndeterminate, boolean adjusting, boolean overwriteComment) {
		super((int) minPValue, (int) minPValue, (int) maxPValue);
		assert parent != null;
		this.level = parent.getTaskDepth() + 1;
		double uptMaxPValue = maxPValue;
		if (minPValue > uptMaxPValue) {
			uptMaxPValue = minPValue;
		}
		this.minValueInParent = minPValue;
		this.maxValueInParent = uptMaxPValue;
		this.extentInParent = uptMaxPValue - minPValue;
		this.overwriteCommentWhenDisconnect = overwriteComment;
		this.parent = new WeakReference<>(parent);
		setAdjusting(adjusting);
		setIndeterminate(isIndeterminate);
	}

	/** Set the parent value and disconnect this subtask from its parent.
	 */
	void disconnect() {
		final DefaultProgression parentInstance = getParent();
		if (parentInstance != null) {
			parentInstance.disconnectSubTask(this, this.maxValueInParent, this.overwriteCommentWhenDisconnect);
		}
		this.parent = null;
	}

	/** Returns the minimal value of this task progression in its parent.
	 *
	 * @return the value at which this model is supposed to start in its parent.
	 */
	public double getMinInParent() {
		return this.minValueInParent;
	}

	/** Returns the maximal value of this task progression in its parent.
	 *
	 * @return the value at which this model is supposed to end in its parent.
	 */
	public double getMaxInParent() {
		return this.maxValueInParent;
	}

	/** Returns the parent task.
	 *
	 * @return the parent task.
	 */
	protected DefaultProgression getParent() {
		return this.parent == null ? null : this.parent.get();
	}

	@Override
	public void end() {
		super.end();
		disconnect();
	}

	@Override
	protected void fireValueChange() {
		// Notify subtask listeners
		super.fireValueChange();
		// Notify parent
		final double factor = getProgressionFactor();
		if (factor < 1.) {
			final DefaultProgression parentInstance = getParent();
			if (parentInstance != null) {
				double valueInParent = this.extentInParent * factor + this.minValueInParent;
				if (valueInParent > this.maxValueInParent) {
					valueInParent = this.maxValueInParent;
				}
				parentInstance.setValue(this, valueInParent, getComment());
			}
		} else {
			disconnect();
		}
	}

	@Override
	public boolean isRootModel() {
		return false;
	}

	@Override
	public Progression getSuperTask() {
		return getParent();
	}

	@Override
	public int getTaskDepth() {
		return this.level;
	}

}
