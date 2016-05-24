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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;

/**
 * This model permit to manage a task progression and could be
 * given to a progress bar.
 * <p>
 * <pre><code>
 * Progression m = ...;
 * TaskProgressionProgressBarWrapper pbm = new TaskProgressionProgressBarWrapper(m);
 * JProgressBar pb = new JProgressBar(pbm);
 * </code></pre>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
final class ProgressionProgressBarWrapper
implements ProgressionWrapper {

	private final Progression model;
	
	/**
	 * @param model is the model to wrap.
	 */
	public ProgressionProgressBarWrapper(Progression model) {
		this.model = model;
	}

	@Override
	public Progression getWrappedModel() {
		return this.model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addChangeListener(ChangeListener listener) {
		this.model.addProgressionListener(new Listener(listener));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeChangeListener(ChangeListener listener) {
		this.model.removeProgressionListener(new Listener(listener));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getExtent() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExtent(int newExtent) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaximum() {
		return this.model.getMaximum();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinimum() {
		return this.model.getMinimum();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getValue() {
		return this.model.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getValueIsAdjusting() {
		return this.model.isAdjusting();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMaximum(int newMaximum) {
		this.model.setMaximum(newMaximum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMinimum(int newMinimum) {
		this.model.setMinimum(newMinimum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRangeProperties(
			int value, int extent, int min, int max,
			boolean adjusting) {
		this.model.setProperties(value, min, max, adjusting);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(int newValue) {
		int v = getValue();
		int m = getMaximum();
		if (v<m && newValue==m) {
			this.model.end();
		}
		else {
			this.model.setValue(newValue);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueIsAdjusting(boolean b) {
		this.model.setAdjusting(b);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class Listener implements ProgressionListener {

		private final ChangeListener listener;
		
		/**
		 * @param l
		 */
		public Listener(ChangeListener l) {
			assert(l!=null);
			this.listener = l;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onProgressionStateChanged(ProgressionEvent event) {
			ChangeEvent e = new ChangeEvent(event.getSource());
			this.listener.stateChanged(e);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onProgressionValueChanged(ProgressionEvent event) {
			ChangeEvent e = new ChangeEvent(event.getSource());
			this.listener.stateChanged(e);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Listener)
				return this.listener.equals(((Listener)obj).listener);
			return this.listener.equals(obj);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return this.listener.hashCode();
		}
		
	}

}