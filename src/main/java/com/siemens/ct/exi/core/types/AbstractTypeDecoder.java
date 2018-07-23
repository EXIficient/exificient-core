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

import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.Datatype;
import com.siemens.ct.exi.core.datatype.RestrictedCharacterSetDatatype;
import com.siemens.ct.exi.core.datatype.charset.RestrictedCharacterSet;
import com.siemens.ct.exi.core.datatype.strings.StringCoder;
import com.siemens.ct.exi.core.datatype.strings.StringDecoder;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.DecoderChannel;
import com.siemens.ct.exi.core.values.StringValue;
import com.siemens.ct.exi.core.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.1
 */

public abstract class AbstractTypeDecoder extends AbstractTypeCoder implements
		TypeDecoder {

	public AbstractTypeDecoder() throws EXIException {
		this(null, null, null);
	}

	public AbstractTypeDecoder(QName[] dtrMapTypes,
			QName[] dtrMapRepresentations,
			Map<QName, Datatype> dtrMapRepresentationsDatatype)
			throws EXIException {
		super(dtrMapTypes, dtrMapRepresentations, dtrMapRepresentationsDatatype);
	}

	protected Value readRCSValue(RestrictedCharacterSetDatatype rcsDT, QNameContext qnContext, DecoderChannel valueChannel,
			StringDecoder stringDecoder) throws IOException {

//		RestrictedCharacterSetDatatype rcsDT = (RestrictedCharacterSetDatatype) datatype;
		RestrictedCharacterSet rcs = rcsDT.getRestrictedCharacterSet();
		
		
		StringValue value;

		int i = valueChannel.decodeUnsignedInteger();

		if (i == 0) {
			// local value partition
			value = stringDecoder.readValueLocalHit(qnContext, valueChannel);
		} else if (i == 1) {
			// found in global value partition
			value = stringDecoder.readValueGlobalHit(valueChannel);
		} else {
			// not found in global value (and local value) partition
			// ==> restricted character string literal is encoded as a String
			// with the length incremented by two.
			int L = i - 2;

			/*
			 * If length L is greater than zero the string S is added
			 */
			if (L > 0) {
				// number of bits
				int numberOfBits = rcs.getCodingLength();
				int size = rcs.size();

				char[] cValue = new char[L];
				value = new StringValue(cValue);

				for (int k = 0; k < L; k++) {
					int code = valueChannel
							.decodeNBitUnsignedInteger(numberOfBits);
					int codePoint;
					if (code == size) {
						// deviation
						codePoint = valueChannel.decodeUnsignedInteger();
					} else {
						codePoint = rcs.getCodePoint(code);
					}

					Character.toChars(codePoint, cValue, k);
				}

				// After encoding the string value, it is added to both the
				// associated "local" value string table partition and the
				// global
				// value string table partition.
				stringDecoder.addValue(qnContext, value);
			} else {
				value = StringCoder.EMPTY_STRING_VALUE;
			}
		}

		return value;
	}

}
