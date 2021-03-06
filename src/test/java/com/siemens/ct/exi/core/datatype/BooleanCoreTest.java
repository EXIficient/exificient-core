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

import java.io.IOException;

import com.siemens.ct.exi.core.Constants;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;
import com.siemens.ct.exi.core.types.TypeDecoder;
import com.siemens.ct.exi.core.types.TypeEncoder;
import com.siemens.ct.exi.core.types.TypedTypeDecoder;
import com.siemens.ct.exi.core.types.TypedTypeEncoder;
import com.siemens.ct.exi.core.values.BooleanValue;
import com.siemens.ct.exi.core.values.StringValue;
import com.siemens.ct.exi.core.values.Value;

public class BooleanCoreTest extends AbstractCoreTestCase {

	public BooleanCoreTest(String testName) {
		super(testName);
	}

	public void testBoolean0() throws IOException, EXIException {
		StringValue s = new StringValue("0");

		TypeDecoder typeDecoder = new TypedTypeDecoder();
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		Datatype bool = new BooleanDatatype(null);
		boolean valid = typeEncoder.isValid(bool, s);
		assertTrue(valid);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		typeEncoder.writeValue(null, bitEC, null);
		bitEC.flush();
		Value val1 = typeDecoder.readValue(bool, null, getBitDecoder(), null);
		assertTrue(Constants.DECODED_BOOLEAN_FALSE.equals(val1.toString()));
		// Byte
		typeEncoder.writeValue(null, getByteEncoder(), null);
		Value val2 = typeDecoder.readValue(bool, null, getByteDecoder(), null);
		assertTrue(Constants.DECODED_BOOLEAN_FALSE.equals(val2.toString()));
	}

	public void testBoolean1() throws IOException, EXIException {
		StringValue s = new StringValue("1");

		TypeDecoder typeDecoder = new TypedTypeDecoder();
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		Datatype bool = new BooleanDatatype(null);
		boolean valid = typeEncoder.isValid(bool, s);
		assertTrue(valid);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		typeEncoder.writeValue(null, bitEC, null);
		bitEC.flush();
		Value val1 = typeDecoder.readValue(bool, null, getBitDecoder(), null);
		assertTrue(Constants.DECODED_BOOLEAN_TRUE.equals(val1.toString()));
		// Byte
		typeEncoder.writeValue(null, getByteEncoder(), null);
		Value val2 = typeDecoder.readValue(bool, null, getByteDecoder(), null);
		assertTrue(Constants.DECODED_BOOLEAN_TRUE.equals(val2.toString()));
	}

	public void testBooleanFalse() throws IOException, EXIException {
		StringValue s = new StringValue("false");
		boolean v = false;

		TypeDecoder typeDecoder = new TypedTypeDecoder();
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		Datatype bool = new BooleanDatatype(null);
		boolean valid = typeEncoder.isValid(bool, s);
		assertTrue(valid);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		typeEncoder.writeValue(null, bitEC, null);
		bitEC.flush();
		BooleanValue val1 = (BooleanValue) typeDecoder.readValue(bool, null,
				getBitDecoder(), null);
		assertTrue(v == val1.toBoolean());
		// Byte
		typeEncoder.writeValue(null, getByteEncoder(), null);
		BooleanValue val2 = (BooleanValue) typeDecoder.readValue(bool, null,
				getByteDecoder(), null);
		assertTrue(v == val2.toBoolean());
	}

	public void testBooleanTrue() throws IOException, EXIException {
		StringValue s = new StringValue("true");
		boolean v = true;

		TypeDecoder typeDecoder = new TypedTypeDecoder();
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		Datatype bool = new BooleanDatatype(null);
		boolean valid = typeEncoder.isValid(bool, s);
		assertTrue(valid);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		typeEncoder.writeValue(null, bitEC, null);
		bitEC.flush();
		BooleanValue val1 = (BooleanValue) typeDecoder.readValue(bool, null,
				getBitDecoder(), null);
		assertTrue(v == val1.toBoolean());
		// Byte
		typeEncoder.writeValue(null, getByteEncoder(), null);
		BooleanValue val2 = (BooleanValue) typeDecoder.readValue(bool, null,
				getByteDecoder(), null);
		assertTrue(v == val2.toBoolean());
	}

	public void testBooleanFalsePatternFalse() throws IOException, EXIException {
		StringValue s = new StringValue("false");

		TypeDecoder typeDecoder = new TypedTypeDecoder();
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		Datatype bool = new BooleanFacetDatatype(null);
		boolean valid = typeEncoder.isValid(bool, s);
		assertTrue(valid);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		typeEncoder.writeValue(null, bitEC, null);
		bitEC.flush();
		Value val1 = typeDecoder.readValue(bool, null, getBitDecoder(), null);
		assertTrue(s.equals(val1.toString()));
		// Byte
		typeEncoder.writeValue(null, getByteEncoder(), null);
		Value val2 = typeDecoder.readValue(bool, null, getByteDecoder(), null);
		assertTrue(s.equals(val2.toString()));
	}

	public void testBooleanTruePatternFalse() throws IOException, EXIException {
		StringValue s = new StringValue("true");

		TypeDecoder typeDecoder = new TypedTypeDecoder();
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		Datatype bool = new BooleanFacetDatatype(null);
		boolean valid = typeEncoder.isValid(bool, s);
		assertTrue(valid);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		typeEncoder.writeValue(null, bitEC, null);
		bitEC.flush();
		Value val1 = typeDecoder.readValue(bool, null, getBitDecoder(), null);
		assertTrue(s.equals(val1.toString()));
		// Byte
		typeEncoder.writeValue(null, getByteEncoder(), null);
		Value val2 = typeDecoder.readValue(bool, null, getByteDecoder(), null);
		assertTrue(s.equals(val2.toString()));
	}

	public void testBooleanFailure1() throws IOException, EXIException {
		StringValue s = new StringValue("00");

		Datatype bool = new BooleanDatatype(null);
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		boolean valid = typeEncoder.isValid(bool, s);
		assertFalse(valid);
	}

	public void testBooleanFailure2() throws IOException, EXIException {
		StringValue s = new StringValue("fAlse");

		Datatype bool = new BooleanDatatype(null);
		TypeEncoder typeEncoder = new TypedTypeEncoder();
		boolean valid = typeEncoder.isValid(bool, s);
		assertFalse(valid);
	}

}