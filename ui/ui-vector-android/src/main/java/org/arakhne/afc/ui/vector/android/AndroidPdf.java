/* 
 * $Id$
 * 
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
package org.arakhne.afc.ui.vector.android;

import java.io.IOException;
import java.net.URL;

import org.arakhne.afc.ui.vector.Dimension;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.ImageObserver;
import org.arakhne.afc.ui.vector.Pdf;

/** Android implementation of the generic Pdf.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AndroidPdf implements Pdf {

	/**
	 * @param url
	 * @throws IOException
	 */
	public AndroidPdf(URL url) throws IOException {
		//
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public void setViewerSize(Dimension dimension) {
		//
	}

	@Override
	public int getPageNumber() {
		return -1;
	}

	@Override
	public boolean setPageNumber(int pageno, ImageObserver observer) {
		return false;
	}

	@Override
	public int getPageCount() {
		return 0;
	}

	@Override
	public void stopPageLoading() {
		//
	}

	@Override
	public void release() {
		//
	}

	@Override
	public float getPageWidth() {
		return Float.NaN;
	}

	@Override
	public float getPageHeight() {
		return Float.NaN;
	}

}