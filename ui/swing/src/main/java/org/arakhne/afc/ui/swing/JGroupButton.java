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

package org.arakhne.afc.ui.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;

/** Button which could acts as a wrapper to a set of buttons.
*
* @author $Author: sgalland$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class JGroupButton extends AbstractButton implements ItemListener {

	private static final long serialVersionUID = -8179091743419793116L;

	private final Collection<AbstractButton> groupedButtons;
	
	private boolean listening = true; 
	
	/** 
	 * @param buttons are the buttons inside the group
	 */
	public JGroupButton(Collection<AbstractButton> buttons) {
		this.groupedButtons = buttons;
       setModel(new JToggleButton.ToggleButtonModel());
		for (AbstractButton button : this.groupedButtons) {
			button.addItemListener(this);
		}
		addItemListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (!this.listening) return;
		this.listening = false;
		if (e.getSource()==this) {
			onGroupButtonStateChanged(e);
		}
		else {
			onButtonStateChanged(e);
		}
		this.listening = true;
	}
	
	private void onGroupButtonStateChanged(ItemEvent e) {
		boolean selected = e.getStateChange()==ItemEvent.SELECTED;
		for (AbstractButton button : this.groupedButtons) {
			button.setSelected(selected);
		}
	}

	/**
	 * Do not set the selected state of the button which has fire this event.
	 * 
	 * @param e
	 */
	private void onButtonStateChanged(ItemEvent e) {
		Object source = e.getSource();
		if (this.groupedButtons.contains(source)) {
			boolean selected = e.getStateChange()==ItemEvent.SELECTED;
			for (AbstractButton b : this.groupedButtons) {
				if (b!=source) {
					b.setSelected(selected);
				}
			}
			setSelected(selected);
		}
	}
	
}
