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
import com.siemens.ct.exi.core.datatype.DatetimeDatatype;
import com.siemens.ct.exi.core.datatype.EnumDatatype;
import com.siemens.ct.exi.core.datatype.EnumerationDatatype;
import com.siemens.ct.exi.core.datatype.ExtendedStringDatatype;
import com.siemens.ct.exi.core.datatype.ListDatatype;
import com.siemens.ct.exi.core.datatype.NBitUnsignedIntegerDatatype;
import com.siemens.ct.exi.core.datatype.RestrictedCharacterSetDatatype;
import com.siemens.ct.exi.core.datatype.strings.StringEncoder;
import com.siemens.ct.exi.core.datatype.strings.StringEncoderImpl.ValueContainer;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;
import com.siemens.ct.exi.core.util.MethodsBag;
import com.siemens.ct.exi.core.values.AbstractBinaryValue;
import com.siemens.ct.exi.core.values.BinaryBase64Value;
import com.siemens.ct.exi.core.values.BinaryHexValue;
import com.siemens.ct.exi.core.values.BooleanValue;
import com.siemens.ct.exi.core.values.DateTimeValue;
import com.siemens.ct.exi.core.values.DecimalValue;
import com.siemens.ct.exi.core.values.FloatValue;
import com.siemens.ct.exi.core.values.IntegerValue;
import com.siemens.ct.exi.core.values.ListValue;
import com.siemens.ct.exi.core.values.StringValue;
import com.siemens.ct.exi.core.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0-SNAPSHOT
 */

public class TypedTypeEncoder extends AbstractTypeEncoder {
	
	public Datatype lastDatatype;
	protected final boolean doNormalize;

	public TypedTypeEncoder() throws EXIException {
		this(false);
	}
	
	public TypedTypeEncoder(boolean doNormalize) throws EXIException {
		this(null, null, null);
	}

	public TypedTypeEncoder(QName[] dtrMapTypes, QName[] dtrMapRepresentations, Map<QName, Datatype> dtrMapRepresentationsDatatype) throws EXIException {
		this(dtrMapTypes, dtrMapRepresentations, dtrMapRepresentationsDatatype, false);
	}
	
	public TypedTypeEncoder(QName[] dtrMapTypes, QName[] dtrMapRepresentations, Map<QName, Datatype> dtrMapRepresentationsDatatype, boolean doNormalize) throws EXIException {
		super(dtrMapTypes, dtrMapRepresentations, dtrMapRepresentationsDatatype);
		this.doNormalize = doNormalize;
	}

	
	protected byte[] bytes;
	protected BooleanValue bool;
	protected int lastValidBooleanID;
	protected boolean lastValidBoolean;
	protected DecimalValue lastValidDecimal;
	protected FloatValue lastValidFloat;
	protected IntegerValue validValue;
	protected IntegerValue lastUnsignedInteger;
	protected IntegerValue lastInteger;
	protected DateTimeValue lastValidDatetime;
	protected String lastValue;
	protected int lastValidIndex;
	protected ListValue listValues;
	
