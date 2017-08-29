/*
 * Copyright (c) 2007-2016 Siemens AG
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

package com.siemens.ct.exi.grammars;

import com.siemens.ct.exi.context.GrammarContext;
import com.siemens.ct.exi.grammars.grammar.Grammar;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.9.7-SNAPSHOT
 */

public abstract class AbstractGrammars implements Grammars {
	
	/*
	 * Document and Fragment Grammars
	 */
	protected Grammar documentGrammar;
	protected Grammar fragmentGrammar;

	private GrammarContext grammarContext;

	private boolean isSchemaInformed;

	public AbstractGrammars() {
	}
	
	public AbstractGrammars(boolean isSchemaInformed,
			GrammarContext grammarContext) {
		setSchemaInformed(isSchemaInformed);
		setGrammarContext(grammarContext);
	}

	public GrammarContext getGrammarContext() {
		return this.grammarContext;
	}
	
	public void setGrammarContext(GrammarContext grammarContext) {
		this.grammarContext = grammarContext;
	}

	public boolean isSchemaInformed() {
		return isSchemaInformed;
	}
	
	public void setSchemaInformed(boolean isSchemaInformed) {
		this.isSchemaInformed = isSchemaInformed;
	}
	
	public Grammar getDocumentGrammar() {
		return documentGrammar;
	}
	
	public void setDocumentGrammar(Grammar documentGrammar) {
		this.documentGrammar = documentGrammar;
	}

	public Grammar getFragmentGrammar() {
		return fragmentGrammar;
	}
	
	public void setFragmentGrammar(Grammar fragmentGrammar) {
		this.fragmentGrammar = fragmentGrammar;
	}

}
