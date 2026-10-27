/*
 * Copyright (c) 2026 Siemens AG
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

package com.siemens.ct.exi.core.coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import com.siemens.ct.exi.core.CodingMode;
import com.siemens.ct.exi.core.Constants;
import com.siemens.ct.exi.core.EXIBodyDecoder;
import com.siemens.ct.exi.core.EXIBodyEncoder;
import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.FidelityOptions;
import com.siemens.ct.exi.core.context.GrammarContext;
import com.siemens.ct.exi.core.context.GrammarUriContext;
import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.BooleanDatatype;
import com.siemens.ct.exi.core.datatype.DecimalDatatype;
import com.siemens.ct.exi.core.grammars.SchemaInformedGrammars;
import com.siemens.ct.exi.core.grammars.event.Attribute;
import com.siemens.ct.exi.core.grammars.event.Characters;
import com.siemens.ct.exi.core.grammars.event.EndDocument;
import com.siemens.ct.exi.core.grammars.event.EndElement;
import com.siemens.ct.exi.core.grammars.event.EventType;
import com.siemens.ct.exi.core.grammars.event.StartDocument;
import com.siemens.ct.exi.core.grammars.event.StartElement;
import com.siemens.ct.exi.core.grammars.event.StartElementGeneric;
import com.siemens.ct.exi.core.grammars.grammar.DocEnd;
import com.siemens.ct.exi.core.grammars.grammar.Document;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedDocContent;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedElement;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedFirstStartTag;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedGrammar;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedStartTag;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.core.values.StringValue;

/**
 * A block whose structure channel takes no bytes must still carry its values
 * (issue #13).
 * <p>
 * Schema and document from the issue:
 * 
 * <pre>
 * &lt;xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="urn:m" xmlns="urn:m" elementFormDefault="qualified"&gt;
 *   &lt;xs:element name="r"&gt;&lt;xs:complexType&gt;&lt;xs:sequence&gt;
 *     &lt;xs:element name="i"&gt;&lt;xs:complexType&gt;&lt;xs:simpleContent&gt;&lt;xs:extension base="xs:decimal"&gt;
 *       &lt;xs:attribute name="k" type="xs:boolean"/&gt;
 *     &lt;/xs:extension&gt;&lt;/xs:simpleContent&gt;&lt;/xs:complexType&gt;&lt;/xs:element&gt;
 *   &lt;/xs:sequence&gt;&lt;/xs:complexType&gt;&lt;/xs:element&gt;
 * &lt;/xs:schema&gt;
 * 
 * &lt;r xmlns="urn:m"&gt;&lt;i k="true"&gt;1&lt;/i&gt;&lt;/r&gt;
 * </pre>
 * 
 * With strict grammars and a block size of 1, the value of {@code k} closes
 * the first block. The characters of {@code i} take zero bits, so the second
 * block has an empty structure channel and the value {@code 1}.
 */
public class EmptyStructureBlockCoreTest extends TestCase {

	private static final String NS = "urn:m";

	public void testPreCompression() throws Exception {
		assertEncoding(CodingMode.PRE_COMPRESSION, "80000001000100");
	}

	public void testCompression() throws Exception {
		assertEncoding(CodingMode.COMPRESSION, "8063606004006360640000");
	}

	private static void assertEncoding(CodingMode codingMode, String expected)
			throws Exception {
		EXIFactory factory = DefaultEXIFactory.newInstance();
		factory.setGrammars(createGrammars());
		factory.setFidelityOptions(FidelityOptions.createStrict());
		factory.setCodingMode(codingMode);
		factory.setBlockSize(1);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		EXIBodyEncoder encoder = factory.createEXIStreamEncoder().encodeHeader(
				baos);
		encoder.encodeStartDocument();
		encoder.encodeStartElement(NS, "r", null);
		encoder.encodeStartElement(NS, "i", null);
		encoder.encodeAttribute("", "k", null, new StringValue("true"));
		encoder.encodeCharacters(new StringValue("1"));
		encoder.encodeEndElement();
		encoder.encodeEndElement();
		encoder.encodeEndDocument();
		encoder.flush();
		assertEquals(expected, toHex(baos.toByteArray()));

		EXIBodyDecoder decoder = factory.createEXIStreamDecoder().decodeHeader(
				new ByteArrayInputStream(baos.toByteArray()));
		assertEquals(EventType.START_DOCUMENT, decoder.next());
		decoder.decodeStartDocument();
		assertEquals(EventType.START_ELEMENT, decoder.next());
		assertEquals(new QName(NS, "r"), decoder.decodeStartElement()
				.getQName());
		assertEquals(EventType.START_ELEMENT, decoder.next());
		assertEquals(new QName(NS, "i"), decoder.decodeStartElement()
				.getQName());
		assertEquals(EventType.ATTRIBUTE, decoder.next());
		assertEquals(new QName("", "k"), decoder.decodeAttribute().getQName());
		assertEquals("true", decoder.getAttributeValue().toString());
		assertEquals(EventType.CHARACTERS, decoder.next());
		assertEquals("1.0", decoder.decodeCharacters().toString());
		assertEquals(EventType.END_ELEMENT, decoder.next());
		decoder.decodeEndElement();
		assertEquals(EventType.END_ELEMENT, decoder.next());
		decoder.decodeEndElement();
		assertEquals(EventType.END_DOCUMENT, decoder.next());
		decoder.decodeEndDocument();
	}

