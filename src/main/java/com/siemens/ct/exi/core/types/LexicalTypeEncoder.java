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
import com.siemens.ct.exi.core.datatype.charset.XSDBase64CharacterSet;
import com.siemens.ct.exi.core.datatype.charset.XSDBooleanCharacterSet;
import com.siemens.ct.exi.core.datatype.charset.XSDDateTimeCharacterSet;
import com.siemens.ct.exi.core.datatype.charset.XSDDecimalCharacterSet;
import com.siemens.ct.exi.core.datatype.charset.XSDDoubleCharacterSet;
import com.siemens.ct.exi.core.datatype.charset.XSDHexBinaryCharacterSet;
import com.siemens.ct.exi.core.datatype.charset.XSDIntegerCharacterSet;
import com.siemens.ct.exi.core.datatype.charset.XSDStringCharacterSet;
import com.siemens.ct.exi.core.datatype.strings.StringEncoder;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;
import com.siemens.ct.exi.core.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public class LexicalTypeEncoder extends AbstractTypeEncoder {

	protected RestrictedCharacterSetDatatype rcsBase64Binary = new RestrictedCharacterSetDatatype(
			new XSDBase64CharacterSet(), null);
	protected RestrictedCharacterSetDatatype rcsHexBinary = new RestrictedCharacterSetDatatype(
			new XSDHexBinaryCharacterSet(), null);
	protected RestrictedCharacterSetDatatype rcsBoolean = new RestrictedCharacterSetDatatype(
			new XSDBooleanCharacterSet(), null);
	protected RestrictedCharacterSetDatatype rcsDateTime = new RestrictedCharacterSetDatatype(
			new XSDDateTimeCharacterSet(), null);
	protected RestrictedCharacterSetDatatype rcsDecimal = new RestrictedCharacterSetDatatype(
			new XSDDecimalCharacterSet(), null);
	protected RestrictedCharacterSetDatatype rcsDouble = new RestrictedCharacterSetDatatype(
			new XSDDoubleCharacterSet(), null);
	protected RestrictedCharacterSetDatatype rcsInteger = new RestrictedCharacterSetDatatype(
			new XSDIntegerCharacterSet(), null);
	protected RestrictedCharacterSetDatatype rcsString = new RestrictedCharacterSetDatatype(
			new XSDStringCharacterSet(), null);

	protected Value lastValue;
	public Datatype lastDatatype;

	public LexicalTypeEncoder() throws EXIException {
		this(null, null, null);
	}

	public LexicalTypeEncoder(QName[] dtrMapTypes,
			QName[] dtrMapRepresentations,
			Map<QName, Datatype> dtrMapRepresentationsDatatype)
			throws EXIException {
		super(dtrMapTypes, dtrMapRepresentations, dtrMapRepresentationsDatatype);
	}

	public boolean isValid(Datatype datatype, Value value) {
		if (this.dtrMapInUse) {
			lastDatatype = this.getDtrDatatype(datatype);
		} else {
			lastDatatype = datatype;
		}

		lastValue = value;
		return true;
	}

	public void writeValue(QNameContext qnContext, EncoderChannel valueChannel,
			StringEncoder stringEncoder) throws IOException {
		String lastValueString = lastValue.toString();
		switch (lastDatatype.getDatatypeID()) {
		case exi_base64Binary:
			isValid(rcsBase64Binary, lastValue);
			writeRCSValue(rcsBase64Binary, qnContext, valueChannel,
					stringEncoder, lastValueString);
			break;
		case exi_hexBinary:
			isValid(rcsHexBinary, lastValue);
			writeRCSValue(rcsHexBinary, qnContext, valueChannel, stringEncoder,
					lastValueString);
			break;
		case exi_boolean:
			isValid(rcsBoolean, lastValue);
			writeRCSValue(rcsBoolean, qnContext, valueChannel, stringEncoder,
					lastValueString);
			break;
		case exi_dateTime:
		case exi_time:
		case exi_date:
		case exi_gYearMonth:
		case exi_gYear:
		case exi_gMonthDay:
		case exi_gDay:
		case exi_gMonth:
			isValid(rcsDateTime, lastValue);
			writeRCSValue(rcsDateTime, qnContext, valueChannel, stringEncoder,
					lastValueString);
			break;
		case exi_decimal:
			isValid(rcsDecimal, lastValue);
			writeRCSValue(rcsDecimal, qnContext, valueChannel, stringEncoder,
					lastValueString);
			break;
		case exi_double:
			isValid(rcsDouble, lastValue);
			writeRCSValue(rcsDouble, qnContext, valueChannel, stringEncoder,
					lastValueString);
			break;
		case exi_integer:
			isValid(rcsInteger, lastValue);
			writeRCSValue(rcsInteger, qnContext, valueChannel, stringEncoder,
					lastValueString);
			break;
		case exi_string:
			// exi:string no restricted character set
			stringEncoder.writeValue(qnContext, valueChannel,
					lastValue.toString());
			// rcsString.isValid(lastValue);
			// rcsString.writeValue(qnContext, valueChannel, stringEncoder);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

}
