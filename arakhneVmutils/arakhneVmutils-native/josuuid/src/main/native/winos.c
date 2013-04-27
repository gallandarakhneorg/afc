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
 
#ifdef DEBUG
# warning THE LIBRARY IS COMPILED WITH DEBUG INFORMATION
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "osmacro.h"
#include "utils.h"
 
#ifndef __WINDOWS__
# error You may use Windows compiler
#endif

#  define W9XFIRST    1
#  define W95         1
#  define W95SP1      2
#  define W95OSR2     3
#  define W98         4
#  define W98SP1      5
#  define W98SE       6
#  define WME         7
#  define W9XLAST    99

#  define WNT_FIRST 101
#  define WNT351    101
#  define WNT4      102
#  define W2K       103
#  define WXP       104
#  define WNT_LAST  199

#  define WCEFIRST  201
#  define WCE       201
#  define WCELAST   299

#  ifndef VER_PLATFORM_WIN32_WINDOWS
#    define VER_PLATFORM_WIN32_WINDOWS   1
#  endif
#  ifndef VER_PLATFORM_WIN32_NT
#    define VER_PLATFORM_WIN32_NT        2
#  endif
#  ifndef VER_PLATFORM_WIN32_CE
#    define VER_PLATFORM_WIN32_CE        3
#  endif

#include <windows.h>
#include <winreg.h>

#define WINUUID_PART_COUNT 4

