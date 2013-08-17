/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.ui.vector.awt;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.matrix.SingularMatrixException;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.vector.Dimension;
import org.arakhne.afc.ui.vector.Pdf;
import org.arakhne.afc.ui.vector.VectorToolkit;

import com.sun.pdfview.Flag;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/** Public implementation of a Paint.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AwtPdf implements Pdf, ImageObserver {

	private static byte[] readFully(InputStream stream) throws IOException {
		byte[] content = new byte[0];
		byte[] buffer = new byte[2048];
		int p,n;

		n = stream.read(buffer);
		while (n>0) {
			p = content.length;
			content = Arrays.copyOf(content, p + n);
			System.arraycopy(buffer, 0, content, p, n);
			n = stream.read(buffer);
		}

		return content;
	}


	/** Permits to manipulate the entire PDF file.
	 */
	private final PDFFile pdfFile;

	/** Current rendered page.
	 */
	private PDFPage currentPdfPage = null;

	/** Number of the current page.
	 */
	private int currentPdfPageNumber = -1;

	/** a flag indicating whether the current page is done or not. */
	private final Flag flag = new Flag();

	/** the current clip, in device space */
	private Rectangle2f clip;
	/** the clipping region used for the image */
	private Rectangle2f prevClip;
	/** the size of the image */
	private Dimension prevSize;
	/** the current transform from device space to page space */
	private Transform2D currentXform;

	/** Size of the viewer. */
	private Dimension viewerSize = null;

	/** Image that is used to render the PDF page.
	 */
	private transient Image image = null;
	
	private final List<org.arakhne.afc.ui.vector.ImageObserver> observers = new ArrayList<org.arakhne.afc.ui.vector.ImageObserver>();

	/**
	 * @param url
	 * @throws IOException
	 */
	public AwtPdf(URL url) throws IOException {
		super();
		this.pdfFile = new PDFFile(ByteBuffer.wrap(readFully(url.openStream())));
	}

	@Override
	public int getPageNumber() {
		return this.currentPdfPageNumber;
	}

	@Override
	public int getPageCount() {
		return this.pdfFile.getNumPages();
	}

	@Override
	public void stopPageLoading() {
		if (this.currentPdfPage!=null) {
			java.awt.geom.Rectangle2D rr = new java.awt.geom.Rectangle2D.Float(
					this.prevClip.getMinX(),
					this.prevClip.getMinY(),
					this.prevClip.getWidth(),
					this.prevClip.getHeight());
			this.currentPdfPage.stop(
					(int)this.prevSize.width(),
					(int)this.prevSize.height(),
					rr);
		}
	}

	@Override
	public synchronized boolean setPageNumber(int pageno, org.arakhne.afc.ui.vector.ImageObserver observer) {
		PDFPage page = this.pdfFile.getPage(pageno);
		if (page!=null) {
			stopPageLoading();
			this.currentPdfPageNumber = pageno;
			this.currentPdfPage = page;
			if (observer!=null) this.observers.add(observer);
			ensureImage();
			return true;
		}
		return false;
	}

	private synchronized boolean ensureImage() {
		assert(this.currentPdfPage!=null);

		// start drawing -- clear the flag to indicate we're in progress.
		this.flag.clear();

		Dimension sz = this.viewerSize;
		if (sz!=null && sz.width() + sz.height() > 0f) {
			// calculate the clipping rectangle in page space from the
			// desired clip in screen space.
			Rectangle2f useClip = this.clip;
			if (this.clip != null && this.currentXform != null) {
				useClip = this.clip.createTransformedShape(
						this.currentXform).toBoundingBox();
			}

			java.awt.geom.Rectangle2D awtUseClip = new java.awt.geom.Rectangle2D.Float(
					useClip.getMinX(),
					useClip.getMinY(),
					useClip.getWidth(),
					useClip.getHeight());

			java.awt.geom.Dimension2D pageSize = this.currentPdfPage.getUnstretchedSize(
					(int)sz.width(),
					(int)sz.height(),
					awtUseClip);

			// get the new image
			this.image = this.currentPdfPage.getImage(
					(int)pageSize.getWidth(),
					(int)pageSize.getHeight(),
					awtUseClip, this);

			// calculate the transform from screen to page space
			this.currentXform = VectorToolkit.makeTransform(this.currentPdfPage.getInitialTransform(
					(int)pageSize.getWidth(),
					(int)pageSize.getHeight(),
					awtUseClip));
			try {
				this.currentXform.invert();
			}
			catch (SingularMatrixException nte) {
				//
			}

			this.prevClip = useClip;
			this.prevSize = VectorToolkit.dimension(
					(float)pageSize.getWidth(), (float)pageSize.getHeight());

			return true;
		}
		return false;
	}

	@Override
	public synchronized void setViewerSize(Dimension dimension) {
		if ((dimension==null && this.viewerSize!=null)||
			(dimension!=null && !dimension.equals(this.viewerSize))) {
			stopPageLoading();
			this.viewerSize = dimension;
			if (this.currentPdfPage!=null) ensureImage();
		}
	}

	@Override
	public synchronized boolean imageUpdate(Image img, int type, int x, int y,
			int width, int height) {
		int flags = 
				(java.awt.image.ImageObserver.ALLBITS |
						java.awt.image.ImageObserver.WIDTH |
						java.awt.image.ImageObserver.HEIGHT);
		boolean loaded = (type & flags) == flags;
		if (loaded) {
			this.image = img;
			for(org.arakhne.afc.ui.vector.ImageObserver obs : this.observers) {
				obs.imageUpdate(new AwtImage(this.image), x, y, width, height);
			}
			this.observers.clear();
		}
		return !loaded;
	}

	@Override
	public org.arakhne.afc.ui.vector.Image getImage() {
		if (this.image==null) {
			if (this.currentPdfPage!=null) ensureImage();
			if (this.image==null) return null;
		}
		return new AwtImage(this.image);
	}

	@Override
	public void release() {
		stopPageLoading();
	}

	@Override
	public float getPageWidth() {
		if (this.currentPdfPage!=null) {
			return this.currentPdfPage.getWidth();
		}
		return Float.NaN;
	}

	@Override
	public float getPageHeight() {
		if (this.currentPdfPage!=null) {
			return this.currentPdfPage.getHeight();
		}
		return Float.NaN;
	}

}