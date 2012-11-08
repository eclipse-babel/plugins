/*******************************************************************************
 * Copyright (c) 2012 Matthias Lettmayer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matthias Lettmayer - created interface to select a key in an editor (fixed issue 59)
 ******************************************************************************/
package org.eclipse.babel.editor;

public interface IMessagesEditor {
	String getSelectedKey();

	void setSelectedKey(String key);
}