/* 
 * $Id$
 * 
 * Copyright (C) 2009-10 St&eacute;phane GALLAND
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
 
#ifndef __OSMACRO_H__
#  define __OSMACRO_H__

#  if defined(WINDOWS) || defined(WIN32) || defined(WIN64) || defined(WINNT)
#    define __WINDOWS__
#    undef __UNIX__
#  else
#    define __UNIX__
#    undef __WINDOWS__
#  endif

#endif /* __OSMACRO_H__ */