	public boolean isValid(Datatype datatype, Value value) {
		if (this.dtrMapInUse && datatype.getBuiltInType() != BuiltInType.EXTENDED_STRING) {
			lastDatatype = this.getDtrDatatype(datatype);
		} else {
			lastDatatype = datatype;
		}

		switch(lastDatatype.getBuiltInType()) {
		case BINARY_BASE64:
		case BINARY_HEX:
			if (value instanceof AbstractBinaryValue) {
				bytes = ((AbstractBinaryValue) value).toBytes();
				return true;
			} else {
				return isValidString(value.toString());
			}
		case BOOLEAN:
			if (value instanceof BooleanValue) {
				bool = (BooleanValue) value;
				return true;
			} else {
				return isValidString(value.toString());
			}
		case BOOLEAN_FACET:
			if (value instanceof BooleanValue) {
				lastValidBoolean = ((BooleanValue) value).toBoolean();
				// TODO not fully correct
				lastValidBooleanID = lastValidBoolean ? 2 : 0;
				return true;
			} else {
				return isValidString(value.toString());
			}
		case DECIMAL:
			if (value instanceof DecimalValue) {
				lastValidDecimal = ((DecimalValue) value);
				return true;
			} else {
				return isValidString(value.toString());
			}
		case FLOAT:
			if (value instanceof FloatValue) {
				lastValidFloat = ((FloatValue) value);
				return true;
			} else {
				return isValidString(value.toString());
			}
		case NBIT_UNSIGNED_INTEGER:
			NBitUnsignedIntegerDatatype nbitDT = (NBitUnsignedIntegerDatatype) lastDatatype;
			if (value instanceof IntegerValue) {
				validValue = ((IntegerValue) value);
				return (validValue.compareTo(nbitDT.getLowerBound()) >= 0 && validValue
						.compareTo(nbitDT.getUpperBound()) <= 0);
				// return checkBounds();
			} else {
				return isValidString(value.toString());
			}
		case UNSIGNED_INTEGER:
			if (value instanceof IntegerValue) {
				lastUnsignedInteger = ((IntegerValue) value);
				return (lastUnsignedInteger.isPositive());
			} else {
				return isValidString(value.toString());
			}
		case INTEGER:
			if (value instanceof IntegerValue) {
				lastInteger = ((IntegerValue) value);
				return true;
			} else {
				return isValidString(value.toString());
			}
			
		case DATETIME:
			if (value instanceof DateTimeValue) {
				lastValidDatetime = ((DateTimeValue) value);
				return true;
			} else {
				return isValidString(value.toString());
			}
		case STRING:
			lastValue = value.toString();
			return true;
		case RCS_STRING:
			// Note: no validity check needed since any char-sequence can be encoded
			// due to fallback mechanism
			lastValue = value.toString();
			return true;
		case EXTENDED_STRING:
			lastValue = value.toString();
			return true;
		case ENUMERATION:
			EnumerationDatatype enumDT = (EnumerationDatatype) lastDatatype;
			int index = 0;
			while (index < enumDT.getEnumerationSize()) {
				if (enumDT.getEnumValue(index).equals(value)) {
					lastValidIndex = index;
					return true;
				}
				index++;
			}
			return false;
		case LIST:
			if (value instanceof ListValue) {
				ListDatatype listDT = (ListDatatype) lastDatatype;
				ListValue lv = (ListValue) value;
				if (listDT.getListDatatype().getBuiltInType() == lv.getListDatatype()
						.getBuiltInType()) {
					this.listValues = lv;
					return true;
				} else {
					listValues = null;
					return false;
				}
			} else {
				return isValidString(value.toString());
			}
		case QNAME:
			/* not allowed datatype */
			return false;
			// throw new IOException("QName is not an allowed as EXI datatype");
		}
		
		return false;
//		return lastDatatype.isValid(value);
	}
	
	
	
//	// Note: isValid MUST be called before and the method MUST return true
//	public void normalize() { // e.g., Canonical DateTime normalization
//		
//		
//	}
	
