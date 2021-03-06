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

package com.siemens.ct.exi.core.grammars.grammar;

import com.siemens.ct.exi.core.grammars.event.EndDocument;
import com.siemens.ct.exi.core.grammars.event.StartElement;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

/*
 * <Built-in Fragment Grammar>
 * 
 * FragmentContent : SE () FragmentContent 0 ED 1 CM FragmentContent 2.0 PI
 * FragmentContent 2.1
 */

public class BuiltInFragmentContent extends AbstractBuiltInGrammar {

	/*
	 * FragmentContent : SE (*) FragmentContent 0 ED 1 CM FragmentContent 2.0 PI
	 * FragmentContent 2.1
	 */
	public BuiltInFragmentContent() {
		super();
		// Note: Add in different order in schema-less grammars
		// EE
		addTerminalProduction(new EndDocument());
		// SE(*) --> FragmentContent
		addProduction(START_ELEMENT_GENERIC, this);
	}

	public GrammarType getGrammarType() {
		return GrammarType.BUILT_IN_FRAGMENT_CONTENT;
	}

	@Override
	// public void learnStartElement(String uri, String localName) {
	public void learnStartElement(StartElement se) {
		// a learned rule is added to the front, technically
		// it is added to the tail
		// StartElement se = new StartElement(uri, localName);
		if (!this.contains(se)) {
			// eventRules.add ( new EventRule ( event, this ) );
			this.addProduction(se, this);
		}
	}

}
