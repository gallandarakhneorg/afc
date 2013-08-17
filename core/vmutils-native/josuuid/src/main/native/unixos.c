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
#include <unistd.h>

#include "osmacro.h"
#include "utils.h"
 
#ifndef __UNIX__
# error You may use Unix compiler
#endif

#define DWORD unsigned long int
#define PATH_SEPARATOR ':' 
#define FILE_SEPARATOR '/'

/* Replies if the given command is in the PATH variable */
static int whichCommand(const char* cmd) {
	char* pathVariable = getenv("PATH");
	if (pathVariable!=NULL) {
		FILE* file;
		int i;
		char* first;
		char* filename;
		int count, count2;
#ifdef DEBUG
		printf("PATH=%s\n", pathVariable);
#endif
		count = strlen(cmd);
		i = 0;
		first = pathVariable;
		filename = NULL;
		while (pathVariable[i]!='\0') {
			if (pathVariable[i]==PATH_SEPARATOR) {
				pathVariable[i] = '\0';
				count2 = strlen(first);
#ifdef DEBUG
				printf("PATH[%d]=%s\n", i, first);
#endif
				filename = (char*)realloc(filename, sizeof(char)*(count+count2+2));
				strncpy(filename, first, count2);
				pathVariable[i] = PATH_SEPARATOR;
				filename[count2] = FILE_SEPARATOR;
				strncpy(filename+count2+1, cmd, count);
				filename[count+count2+1] = '\0';
#ifdef DEBUG
				printf("TEST: %s\n", filename);
#endif
				file = fopen(filename, "rb");
				if (file!=NULL) {
					fclose(file);
#ifdef DEBUG
					printf("EXECUTABLE: %s\n", filename);
#endif
					if (filename!=NULL) {
						free(filename);
					}
					return 1;
				}
				first = pathVariable + i + 1;
			}
			++i;
		}
		if (filename!=NULL) {
			free(filename);
		}
#ifdef DEBUG
		printf("PATH=%s\n", pathVariable);
#endif
	}
#ifdef DEBUG
	else {
		printf("PATH=\n");
	}
#endif		
	return 0;
}

/* Run the specified shell command and replies its standard output */
static char* runCommand(const char* cmd) {
	FILE* cmdOutput;
	char* result = NULL;
	
	cmdOutput = popen(cmd, "r");
	
	if (cmdOutput!=NULL) {
		char buffer[128];
		unsigned long i,j, count = 0;
		unsigned long charCount;
		
		charCount = fread(buffer, sizeof(char), 128, cmdOutput);
		while (charCount>0) {
			result = (char*)realloc(result,sizeof(char)*(count+charCount+1));
			for(i=0, j=count; i<charCount; i++, j++) {
				result[j] = buffer[i];
			} 
			count += charCount;
			result[count] = '\0';
			charCount = fread(buffer, sizeof(char), 128, cmdOutput);
		}
	
		pclose(cmdOutput);
	}
	
    return result;
}

void iddle() {
	whichCommand("");
	runCommand("");
}

/* Replies the serial number of the system */
char* getOSSerial(int enableSuperUser, int enableGUI) {
	char* result = NULL;
	if (whichCommand("udevadm")) {
		result = runCommand("udevadm info -q property -n /dev/sda|grep ID_SERIAL_SHORT=|cut -d= -f2 2>/dev/null");
		if (result!=NULL) {
			trim(&result);
		}
	}
	if (result==NULL && whichCommand("udevadm")) {
		result = runCommand("udevadm info -q property -n /dev/hda|grep ID_SERIAL_SHORT=|cut -d= -f2 2>/dev/null");
		if (result!=NULL) {
			trim(&result);
		}
	}
	if (result==NULL && whichCommand("hal-get-property")) {
		result = runCommand("hal-get-property --udi /org/freedesktop/Hal/devices/computer --key system.hardware.serial 2>/dev/null");
		if (result!=NULL) {
			trim(&result);
		}
		else {
			result = runCommand("hal-get-property --udi /org/freedesktop/Hal/devices/computer --key smbios.system.serial 2>/dev/null");
			if (result!=NULL) {
				trim(&result);
			}
		}
		return result;
	}
	/* no way to obtain the serial number */
	return result;
}

/* Replies the UUID of the system */
char* getOSUUID(int enableSuperUser, int enableGUI) {
	char* result = NULL;
	if (whichCommand("udevadm")) {
		result = runCommand("udevadm info -q property -n /dev/sda|grep ID_SERIAL=|cut -d= -f2 2>/dev/null");
		if (result!=NULL) {
			trim(&result);
		}
	}
	if (result==NULL && whichCommand("udevadm")) {
		result = runCommand("udevadm info -q property -n /dev/hda|grep ID_SERIAL=|cut -d= -f2 2>/dev/null");
		if (result!=NULL) {
			trim(&result);
		}
	}
	if (result==NULL && whichCommand("hal-get-property")) {
		result = runCommand("hal-get-property --udi /org/freedesktop/Hal/devices/computer --key system.hardware.uuid 2>/dev/null");
		if (result!=NULL) {
			trim(&result);
		}
		else {
			result = runCommand("hal-get-property --udi /org/freedesktop/Hal/devices/computer --key smbios.system.uuid 2>/dev/null");
			if (result!=NULL) {
				trim(&result);
			}
		}
	}
	/* no way to obtain the UUID */
	return result;
}
