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

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import com.siemens.ct.exi.core.Constants;
import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.Datatype;
import com.siemens.ct.exi.core.datatype.IntegerDatatype;
import com.siemens.ct.exi.core.datatype.NBitUnsignedIntegerDatatype;
import com.siemens.ct.exi.core.datatype.UnsignedIntegerDatatype;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.DecoderChannel;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;
import com.siemens.ct.exi.core.types.TypeEncoder;
import com.siemens.ct.exi.core.types.TypedTypeEncoder;
import com.siemens.ct.exi.core.values.BinaryBase64Value;
import com.siemens.ct.exi.core.values.BinaryHexValue;
import com.siemens.ct.exi.core.values.IntegerValue;
import com.siemens.ct.exi.core.values.StringValue;
import com.siemens.ct.exi.core.values.Value;

public class TypeCoreTest extends TestCase {

	public TypeCoreTest(String testName) {
		super(testName);
	}

	public void testInteger1() throws IOException, EXIException {
		
		TypeEncoder te = new TypedTypeEncoder();
		
		QNameContext qnc = new QNameContext(-1, -1, new QName(Constants.XML_SCHEMA_NS_URI, "integer"));
		Datatype dt = new IntegerDatatype(qnc);
		
		assertTrue(te.isValid(dt, new StringValue("1231")));
		assertTrue(te.isValid(dt, new StringValue("-331")));
		assertFalse(te.isValid(dt, new StringValue("xxx")));		
	}
	
	public void testUnsignedInteger1() throws IOException, EXIException {
		
		TypeEncoder te = new TypedTypeEncoder();
		
		QNameContext qnc = new QNameContext(-1, -1, new QName(Constants.XML_SCHEMA_NS_URI, "unsignedInt"));
		Datatype dt = new UnsignedIntegerDatatype(qnc);
		
		assertTrue(te.isValid(dt, new StringValue("1231")));
		assertFalse(te.isValid(dt, new StringValue("-331")));
		assertFalse(te.isValid(dt, new StringValue("xxx")));		
	}

	
	public void testNBitUnsignedInteger1() throws IOException, EXIException {
		
		TypeEncoder te = new TypedTypeEncoder();
		
		QNameContext qnc = new QNameContext(-1, -1, new QName(Constants.XML_SCHEMA_NS_URI, "unsignedByte"));
		Datatype dt = new NBitUnsignedIntegerDatatype(IntegerValue.valueOf(0), IntegerValue.valueOf(255), qnc);
		
		assertTrue(te.isValid(dt, new StringValue("12")));
		assertFalse(te.isValid(dt, new StringValue("-3")));
		assertFalse(te.isValid(dt, new StringValue("xxx")));		
	}
	
	public void testNBitUnsignedInteger2() throws IOException, EXIException {
		
		TypeEncoder te = new TypedTypeEncoder();
		
		QNameContext qnc = new QNameContext(-1, -1, new QName(Constants.XML_SCHEMA_NS_URI, "byte"));
		Datatype dt = new NBitUnsignedIntegerDatatype(IntegerValue.valueOf(-128), IntegerValue.valueOf(127), qnc);
		
		assertTrue(te.isValid(dt, new StringValue("12")));
		assertTrue(te.isValid(dt, new StringValue("-3")));
		assertFalse(te.isValid(dt, new StringValue("200")));	
		assertFalse(te.isValid(dt, new StringValue("xxx")));		
	}
	
	
	public void testDTRInteger1() throws IOException, EXIException {
		
		/* DTR Map */
		QName type = new QName(Constants.XML_SCHEMA_NS_URI, "integer");
		QName representation = new QName(Constants.W3C_EXI_NS_URI, "integer");
		QName[] dtrMapTypes = { type };
		QName[] dtrMapRepresentations = { representation };
		TypeEncoder te = new TypedTypeEncoder(dtrMapTypes, dtrMapRepresentations, null);
		
		QNameContext qncByte = new QNameContext(-1, -1, new QName(Constants.XML_SCHEMA_NS_URI, "byte"));
		Datatype dt = new NBitUnsignedIntegerDatatype(IntegerValue.valueOf(-128), IntegerValue.valueOf(127), qncByte);
		// fake base type to integer and skip short, int, long etc
//		QNameContext qncInteger = new QNameContext(-1, -1, new QName(Constants.XML_SCHEMA_NS_URI, "integer"));
//		qncByte.setSimpleBaseType(qncInteger);
//		dt.setBaseDatatype(new IntegerDatatype(qncInteger));
		 dt.setBaseDatatype(new IntegerDatatype(new QNameContext(-1, -1, new QName(Constants.XML_SCHEMA_NS_URI, "integer"))));
		
		// should allow ONLY byte integer
		assertTrue(te.isValid(dt, new StringValue("12")));
		assertTrue(te.isValid(dt, new StringValue("-23")));
		assertFalse(te.isValid(dt, new StringValue("12999")));
		assertFalse(te.isValid(dt, new StringValue("-33113")));
		assertFalse(te.isValid(dt, new StringValue("xxx")));		
	}
	

}