	/**
	 * Builds the strict grammars exificient-grammars derives from the schema.
	 */
	private static SchemaInformedGrammars createGrammars() {
		QNameContext k = new QNameContext(0, 0, new QName("", "k"));
		QNameContext i = new QNameContext(4, 0, new QName(NS, "i"));
		QNameContext r = new QNameContext(4, 1, new QName(NS, "r"));
		QNameContext[] xsdNames = qNames(3, Constants.XML_SCHEMA_NS_URI,
				Constants.LOCAL_NAMES_XSD);
		GrammarUriContext[] uris = {
				new GrammarUriContext(0, "", new QNameContext[] { k }),
				new GrammarUriContext(1, Constants.XML_NS_URI, qNames(1,
						Constants.XML_NS_URI, Constants.LOCAL_NAMES_XML)),
				new GrammarUriContext(2,
						Constants.XML_SCHEMA_INSTANCE_NS_URI, qNames(2,
								Constants.XML_SCHEMA_INSTANCE_NS_URI,
								Constants.LOCAL_NAMES_XSI)),
				new GrammarUriContext(3, Constants.XML_SCHEMA_NS_URI, xsdNames),
				new GrammarUriContext(4, NS, new QNameContext[] { i, r }) };
		BooleanDatatype xsBoolean = new BooleanDatatype(xsdName(xsdNames,
				"boolean"));
		DecimalDatatype xsDecimal = new DecimalDatatype(xsdName(xsdNames,
				"decimal"));

		// i: AT(k) or CH, then CH, then EE
		SchemaInformedElement iEnd = new SchemaInformedElement();
		iEnd.addTerminalProduction(new EndElement());
		SchemaInformedStartTag iContent = new SchemaInformedStartTag(iEnd);
		iContent.addProduction(new Characters(xsDecimal), iEnd);
		SchemaInformedFirstStartTag iStart = new SchemaInformedFirstStartTag(
				iEnd);
		iStart.addProduction(new Attribute(k, xsBoolean), iContent);
		iStart.addProduction(new Characters(xsDecimal), iEnd);
		StartElement seI = new StartElement(i);
		seI.setGrammar(iStart);

		// r: SE(i), then EE
		SchemaInformedElement rEnd = new SchemaInformedElement();
		rEnd.addTerminalProduction(new EndElement());
		SchemaInformedFirstStartTag rStart = new SchemaInformedFirstStartTag(
				rEnd);
		rStart.addProduction(seI, rEnd);
		StartElement seR = new StartElement(r);
		seR.setGrammar(rStart);
		r.setGlobalStartElement(seR);

		DocEnd docEnd = new DocEnd("DocEnd");
		docEnd.addTerminalProduction(new EndDocument());
		SchemaInformedGrammar docContent = new SchemaInformedDocContent(
				"DocContent");
		docContent.addProduction(new StartElementGeneric(), docEnd);
		docContent.addProduction(seR, docEnd);
		Document document = new Document("Document");
		document.addProduction(new StartDocument(), docContent);

		int qNames = 3 + Constants.LOCAL_NAMES_XML.length
				+ Constants.LOCAL_NAMES_XSI.length + xsdNames.length;
		return new SchemaInformedGrammars(new GrammarContext(uris, qNames),
				document, null, null);
	}

	private static QNameContext[] qNames(int uriId, String uri,
			String[] localNames) {
		QNameContext[] qNames = new QNameContext[localNames.length];
		for (int n = 0; n < localNames.length; n++) {
			qNames[n] = new QNameContext(uriId, n, new QName(uri,
					localNames[n]));
		}
		return qNames;
	}

	private static QNameContext xsdName(QNameContext[] xsdNames,
			String localName) {
		return xsdNames[Arrays.asList(Constants.LOCAL_NAMES_XSD).indexOf(
				localName)];
	}

	private static String toHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (byte b : bytes) {
			hex.append(String.format("%02x", b));
		}
		return hex.toString();
	}
}
