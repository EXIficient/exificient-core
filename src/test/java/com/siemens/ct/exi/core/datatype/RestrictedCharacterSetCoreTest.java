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
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.charset.CodePointCharacterSet;
import com.siemens.ct.exi.core.datatype.strings.BoundedStringEncoderImpl;
import com.siemens.ct.exi.core.datatype.strings.StringDecoderImpl;
import com.siemens.ct.exi.core.datatype.strings.StringEncoderImpl;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.io.channel.DecoderChannel;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;
import com.siemens.ct.exi.core.types.TypedTypeDecoder;
import com.siemens.ct.exi.core.types.TypedTypeEncoder;
import com.siemens.ct.exi.core.values.StringValue;
import com.siemens.ct.exi.core.values.Value;

public class RestrictedCharacterSetCoreTest extends AbstractCoreTestCase {

	public RestrictedCharacterSetCoreTest(String testName) {
		super(testName);
	}

	// the set {a, b}: index 0 and 1, 2 is the escape, coded in 2 bits
	private Datatype abDatatype() {
		Set<Integer> codePoints = new HashSet<Integer>();
		codePoints.add((int) 'a');
		codePoints.add((int) 'b');
		return new RestrictedCharacterSetDatatype(new CodePointCharacterSet(
				codePoints), null);
	}

	private final QNameContext qnc = new QNameContext(0, 0, new QName("v"));

	public void testNonBMPEncode() throws IOException, EXIException {
		// a, U+1F600 outside the set and the BMP, b: three characters
		String s = new StringBuilder("a").appendCodePoint(0x1F600)
				.append("b").toString();
		assertEquals(4, s.length()); // UTF-16 code units

		TypedTypeEncoder te = new TypedTypeEncoder();
		Datatype dt = abDatatype();
		assertTrue(te.isValid(dt, new StringValue(s)));
		EncoderChannel ec = getBitEncoder();
		te.writeValue(qnc, ec, new StringEncoderImpl(false));
		ec.flush();

		DecoderChannel dc = getBitDecoder();
		assertEquals(3 + 2, dc.decodeUnsignedInteger()); // L + 2, L characters
		assertEquals(0, dc.decodeNBitUnsignedInteger(2)); // a
		assertEquals(2, dc.decodeNBitUnsignedInteger(2)); // escape
		assertEquals(0x1F600, dc.decodeUnsignedInteger()); // one code point
		assertEquals(1, dc.decodeNBitUnsignedInteger(2)); // b
	}

	public void testNonBMPDecode() throws IOException, EXIException {
		EncoderChannel ec = getBitEncoder();
		ec.encodeUnsignedInteger(3 + 2);
		ec.encodeNBitUnsignedInteger(0, 2);
		ec.encodeNBitUnsignedInteger(2, 2);
		ec.encodeUnsignedInteger(0x1F600);
		ec.encodeNBitUnsignedInteger(1, 2);
		ec.flush();

		Value v = new TypedTypeDecoder().readValue(abDatatype(), qnc,
				getBitDecoder(), new StringDecoderImpl(false));
		assertEquals(new StringBuilder("a").appendCodePoint(0x1F600)
				.append("b").toString(), v.toString());
	}

	public void testNonBMPRoundTrip() throws IOException, EXIException {
		String s = new StringBuilder().appendCodePoint(0x10FFF).append("ab")
				.appendCodePoint(0x1D11E).toString();
		TypedTypeEncoder te = new TypedTypeEncoder();
		Datatype dt = abDatatype();
		assertTrue(te.isValid(dt, new StringValue(s)));
		EncoderChannel ec = getBitEncoder();
		te.writeValue(qnc, ec, new StringEncoderImpl(false));
		ec.flush();
		Value v = new TypedTypeDecoder().readValue(dt, qnc, getBitDecoder(),
				new StringDecoderImpl(false));
		assertEquals(s, v.toString());
	}

	public void testValueMaxLengthCountsCharacters() throws IOException {
		// one character, two UTF-16 units: within valueMaxLength 1
		String s = new StringBuilder().appendCodePoint(0x1F600).toString();
		BoundedStringEncoderImpl enc = new BoundedStringEncoderImpl(true, 1, -1);
		enc.addValue(qnc, s);
		assertTrue(enc.isStringHit(s));
		enc.addValue(qnc, "ab"); // two characters: too long
		assertFalse(enc.isStringHit("ab"));
	}
}
