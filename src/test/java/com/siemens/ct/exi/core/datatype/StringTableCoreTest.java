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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.siemens.ct.exi.core.Constants;
import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.Datatype;
import com.siemens.ct.exi.core.datatype.strings.StringDecoder;
import com.siemens.ct.exi.core.datatype.strings.StringEncoder;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.core.io.channel.BitDecoderChannel;
import com.siemens.ct.exi.core.io.channel.BitEncoderChannel;
import com.siemens.ct.exi.core.types.BuiltIn;
import com.siemens.ct.exi.core.types.TypeDecoder;
import com.siemens.ct.exi.core.types.TypeEncoder;
import com.siemens.ct.exi.core.values.StringValue;

public class StringTableCoreTest extends AbstractCoreTestCase {

	@Test
	public void testStringTableValue0() throws IOException, EXIException {
		QName context = new QName("el");

		// EvolvingUriContext uc = new RuntimeEvolvingUriContext(0, "");
		QNameContext qncContext = new QNameContext(0, 0, context);

		String val1 = "val1";
		String val2 = "val2";
		String val3 = "val3";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		EXIFactory exiFactory = DefaultEXIFactory.newInstance();
		// GrammarContext grammarContext =
		// exiFactory.getGrammars().getGrammarContext();

		/*
		 * Encode
		 */
		StringEncoder stringEncoder = exiFactory.createStringEncoder();
		// EncoderContext encoderContext = new
		// EncoderContextImpl(grammarContext, stringEncoder);
		TypeEncoder tes = exiFactory.createTypeEncoder(); // new
															// StringTypeEncoder();
		BitEncoderChannel bec = new BitEncoderChannel(baos);

		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val1));
		tes.writeValue(qncContext, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val2));
		tes.writeValue(qncContext, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val1));
		tes.writeValue(qncContext, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val3));
		tes.writeValue(qncContext, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val3));
		tes.writeValue(qncContext, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val3));
		tes.writeValue(qncContext, bec, stringEncoder);

		bec.flush();
		baos.flush();

		/*
		 * Decode
		 */
		TypeDecoder ddl = exiFactory.createTypeDecoder(); // new
															// StringTypeDecoder(new
															// StringDecoderImpl());
		StringDecoder stringDecoder = exiFactory.createStringDecoder();
		// DecoderContext decoderContext = new
		// DecoderContextImpl(grammarContext, stringDecoder);

		BitDecoderChannel bdc = new BitDecoderChannel(new ByteArrayInputStream(
				baos.toByteArray()));

		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncContext, bdc,
						stringDecoder).toString().equals(val1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncContext, bdc,
						stringDecoder).toString().equals(val2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncContext, bdc,
						stringDecoder).toString().equals(val1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncContext, bdc,
						stringDecoder).toString().equals(val3));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncContext, bdc,
						stringDecoder).toString().equals(val3));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncContext, bdc,
						stringDecoder).toString().equals(val3));
	}

	@Test
	public void testStringTableValue1() throws IOException, EXIException {
		QName c1 = new QName("el1");
		QName c2 = new QName("el2");
		QName c3 = new QName("el3");

		// EvolvingUriContext uc = new RuntimeEvolvingUriContext(0, "");
		int namespaceUriID = 0;
		QNameContext qncC1 = new QNameContext(namespaceUriID, 0, c1);
		QNameContext qncC2 = new QNameContext(namespaceUriID, 1, c2);
		QNameContext qncC3 = new QNameContext(namespaceUriID, 2, c3);

		String val1 = "val1";
		String val2 = "val2";
		String val3 = "val3";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		EXIFactory exiFactory = DefaultEXIFactory.newInstance();
		// GrammarContext grammarContext =
		// exiFactory.getGrammars().getGrammarContext();

		/*
		 * Encode
		 */
		StringEncoder stringEncoder = exiFactory.createStringEncoder();
		// EncoderContext encoderContext = new
		// EncoderContextImpl(grammarContext, stringEncoder);
		TypeEncoder tes = exiFactory.createTypeEncoder(); // new
															// StringTypeEncoder();
		// StringTypeEncoder tes = new StringTypeEncoder(new
		// StringEncoderImpl());

		BitEncoderChannel bec = new BitEncoderChannel(baos);

		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val1));
		tes.writeValue(qncC1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val2));
		tes.writeValue(qncC3, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val1));
		tes.writeValue(qncC2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val3));
		tes.writeValue(qncC3, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val3));
		tes.writeValue(qncC1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(val3));
		tes.writeValue(qncC3, bec, stringEncoder);

		bec.flush();
		baos.flush();

		/*
		 * Decode
		 */
		TypeDecoder ddl = exiFactory.createTypeDecoder();
		StringDecoder stringDecoder = exiFactory.createStringDecoder();
		// DecoderContext decoderContext = new
		// DecoderContextImpl(grammarContext, stringDecoder);
		// StringTypeDecoder ddl = new StringTypeDecoder(new
		// StringDecoderImpl());

		BitDecoderChannel bdc = new BitDecoderChannel(new ByteArrayInputStream(
				baos.toByteArray()));

		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncC1, bdc, stringDecoder)
				.toString().equals(val1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncC3, bdc, stringDecoder)
				.toString().equals(val2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncC2, bdc, stringDecoder)
				.toString().equals(val1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncC3, bdc, stringDecoder)
				.toString().equals(val3));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncC1, bdc, stringDecoder)
				.toString().equals(val3));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncC3, bdc, stringDecoder)
				.toString().equals(val3));
	}

	@Test
	public void testStringTableValue() throws IOException, EXIException {

		QName ca1 = new QName("at1");

		QName cex2 = new QName("elx2");
		QName cex3 = new QName("elx3");

		QName cexx1 = new QName("elxx1");
		QName cexx2 = new QName("elxx2");

		// EvolvingUriContext uc = new RuntimeEvolvingUriContext(0, "");
		int namespaceUriID = 0;
		QNameContext qncCa1 = new QNameContext(namespaceUriID, 0, ca1);
		QNameContext qncCex2 = new QNameContext(namespaceUriID, 1, cex2);
		QNameContext qncCex3 = new QNameContext(namespaceUriID, 2, cex3);
		QNameContext qncCexx1 = new QNameContext(namespaceUriID, 2, cexx1);
		QNameContext qncCexx2 = new QNameContext(namespaceUriID, 2, cexx2);

		String atCh1 = "at-ch1";
		String atCh2 = "at-ch2";
		String ch1 = "ch1";
		String ch2 = "ch2";
		String ch3 = "ch3";

		EXIFactory exiFactory = DefaultEXIFactory.newInstance();
		// GrammarContext grammarContext =
		// exiFactory.getGrammars().getGrammarContext();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		/*
		 * Encode
		 */
		StringEncoder stringEncoder = exiFactory.createStringEncoder();
		// EncoderContext encoderContext = new
		// EncoderContextImpl(grammarContext, stringEncoder);
		TypeEncoder tes = exiFactory.createTypeEncoder(); // new
															// StringTypeEncoder();
		// StringTypeEncoder tes = new StringTypeEncoder(new
		// StringEncoderImpl());

		BitEncoderChannel bec = new BitEncoderChannel(baos);

		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(atCh1));
		tes.writeValue(qncCa1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch1));
		tes.writeValue(qncCexx1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCexx2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCex2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch3));
		tes.writeValue(qncCex3, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(atCh2));
		tes.writeValue(qncCa1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch1));
		tes.writeValue(qncCexx1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCexx2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCex2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch3));
		tes.writeValue(qncCex2, bec, stringEncoder);

		bec.flush();
		baos.flush();

		/*
		 * Decode
		 */
		TypeDecoder ddl = exiFactory.createTypeDecoder();
		StringDecoder stringDecoder = exiFactory.createStringDecoder();
		// DecoderContext decoderContext = new
		// DecoderContextImpl(grammarContext, stringDecoder);
		// StringTypeDecoder ddl = new StringTypeDecoder(new
		// StringDecoderImpl());

		BitDecoderChannel bdc = new BitDecoderChannel(new ByteArrayInputStream(
				baos.toByteArray()));

		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCa1, bdc, stringDecoder)
				.toString().equals(atCh1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx1, bdc,
						stringDecoder).toString().equals(ch1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex3, bdc,
						stringDecoder).toString().equals(ch3));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCa1, bdc, stringDecoder)
				.toString().equals(atCh2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx1, bdc,
						stringDecoder).toString().equals(ch1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex2, bdc,
						stringDecoder).toString().equals(ch3));
	}

	@Test
	public void testStringTableValueMaxLength1() throws IOException,
			EXIException {

		QName qa = new QName("a");
		QName qb = new QName("b");

		// EvolvingUriContext uc = new RuntimeEvolvingUriContext(0, "");
		int namespaceUriID = 0;
		QNameContext qncA = new QNameContext(namespaceUriID, 0, qa);
		QNameContext qncB = new QNameContext(namespaceUriID, 1, qb);

		String s3 = "123";
		String s4 = "1234";
		String s5 = "12345";
		String s6 = "123456";
		String s7 = "1234567";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int valueMaxLength = 5;
		// int valuePartitionCapacity =
		// Constants.DEFAULT_VALUE_PARTITON_CAPACITY;

		Datatype dt = BuiltIn.DEFAULT_DATATYPE;

		EXIFactory exiFactory = DefaultEXIFactory.newInstance();
		exiFactory.setValueMaxLength(valueMaxLength);
		// GrammarContext grammarContext =
		// exiFactory.getGrammars().getGrammarContext();

		/*
		 * Encode
		 */
		StringEncoder stringEncoder = exiFactory.createStringEncoder();
		// EncoderContext encoderContext = new
		// EncoderContextImpl(grammarContext, stringEncoder);
		TypeEncoder tes = exiFactory.createTypeEncoder();
		// StringEncoder se = new BoundedStringEncoderImpl(valueMaxLength,
		// valuePartitionCapacity);
		// StringTypeEncoder tes = new StringTypeEncoder(se);

		BitEncoderChannel bec = new BitEncoderChannel(baos);

		// a: 123
		tes.isValid(dt, new StringValue(s3));
		tes.writeValue(qncA, bec, stringEncoder);
		// b: 1234
		tes.isValid(dt, new StringValue(s4));
		tes.writeValue(qncB, bec, stringEncoder);
		// a: 12345
		tes.isValid(dt, new StringValue(s5));
		tes.writeValue(qncA, bec, stringEncoder);
		// b: 123456 /* to large */
		assertFalse(stringEncoder.isStringHit(s6));
		tes.isValid(dt, new StringValue(s6));
		tes.writeValue(qncB, bec, stringEncoder);
		// a: 1234567 /* to large */
		assertFalse(stringEncoder.isStringHit(s7));
		tes.isValid(dt, new StringValue(s7));
		tes.writeValue(qncA, bec, stringEncoder);

		bec.flush();
		baos.flush();

		/*
		 * Decode
		 */
		TypeDecoder ddl = exiFactory.createTypeDecoder();
		StringDecoder stringDecoder = exiFactory.createStringDecoder();
		// DecoderContext decoderContext = new
		// DecoderContextImpl(grammarContext, stringDecoder);
		// StringDecoder sd = new BoundedStringDecoderImpl(valueMaxLength,
		// valuePartitionCapacity);
		// StringTypeDecoder ddl = new StringTypeDecoder(sd);

		BitDecoderChannel bdc = new BitDecoderChannel(new ByteArrayInputStream(
				baos.toByteArray()));

		assertTrue(ddl.readValue(dt, qncA, bdc, stringDecoder).toString()
				.equals(s3));
		assertTrue(ddl.readValue(dt, qncB, bdc, stringDecoder).toString()
				.equals(s4));
		assertTrue(ddl.readValue(dt, qncA, bdc, stringDecoder).toString()
				.equals(s5));
		assertTrue(ddl.readValue(dt, qncB, bdc, stringDecoder).toString()
				.equals(s6));
		assertTrue(ddl.readValue(dt, qncA, bdc, stringDecoder).toString()
				.equals(s7));
	}

	@Test
	public void testStringTableValuePartitionCapacity() throws IOException,
			EXIException {

		QName qa = new QName("a");
		QName qb = new QName("b");
		QName qc = new QName("c");

		// EvolvingUriContext uc = new RuntimeEvolvingUriContext(0, "");
		int namespaceUriID = 0;
		QNameContext qncQa = new QNameContext(namespaceUriID, 0, qa);
		QNameContext qncQb = new QNameContext(namespaceUriID, 1, qb);
		QNameContext qncQc = new QNameContext(namespaceUriID, 2, qc);

		String s1 = "1";
		String s2 = "12";
		String s3 = "123";
		String s4 = "1234";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int valueMaxLength = Constants.DEFAULT_VALUE_MAX_LENGTH;
		int valuePartitionCapacity = 3;

		EXIFactory exiFactory = DefaultEXIFactory.newInstance();
		exiFactory.setValueMaxLength(valueMaxLength);
		exiFactory.setValuePartitionCapacity(valuePartitionCapacity);
		// GrammarContext grammarContext =
		// exiFactory.getGrammars().getGrammarContext();

		Datatype dt = BuiltIn.DEFAULT_DATATYPE;
		/*
		 * Encode
		 */
		StringEncoder stringEncoder = exiFactory.createStringEncoder();
		// EncoderContext encoderContext = new
		// EncoderContextImpl(grammarContext, stringEncoder);
		TypeEncoder tes = exiFactory.createTypeEncoder();
		// StringEncoder se = new BoundedStringEncoderImpl(valueMaxLength,
		// valuePartitionCapacity);
		// StringTypeEncoder tes = new StringTypeEncoder(se);

		BitEncoderChannel bec = new BitEncoderChannel(baos);

		// a: 1
		tes.isValid(dt, new StringValue(s1));
		tes.writeValue(qncQa, bec, stringEncoder); // ["1"{a}]
		// b: 12
		tes.isValid(dt, new StringValue(s2));
		tes.writeValue(qncQb, bec, stringEncoder); // ["1"{a},"12"{b}]
		// a: 123
		tes.isValid(dt, new StringValue(s3));
		tes.writeValue(qncQa, bec, stringEncoder); // ["1"{a},"12"{b},"123"{a}]
		// c: 123 /* global hit */
		tes.isValid(dt, new StringValue(s3));
		tes.writeValue(qncQc, bec, stringEncoder); // ["1","12","123"]
		// c: 1234
		tes.isValid(dt, new StringValue(s4));
		tes.writeValue(qncQc, bec, stringEncoder); // ["1234","12","123"]
		// a: 1 /* no local hit due to valuePartitionCapacity*/
		assertFalse(stringEncoder.isStringHit(s1));
		tes.isValid(dt, new StringValue(s1));
		tes.writeValue(qncQa, bec, stringEncoder); // ["1234","1","123"]
		// c: 1 /* no local hit due to valuePartitionCapacity*/
		assertTrue(stringEncoder.isStringHit(s4));
		assertTrue(stringEncoder.isStringHit(s1));
		assertTrue(stringEncoder.isStringHit(s3));
		assertFalse(stringEncoder.isStringHit(s2));
		tes.isValid(dt, new StringValue(s2));
		tes.writeValue(qncQc, bec, stringEncoder); // ["1234","1","12"]
		assertTrue(stringEncoder.isStringHit(s4));
		assertTrue(stringEncoder.isStringHit(s1));
		assertTrue(stringEncoder.isStringHit(s2));

		bec.flush();
		baos.flush();

		/*
		 * Decode
		 */
		TypeDecoder ddl = exiFactory.createTypeDecoder();
		StringDecoder stringDecoder = exiFactory.createStringDecoder();
		// DecoderContext decoderContext = new
		// DecoderContextImpl(grammarContext, stringDecoder);
		// StringTypeDecoder ddl = new StringTypeDecoder(
		// new BoundedStringDecoderImpl(valueMaxLength,
		// valuePartitionCapacity));

		BitDecoderChannel bdc = new BitDecoderChannel(new ByteArrayInputStream(
				baos.toByteArray()));

		assertTrue(ddl.readValue(dt, qncQa, bdc, stringDecoder).toString()
				.equals(s1));
		assertTrue(ddl.readValue(dt, qncQb, bdc, stringDecoder).toString()
				.equals(s2));
		assertTrue(ddl.readValue(dt, qncQa, bdc, stringDecoder).toString()
				.equals(s3));
		assertTrue(ddl.readValue(dt, qncQc, bdc, stringDecoder).toString()
				.equals(s3)); /* global hit */
		assertTrue(ddl.readValue(dt, qncQc, bdc, stringDecoder).toString()
				.equals(s4)); /* replaces values */
		assertTrue(ddl.readValue(dt, qncQa, bdc, stringDecoder).toString()
				.equals(s1)); /* no local hit */
		assertTrue(ddl.readValue(dt, qncQc, bdc, stringDecoder).toString()
				.equals(s2)); /* no local hit */
	}
	
	
	@Test
	public void testStringTableValueSharedStrings() throws IOException, EXIException {

		QName ca1 = new QName("at1");

		QName cex2 = new QName("elx2");
		QName cex3 = new QName("elx3");

		QName cexx1 = new QName("elxx1");
		QName cexx2 = new QName("elxx2");

		// EvolvingUriContext uc = new RuntimeEvolvingUriContext(0, "");
		int namespaceUriID = 0;
		QNameContext qncCa1 = new QNameContext(namespaceUriID, 0, ca1);
		QNameContext qncCex2 = new QNameContext(namespaceUriID, 1, cex2);
		QNameContext qncCex3 = new QNameContext(namespaceUriID, 2, cex3);
		QNameContext qncCexx1 = new QNameContext(namespaceUriID, 2, cexx1);
		QNameContext qncCexx2 = new QNameContext(namespaceUriID, 2, cexx2);

		String atCh1 = "at-ch1";
		String atCh2 = "at-ch2";
		String ch1 = "ch1";
		String ch2 = "ch2";
		String ch3 = "ch3";

		EXIFactory exiFactory = DefaultEXIFactory.newInstance();
		// GrammarContext grammarContext =
		// exiFactory.getGrammars().getGrammarContext();

		List<String> sharedStrings = Arrays.asList(new String[]{atCh2, ch2, ch3});
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		/*
		 * Encode
		 */
		StringEncoder stringEncoder = exiFactory.createStringEncoder();
		stringEncoder.setSharedStrings(sharedStrings);
		// EncoderContext encoderContext = new
		// EncoderContextImpl(grammarContext, stringEncoder);
		TypeEncoder tes = exiFactory.createTypeEncoder(); // new
															// StringTypeEncoder();
		// StringTypeEncoder tes = new StringTypeEncoder(new
		// StringEncoderImpl());

		BitEncoderChannel bec = new BitEncoderChannel(baos);

		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(atCh1));
		tes.writeValue(qncCa1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch1));
		tes.writeValue(qncCexx1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCexx2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCex2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch3));
		tes.writeValue(qncCex3, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(atCh2));
		tes.writeValue(qncCa1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch1));
		tes.writeValue(qncCexx1, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCexx2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch2));
		tes.writeValue(qncCex2, bec, stringEncoder);
		tes.isValid(BuiltIn.DEFAULT_DATATYPE, new StringValue(ch3));
		tes.writeValue(qncCex2, bec, stringEncoder);

		bec.flush();
		baos.flush();

		/*
		 * Decode
		 */
		TypeDecoder ddl = exiFactory.createTypeDecoder();
		StringDecoder stringDecoder = exiFactory.createStringDecoder();
		stringDecoder.setSharedStrings(sharedStrings);
		// DecoderContext decoderContext = new
		// DecoderContextImpl(grammarContext, stringDecoder);
		// StringTypeDecoder ddl = new StringTypeDecoder(new
		// StringDecoderImpl());

		BitDecoderChannel bdc = new BitDecoderChannel(new ByteArrayInputStream(
				baos.toByteArray()));

		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCa1, bdc, stringDecoder)
				.toString().equals(atCh1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx1, bdc,
						stringDecoder).toString().equals(ch1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex3, bdc,
						stringDecoder).toString().equals(ch3));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCa1, bdc, stringDecoder)
				.toString().equals(atCh2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx1, bdc,
						stringDecoder).toString().equals(ch1));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCexx2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex2, bdc,
						stringDecoder).toString().equals(ch2));
		assertTrue(ddl
				.readValue(BuiltIn.DEFAULT_DATATYPE, qncCex2, bdc,
						stringDecoder).toString().equals(ch3));
	}


}
