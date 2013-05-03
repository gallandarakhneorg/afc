/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2005-10 Stephane GALLAND.
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

package org.arakhne.afc.ui.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;

/** Button which could acts as a wrapper to a set of buttons.
*
* @author $Author: galland$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
*/
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