	protected boolean isValidString(String value) {
		switch(lastDatatype.getBuiltInType()) {
		case BINARY_BASE64:
			value = value.trim();
			BinaryBase64Value bvb = BinaryBase64Value.parse(value);
			if (bvb == null) {
				return false;
			} else {
				bytes = bvb.toBytes();
				return true;
			}
		case BINARY_HEX:
			value = value.trim();
			BinaryHexValue bvh = BinaryHexValue.parse(value);
			if (bvh == null) {
				return false;
			} else {
				bytes = bvh.toBytes();
				return true;
			}
		case BOOLEAN:
			bool = BooleanValue.parse(value);
			return (bool != null);
		case BOOLEAN_FACET:
			value = value.trim();
			boolean retValue = true;

			if (value.equals(Constants.XSD_BOOLEAN_FALSE)) {
				lastValidBooleanID = 0;
				lastValidBoolean = false;
			} else if (value.equals(Constants.XSD_BOOLEAN_0)) {
				lastValidBooleanID = 1;
				lastValidBoolean = false;
			} else if (value.equals(Constants.XSD_BOOLEAN_TRUE)) {
				lastValidBooleanID = 2;
				lastValidBoolean = true;
			} else if (value.equals(Constants.XSD_BOOLEAN_1)) {
				lastValidBooleanID = 3;
				lastValidBoolean = true;
			} else {
				retValue = false;
			}

			return retValue;
		case DECIMAL:
			lastValidDecimal = DecimalValue.parse(value);
			return (lastValidDecimal != null);
		case FLOAT:
			lastValidFloat = FloatValue.parse(value);
			return (lastValidFloat != null);
		case NBIT_UNSIGNED_INTEGER:
			validValue = IntegerValue.parse(value);
			if (validValue == null) {
				return false;
			} else {
				NBitUnsignedIntegerDatatype nbitDT = (NBitUnsignedIntegerDatatype) lastDatatype;
				return (validValue.compareTo(nbitDT.getLowerBound()) >= 0 && validValue
						.compareTo(nbitDT.getUpperBound()) <= 0);
				// return checkBounds();
			}
		case UNSIGNED_INTEGER:
			lastUnsignedInteger = IntegerValue.parse(value);
			if (lastUnsignedInteger != null) {
				return (lastUnsignedInteger.isPositive());
			} else {
				return false;
			}
		case INTEGER:
			lastInteger = IntegerValue.parse(value);
			return (lastInteger != null);
		case DATETIME:
			DatetimeDatatype datetimeDT = (DatetimeDatatype) lastDatatype;
			lastValidDatetime = DateTimeValue.parse(value, datetimeDT.getDatetimeType());
			return (lastValidDatetime != null);
		case LIST:
			ListDatatype listDT = (ListDatatype) lastDatatype;
			listValues = ListValue.parse(value, listDT.getListDatatype());
			return listValues != null;
		default:
			return false;
		}
	}
	
	
	protected void normalize(Datatype datatype) {
		switch(datatype.getBuiltInType()) {
		case DATETIME:
			// see https://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#dateTime-canonical-representation
			if(lastValidDatetime != null) {
				lastValidDatetime = lastValidDatetime.normalize();
			}
			break;
		case LIST:
			if(listValues != null) {
				Datatype dt = listValues.getListDatatype();
				for(Value v : listValues.toValues()) {
					isValid(dt, v);
					normalize(dt);
				}
			}
			break;
		default:
			/* do nothing*/	
		}
	}

