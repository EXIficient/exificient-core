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

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public class GrammarContext {

	protected final GrammarUriContext[] grammarUriContexts;
	protected final int numberofQNamesContexts;

	public GrammarContext(GrammarUriContext[] grammarUriContexts,
			int numberofQNamesContexts) {
		this.grammarUriContexts = grammarUriContexts;
		this.numberofQNamesContexts = numberofQNamesContexts;
	}

	public int getNumberOfGrammarUriContexts() {
		return grammarUriContexts.length;
	}

	public GrammarUriContext getGrammarUriContext(int id) {
		return grammarUriContexts[id];
	}

	public GrammarUriContext getGrammarUriContext(String namespaceUri) {
		for (int i = 0; i < grammarUriContexts.length; i++) {
			GrammarUriContext uc = grammarUriContexts[i];
			if (uc.namespaceUri.equals(namespaceUri)) {
				return uc;
			}
		}
		return null;
	}

	public int getNumberOfGrammarQNameContexts() {
		return numberofQNamesContexts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GrammarContext))
			return false;

		GrammarContext that = (GrammarContext) o;

		if (numberofQNamesContexts != that.numberofQNamesContexts)
			return false;
		return Arrays.equals(grammarUriContexts, that.grammarUriContexts);
	}
}
