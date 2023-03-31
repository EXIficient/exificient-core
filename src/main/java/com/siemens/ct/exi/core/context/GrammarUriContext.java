/*
 * Copyright (c) 2007-2018 Siemens AG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */

package com.siemens.ct.exi.core.context;

import java.util.Arrays;
import java.util.Objects;

import com.siemens.ct.exi.core.Constants;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public class GrammarUriContext extends AbstractUriContext {

	public static String[] EMPTY_PREFIXES = new String[0];

	// grammar local-names
	final QNameContext[] grammarQNames;

	// grammar prefixes
	final String[] grammarPrefixes;

	// default prefix
	final String defaultPrefix;

	public GrammarUriContext(int namespaceUriID, String namespaceUri,
			QNameContext[] grammarQNames, String[] grammarPrefixes) {
		super(namespaceUriID, namespaceUri);
		this.grammarQNames = grammarQNames;
		this.grammarPrefixes = grammarPrefixes;

		switch (namespaceUriID) {
		case 0:
			// "" [empty string]
			this.defaultPrefix = "";
			break;
		case 1:
			this.defaultPrefix = "xml";
			break;
		case 2:
			this.defaultPrefix = "xsi";
			break;
		default:
			this.defaultPrefix = "ns" + namespaceUriID;
		}
	}

	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	public GrammarUriContext(int namespaceUriID, String namespaceUri,
			QNameContext[] grammarQNames) {
		this(namespaceUriID, namespaceUri, grammarQNames, EMPTY_PREFIXES);
	}

	public int getNumberOfQNames() {
		return grammarQNames.length;
	}

	public QNameContext getQNameContext(int localNameID) {
		if (localNameID < grammarQNames.length) {
			return grammarQNames[localNameID];
		}
		return null;
	}

	public QNameContext getQNameContext(String localName) {
		assert (localName != null);
		return binarySearch(grammarQNames, localName);
	}

	protected static QNameContext binarySearch(QNameContext[] grammarQNames,
			String localName) {
		Objects.requireNonNull(grammarQNames, "grammarQNames parameter cannot be null");

		int low = 0;
		int high = grammarQNames.length - 1;

		while (low <= high) {
			int mid = (low + high) >> 1;
			QNameContext midVal = grammarQNames[mid];
			int cmp = midVal.compareTo(localName);

			if (cmp < 0) {
				low = mid + 1;
			} else if (cmp > 0) {
				high = mid - 1;
			} else {
				// return mid; // key found
				return midVal; // key found
			}
		}

		// return -(low + 1); // key not found.
		return null; // key not found.
	}

	public int getNumberOfPrefixes() {
		return grammarPrefixes.length;
	}

	public String getPrefix(int prefixID) {
		assert (prefixID >= 0);
		String pfx = null;
		if (prefixID < grammarPrefixes.length) {
			pfx = grammarPrefixes[prefixID];
		}
		return pfx;
	}

	public int getPrefixID(String prefix) {
		for (int i = 0; i < grammarPrefixes.length; i++) {
			String pfx = grammarPrefixes[i];
			if (pfx.equals(prefix)) {
				return i;
			}
		}
		return Constants.NOT_FOUND;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GrammarUriContext))
			return false;
		if (!super.equals(o))
			return false;

		GrammarUriContext that = (GrammarUriContext) o;
		if (!super.equals(that))
			return false;
		if (!Arrays.equals(grammarQNames, that.grammarQNames))
			return false;
		if (!Arrays.equals(grammarPrefixes, that.grammarPrefixes))
			return false;
		return defaultPrefix != null ? defaultPrefix.equals(that.defaultPrefix)
				: that.defaultPrefix == null;
	}

}
