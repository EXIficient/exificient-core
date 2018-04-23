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

package com.siemens.ct.exi.core.grammars.production;

import com.siemens.ct.exi.core.grammars.event.Event;
import com.siemens.ct.exi.core.grammars.grammar.Grammar;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0
 */

public abstract class AbstractProduction implements Production {

	protected final Grammar next;
	protected final int eventCode;
	protected final Event event;

	public AbstractProduction(Grammar next, Event event, int eventCode) {
		this.next = next;
		this.event = event;
		this.eventCode = eventCode;
	}

	abstract public int getEventCode();
	
	public Event getEvent() {
		return event;
	}
	
	public Grammar getNextGrammar() {
		return next;
	}
	

	@Override
	public String toString() {
		return "[" + eventCode + "] " + event + " -> " + next;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractProduction)) return false;

		AbstractProduction that = (AbstractProduction) o;

		if (eventCode != that.eventCode) return false;
		//we do not test the equality of the next Grammar here has it would make recursive calls
		return event != null ? event.equals(that.event) : that.event == null;
	}
}
