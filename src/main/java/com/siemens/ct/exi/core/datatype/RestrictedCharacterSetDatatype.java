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
import com.siemens.ct.exi.core.datatype.charset.RestrictedCharacterSet;
import com.siemens.ct.exi.core.types.BuiltInType;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0-SNAPSHOT
 */

public class RestrictedCharacterSetDatatype extends AbstractDatatype {
	
	protected RestrictedCharacterSet rcs;

	public RestrictedCharacterSetDatatype(RestrictedCharacterSet rcs,
			QNameContext schemaType) {
		// default whiteSpace facet for string is preserve
		this(rcs, schemaType, WhiteSpace.preserve);
	}
	
	public RestrictedCharacterSetDatatype(RestrictedCharacterSet rcs,
			QNameContext schemaType, WhiteSpace whiteSpace) {
		super(BuiltInType.RCS_STRING, schemaType);
		this.rcs = rcs;
		this.whiteSpace = whiteSpace;
	}
	
	public RestrictedCharacterSet getRestrictedCharacterSet() {
		return this.rcs;
	}
	
	public DatatypeID getDatatypeID() {
		return DatatypeID.exi_string;
	}
	
	@Override
	public boolean equals(Object o) {
		if(super.equals(o) && o instanceof RestrictedCharacterSetDatatype ) {
			RestrictedCharacterSetDatatype r = (RestrictedCharacterSetDatatype) o;
			return (this.rcs.equals(r.rcs));
		}
		return false;
	}

}