static char hexCharacters[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

/* Replies the windows version */
BOOL getWindowsVersion(DWORD *version) {
  OSVERSIONINFO osinfo;
  osinfo.dwOSVersionInfoSize = sizeof(OSVERSIONINFO);
 
  if (!GetVersionEx(&osinfo))
    return FALSE;

  DWORD platform_id   = osinfo.dwPlatformId;
  DWORD minor_version = osinfo.dwMinorVersion;
  DWORD major_version = osinfo.dwMajorVersion;
  DWORD build_number  = osinfo.dwBuildNumber & 0xFFFF;   // Win 95 needs this

  if ((platform_id == VER_PLATFORM_WIN32_WINDOWS) && (major_version == 4)) {
    if ((minor_version < 10) && (build_number == 950))
      *version = W95;
    else if ((minor_version < 10) &&
             ((build_number > 950) && (build_number <= 1080)))
      *version = W95SP1;
    else if ((minor_version < 10) && (build_number > 1080))
      *version = W95OSR2;
    else if ((minor_version == 10) && (build_number == 1998))
      *version = W98;
    else if ((minor_version == 10) &&
             ((build_number > 1998) && (build_number < 2183)))
      *version = W98SP1;
    else if ((minor_version == 10) && (build_number >= 2183))
      *version = W98SE;
    else if (minor_version == 90)
      *version = WME;
  }
  else if (platform_id == VER_PLATFORM_WIN32_NT) {
    if ((major_version == 3) && (minor_version == 51))
      *version = WNT351;
    else if ((major_version == 4) && (minor_version == 0))
      *version = WNT4;
    else if ((major_version == 5) && (minor_version == 0))
      *version = W2K;
    else if ((major_version == 5) && (minor_version == 1))
      *version = WXP;
  }
  else if (platform_id == VER_PLATFORM_WIN32_CE) {
      *version = WCE;
  }
 
  return TRUE;
} 

/* Read the value of a registrery value */
BOOL readRegistryI(const CHAR* key, const CHAR* valueName, BYTE** data, DWORD* size, BYTE allocationFactor) {
	HKEY hKey = NULL;  // registry handle, kept open between calls
	LONG ret;

	ret = RegOpenKeyEx(HKEY_LOCAL_MACHINE, key, 0, KEY_ALL_ACCESS /*KEY_QUERY_VALUE*/, &hKey);
	if (ret != ERROR_SUCCESS) return FALSE;

	// Get the size of the value
	DWORD valueSize=0;
	ret = RegQueryValueEx(
		hKey,
		valueName,
		NULL, //reserved
		NULL, //type
		NULL, //data
		&valueSize);
  	if (ret != ERROR_SUCCESS) return FALSE;
	if (size!=NULL) {
		*size = valueSize;
	}
	// Read the value data
  	BYTE* valueData;

	if (data!=NULL) {
		DWORD valueType;
		valueData = (BYTE*)malloc(sizeof(BYTE)*valueSize*allocationFactor);
	  	ret = RegQueryValueEx(
	  			hKey,
	  			valueName,
	  			NULL, //reserved
	  			&valueType, //type
	  			valueData,
	  			&valueSize);
		RegCloseKey(hKey);
		if (ret != ERROR_SUCCESS) {
			free(valueData);
			return FALSE;
		}
		*data = valueData;
	}
	else {
		RegCloseKey(hKey);
	}

	return TRUE;
}

/* Read the value of a registrery value */
BOOL readRegistry(const CHAR* key, const CHAR* valueName, BYTE** data, DWORD* size) {
	return readRegistryI(key,valueName,data,size,1);
}

/* Replies the serial number of the system */
BOOL getWindowsSerialI(BYTE** serial, DWORD* serialSize, BYTE allocationFactor) {
	DWORD version;
  	getWindowsVersion(&version);

	CHAR* reg_path;  	
	if ((version >=WNT_FIRST) && (version <= WNT_LAST))
		reg_path = (CHAR*)("Software\\Microsoft\\Windows NT\\CurrentVersion");
	else
	    reg_path = (CHAR*)("Software\\Microsoft\\Windows\\CurrentVersion");
  	
  	DWORD size = 0;
  	BYTE* data = NULL;
  	if (!readRegistryI(reg_path, "ProductId", &data, &size, allocationFactor)) {
  		if ((version >=WNT_FIRST) && (version <= WNT_LAST)) {
	    	reg_path = (CHAR*)("Software\\Microsoft\\Windows\\CurrentVersion");
	    	if (!readRegistryI(reg_path, "ProductId", &data, &size, allocationFactor))
	    		return FALSE;
  		}
  		else return FALSE;
  	}
  	
  	if (serial!=NULL) {
  		*serial = data;
  	}
  	else {
  		free(data);
  	}
  	
  	if (serialSize!=NULL) {
  		*serialSize = size;
  	}  	
  	
  	return TRUE;
}

/* Replies the serial number of the system */
BOOL getWindowsSerial(BYTE** serial, DWORD* serialSize) {
	return getWindowsSerialI(serial, serialSize, 1);
}

/* Replies the serial number of the system */
char* getOSSerial(int enableSuperUser, int enableGUI) {
  	DWORD size = 0;
  	BYTE* data = NULL;
  	if (getWindowsSerial(&data, &size)) {
		unsigned long i;
	  	char* serial = (char*)malloc(sizeof(char)*size);
	  	for(i=0; i<size; i++) {
	  		serial[i] = data[i];
	  	}
	  	free(data);
	  	trim(&serial);
	  	return serial;
  	}
  	return NULL;
}

/* Replies the UUID of the system */
char* getOSUUID(int enableSuperUser, int enableGUI) {
	DWORD size;
	BYTE* data;
  	if (getWindowsSerial(&data, &size)) {
  		unsigned long i, j, k, totalSize = 2*(size-1);
	  	char* serial = (char*)malloc(sizeof(char)*(totalSize+(totalSize/WINUUID_PART_COUNT)+2));
  		char characterToTreat;
  		short b0, b1;
  		BOOL lastIsSeparator = FALSE;
	  	
	  	for(i=0, j=0, k=WINUUID_PART_COUNT; i<size; i++) {
	  		characterToTreat = data[i];
	  		if (isalnum(characterToTreat)) {
		  		b0 = (characterToTreat & 0x0F) ^ 0x0F;
		  		b1 = ((characterToTreat & 0xF0) >> 4) ^ 0x0F;
				serial[j++] = hexCharacters[b0];
				serial[j++] = hexCharacters[b1];
				k --;
				if (k<=0) {
					serial[j++] = '-';
					k = WINUUID_PART_COUNT;
					lastIsSeparator = TRUE;
				}
				else {
					lastIsSeparator = FALSE;
				}
	  		}
	  	}
	  	if (lastIsSeparator) j--;
	  	serial[j] = '\0';
	  	free(data);
	  	
	  	trim(&serial);
	  	
	  	return serial;
  	}
  	return NULL;
}
