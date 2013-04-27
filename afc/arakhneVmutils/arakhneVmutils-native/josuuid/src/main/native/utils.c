/* 
 * $Id$
 * 
 * Copyright (C) 2005-2011 St&eacute;phane GALLAND
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
 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "utils.h"

#ifdef DEBUG
# warning THE LIBRARY IS COMPILED WITH DEBUG INFORMATION
#endif

/* Remove white spaces at the begining and at the end of a string */
void trim(char** text) {
	char* t;
	unsigned long startIdx, endIdx;
	unsigned long len, i;
	if ((text==NULL)||(*text==NULL)) return;
    len = strlen(*text);
    // Search starting character
    for(startIdx=0; startIdx<len; startIdx++) {
    	if (!isspace((*text)[startIdx])) break;
    }
    if (startIdx>=len) {
    	free(*text);
    	*text = strdup("");
    	return;
    }
    // Search ending character
    for(endIdx=len-1; endIdx>=0; endIdx--) {
    	if (!isspace((*text)[endIdx])) break;
    }
    if (endIdx<0) {
    	free(*text);
    	*text = strdup("");
    	return;
    }
    // Create the new string
    t = (char*)malloc(sizeof(char)*(endIdx-startIdx+2));
    for(i=0; startIdx<=endIdx; i++, startIdx++) {
    	t[i] = (*text)[startIdx];
    }
    t[i] = '\0';
    free(*text);
    *text = t;
}
