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

package com.siemens.ct.exi.core.types;

import java.io.IOException;
import java.util.Map;

import javax.xml.namespace.QName;

import com.siemens.ct.exi.core.Constants;
import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.Datatype;
import com.siemens.ct.exi.core.datatype.RestrictedCharacterSetDatatype;
import com.siemens.ct.exi.core.datatype.charset.RestrictedCharacterSet;
import com.siemens.ct.exi.core.datatype.strings.StringEncoder;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0
 */

public abstract class AbstractTypeEncoder extends AbstractTypeCoder implements
		TypeEncoder {

	public AbstractTypeEncoder() throws EXIException {
		this(null, null, null);
	}

	public AbstractTypeEncoder(QName[] dtrMapTypes,
			QName[] dtrMapRepresentations, Map<QName, Datatype> dtrMapRepresentationsDatatype) throws EXIException {
		super(dtrMapTypes, dtrMapRepresentations, dtrMapRepresentationsDatatype);
	}
	
	
	protected void writeRCSValue(RestrictedCharacterSetDatatype rcsDT, QNameContext qnContext, EncoderChannel valueChannel,
			StringEncoder stringEncoder, String lastValidValue) throws IOException {
		if (stringEncoder.isStringHit(lastValidValue)) {
			stringEncoder.writeValue(qnContext, valueChannel, lastValidValue);
		} else {
			// NO local or global value hit
			// string-table miss ==> restricted character
			// string literal is encoded as a String with the length
			// incremented by two.
			final int L = lastValidValue.length();

			valueChannel.encodeUnsignedInteger(L + 2);

			RestrictedCharacterSet rcs = rcsDT.getRestrictedCharacterSet();
			
			/*
			 * If length L is greater than zero the string S is added
			 */
			if (L > 0) {
				// number of bits
				int numberOfBits = rcs.getCodingLength();

				for (int i = 0; i < L; i++) {
					int codePoint = lastValidValue.codePointAt(i);
					int code = rcs.getCode(codePoint);
					if (code == Constants.NOT_FOUND) {
						// indicate deviation
						valueChannel.encodeNBitUnsignedInteger(rcs.size(),
								numberOfBits);

						valueChannel.encodeUnsignedInteger(codePoint);
					} else {
						valueChannel.encodeNBitUnsignedInteger(code,
								numberOfBits);
					}
				}

				// After encoding the string value, it is added to both the
				// associated "local" value string table partition and the
				// global value string table partition.
				stringEncoder.addValue(qnContext, lastValidValue);
			}
		}
	}

}
