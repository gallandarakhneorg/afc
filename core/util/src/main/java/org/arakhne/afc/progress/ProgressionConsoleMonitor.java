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
import java.util.logging.Logger;

/**
 * An object that permits to indicates the progression of
 * a task. This monitor display on the console the progression
 * of the task.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProgressionConsoleMonitor implements ProgressionListener {
	
	private Progression model = new DefaultProgression();
	
	/**
	 */
	public ProgressionConsoleMonitor() {
		this.model.addProgressionListener(new WeakListener(this, this.model));
	}
	
	/** Replies the task progression model.
	 * 
	 * @return the task progression model.
	 */
	public Progression getModel() {
		return this.model;
	}

	/** Change the task progression model.
	 * 
	 * @param model - the task progression model.
	 */
	public void setModel(Progression model) {
		this.model.removeProgressionListener(new WeakListener(this, this.model));
		if (model==null) {
			this.model = new DefaultProgression();
		}
		else {
			this.model = model;
		}
		this.model.addProgressionListener(new WeakListener(this, this.model));
	}

	@Override
	public void onProgressionStateChanged(ProgressionEvent event) {
		//
	}

	@Override
	public void onProgressionValueChanged(ProgressionEvent event) {
		if (!event.isIndeterminate()) {
			StringBuilder txt = new StringBuilder();
			Progression p = event.getTaskProgression();
			
			txt.append('[');
			txt.append("%] "); //$NON-NLS-1$
			String comment = p.getComment();
			if (comment!=null) {
				txt.append(comment);
			}
			
			Logger.getAnonymousLogger().info(txt.toString());
		}
	}

	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class WeakListener implements ProgressionListener {
		
		private final WeakReference<ProgressionListener> listener;
		private final WeakReference<Progression> model;
		
		/**
		 * @param listener
		 * @param model
		 */
		public WeakListener(ProgressionListener listener, Progression model) {
			this.listener = new WeakReference<ProgressionListener>(listener);
			this.model = new WeakReference<Progression>(model);
		}

		@Override
		public void onProgressionValueChanged(ProgressionEvent event) {
			ProgressionListener l = this.listener.get();
			if (l!=null) {
				l.onProgressionValueChanged(event);
			}
			else {
				removeListener();
			}
		}

		@Override
		public void onProgressionStateChanged(ProgressionEvent event) {
			ProgressionListener l = this.listener.get();
			if (l!=null) {
				l.onProgressionStateChanged(event);
			}
			else {
				removeListener();
			}
		}
		
		private void removeListener() {
			Progression p = this.model.get();
			if (p!=null) {
				p.removeProgressionListener(this);
			}
		}
		
	}
	
}
