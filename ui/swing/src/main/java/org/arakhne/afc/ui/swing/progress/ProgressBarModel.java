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

package org.arakhne.afc.ui.swing.progress;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.ref.WeakReference;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;

/**
 * This model permit to manage a task progression and could be
 * given to a progress bar.
 * <p>
 * <pre><code>
 * JProgressBar pb = new JProgressBar();
 * ProgressBarModel pbm = new ProgressBarModel(pb);
 * </code></pre>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class ProgressBarModel extends DefaultProgression implements BoundedRangeModel {

	/** Create a wrapped for the given task progression model that may
	 * be used by the Swing progress bars.
	 * 
	 * @param bar is the bar to set.
	 * @param model is the model to wrap.
	 * @return the Swing-compliant model.
	 * @since 4.0
	 */
	public static BoundedRangeModel setBarModel(JProgressBar bar, Progression model) {
		BoundedRangeModel jmodel;
		if (model instanceof ProgressBarModel) {
			ProgressBarModel pbm = (ProgressBarModel)model;
			jmodel = pbm;
			pbm.setProgressBar(bar);
		}
		else {
			if (model instanceof BoundedRangeModel)
				jmodel = (BoundedRangeModel)model;
			else
				jmodel = new ProgressionProgressBarWrapper(model);
			bar.setModel(jmodel);
		}
		return jmodel;
	}
	
	private WeakReference<JProgressBar> progressBar;
	
	private final boolean percentInTooltip;
	
	private ComponentListener componentListener = null;
	
	private boolean savedStringPaintedFlag;
	
	/** Create a progress model with the specified <i>determinate</i> state.
	 *  The progression will be also shown in
	 * a tooltip.
	 * 
	 * @param progressBar is the progress bar that owns this model
	 * @param value is the current value (between <var>min</var> and <var>max</var>).
	 * @param min is the minimal value
	 * @param max is the maximal value
	 * @param adjusting must be <code>true</code> to indicates that the progress model
	 * is currently under adjustement (the value is not known or sure), otherwise
	 * <code>false</code> 
	 */
	public ProgressBarModel(JProgressBar progressBar, int value, int min, int max, boolean adjusting) {
		this(progressBar, value, min, max, adjusting, true);
	}
	
	/** Create a progress model with the specified <i>determinate</i> state.
	 * The model is not adjusting.
	 * 
	 * @param progressBar is the progress bar that owns this model
	 * @param value is the current value (between <var>min</var> and <var>max</var>).
	 * @param min is the minimal value
	 * @param max is the maximal value
	 * @param adjusting must be <code>true</code> to indicates that the progress model
	 * is currently under adjustement (the value is not known or sure), otherwise
	 * <code>false</code> 
	 * @param percentInTooltip indicates if the prograssion must be shown in tooptip
	 */
	public ProgressBarModel(JProgressBar progressBar, int value, int min, int max, boolean adjusting, boolean percentInTooltip) {
    	super(value, min, max, adjusting);
    	this.percentInTooltip = percentInTooltip;
    	this.progressBar = progressBar==null ? null : new WeakReference<>(progressBar);
    	sharedInit(progressBar);
	}

	/** Create a progress model with the specified <i>determinate</i> state.
	 * The model is not adjusting. The progression will be also shown in
	 * a tooltip.
	 * 
	 * @param progressBar is the progress bar that owns this model
	 * @param value is the current value (between <var>min</var> and <var>max</var>).
	 * @param min is the minimal value
	 * @param max is the maximal value
	 */
	public ProgressBarModel(JProgressBar progressBar, int value, int min, int max) {
    	super(value, min, max);
    	this.percentInTooltip = true;
    	this.progressBar = progressBar==null ? null : new WeakReference<>(progressBar);
    	sharedInit(progressBar);
	}

	/** Create a progress model with the specified <i>indeterminate</i> state.
	 * The model is not adjusting.
	 * 
	 * @param progressBar is the progress bar that owns this model
	 * @param min is the minimal value
	 * @param max is the maximal value
	 * @param percentInTooltip indicates if the prograssion must be shown in tooptip
	 */
	public ProgressBarModel(JProgressBar progressBar, int min, int max, boolean percentInTooltip) {
    	super(min, max);
    	this.percentInTooltip = percentInTooltip;
    	this.progressBar = progressBar==null ? null : new WeakReference<>(progressBar);
    	sharedInit(progressBar);
	}

	/** Create a progress model with the specified <i>indeterminate</i> state.
	 * The model is not adjusting. The progression will be also shown in
	 * a tooltip.
	 * 
	 * @param progressBar is the progress bar that owns this model
	 * @param min is the minimal value
	 * @param max is the maximal value
	 */
	public ProgressBarModel(JProgressBar progressBar, int min, int max) {
    	this(progressBar, min, max, true);
	}

	/** Create a progress model in a <i>indeterminate</i> state.
	 * The model is not adjusting.
	 * 
	 * @param progressBar is the progress bar that owns this model
	 * @param percentInTooltip indicates if the prograssion must be shown in tooptip
	 */
	public ProgressBarModel(JProgressBar progressBar, boolean percentInTooltip) {
		super();
    	this.percentInTooltip = percentInTooltip;
    	this.progressBar = progressBar==null ? null : new WeakReference<>(progressBar);
    	sharedInit(progressBar);
	}

	/** Create a progress model in a <i>indeterminate</i> state.
	 * The model is not adjusting. The progression will be also shown
	 * in a tooltip.
	 * 
	 * @param progressBar is the progress bar that owns this model
	 */
	public ProgressBarModel(JProgressBar progressBar) {
		this(progressBar, true);
	}
	
	private void sharedInit(JProgressBar pb) {
    	if (pb!=null) {
    		this.savedStringPaintedFlag = pb.isStringPainted();
    		pb.setModel(this);
    		this.componentListener = new ComponentListener() {
    			
				@Override
				public void componentHidden(ComponentEvent e) {
					update();
				}

				@Override
				public void componentMoved(ComponentEvent e) {
					update();
				}

				@Override
				public void componentResized(ComponentEvent e) {
					update();
				}

				@Override
				public void componentShown(ComponentEvent e) {
					update();
				}

				@SuppressWarnings("synthetic-access")
				public void update() {
					JProgressBar pbar = ProgressBarModel.this.progressBar.get();
					ComponentListener list = ProgressBarModel.this.componentListener;
					ProgressBarModel.this.componentListener = null;
					if (pbar!=null) {
						if (list!=null) pbar.removeComponentListener(list);
						updateIndeterminateState(isIndeterminate());
					}
				}
        		
    		};
    		pb.addComponentListener(this.componentListener);
    	}
    	else {
    		updateIndeterminateState(isIndeterminate());
    	}
	}
	
	/**
	 * This function is a workaround for a Swing bug.
	 * String painting and indeterminate state may not 
	 * set to <code>true</code> at the same time.
	 * 
	 * @param isIndeterminate
	 */
	private void updateIndeterminateState(boolean isIndeterminate) {
		JProgressBar pg = this.progressBar!=null ? this.progressBar.get() : null;
		if (pg!=null) {
			if (isIndeterminate) {
				this.savedStringPaintedFlag = pg.isStringPainted();
				pg.setStringPainted(false);
				pg.setIndeterminate(true);
			}
			else {
				pg.setIndeterminate(false);
				pg.setStringPainted(this.savedStringPaintedFlag);
			}
		}
	}

	@Override
	public void addChangeListener(ChangeListener listener) {
		this.listeners.add(ChangeListener.class, listener);
	}

	@Override
	public void removeChangeListener(ChangeListener listener) {
		this.listeners.remove(ChangeListener.class, listener);
	}
	
	private void fireChange() {
		ChangeEvent event = new ChangeEvent(this);
		for(ChangeListener listener : this.listeners.getListeners(ChangeListener.class)) {
			listener.stateChanged(event);
		}
	}

	@Override
	public int getExtent() {
		return 1;
	}

	@Override
	public boolean getValueIsAdjusting() {
		return isAdjusting();
	}

	@Override
	public void setExtent(int newExtent) {
		//
	}

	@Override
	public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
		setProperties(value, min, max, adjusting);
	}

	@Override
	public void setValueIsAdjusting(boolean b) {
		setAdjusting(b);
	}

	@Override
	protected void fireStateChange() {
		JProgressBar bar = this.progressBar==null ? null : this.progressBar.get();
		if (bar!=null) {
	    	updateIndeterminateState(isIndeterminate());
			if (this.percentInTooltip) {
				updateToolip(bar);
			}
		}
		super.fireStateChange();
	}

	@Override
	protected void fireValueChange() {
		if (this.percentInTooltip) {
			JProgressBar bar = this.progressBar==null ? null : this.progressBar.get();
			updateToolip(bar);
		}
		super.fireValueChange();
		fireChange();
	}
	
	private void updateToolip(JProgressBar bar) {
		if (bar!=null) {
			StringBuilder buffer = new StringBuilder();
			buffer.append((int)getPercent());
			buffer.append("%"); //$NON-NLS-1$
			String comment = getComment();
			if (comment!=null) {
				buffer.append(" - ");  //$NON-NLS-1$
				buffer.append(comment);
			}
			bar.setString(comment);
			bar.setToolTipText(buffer.toString());
		}
	}
	
	/** Attach a progress bar to this model.
	 * 
	 * @param bar
	 */
	public void setProgressBar(JProgressBar bar) {
		JProgressBar oldBar = this.progressBar==null ? null : this.progressBar.get();
		if (oldBar!=bar) {
			if (oldBar!=null) {
				DefaultBoundedRangeModel defaultModel = new DefaultBoundedRangeModel();
				defaultModel.setRangeProperties(
						getValue(),
						getExtent(),
						getMinimum(), 
						getMaximum(),
						isAdjusting());
				oldBar.setModel(defaultModel);
			}
	    	this.progressBar = bar==null ? null : new WeakReference<>(bar);
	    	if (bar!=null) {
	    		bar.setModel(this);
	    	}
	    	updateIndeterminateState(isIndeterminate());
	    	fireChange();
		}
	}
	
}
