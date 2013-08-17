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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.event.EventListenerList;

/**
 * This button permits to select a color.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JColorSelector extends JButton {

	private static final long serialVersionUID = 3759677089992951265L;
	
	private ColorButtonModel colorModel = null;
	private Handler handler = null;

	/**
	 */
	public JColorSelector() {
		this(new ColorButtonModel());
	}

	/**
	 * @param initialeColor is the initial color inside the associated model.
	 */
	public JColorSelector(Color initialeColor) {
		this(new ColorButtonModel(initialeColor));
	}

	/**
	 * @param model is the color model to use.
	 */
	public JColorSelector(ColorButtonModel model) {
		setColorButtonModel(model);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Dimension sizes = getSize();

		if (this.colorModel!=null) {
			Color c = this.colorModel.getColor();
			if (c!=null) {
				g.setColor(c);
				g.fill3DRect(5, 5, sizes.width-10,sizes.height-10, false);
				return;
			}
		}

		// Draw a grid
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		Stroke oldStroke = g2d.getStroke();
		BasicStroke bs = new BasicStroke(1f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				1f,
				new float[]{1f, 2f},
				0f);
		g2d.setStroke(bs);
		g2d.drawRect(5, 5, sizes.width-10,sizes.height-10);
		g2d.setStroke(oldStroke);
	}

	/** Set the color.
	 * 
	 * @param color
	 */
	public void setSelectedColor(Color color) {
		if (this.colorModel!=null)
			this.colorModel.setColor(color);
	}

	/** Get the color.
	 * 
	 * @return the color or <code>null</code>
	 */
	public Color getSelectedColor() {
		if (this.colorModel!=null)
			return this.colorModel.getColor();
		return null;
	}

	/** Replies the color model.
	 * 
	 * @return the color model.
	 */
	public ColorButtonModel getColorButtonModel() {
		return this.colorModel;
	}

	/** Set the color model.
	 * 
	 * @param model
	 */
	public void setColorButtonModel(ColorButtonModel model) {
		if (model==null) return;
		if (this.colorModel!=null && this.handler!=null) {
			this.colorModel.removeColorButtonModelListener(this.handler);
			removeActionListener(this.handler);
			this.handler = null;
		}
		this.colorModel = model;
		this.handler = new Handler();
		addActionListener(this.handler);
		this.colorModel.addColorButtonModelListener(this.handler);
	}

	/**
	 * Model for color buttons
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class ColorButtonModel {

		private EventListenerList listeners = null;
		private Color color;

		/**
		 */
		public ColorButtonModel() {
			this.color = null;
		}

		/**
		 * @param initialColor
		 */
		public ColorButtonModel(Color initialColor) {
			this.color = initialColor;
		}

		/** Add a listener.
		 * 
		 * @param listener
		 */
		public void addColorButtonModelListener(ColorButtonModelListener listener) {
			if (this.listeners==null)
				this.listeners = new EventListenerList();
			this.listeners.add(ColorButtonModelListener.class, listener);
		}

		/** Remove a listener.
		 * 
		 * @param listener
		 */
		public void removeColorButtonModelListener(ColorButtonModelListener listener) {
			if (this.listeners==null) return;
			this.listeners.remove(ColorButtonModelListener.class, listener);
			if (this.listeners.getListenerCount()==0)
				this.listeners = null;
		}

		/** Fire color change event.
		 * 
		 * @param oldColor
		 * @param newColor
		 */
		protected void fireColorChanged(Color oldColor, Color newColor) {
			if (this.listeners!=null) {
				ColorButtonModelListener[] list = this.listeners.getListeners(ColorButtonModelListener.class);
				for(ColorButtonModelListener listener : list) {
					listener.onColorChange(this, oldColor, newColor);
				}
			}
		}

		/** Replies the color.
		 * 
		 * @return the color or <code>null</code>
		 */
		public Color getColor() {
			return this.color;
		}

		/** Set the color.
		 * 
		 * @param color is the color or <code>null</code>
		 */
		public void setColor(Color color) {
			if (color==null || this.color==color || color.equals(this.color))
				return;
			Color oldColor = this.color;
			this.color = color;
			fireColorChanged(oldColor, this.color);
		}

	} /* class ColorButtonModel */	

	/**
	 * Listeners on the color model for color buttons
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public interface ColorButtonModelListener extends EventListener {

		/** Invoked when color change.
		 * 
		 * @param source is the button that thrown this event.
		 * @param oldColor is the old value of the color.
		 * @param newColor is the new value of the color.
		 */
		public void onColorChange(ColorButtonModel source, Color oldColor, Color newColor);

	} /* class ColorButtonModelListener */	

	/**
	 * Model for color buttons
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Handler implements ColorButtonModelListener, ActionListener {

		/**
		 */
		public Handler() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onColorChange(ColorButtonModel source, Color oldColor, Color newColor) {
			JColorSelector.this.repaint();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			ColorButtonModel btModel = getColorButtonModel();
			if (btModel!=null) {
				Color color = btModel.getColor();
				Color newColor = JColorChooser.showDialog(JColorSelector.this, null, color);
				if (newColor!=null) {
					btModel.setColor(newColor);
				}
			}
		}

	} /* class ColorButtonModelListener */	

}
