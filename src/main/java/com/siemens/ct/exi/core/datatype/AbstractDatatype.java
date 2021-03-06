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

package com.siemens.ct.exi.core.datatype;

import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.types.BuiltInType;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public abstract class AbstractDatatype implements Datatype {

	// default built-in datatype (no dtr map used)
	protected final BuiltInType builtInType;

	// for codec map
	protected final QNameContext schemaType;

	// base datatype
	protected Datatype baseDatatype;

	// grammar enumeration
	protected EnumDatatype grammarEnumeration;

	// whiteSpace
	protected WhiteSpace whiteSpace;

	public AbstractDatatype() {
		this(null, null);
	}

	public AbstractDatatype(BuiltInType builtInType, QNameContext schemaType) {
		this.builtInType = builtInType;
		this.schemaType = schemaType;
		// For all atomic datatypes other than string the value of whiteSpace is
		// collapse
		whiteSpace = WhiteSpace.collapse;
	}

	public BuiltInType getBuiltInType() {
		return builtInType;
	}

	public QNameContext getSchemaType() {
		return schemaType;
	}

	public Datatype getBaseDatatype() {
		return baseDatatype;
	}

	public void setBaseDatatype(Datatype baseDatatype) {
		this.baseDatatype = baseDatatype;
	}

	public void setGrammarEnumeration(EnumDatatype grammarEnumeration) {
		this.grammarEnumeration = grammarEnumeration;
	}

	public EnumDatatype getGrammarEnumeration() {
		return grammarEnumeration;
	}

	public WhiteSpace getWhiteSpace() {
		return this.whiteSpace;
	}

	public boolean equals(Object o) {
		if (o instanceof Datatype) {
			if (builtInType == ((Datatype) o).getBuiltInType()) {
				if (schemaType == null) {
					return (((Datatype) o).getSchemaType() == null);
				} else {
					return (schemaType.equals(((Datatype) o).getSchemaType()));
				}
			}
		}
		return false;
	}

	public int hashCode() {
		return builtInType.ordinal();
	}

	public String toString() {
		return builtInType.toString();
	}

}
