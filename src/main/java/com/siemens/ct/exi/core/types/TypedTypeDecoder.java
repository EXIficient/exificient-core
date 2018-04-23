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
import com.siemens.ct.exi.core.datatype.DatetimeDatatype;
import com.siemens.ct.exi.core.datatype.EnumDatatype;
import com.siemens.ct.exi.core.datatype.EnumerationDatatype;
import com.siemens.ct.exi.core.datatype.ExtendedStringDatatype;
import com.siemens.ct.exi.core.datatype.ListDatatype;
import com.siemens.ct.exi.core.datatype.NBitUnsignedIntegerDatatype;
import com.siemens.ct.exi.core.datatype.RestrictedCharacterSetDatatype;
import com.siemens.ct.exi.core.datatype.strings.StringCoder;
import com.siemens.ct.exi.core.datatype.strings.StringDecoder;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.DecoderChannel;
import com.siemens.ct.exi.core.values.BinaryBase64Value;
import com.siemens.ct.exi.core.values.BinaryHexValue;
import com.siemens.ct.exi.core.values.BooleanValue;
import com.siemens.ct.exi.core.values.IntegerValue;
import com.siemens.ct.exi.core.values.ListValue;
import com.siemens.ct.exi.core.values.StringValue;
import com.siemens.ct.exi.core.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0
 */

public class TypedTypeDecoder extends AbstractTypeDecoder {

	public TypedTypeDecoder() throws EXIException {
		this(null, null, null);
	}

	public TypedTypeDecoder(QName[] dtrMapTypes, QName[] dtrMapRepresentations, Map<QName, Datatype> dtrMapRepresentationsDatatype)
			throws EXIException {
		super(dtrMapTypes, dtrMapRepresentations, dtrMapRepresentationsDatatype);
	}

	public Value readValue(Datatype datatype, QNameContext qnContext,
			DecoderChannel valueChannel, StringDecoder stringDecoder)
			throws IOException {
		if (this.dtrMapInUse) {
			datatype = this.getDtrDatatype(datatype);
		}
		
		switch(datatype.getBuiltInType()) {
		case BINARY_BASE64:
			return new BinaryBase64Value(valueChannel.decodeBinary());
		case BINARY_HEX:
			return new BinaryHexValue(valueChannel.decodeBinary());
		case BOOLEAN:
			return valueChannel.decodeBooleanValue();
		case BOOLEAN_FACET:
			int booleanID = valueChannel.decodeNBitUnsignedInteger(2);
			return BooleanValue.getBooleanValue(booleanID);
		case DECIMAL:
			return valueChannel.decodeDecimalValue();
		case FLOAT:
			return valueChannel.decodeFloatValue();
		case NBIT_UNSIGNED_INTEGER:
			NBitUnsignedIntegerDatatype nbitDT = (NBitUnsignedIntegerDatatype) datatype;
			IntegerValue iv = valueChannel.decodeNBitUnsignedIntegerValue(nbitDT.getNumberOfBits());
			return iv.add(nbitDT.getLowerBound());
		case UNSIGNED_INTEGER:
			return valueChannel.decodeUnsignedIntegerValue();
		case INTEGER:
			return valueChannel.decodeIntegerValue();
		case DATETIME:
			DatetimeDatatype dtDT = (DatetimeDatatype) datatype;
			return valueChannel.decodeDateTimeValue(dtDT.getDatetimeType());
		case STRING:
			return stringDecoder.readValue(qnContext, valueChannel);
		case RCS_STRING:
			RestrictedCharacterSetDatatype rcsDT = (RestrictedCharacterSetDatatype) datatype;
			return readRCSValue(rcsDT, qnContext, valueChannel, stringDecoder);
		case EXTENDED_STRING:
			ExtendedStringDatatype esDT = (ExtendedStringDatatype) datatype;
			return readExtendedString(esDT, qnContext, valueChannel, stringDecoder);
		case ENUMERATION:
			EnumerationDatatype enumDT = (EnumerationDatatype) datatype;
			int index = valueChannel.decodeNBitUnsignedInteger(enumDT.getCodingLength());
			assert (index >= 0 && index < enumDT.getEnumerationSize());
			return enumDT.getEnumValue(index);
		case LIST:
			ListDatatype lDT = (ListDatatype) datatype;
			Datatype listDatatype = lDT.getListDatatype();
			
			int len = valueChannel.decodeUnsignedInteger();
			Value[]  values = new Value[len];
			for (int l = 0; l < len; l++) {
				values[l] = readValue(listDatatype, qnContext, valueChannel,stringDecoder);
				// values[l] = listDatatype.readValue(qnContext, valueChannel,stringDecoder);
			}
			Value retVal = new ListValue(values, listDatatype);

			return retVal;
		case QNAME:
			/* not allowed datatype */
			throw new IOException("QName is not an allowed as EXI datatype");
		}
		
		return null;
		// return datatype.readValue(qnContext, valueChannel, stringDecoder);
	}
	
	
	protected StringValue readExtendedString(ExtendedStringDatatype esDT, QNameContext context, DecoderChannel valueChannel,
			StringDecoder stringDecoder) throws IOException {
		EnumDatatype grammarStrings = esDT.getGrammarStrings();
		
		StringValue value = null;

		int i = valueChannel.decodeUnsignedInteger();

		switch (i) {
		case 0:
			// local value partition
			if (stringDecoder.isLocalValuePartitions()) {
				value = stringDecoder.readValueLocalHit(context, valueChannel);
			} else {
				throw new IOException(
						"EXI stream contains local-value hit even though profile options indicate otherwise.");
			}
			break;
		case 1:
			// found in global value partition
			value = stringDecoder.readValueGlobalHit(valueChannel);
			break;
		case 2:
			// grammar string (enum)
			int index = valueChannel.decodeNBitUnsignedInteger(grammarStrings.getCodingLength());
			assert (index >= 0 && index < grammarStrings.getEnumerationSize());
			Value v = grammarStrings.getEnumValue(index);
			// Value v = grammarStrings.readValue(context, valueChannel, stringDecoder);
			
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
				stringDecoder.addValue(context, value);
			} else {
				value = StringCoder.EMPTY_STRING_VALUE;
			}
			break;
		}
		
		assert (value != null);
		return value;
	}
}
