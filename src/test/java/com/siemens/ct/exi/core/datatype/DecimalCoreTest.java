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
import java.math.BigDecimal;

import com.siemens.ct.exi.core.io.channel.DecoderChannel;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;
import com.siemens.ct.exi.core.values.DecimalValue;

public class DecimalCoreTest extends AbstractCoreTestCase {

	public DecimalCoreTest(String testName) {
		super(testName);
	}

	public void testDecimal0() throws IOException {
		String s = "-1.23";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		BigDecimal a = getBitDecoder().decodeDecimalValue().toBigDecimal();
		assertTrue(a.equals(new BigDecimal(s)));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(getByteDecoder().decodeDecimalValue().toBigDecimal()
				.equals(new BigDecimal(s)));
	}

	public void testDecimal1() throws IOException {
		String s = "12678967.543233";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		BigDecimal bdBit = getBitDecoder().decodeDecimalValue().toBigDecimal();
		assertTrue(bdBit + "!=" + new BigDecimal(s),
				bdBit.equals(new BigDecimal(s)));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(getByteDecoder().decodeDecimalValue().toBigDecimal()
				.equals(new BigDecimal(s)));
	}

	public void testDecimal2() throws IOException {
		String s = "+100000.0012";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		assertTrue(getBitDecoder().decodeDecimalValue().toBigDecimal()
				.equals(new BigDecimal(s)));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(getByteDecoder().decodeDecimalValue().toBigDecimal()
				.equals(new BigDecimal(s)));
	}

	public void testDecimal3() throws IOException {
		String s = "210";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		BigDecimal bdBit = getBitDecoder().decodeDecimalValue().toBigDecimal();
		assertTrue(bdBit + "!=" + new BigDecimal(s + ".0"),
				bdBit.equals(new BigDecimal(s + ".0")));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(getByteDecoder().decodeDecimalValue().toBigDecimal()
				.equals(new BigDecimal(s + ".0")));
	}

	public void testDecimal4() throws IOException {
		String s = "380";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		assertTrue((s + ".0").equals(getBitDecoder().decodeDecimalValue()
				.toString()));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue((s + ".0").equals(getByteDecoder().decodeDecimalValue()
				.toString()));
	}

	public void testDecimal5() throws IOException {
		String s = "0.001359";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		assertTrue(s.equals(getBitDecoder().decodeDecimalValue().toString()));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(s.equals(getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testDecimal6() throws IOException {
		String s = "110.74080";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + "110.7408", "110.7408".equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue("110.7408".equals(getByteDecoder().decodeDecimalValue()
				.toString()));
	}

	public void testDecimal7() throws IOException {
		String s = "55000.0";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		DecoderChannel bitDC = getBitDecoder();
		assertTrue(s.equals(bitDC.decodeDecimalValue().toString()));
		// Byte
		EncoderChannel byteEC = getByteEncoder();
		byteEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		DecoderChannel byteDC = getByteDecoder();
		assertTrue(s.equals(byteDC.decodeDecimalValue().toString()));
	}

	public void testDecimal8() throws IOException {
		String s = "3.141592653589";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + s, s.equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(s.equals(getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testDecimal9() throws Exception {
		String s = "-.1";
		String s2 = "-0.1";

		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + s2, s2.equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(s2.equals(getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testDecimal10() throws Exception {
		String s = "-.234";
		String s2 = "-0.234";

		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + s2, s2.equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(s2.equals(getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testDecimalBig1() throws IOException {
		String s = "36.087139166666670000000000000000001";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + s, s.equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(s.equals(getByteDecoder().decodeDecimalValue().toString()));
	}

	// deviation 8
	public void testDecimalBig2() throws IOException {
		String s = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890.1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678912345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
		DecimalValue d = DecimalValue.parse(s);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		DecimalValue dv1 = getBitDecoder().decodeDecimalValue();
		String sBit = dv1.toString();
		assertTrue(sBit + "!=" + s, s.equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(s.equals(getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testBigDecimal1() throws IOException {
		BigDecimal bd = new BigDecimal("-1.3");
		DecimalValue d = DecimalValue.parse(bd);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + bd, bd.toString().equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(bd.toString().equals(
				getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testBigDecimal2() throws IOException {
		BigDecimal bd = new BigDecimal("1236.087139166666670000000000000000001");
		DecimalValue d = DecimalValue.parse(bd);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + bd, bd.toString().equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(bd.toString().equals(
				getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testBigDecimal3() throws IOException {
		String dec = "-65.1203389898098908";
		BigDecimal bd = new BigDecimal("-65.12033898980989080");
		DecimalValue d = DecimalValue.parse(bd);
		assertTrue(d != null);

		// Bit
		EncoderChannel bitEC = getBitEncoder();
		bitEC.encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		bitEC.flush();
		String sBit = getBitDecoder().decodeDecimalValue().toString();
		assertTrue(sBit + "!=" + bd, dec.equals(sBit));
		// Byte
		getByteEncoder().encodeDecimal(d.isNegative(), d.getIntegral(),
				d.getRevFractional());
		assertTrue(dec.equals(getByteDecoder().decodeDecimalValue().toString()));
	}

	public void testDecimalEquals() throws IOException {
		String s1 = "-0.0";
		String s2 = "+0.0";
		String s3 = "0.0";
		String s4 = "+.0";
		DecimalValue d1 = DecimalValue.parse(s1);
		DecimalValue d2 = DecimalValue.parse(s2);
		DecimalValue d3 = DecimalValue.parse(s3);
		DecimalValue d4 = DecimalValue.parse(s4);
		assertTrue(d1.equals(d2));
		assertTrue(d2.equals(d3));
		assertTrue(d3.equals(d4));
		assertTrue(d4.equals(d1));
	}

	public void testDecimalFail1() throws IOException {
		String s = "9.213.456";
		DecimalValue d = DecimalValue.parse(s);
		assertFalse(d != null);

	}

}