	public void writeValue(QNameContext qnContext, EncoderChannel valueChannel,
			StringEncoder stringEncoder) throws IOException {
		if(doNormalize) {
			normalize(lastDatatype);
//			lastDatatype.normalize();
		}
		
		switch(lastDatatype.getBuiltInType()) {
		case BINARY_BASE64:
		case BINARY_HEX:
			valueChannel.encodeBinary(bytes);
			break;
		case BOOLEAN:
			valueChannel.encodeBoolean(bool.toBoolean());
			break;
		case BOOLEAN_FACET:
			valueChannel.encodeNBitUnsignedInteger(lastValidBooleanID, 2);
			break;
		case DECIMAL:
			valueChannel.encodeDecimal(lastValidDecimal.isNegative(),
					lastValidDecimal.getIntegral(),
					lastValidDecimal.getRevFractional());
			break;
		case FLOAT:
			valueChannel.encodeFloat(lastValidFloat);
			break;
		case NBIT_UNSIGNED_INTEGER:
			NBitUnsignedIntegerDatatype nbitDT = (NBitUnsignedIntegerDatatype) lastDatatype;
			IntegerValue iv = validValue.subtract(nbitDT.getLowerBound());
			valueChannel.encodeNBitUnsignedInteger(iv.intValue(), nbitDT.getNumberOfBits());
			break;
		case UNSIGNED_INTEGER:
			valueChannel.encodeUnsignedIntegerValue(lastUnsignedInteger);
			break;
		case INTEGER:
			valueChannel.encodeIntegerValue(lastInteger);
			break;
		case DATETIME:
			valueChannel.encodeDateTime(lastValidDatetime);
			break;
		case STRING:
			stringEncoder.writeValue(qnContext, valueChannel, lastValue);
			break;
		case RCS_STRING:
			RestrictedCharacterSetDatatype rcsDT = (RestrictedCharacterSetDatatype) lastDatatype;
			writeRCSValue(rcsDT, qnContext, valueChannel,
					stringEncoder, lastValue);
			break;
		case EXTENDED_STRING:
			ExtendedStringDatatype esDT = (ExtendedStringDatatype) lastDatatype;
			writeExtendedValue(esDT, qnContext, valueChannel, stringEncoder, lastValue);
			break;
		case ENUMERATION:
			EnumerationDatatype enumDT = (EnumerationDatatype) lastDatatype;
			valueChannel.encodeNBitUnsignedInteger(lastValidIndex, enumDT.getCodingLength());
			break;
		case LIST:
			ListDatatype listDT = (ListDatatype) lastDatatype;
			Datatype listDatatype = listDT.getListDatatype();
			
			// length prefixed sequence of values
			Value[] values = listValues.toValues();
			valueChannel.encodeUnsignedInteger(values.length);

			// iterate over all tokens
			for (int i = 0; i < values.length; i++) {
				Value v = values[i];
				boolean valid = isValid(listDatatype, v);
				if (!valid) {
					throw new RuntimeException("ListValue is not valid, " + v);
				}
				writeValue(qnContext, valueChannel, stringEncoder);
			}
			break;
		case QNAME:
			throw new IOException("QName is not an allowed as EXI datatype");
		default:
			throw new IOException("EXI datatype not supported");
		}
		
		
//		lastDatatype.writeValue(qnContext, valueChannel, stringEncoder);
	}
	
	
	int getEnumIndex(EnumDatatype grammarStrings, StringValue sv) {
		for(int i=0; i< grammarStrings.getEnumerationSize() ; i++) {
			Value v = grammarStrings.getEnumValue(i);
			if(sv.equals(v)) {
				return i;
			}
		}
		return -1;
	}
	
	
	protected void writeExtendedValue(ExtendedStringDatatype esDT, QNameContext context,
			EncoderChannel valueChannel, StringEncoder stringEncoder, String value) throws IOException {

		EnumDatatype grammarStrings = esDT.getGrammarStrings();
		
		ValueContainer vc = stringEncoder.getValueContainer(value); //    .stringValues.get(value);

		if (vc != null) {
			// hit
			if (stringEncoder.isLocalValuePartitions() && context.equals(vc.context)) {
				/*
				 * local value hit ==> is represented as zero (0) encoded as an
				 * Unsigned Integer followed by the compact identifier of the
				 * string value in the "local" value partition
				 */
				valueChannel.encodeUnsignedInteger(0);
				int numberBitsLocal = MethodsBag.getCodingLength(stringEncoder.getNumberOfStringValues(context));
				valueChannel.encodeNBitUnsignedInteger(vc.localValueID, numberBitsLocal);
			} else {
				/*
				 * global value hit ==> value is represented as one (1) encoded
				 * as an Unsigned Integer followed by the compact identifier of
				 * the String value in the global value partition.
				 */
				valueChannel.encodeUnsignedInteger(1);
				// global value size
				
				int numberBitsGlobal = MethodsBag.getCodingLength(stringEncoder.getValueContainerSize());
				valueChannel.encodeNBitUnsignedInteger(vc.globalValueID, numberBitsGlobal);
			}
		} else {
			/*
			 * miss [not found in local nor in global value partition] ==>
			 * string literal is encoded as a String with the length incremented
			 * by 6.
			 */
			
			// --> check grammar strings
			int gindex = -1;
			if(grammarStrings != null && 
					(gindex = getEnumIndex(grammarStrings, new StringValue(value))) >= 0
//					isValid(esDT, new StringValue(value))
					) {
				valueChannel.encodeUnsignedInteger(2); // grammar string
				valueChannel.encodeNBitUnsignedInteger(gindex, grammarStrings.getCodingLength());
			
				// this.isValid(ExtendedStringDatatype, value)
				// writeValue(context, valueChannel, stringEncoder);
			} else {
				// TODO (3)shared string, (4)split string, (5)undefined
				
				final int L = value.codePointCount(0, value.length());
				valueChannel.encodeUnsignedInteger(L + 6);
				/*
				 * If length L is greater than zero the string S is added
				 */
				if (L > 0) {
					valueChannel.encodeStringOnly(value);
					// After encoding the string value, it is added to both the
					// associated "local" value string table partition and the
					// global value string table partition.
					stringEncoder.addValue(context, value);
				}
			}
		}

	}
	
}
