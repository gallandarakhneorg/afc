/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.vector;



/** Wrapper on a PDF API able to extract bitmaps from PDF documents.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Pdf {
	
	/** Replies the image of the current page.
	 * 
	 * @return the image or <code>null</code> if the image cannot
	 * be obtained.
	 */
	public Image getImage();
	
	/** Set the dimension of the viewer.
	 * 
	 * @param dimension
	 */
	public void setViewerSize(Dimension dimension);
	
	/** Replies the current page number that is replied by {@link #getImage()}.
	 * 
	 * @return the current page number.
	 */
	public int getPageNumber();
	
	/** Set the current page number that is replied by {@link #getImage()}.
	 * 
	 * @param pageno is the current page number.
	 * @param observer is the observer of the image loading.
	 * @return <code>true</code> if the page number has changed.
	 */
	public boolean setPageNumber(int pageno, ImageObserver observer);

	/** Replies the total number of pages in the PDF document.
	 * 
	 * @return the total number of pages.
	 */
	public int getPageCount();
	
	/** Stop any asynchronous page loading.
	 */
	public void stopPageLoading();
	
	/** Release any internal resources.
	 */
	public void release();
	
	/** Replies the width of the PDF page.
	 * 
	 * @return the width or Float.NaN.
	 */
	public float getPageWidth();

	/** Replies the height of the PDF page.
	 * 
	 * @return the height or Float.NaN.
	 */
	public float getPageHeight();

}
