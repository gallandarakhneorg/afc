/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
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

package org.arakhne.afc.ui.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.StringAnchor;
import org.arakhne.afc.ui.TextAlignment;

/** This graphic context permits to display
 *  something with a level of details.
 *
 * @param <G> is the type of the graphics pointed by this LODGraphics2D.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractLODGraphics2D<G extends Graphics2D> extends Graphics2D implements LODGraphics2D {

	/** Is the minimal size of the displayed figures when
	 * a low level of details was adviced.
	 */
	protected static final float LOW_DETAIL_LEVEL = 5;
	
	/** Margin in the cartridges.
	 */
	protected static final float CARTRIDGE_MARGIN = 3;

	////////////////////////////////////////////////////////////
	// Attributes

	/** This is the graphics target.
	 */
	protected final G target ;

	/** This is the target component.
	 */
	protected final WeakReference<Component> targetComponent ;

	/** Indicates if this graphics is for printing.
	 */
	private final boolean isForPrinting;

	/** Indicates the current LOD.
	 */
	private final Graphics2DLOD lod;
	
	/** Cartridges.
	 */
	private CartridgeNode cartridges = null;

	/** The URL of the image to draw.
	 */
	private URL imageResource = null;

	////////////////////////////////////////////////////////////
	// Constructor

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param antialiasing permits to force the anti-aliasing flag for the target graphical context
	 * @param is_for_printing indicates if this graphics environment is for printing or not.
	 * @param lod indicates the desired LOD used by this graphical context.
	 */
	public AbstractLODGraphics2D(G target, Component target_component, boolean antialiasing, boolean is_for_printing, Graphics2DLOD lod) {
		this.target = target ;
		this.isForPrinting = is_for_printing;
		this.lod = lod;

		// Force the anti-aliasing for this graphical context
		setAntiAliased(antialiasing);

		this.targetComponent = target_component==null ? null : new WeakReference<Component>(target_component);
	}

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param antialiasing permits to force the anti-aliasing flag for the target graphical context
	 * @param lod indicates the desired LOD used by this graphical context.
	 */
	public AbstractLODGraphics2D(G target, Component target_component, boolean antialiasing, Graphics2DLOD lod) {
		this(target, target_component, antialiasing, false, lod);
	}

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param lod indicates the desired LOD used by this graphical context.
	 */
	public AbstractLODGraphics2D(G target, Component target_component, Graphics2DLOD lod) {
		this(target, target_component, false, false, lod);
	}
	
	/** Replies the graphics object that is really used to render.
	 * <p>
	 * The implementation of the replied object is based on the AWT/Swing API.
	 * 
	 * @return the rendering graphics
	 */
	public Graphics2D getRenderingGraphics() {
		return this.getRenderingGraphics();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setImageURL(URL url) {
		this.imageResource = url;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public URL getImageURL() {
		return this.imageResource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringAnchor getStringAnchor() {
		return StringAnchor.LEFT_BASELINE;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Graphics2DLOD getLOD() {
		return this.lod;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isPrinting() {
		return this.isForPrinting;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isAntiAliased() {
		Object a = this.target.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		return ((a!=null)&&
				((a==RenderingHints.VALUE_ANTIALIAS_ON)||
						(a.equals(RenderingHints.VALUE_ANTIALIAS_ON))));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setAntiAliased(boolean anti_aliased_enable) {
		this.target.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				anti_aliased_enable
				? RenderingHints.VALUE_ANTIALIAS_ON
						: RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	/** {@inheritDoc}
	 */
	@Override
    public final boolean hit(Shape shape) {
    	Rectangle2D bounds = shape.getBounds2D();
        if (bounds!=null) return hit(bounds);
        return true;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public boolean hit(Rectangle2D rect) {
    	if (rect==null || rect.isEmpty()) return false;
    	
    	float w = (float)rect.getWidth();
    	float h = (float)rect.getHeight();
    	
    	if ((w<=0)&&(h<=0)) return false;
    	
    	if (!this.target.hitClip(
    			(int)rect.getX(),
    			(int)rect.getY(),
    			((int)w)+2,
    			((int)h)+2)) {
    		return false;
    	}
    
    	switch(this.lod) {
    	case SHADOW:
    	case LOW_LEVEL_OF_DETAIL:
    		float mini_length = LOW_DETAIL_LEVEL;
    		float diag = (float)Math.hypot(w,h);
        	return (diag>=mini_length);
    	case HIGH_LEVEL_OF_DETAIL:
    	case NORMAL_LEVEL_OF_DETAIL:
    		return true;
		default:
	    	return true;
    	}
    }

	/** {@inheritDoc}
	 */
	@Override
	public Dimension2D getTargetComponentSize() {
		Component c = (this.targetComponent==null) ? null : 
			this.targetComponent.get();
		return (c!=null) ? c.getSize() : null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2D getViewportRect() {
		Component c = (this.targetComponent==null) ? null : 
			this.targetComponent.get();
		if (c==null) return null;
		Dimension2D d = c.getSize();
		return new Rectangle2D.Double(
				0,
				0,
				d.getWidth()-1,
				d.getHeight()-1);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Dimension2D getViewportSize() {
		Dimension2D d = getTargetComponentSize();
		if (d==null) return d;
		return new DoubleDimension(d.getWidth(), d.getHeight());
	}

	////////////////////////////////////////////////////////////
	// Graphics API

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if (this.cartridges!=null) {
			this.cartridges = null;
		}
		this.target.dispose() ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Graphics create(float x, float y, float width, float height) {
		Graphics2D g = (Graphics2D)create();
        if (g == null) return null;
        g.translate(x, y);
        g.clip(new Rectangle2D.Double(0, 0, width, height));
        return g;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Graphics create(int x, int y, int width, int height) {
		return create((float)x, (float)y, (float)width, (float)height);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clipRect(float x, float y, float width, float height) {
		Rectangle2D r = new Rectangle2D.Double(x,y,width,height);
		this.target.clip(r);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void clipRect(int x, int y, int width, int height) {
		clipRect((float)x, (float)y, (float)width, (float)height);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClip(float x, float y, float width, float height) {
		Rectangle2D r = new Rectangle2D.Double(x,y,width,height);
		this.target.setClip(r);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setClip(int x, int y, int width, int height) {
		setClip((float)x, (float)y, (float)width, (float)height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw3DRect(float x, float y, float width, float height,
			boolean raised) {
		Paint p = getPaint();
        Color c = getColor();
        Color brighter = c.brighter();
        Color darker = c.darker();

        setColor(raised ? brighter : darker);
        //drawLine(x, y, x, y + height);
        fillRect(x, y, 1, height + 1);
        //drawLine(x + 1, y, x + width - 1, y);
        fillRect(x + 1, y, width - 1, 1);
        setColor(raised ? darker : brighter);
        //drawLine(x + 1, y + height, x + width, y + height);
        fillRect(x + 1, y + height, width, 1);
        //drawLine(x + width, y, x + width, y + height - 1);
        fillRect(x + width, y, 1, height);
        setPaint(p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void draw3DRect(int x, int y, int width, int height, boolean raised) {
		draw3DRect((float)x, (float)y, (float)width, (float)height, raised);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fill3DRect(float x, float y, float width, float height,
			boolean raised) {
		Paint p = getPaint();
        Color c = getColor();
        Color brighter = c.brighter();
        Color darker = c.darker();

        if (!raised) {
            setColor(darker);
        } else if (p != c) {
            setColor(c);
        }
        fillRect(x+1, y+1, width-2, height-2);
        setColor(raised ? brighter : darker);
        //drawLine(x, y, x, y + height - 1);
        fillRect(x, y, 1, height);
        //drawLine(x + 1, y, x + width - 2, y);
        fillRect(x + 1, y, width - 2, 1);
        setColor(raised ? darker : brighter);
        //drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
        fillRect(x + 1, y + height - 1, width - 1, 1);
        //drawLine(x + width - 1, y, x + width - 1, y + height - 2);
        fillRect(x + width - 1, y, 1, height - 1);
        setPaint(p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void fill3DRect(int x, int y, int width, int height, boolean raised) {
		fill3DRect((float)x, (float)y, (float)width, (float)height, raised);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawChars(char[] data, int offset, int length, float x,
			float y) {
		drawString(new String(data, offset, length), x, y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawChars(char[] data, int offset, int length, int x, int y) {
		drawChars(data, offset, length, (float)x, (float)y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawBytes(byte[] data, int offset, int length, float x,
			float y) {
		drawString(new String(data, offset, length), x, y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawBytes(byte[] data, int offset, int length, int x, int y) {
		drawBytes(data, offset, length, (float)x, (float)y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hitClip(float x, float y, float width, float height) {
		return this.target.hitClip((int)x, (int)y, (int)width, (int)height);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean hitClip(int x, int y, int width, int height) {
		return hitClip((float)x, (float)y, (float)width, (float)height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hit(Rectangle2D rect, Shape s, boolean onStroke) {
		return this.target.hit(rect.getBounds(), s, onStroke);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return hit((Rectangle2D)rect, s, onStroke);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return this.target.getDeviceConfiguration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setComposite(Composite comp) {
		this.target.setComposite(comp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPaint(Paint paint) {
		this.target.setPaint(paint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStroke(Stroke s) {
		this.target.setStroke(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		this.target.setRenderingHint(hintKey, hintValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getRenderingHint(Key hintKey) {
		return this.target.getRenderingHint(hintKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		this.target.setRenderingHints(hints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		this.target.addRenderingHints(hints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RenderingHints getRenderingHints() {
		return this.target.getRenderingHints();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void scale(double sx, double sy) {
		scale((float)sx, (float)sy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(float sx, float sy) {
		this.target.scale(sx, sy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void shear(double shx, double shy) {
		shear((float)shx, (float)shy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shear(float shx, float shy) {
		this.target.shear(shx, shy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transform(AffineTransform Tx) {
		this.target.transform(Tx);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void rotate(double theta) {
		rotate((float)theta);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(float theta) {
		this.target.rotate(theta);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void rotate(double theta, double x, double y) {
		rotate((float)theta, (float)x, (float)y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(float theta, float x, float y) {
		this.target.rotate(theta, x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTransform(AffineTransform Tx) {
		this.target.setTransform(Tx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AffineTransform getTransform() {
		return this.target.getTransform();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Paint getPaint() {
		return this.target.getPaint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Composite getComposite() {
		return this.target.getComposite();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(Color color) {
		this.target.setBackground(color);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getBackground() {
		return this.target.getBackground();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stroke getStroke() {
		return this.target.getStroke();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FontRenderContext getFontRenderContext() {
		return this.target.getFontRenderContext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColor() {
		return this.target.getColor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColor(Color c) {
		this.target.setColor(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPaintMode() {
		this.target.setPaintMode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setXORMode(Color c1) {
		this.target.setXORMode(c1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		drawImage(img, op, (float)x, (float)y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, float x,
			float y) {
		this.target.drawImage(img, op, (int)x, (int)y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return drawImage(img, (float)x, (float)y, observer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drawImage(Image img, float x, float y,
			ImageObserver observer) {
		return this.target.drawImage(img, (int)x, (int)y, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean drawImage(Image img, int x, int y, int width, int height,
			ImageObserver observer) {
		return drawImage(img, (float)x, (float)y, (float)width, (float)height, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drawImage(Image img, float x, float y, float width, float height,
			ImageObserver observer) {
		return this.target.drawImage(img, (int)x, (int)y, (int)width, (int)height, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean drawImage(Image img, int x, int y, Color bgcolor,
			ImageObserver observer) {
		return drawImage(img, (float)x, (float)y, bgcolor, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drawImage(Image img, float x, float y, Color bgcolor,
			ImageObserver observer) {
		return this.target.drawImage(img, (int)x, (int)y, bgcolor, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean drawImage(Image img, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		return drawImage(img, (float)x, (float)y, (float)width, (float)height, bgcolor, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drawImage(Image img, float x, float y, float width, float height,
			Color bgcolor, ImageObserver observer) {
		return this.target.drawImage(img, (int)x, (int)y, (int)width, (int)height, bgcolor, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return drawImage(img, (float)dx1, (float)dy1, (float)dx2, (float)dy2,
				sx1, sy1, sx2, sy2, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drawImage(Image img, float dx1, float dy1, float dx2, float dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return this.target.drawImage(img,
				(int)dx1, (int)dy1, (int)dx2, (int)dy2,
				sx1, sy1, sx2, sy2, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor,
			ImageObserver observer) {
		return drawImage(img, (float)dx1, (float)dy1, (float)dx2, (float)dy2,
				sx1, sy1, sx2, sy2, bgcolor, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drawImage(Image img, float dx1, float dy1, float dx2, float dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
		return this.target.drawImage(img,
				(int)dx1, (int)dy1, (int)dx2, (int)dy2,
				sx1, sy1, sx2, sy2, bgcolor, observer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		this.target.drawGlyphVector(g, x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyArea(float x, float y, float width, float height,
			float dx, float dy) {
		this.target.copyArea((int)x, (int)y, (int)width, (int)height, (int)dx, (int)dy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void copyArea(int x, int y, int width, int height, int dx, int dy) {
		copyArea((float)x, (float)y, (float)width, (float)height, (float)dx, (float)dy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		Line2D line = new Line2D.Double(x1,y1,x2,y2);
		this.target.draw(line);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawLine(int x1, int y1, int x2, int y2) {
		drawLine((float)x1, (float)y1, (float)x2, (float)y2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillRect(float x, float y, float width, float height) {
		Rectangle2D r = new Rectangle2D.Double(x,y,width,height);
		this.target.fill(r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void fillRect(int x, int y, int width, int height) {
		fillRect((float)x, (float)y, (float)width, (float)height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawRect(float x, float y, float width, float height) {
		Rectangle2D r = new Rectangle2D.Double(x,y,width,height);
		this.target.draw(r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearRect(float x, float y, float width, float height) {
		this.target.clearRect((int)x, (int)y, (int)width, (int)height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void clearRect(int x, int y, int width, int height) {
		this.target.clearRect(x, y, width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawRoundRect(float x, float y, float width, float height,
			float arcWidth, float arcHeight) {
		RoundRectangle2D r = new RoundRectangle2D.Double(
				x, y, width, height, arcWidth, arcHeight);
		this.target.draw(r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		drawRoundRect((float)x, (float)y, (float)width, (float)height, (float)arcWidth, (float)arcHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillRoundRect(float x, float y, float width, float height,
			float arcWidth, float arcHeight) {
		RoundRectangle2D r = new RoundRectangle2D.Double(
				x, y, width, height, arcWidth, arcHeight);
		this.target.fill(r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		drawRoundRect((float)x, (float)y, (float)width, (float)height, (float)arcWidth, (float)arcHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawOval(float x, float y, float width, float height) {
		Ellipse2D o = new Ellipse2D.Double(x, y, width, height);
		this.target.draw(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawOval(int x, int y, int width, int height) {
		drawOval((float)x, (float)y, (float)width, (float)height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillOval(float x, float y, float width, float height) {
		Ellipse2D o = new Ellipse2D.Double(x, y, width, height);
		this.target.fill(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void fillOval(int x, int y, int width, int height) {
		fillOval((float)x, (float)y, (float)width, (float)height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawArc(float x, float y, float width, float height,
			float startAngle, float arcAngle) {
		Arc2D a = new Arc2D.Double(x, y, width, height, startAngle, arcAngle,
				Arc2D.OPEN);
		this.target.draw(a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		drawArc((float)x, (float)y, (float)width, (float)height, (float)startAngle, (float)arcAngle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillArc(float x, float y, float width, float height,
			float startAngle, float arcAngle) {
		Arc2D a = new Arc2D.Double(x, y, width, height, startAngle, arcAngle,
				Arc2D.OPEN);
		this.target.fill(a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		fillArc((float)x, (float)y, (float)width, (float)height, (float)startAngle, (float)arcAngle);
	}
	
	/** Cast the specified x coordinate for poly-elements.
	 * 
	 * @param x
	 * @return the casted x
	 */
	protected abstract float polyX(float x);

	/** Cast the specified y coordinate for poly-elements.
	 * 
	 * @param y
	 * @return the casted y
	 */
	protected abstract float polyY(float y);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawPolyline(float[] xPoints, float[] yPoints, int nPoints) {
		if (nPoints>0) {
			GeneralPath path = new GeneralPath();
			path.moveTo(xPoints[0], yPoints[0]);
			for(int i=1; i<nPoints; ++i) {
				path.moveTo(polyX(xPoints[i]), polyY(yPoints[i]));
			}
			this.target.draw(path);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		if (nPoints>0) {
			GeneralPath path = new GeneralPath();
			path.moveTo(xPoints[0], yPoints[0]);
			for(int i=1; i<nPoints; ++i) {
				path.moveTo(polyX(xPoints[i]), polyY(yPoints[i]));
			}
			this.target.draw(path);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawPolygon(float[] xPoints, float[] yPoints, int nPoints) {
		if (nPoints>0) {
			GeneralPath path = new GeneralPath();
			path.moveTo(xPoints[0], yPoints[0]);
			for(int i=1; i<nPoints; ++i) {
				path.moveTo(polyX(xPoints[i]), polyY(yPoints[i]));
			}
			path.closePath();
			this.target.draw(path);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void fillPolygon(float[] xPoints, float[] yPoints, int nPoints) {
		if (nPoints>0) {
			GeneralPath path = new GeneralPath();
			path.moveTo(xPoints[0], yPoints[0]);
			for(int i=1; i<nPoints; ++i) {
				path.moveTo(polyX(xPoints[i]), polyY(yPoints[i]));
			}
			path.closePath();
			this.target.fill(path);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		if (nPoints>0) {
			GeneralPath path = new GeneralPath();
			path.moveTo(xPoints[0], yPoints[0]);
			for(int i=1; i<nPoints; ++i) {
				path.moveTo(polyX(xPoints[i]), polyY(yPoints[i]));
			}
			path.closePath();
			this.target.draw(path);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		if (nPoints>0) {
			GeneralPath path = new GeneralPath();
			path.moveTo(xPoints[0], yPoints[0]);
			for(int i=1; i<nPoints; ++i) {
				path.moveTo(polyX(xPoints[i]), polyY(yPoints[i]));
			}
			path.closePath();
			this.target.fill(path);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(Shape s) {
		this.target.draw(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return this.target.drawImage(img, xform, obs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		this.target.drawRenderedImage(img, xform);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		this.target.drawRenderableImage(img, xform);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawString(String str, int x, int y) {
		drawString(str, (float)x, (float)y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawString(String str, float x, float y) {
		this.target.drawString(str, x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawString(AttributedCharacterIterator iterator, int x, int y) {
		drawString(iterator, (float)x, (float)y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawString(AttributedCharacterIterator iterator, float x,
			float y) {
		this.target.drawString(iterator, x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fill(Shape s) {
		this.target.fill(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void translate(int x, int y) {
		translate((float)x, (float)y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void translate(double x, double y) {
		translate((float)x, (float)y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(float tx, float ty) {
		this.target.translate(tx, ty);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clip(Shape s) {
		this.target.clip(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Font getFont() {
		return this.target.getFont();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(Font font) {
		this.target.setFont(font);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FontMetrics getFontMetrics(Font f) {
		return this.target.getFontMetrics(f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Rectangle getClipBounds() {
		return getClip().getBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Shape getClip() {
		return this.target.getClip();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClip(Shape clip) {
		this.target.setClip(clip);
	}

	////////////////////////////////////////////////////////////
	// LODGraphics2D API
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void drawPoint(float x, float y) {
		drawPoint(x, y, 1);
	}
	
	/** Draw a point.
	 * 
	 * @param x
	 * @param y
	 * @param pointSizeInPixel is the size of the point in pixels.
	 */
	protected void drawPoint(float x, float y, float pointSizeInPixel) {
		float s = pointSizeInPixel/2f;
		fillRect(x-s, y-s, pointSizeInPixel, pointSizeInPixel);
	}

	/** Compute the bounds of a cartridge which is containing
	 * the given text.
	 * 
	 * @param lines is the text to put in the cartridge.
	 * @param x is the position of the upper left corner.
	 * @param y is the position of the upper left corner.
	 * @param margin is the margin around the text.
	 * @param bounds are the output bounds.
	 */
	protected final void cartridgeBounds(String[] lines, float x, float y, float margin, Rectangle2D bounds) {
		float px = x;
		float py = y;
		
		FontMetrics metrics = this.target.getFontMetrics();
		
		float width, maxWidth = 0;
		float lineHeight = metrics.getHeight();
		float height = lineHeight * lines.length + 2*margin;
		
		for(String line : lines) {
			width = metrics.stringWidth(line);
			if (width>maxWidth) maxWidth = width;
		}
		
		maxWidth += 2*margin;

		Dimension2D screen = getTargetComponentSize();
		float d = px + maxWidth - (float)screen.getWidth();
		if (d>0) px -= d;
		d = py + height - (float)screen.getHeight();
		if (d>0) py -= d;
		if (px<0) px = 0;
		if (py<0) py = 0;
		
		bounds.setRect(px,py,maxWidth,height);
	}

	/** Draw a string inside a cartridge.
	 * <p>
	 * A cartridge is a text surround by a box. The LODGraphics2D object
	 * ensures that the cartridges do not overlaps and tries to move
	 * them if it is the case.
	 * 
	 * @param lines is the text inside the cartridge.
	 * @param bounds is the position and dimension of the cartridge.
	 * @param margin is the margin to add into the cartridge box.
	 * @param backgroundColor is the color of the background, or <code>null</code> for transparent background.
	 * @param textColor is the color of the text, or <code>null</code> for the current LODGraphics2D color.
	 * @param borderColor is the color of the cartridge border, or <code>null</code> for transparent borders.
	 * @param alignment is the alignment of the text in the cartridge
	 */
	protected final void drawCartridge(String[] lines, Rectangle2D bounds, float margin, Color textColor, Color backgroundColor, Color borderColor, TextAlignment alignment) {
		Color tColor = textColor;
		
		if (this.cartridges==null) {
			this.cartridges = new CartridgeNode(bounds);
		}
		else {
			this.cartridges.add(bounds);
		}
				
		if (tColor==null) tColor = this.target.getColor();
		
		if (backgroundColor!=null) {
			this.target.setColor(backgroundColor);
			this.target.fill(bounds);
		}

		if (borderColor!=null) {
			this.target.setColor(borderColor);
			this.target.draw(bounds);
		}

		this.target.setColor(tColor);
		
		float ix = (float)bounds.getMinX();
		float iy = (float)bounds.getMinY();
		float iix = ix;
		switch(alignment) {
		case CENTER_ALIGN:
			ix = (int)(ix + bounds.getWidth()/2);
			break;
		case RIGHT_ALIGN:
			ix = (int)(ix + bounds.getWidth() - margin);
			break;
		default:
			iix = ix + margin;
			break;
		}
		
		FontMetrics metrics = this.target.getFontMetrics();

		for(String line : lines) {
			iy += metrics.getHeight();
			switch(alignment) {
			case CENTER_ALIGN:
				iix = ix - metrics.stringWidth(line)/2;
				break;
			case RIGHT_ALIGN:
				iix = ix - metrics.stringWidth(line);
				break;
			default:
			}
			this.target.drawString(line, iix, iy);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void drawCartridge(String text, float x, float y, Color textColor, Color backgroundColor, Color borderColor, TextAlignment alignment) {
		String[] lines = text.split("\n"); //$NON-NLS-1$
		float margin = (backgroundColor!=null || borderColor!=null) ? CARTRIDGE_MARGIN : 0;
		Rectangle2D rectangle = new Rectangle2D.Double();
		cartridgeBounds(lines, x, y, margin, rectangle);
		drawCartridge(lines, rectangle, margin, textColor, backgroundColor, borderColor, alignment);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void drawCartridge(String text, float x, float y) {
		drawCartridge(text, x, y, null, null, null, TextAlignment.LEFT_ALIGN);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void drawCartridge(String text, float x, float y, Color backgroundColor, Color borderColor) {
		drawCartridge(text, x, y, null, backgroundColor, borderColor, TextAlignment.LEFT_ALIGN);
	}
		
	/** R-Tree node for cartridge storage
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CartridgeNode {
		
		private final Rectangle2D data;
		private CartridgeNode[] children = new CartridgeNode[8];
		
		public CartridgeNode(Rectangle2D data) {
			this.data = data;
		}

		public void add(Rectangle2D nData) {
			assert(nData!=null);
			if (nData==this.data)
				return; // special case of the rectangle added many times.
			int childIdx = getChildIndex(nData);
			if (childIdx==-1) {
				AwtUtil.avoidCollision(nData, this.data);
				childIdx = getChildIndex(nData);
			}
			if (this.children[childIdx]==null) {
				this.children[childIdx] = new CartridgeNode(nData);
			}
			else {
				this.children[childIdx].add(nData);
			}
		}
		
		private int getChildIndex(Rectangle2D newData) {
			if (newData.getMaxX() < this.data.getMinX()) {
				if (newData.getMaxY() <= this.data.getMinY()) return 0;
				if (newData.getMinY() >= this.data.getMaxY()) return 1;
				return 2;
			}
			if (newData.getMinX() > this.data.getMaxX()) {
				if (newData.getMaxY() <= this.data.getMinY()) return 3;
				if (newData.getMinY() >= this.data.getMaxY()) return 4;
				return 5;
			}
			if (newData.getMaxY() <= this.data.getMinY()) return 6;
			if (newData.getMinY() >= this.data.getMaxY()) return 7;
			return -1;
		}
		
	}

}
