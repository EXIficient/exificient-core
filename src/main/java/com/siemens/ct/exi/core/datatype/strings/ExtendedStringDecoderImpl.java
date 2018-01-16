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

package com.siemens.ct.exi.core.datatype.strings;

import java.io.IOException;
import java.util.List;

import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.EnumDatatype;
import com.siemens.ct.exi.core.io.channel.DecoderChannel;
import com.siemens.ct.exi.core.values.StringValue;
import com.siemens.ct.exi.core.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0-SNAPSHOT
 */

public class ExtendedStringDecoderImpl implements StringDecoder {

	final StringDecoderImpl stringDecoder;
	
	EnumDatatype grammarStrings;

	public ExtendedStringDecoderImpl(StringDecoderImpl stringDecoder) {
		this.stringDecoder = stringDecoder;
	}
	
	public void setGrammarStrings(EnumDatatype grammarStrings) {
		this.grammarStrings = grammarStrings;
	}

	public StringValue readValue(QNameContext context,
			DecoderChannel valueChannel) throws IOException {
		StringValue value = null;

		int i = valueChannel.decodeUnsignedInteger();

		switch (i) {
		case 0:
			// local value partition
			if (stringDecoder.localValuePartitions) {
				value = this.readValueLocalHit(context, valueChannel);
			} else {
				throw new IOException(
						"EXI stream contains local-value hit even though profile options indicate otherwise.");
			}
			break;
		case 1:
			// found in global value partition
			value = readValueGlobalHit(valueChannel);
			break;
		case 2:
			// grammar string 
			Value v = grammarStrings.readValue(context, valueChannel, stringDecoder);
			if(v instanceof StringValue) {
				value = (StringValue) v;	
			} else {
				value = new StringValue(v.toString());
			}
			break;
		case 3:
			// shared string
			throw new IOException("ExtendedString, no support for <shared string>");
			// break;
		case 4:
			// split string
			throw new IOException("ExtendedString, no support for <split string>");
			// break;
		case 5:
			// undefined 
			throw new IOException("ExtendedString, no support for <undefined>");
			// break;
		default:
			// not found in global value (and local value) partition
			// ==> string literal is encoded as a String with the length
			// incremented by 6.
			int L = i - 6;
			/*
			 * If length L is greater than zero the string S is added
			 */
			if (L > 0) {
				value = new StringValue(valueChannel.decodeStringOnly(L));
				// After encoding the string value, it is added to both the
				// associated "local" value string table partition and the
				// global
				// value string table partition.
				// addValue(context, value);
				this.addValue(context, value);
			} else {
				value = StringCoder.EMPTY_STRING_VALUE;
			}
			break;
		}
		
		assert (value != null);
		return value;
	}

	public StringValue readValueLocalHit(
			QNameContext qnc, DecoderChannel valueChannel)
			throws IOException {
		return stringDecoder.readValueLocalHit(qnc, valueChannel);
	}

	public final StringValue readValueGlobalHit(DecoderChannel valueChannel)
			throws IOException {
		return stringDecoder.readValueGlobalHit(valueChannel);
	}

	public void addValue(QNameContext qnc,
			StringValue value) {
		stringDecoder.addValue(qnc, value);
	}

	public void clear() {
		stringDecoder.clear();
	}
	
	public void setSharedStrings(List<String> sharedStrings) {
		stringDecoder.setSharedStrings(sharedStrings);
	}

	public int getNumberOfStringValues(QNameContext qnc) {
		return stringDecoder.getNumberOfStringValues(qnc);
	}


}
