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
	 * the comment of its parent when it is disconnected.  
	 */
	SubProgressionModel(
			DefaultProgression parent,
			double minPValue, double maxPValue,
			int minValue, int maxValue,
			boolean isIndeterminate, boolean adjusting,
			boolean overwriteComment) {
		super(minValue, minValue, maxValue, adjusting);
		assert(parent!=null);
		this.level = parent.getTaskDepth() + 1;
		double uptMaxPValue = maxPValue;
		if (minPValue>uptMaxPValue) {
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
	 * the comment of its parent when it is disconnected.  
	 */
	SubProgressionModel(DefaultProgression parent, double minPValue, double maxPValue,
			boolean isIndeterminate, boolean adjusting, boolean overwriteComment) {
		super((int)minPValue, (int)minPValue, (int)maxPValue);
		assert(parent!=null);
		this.level = parent.getTaskDepth() + 1;
		double uptMaxPValue = maxPValue;
		if (minPValue>uptMaxPValue) {
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
		DefaultProgression parentInstance = getParent();
		if (parentInstance!=null) {
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
    	return this.parent==null ? null : this.parent.get();
    }
    
    /**
     * {@inheritDoc}
     */
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
		double factor = getProgressionFactor();
		if (factor<1f) {
			DefaultProgression parentInstance = getParent();
			if (parentInstance!=null) {
				double valueInParent = this.extentInParent * factor + this.minValueInParent;
				if (valueInParent>this.maxValueInParent)
					valueInParent = this.maxValueInParent;
				parentInstance.setValue(this, valueInParent, getComment());
			}
		}
		else {
			disconnect();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isRootModel() {
		return false;
	}    
    
	/** {@inheritDoc}
	 */
	@Override
	public Progression getSuperTask() {
		return getParent();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getTaskDepth() {
		return this.level;
	}
}
