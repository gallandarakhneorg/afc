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
 * version 2.1 of the License, or (at your option) any later version.
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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/** This is the default implementation of an action which
 * could be associated to a text, an icon and a tooltip text.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class StandardAction extends AbstractAction {
	
	private static final long serialVersionUID = -4672742688176987414L;

	/** used to say that this command have an associated icon.
	 */
	public static final boolean HAS_ICON = true;
	
	/** used to say that this command have not an associated icon.
	 */
	public static final boolean NO_ICON = false;
	

	/** Construct a new SimpleAction.
	 *
	 * @param name name of this command.
	 * @param icon is the asscoiated icon.
	 * 
	 */
	public StandardAction(String name, Icon icon) {
		setText(name);
		setIcon(icon);
	}

	/** Construct a new SimpleAction.
	 *
	 * @param name name of this command.
	 */
	public StandardAction(String name) { 
		setText(name);
	}

	/** Construct a new SimpleAction.
	 *
	 * @param icon is the asscoiated icon.
	 * 
	 */
	public StandardAction(Icon icon) {
		setIcon(icon);
	}

	/** Construct a new SimpleAction.
	 */
	public StandardAction() {
		//
	}

	////////////////////////////////////////////////////////////////
	// enabling and disabling
	
	/** Determine if this SimpleAction should be shown as grayed out in menus and
	 *  toolbars.
	 */
	public void updateEnabled() { 
		setEnabled(shouldBeEnabled());
	}
	
	/** Return <code>true</code> if this action should be available
	 *  to the user.
	 *
	 * @return <code>true</code> if this command is enabled,
	 *         otherwise <code>false</code>.
	 */
	public boolean shouldBeEnabled() {
		return isEnabled();
	}
	
	////////////////////////////////////////////////////////////////
	// accessors

	/** Replies the associated icon.
	 * 
	 * @return the associated icon.
	 */
	public Icon getIcon() {
		Object o = getValue(SMALL_ICON);
		if ((o!=null)&&(o instanceof Icon)) {
			return (Icon)o;
		}
		return null;
	}
	
	/** Set the associated icon.
	 * 
	 * @param icon 
	 */
	public void setIcon(Icon icon) {
		Object old = getValue(SMALL_ICON);
		putValue(SMALL_ICON, icon);
		if (old!=icon)
			firePropertyChange(SMALL_ICON,old,icon);
	}
	
	/** Replies the associated tooltip text.
	 * 
	 * @return the associated tooltip text
	 */
	public String getToolTipText() {
		Object o = getValue(SHORT_DESCRIPTION);
		return (o!=null)? o.toString() : null;
	}
	
	/** Set the associated tooltip text.
	 * 
	 * @param text
	 */
	public void setToolTipText(String text) {
		Object old = getValue(SHORT_DESCRIPTION);
		putValue(SHORT_DESCRIPTION, text);
		firePropertyChange(SHORT_DESCRIPTION,old,text);
	}

	/** Replies the associated text.
	 * 
	 * @return the associated text.
	 */
	public String getText() {
		Object o = getValue(NAME);
		return (o!=null)? o.toString() : null;
	}
	
	/** Set the associated text.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		Object old = getValue(NAME);
		putValue(NAME, text);
		firePropertyChange(NAME,old,text);
	}

	/** Replies the associated context help message.
	 * 
	 * @return the associated context help message.
	 */
	public String getContextHelpText() {
		Object o = getValue(LONG_DESCRIPTION);
		return (o!=null)? o.toString() : null;
	}
	
	/** Set the associated context help message.
	 * 
	 * @param text
	 */
	public void setContextHelpText(String text) {
		Object old = getValue(LONG_DESCRIPTION);
		putValue(LONG_DESCRIPTION, text);
		firePropertyChange(LONG_DESCRIPTION,old,text);
	}

	/** Replies the associated mnemonic character.
	 * 
	 * @return the associated mnemonic character.
	 */
	public int getMnemonic() {
		Object o = getValue(MNEMONIC_KEY);
		return ((o!=null)&&(o instanceof Character)) ? (Character)o : '\0';
	}
	
	/** Set the associated mnemonic character.
	 * 
	 * @param mnemonic
	 */
	public void setMnemonic(int mnemonic) {
		Object old = getValue(MNEMONIC_KEY);
		putValue(Action.MNEMONIC_KEY, mnemonic);
		firePropertyChange(MNEMONIC_KEY,old,MNEMONIC_KEY);
	}

	/** Replies the associated action command.
	 * 
	 * @return the associated action command.
	 */
	public String getActionCommand() {
		Object o = getValue(ACTION_COMMAND_KEY);
		return (o!=null)? o.toString() : null;
	}
	
	/** Set the associated action command.
	 * 
	 * @param command
	 */
	public void setActionCommand(String command) {
		Object old = getValue(ACTION_COMMAND_KEY);
		putValue(ACTION_COMMAND_KEY, command);
		firePropertyChange(ACCELERATOR_KEY,old,command);
	}

    /** Returns the state of the action. <code>true</code> if the
     * toggle action is selected, <code>false</code> if it's not.
     * 
     * @return <code>true</code> if the toggle action is selected, otherwise
     * <code>false</code>
     * @since 4.0
     */
	public boolean isSelected() {
		Object o = getValue(SELECTED_KEY);
		return Boolean.TRUE.equals(o);
	}
	
    /** Sets the state of the action. Note that this method does not
     * trigger an <code>actionEvent</code>.
     *
     * @param isSelected is <code>true</code> if the action is selected,
     * otherwise <code>false</code>.
     * @since 4.0
     */
	public void setSelected(boolean isSelected) {
		Object old = getValue(SELECTED_KEY);
		Object newValue = Boolean.valueOf(isSelected);
		putValue(SELECTED_KEY, newValue);
		firePropertyChange(SELECTED_KEY,old,newValue);
	}

}
