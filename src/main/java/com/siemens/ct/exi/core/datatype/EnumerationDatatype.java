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
import com.siemens.ct.exi.core.util.MethodsBag;
import com.siemens.ct.exi.core.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public class EnumerationDatatype extends AbstractDatatype implements
		EnumDatatype {

	protected Datatype dtEnumValues;

	protected int codingLength;
	protected Value[] enumValues;

	public EnumerationDatatype(Value[] enumValues, Datatype dtEnumValues,
			QNameContext schemaType) {
		super(BuiltInType.ENUMERATION, schemaType);

		if (dtEnumValues.getBuiltInType() != BuiltInType.QNAME
				&& dtEnumValues.getBuiltInType() != BuiltInType.ENUMERATION) {
			this.dtEnumValues = dtEnumValues;
			this.enumValues = enumValues;
			this.codingLength = MethodsBag.getCodingLength(enumValues.length);
		} else {
			throw new RuntimeException(
					"Enumeration type values can't be of type Enumeration or QName");
		}
	}

	public Datatype getEnumValueDatatype() {
		return dtEnumValues;
	}

	public DatatypeID getDatatypeID() {
		return dtEnumValues.getDatatypeID();
	}

	public int getEnumerationSize() {
		return enumValues.length;
	}

	public int getCodingLength() {
		return codingLength;
	}

	public Value getEnumValue(int i) {
		assert (i >= 0 && i < enumValues.length);
		return enumValues[i];
	}

	@Override
	public boolean equals(Object o) {
		if (super.equals(o) && o instanceof EnumerationDatatype) {
			EnumerationDatatype e = (EnumerationDatatype) o;
			if (this.dtEnumValues.equals(e.dtEnumValues)
					&& this.enumValues.length == e.enumValues.length) {
				for (int i = 0; i < this.enumValues.length; i++) {
					if (!this.enumValues[i].equals(e.enumValues[i])) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}