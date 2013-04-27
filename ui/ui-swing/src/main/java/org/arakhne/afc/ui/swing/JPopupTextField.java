/* 
 * $Id$
 * 
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

/**
 * This class provides a UI panel that permits to enter
 * the string in a popup text field.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class JPopupTextField extends JPanel {

	private static final long serialVersionUID = 374219507363847111L;

	/** Default size of the border.
	 */
	protected static final int BORDER_SIZE = 3;

	private final WeakReference<JComponent> component;

	private Point2D preferredLocation = null;
	private final EventHandler handler = new EventHandler();
	private final JTextField textField = new JTextField();

	/**
	 * @param owner is the component which is owning this popup text field.
	 * @param borderColor is the color of the borders of the pane. 
	 * @param backColor is the color of the background of the pane. 
	 */
	public JPopupTextField(JComponent owner, Color borderColor, Color backColor) {
		setFocusable(true);
		this.component = new WeakReference<JComponent>(owner);
		Font font = this.textField.getFont();
		int size = font.getSize() - 2;
		Font smallFont = font.deriveFont(size);
		this.textField.setFont(smallFont);
		this.textField.setMinimumSize(new Dimension(2*font.getSize(), font.getSize()));
		this.textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");  //$NON-NLS-1$
		this.textField.getActionMap().put("Cancel", new AbstractAction() { //$NON-NLS-1$
			private static final long serialVersionUID = -2343325366957567597L;
			@Override
			public void actionPerformed(ActionEvent e) {
				hideInputComponent();
			}
		});

		setLayout(new BorderLayout());
		add(this.textField, BorderLayout.CENTER);
		if (borderColor!=null) 
			setBorder(BorderFactory.createLineBorder(borderColor));
		if (backColor!=null)
			setBackground(backColor);
	}

	/** Set the preferred location of the popup text field.
	 * 
	 * @param x
	 * @param y 
	 */
	public void setPreferredLocation(float x, float y) {
		Point2D old = this.preferredLocation;
		if (old==null || old.getX()!=x || old.getY()!=y) {
			this.preferredLocation = new Point2D.Float(x,y);
			layoutInputComponent();
			firePropertyChange("preferredLocation", old, this.preferredLocation); //$NON-NLS-1$
		}
	}

	/** Set the preferred location of the popup text field.
	 * 
	 * @param p 
	 */
	public void setPreferredLocation(Point2D p) {
		Point2D old = this.preferredLocation;
		if (old!=p &&
				(old==null || p==null || !p.equals(old))) {
			this.preferredLocation = p;
			layoutInputComponent();
			firePropertyChange("preferredLocation", old, this.preferredLocation); //$NON-NLS-1$
		}
	}

	/** Replies the preferred location of the popuptext field.
	 * 
	 * @return the preferred location or <code>null</code> if none.
	 */
	public Point2D getPreferredLocation() {
		return this.preferredLocation;
	}

	/** Replies the embedded text field.
	 * 
	 * @return the embedded text field.
	 */
	protected JTextField getTextField() {
		return this.textField;
	}

	/** Replies the document used by this puop up text field.
	 * 
	 * @return the document.
	 */
	public Document getDocument() {
		return this.textField.getDocument();
	}

	/** Set the document used by this puop up text field.
	 * 
	 * @param doc is the the new document.
	 */
	public void setDocument(Document doc) {
		this.textField.setDocument(doc);
	}

	/** Add listener on the validation action in the popup text field.
	 * 
	 * @param listener
	 */
	public void addActionListener(ActionListener listener) {
		this.listenerList.add(ActionListener.class, listener);
	}

	/** Remove listener on the validation action in the popup text field.
	 * 
	 * @param listener
	 */
	public void removeActionListener(ActionListener listener) {
		this.listenerList.remove(ActionListener.class, listener);
	}

	/**
	 * Fire the event that corresponds to the validation in the popup text field.
	 * 
	 * @param eventSource
	 */
	protected void fireValidationAction(ActionEvent eventSource) {
		ActionListener[] list = this.listenerList.getListeners(ActionListener.class);
		for(ActionListener listener : list) {
			listener.actionPerformed(eventSource);
		}
	}

	/** Invoked when the validation action was processed.
	 * <p>
	 * By default this function does nothing.
	 */
	protected void onValidationAction() {
		//
	}

	/** Simulate the "enter" key press.
	 */
	public void doValidation() {
		ActionEvent e = new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED,
				null,
				System.currentTimeMillis(),
				0);
		fireValidationAction(e);
	}

	/** Simulate the "enter" key press.
	 * 
	 * @param sourceEvent is the event that has cause the validation.
	 */
	public void doValidation(ActionEvent sourceEvent) {
		assert(sourceEvent!=null);
		ActionEvent e = sourceEvent;
		if (e.getSource()!=this) {
			e = new ActionEvent(this,
					sourceEvent.getID(),
					sourceEvent.getActionCommand(),
					sourceEvent.getWhen(),
					sourceEvent.getModifiers());
		}
		hideInputComponent();
		onValidationAction();
		fireValidationAction(e);
	}

	/** Replies the associated owner component.
	 * 
	 * @return the associated owner component.
	 */
	public JComponent getOwner() {
		return this.component.get();
	}

	/** Resize and move the popup text field to fit the owner bounds.
	 */
	protected void layoutInputComponent() {
		JComponent component = getOwner();
		if (component!=null && getParent()!=null) {
			Dimension cdim = getPreferredSize();
			if (cdim==null) cdim = new Dimension(100, 20);
			setSize(cdim);

			JRootPane root = component.getRootPane();
			JLayeredPane lpane = root.getLayeredPane();
			Rectangle r = component.getVisibleRect();
			r = SwingUtilities.convertRectangle(component, r, lpane);

			int x = 0;
			int y = 0;
			if (this.preferredLocation!=null) {
				Point p = new Point();
				p.setLocation(this.preferredLocation);
				p = SwingUtilities.convertPoint(component, p, lpane);
				x = p.x;
				y = p.y;
			}

			if (x+cdim.width>r.width) x = r.width - cdim.width;
			if (y+cdim.height>r.height) y = r.height - cdim.height;
			if (x<0) x = 0;
			if (y<0) y = 0;

			setLocation(x, y);

			revalidate();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean aFlag) {
		if (aFlag) {
			showInputComponent();
		}
		else {
			hideInputComponent();
		}
	}

	/** Show the popup text field.
	 * 
	 * @return <code>true</code> if the component was shown,
	 * otherwise <code>false</code>.
	 */
	protected boolean showInputComponent() {
		JComponent component = getOwner();
		if (component!=null && getParent()==null) {
			JRootPane root = component.getRootPane();
			JLayeredPane lpane = root.getLayeredPane();
			lpane.add(this, JLayeredPane.PALETTE_LAYER);

			layoutInputComponent();

			super.setVisible(true);
			this.textField.requestFocusInWindow();

			this.textField.addActionListener(this.handler);
			this.textField.addFocusListener(this.handler);
			this.textField.addHierarchyBoundsListener(this.handler);
			onPopupFieldOpened();
			return true;
		}
		return false;
	}

	/** Hide the popup text field.
	 * 
	 * @return <code>true</code> if the component was hidden,
	 * otherwise <code>false</code>.
	 */
	protected boolean hideInputComponent() {
		JComponent component = getOwner();
		if (component!=null && getParent()!=null) {
			this.textField.removeFocusListener(this.handler);
			this.textField.removeActionListener(this.handler);

			super.setVisible(false);
			JRootPane root = component.getRootPane();
			if (root!=null) {
				JLayeredPane lpane = root.getLayeredPane();
				if (lpane!=null) lpane.remove(this);
			}
			component.requestFocusInWindow();
			onPopupFieldClosed();
			return true;
		}
		return false;
	}
	
	/** Invoked when the popup field is closed.
	 */
	protected void onPopupFieldClosed() {
		//
	}

	/** Invoked when the popup field is opened.
	 */
	protected void onPopupFieldOpened() {
		//
	}

	/**
	 * Replies the text inside the text field.
	 * 
	 * @return the text.
	 */
	public String getText() {
		return this.textField.getText();
	}

	/**
	 * Set the text inside the field.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.textField.setText(text);
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private class EventHandler
	implements FocusListener, ActionListener,
	HierarchyBoundsListener {

		/**
		 */
		public EventHandler() {
			//
		}

		//--------------------------------
		// FocusListener
		//--------------------------------

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void focusGained(FocusEvent e) {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void focusLost(FocusEvent e) {
			hideInputComponent();
		}

		//--------------------------------
		// KeyListener
		//--------------------------------

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			doValidation(e);
		}

		//--------------------------------
		// FocusListener
		//--------------------------------

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void ancestorMoved(HierarchyEvent e) {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void ancestorResized(HierarchyEvent e) {
			layoutInputComponent();
		}

	}

}
