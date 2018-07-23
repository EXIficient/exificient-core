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

package com.siemens.ct.exi.core.grammars;

import com.siemens.ct.exi.core.context.GrammarContext;
import com.siemens.ct.exi.core.grammars.grammar.Grammar;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public abstract class AbstractGrammars implements Grammars {

	/*
	 * Document and Fragment Grammars
	 */
	protected Grammar documentGrammar;
	protected Grammar fragmentGrammar;

	private final GrammarContext grammarContext;

	private final boolean isSchemaInformed;

	public AbstractGrammars(boolean isSchemaInformed,
			GrammarContext grammarContext) {
		this.isSchemaInformed = isSchemaInformed;
		this.grammarContext = grammarContext;
	}

	public GrammarContext getGrammarContext() {
		return this.grammarContext;
	}

	public boolean isSchemaInformed() {
		return isSchemaInformed;
	}

	public Grammar getDocumentGrammar() {
		return documentGrammar;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AbstractGrammars))
			return false;

		AbstractGrammars that = (AbstractGrammars) o;

		if (isSchemaInformed != that.isSchemaInformed)
			return false;
		if (documentGrammar != null ? !documentGrammar
				.equals(that.documentGrammar) : that.documentGrammar != null)
			return false;
		if (fragmentGrammar != null ? !fragmentGrammar
				.equals(that.fragmentGrammar) : that.fragmentGrammar != null)
			return false;
		return grammarContext != null ? grammarContext
				.equals(that.grammarContext) : that.grammarContext == null;
	}
}
