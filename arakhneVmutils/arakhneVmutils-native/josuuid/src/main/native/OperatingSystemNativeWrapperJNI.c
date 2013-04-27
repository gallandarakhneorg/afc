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

#include <jni.h>
#include <stdlib.h>

#include "org_arakhne_vmutil_OperatingSystemNativeWrapper.h"

/* Replies the serial number of the system */
char* getOSSerial(int enableSuperUser, int enableGUI);

/* Replies the UUID of the system */
char* getOSUUID(int enableSuperUser, int enableGUI);

/*
 * Class:     org_arakhne_vmutil_OperatingSystemNativeWrapper
 * Method:    getOSSerialNumber
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_arakhne_vmutil_OperatingSystemNativeWrapper_getOSSerialNumber
(JNIEnv * env, jobject instance, jboolean enableSuperUser,
 jboolean enableGUI) {
	jstring jSerial = NULL;
	char* cSerial = getOSSerial(
						enableSuperUser==JNI_TRUE,
						enableGUI==JNI_TRUE);
	if (cSerial!=NULL) {
		jSerial = (*env)->NewStringUTF (env, cSerial);
		free(cSerial);
	}
	return jSerial;
}


/*
 * Class:     org_arakhne_vmutil_OperatingSystemNativeWrapper
 * Method:    getOSUUID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_arakhne_vmutil_OperatingSystemNativeWrapper_getOSUUID
(JNIEnv *env, jobject instance, jboolean enableSuperUser,
 jboolean enableGUI) {
	jstring jUUID = NULL;
	char* cUUID = getOSUUID(
						enableSuperUser==JNI_TRUE,
						enableGUI==JNI_TRUE);
	if (cUUID!=NULL) {
		jUUID = (*env)->NewStringUTF (env, cUUID);
		free(cUUID);
	}
	return jUUID;
}
