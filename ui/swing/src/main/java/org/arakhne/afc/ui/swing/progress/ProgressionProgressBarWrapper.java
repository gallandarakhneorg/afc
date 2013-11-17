/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
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
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
	 * @author $Author: galland$
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