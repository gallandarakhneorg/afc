/* $Id$
 * 
 * This is a part of NetEditor project from Arakhne.org:
 * package org.arakhne.neteditor.zoompanel.
 * 
 * Copyright (C) 2002  St&eacute;phane GALLAND, Mahdi Hannoun
 * Copyright (C) 2004-2013  St&eacute;phane GALLAND
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
package org.arakhne.afc.ui.swing.zoompanel;


import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.Printable;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.ui.ZoomableContext;

/** This interface describes the viewport that supports
 * zooming.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface ZoomableViewport extends ZoomableContext, Printable {
    
    /** Replies the target point in the screen coordinate system.
     * 
     * @return the target point in the screen coordinate system
     */
    public Point2D getFocusPointPixel();

    /** Replies the target point.
     * 
     * @return the target point.
     */
    public Point2f getFocusPoint();

    /** Sets the target point.
     * 
     * @param x
     * @param y
     */
    public void setFocusPoint(float x, float y);
	
    /** Replies the drawing area size in screen coordinates.
     * 
     * @return the drawing area size in screen coordinates.
     */
    public Dimension2D getDrawingAreaSize();

    /** Replies the document rectangle in screen coordinate system.
     * 
     * @return the document rectangle in screen coordinate system.
     */
    public Rectangle2D getDrawingAreaRect();
    
    /** Reply the rectangle that enclose the
     *  current document.
     *
     * @return a Rectangle that correspond to the
     *          size of the document (in workspace coordinates).
     */
    public Rectangle2D getDocumentRect();
		
    /** Set the zoom factor.
     * 
     * @param factor is the new zoom factor.
     */
    public void setScalingFactor(float factor);

    /** Set the zoom factor.
     * 
     * @param factor is the new zoom factor.
     * @param enable_repaint is <code>true</code> to enable the repainting
     * of the pane, otherwhise it is <code>false</code>.
     */
    public void setScalingFactor(float factor, boolean enable_repaint);

    /** Zoom in.
     */
    public void zoomIn();

    /** Zoom out.
     */
    public void zoomOut();
    
    /** Zoom in.
     * 
     * @param enable_repaint is <code>true</code> to enable the repainting
     * of the pane, otherwhise it is <code>false</code>.
     */
    public void zoomIn(boolean enable_repaint);

    /** Zoom out.
     * 
     * @param enable_repaint is <code>true</code> to enable the repainting
     * of the pane, otherwhise it is <code>false</code>.
     */
    public void zoomOut(boolean enable_repaint);
    
    /** Force the efreshing of this panel.
     */
    public void refreshPanelContent();
    
}
