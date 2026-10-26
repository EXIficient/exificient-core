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
import com.siemens.ct.exi.core.datatype.Datatype;
import com.siemens.ct.exi.core.datatype.StringDatatype;
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
 * With the strict grammars of
 * {@code <xs:element name="r"><xs:complexType><xs:simpleContent>
 * <xs:extension base="xs:string"><xs:attribute name="k" type="xs:string" use="required"/>
 * </xs:extension></xs:simpleContent></xs:complexType></xs:element>} and a
 * block size of 1, the value of {@code k} closes the first block. The
 * characters of {@code r} and the following end events take zero bits, so the
 * second block has an empty structure channel and one value.
 */
public class EmptyStructureBlockCoreTest extends TestCase {

	public void testPreCompression() throws Exception {
		assertRoundTrip(CodingMode.PRE_COMPRESSION);
	}

	public void testCompression() throws Exception {
		assertRoundTrip(CodingMode.COMPRESSION);
	}

	private static void assertRoundTrip(CodingMode codingMode)
			throws Exception {
		EXIFactory factory = DefaultEXIFactory.newInstance();
		factory.setGrammars(createGrammars());
		factory.setFidelityOptions(FidelityOptions.createStrict());
		factory.setCodingMode(codingMode);
		factory.setBlockSize(1);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		EXIBodyEncoder encoder = factory.createEXIBodyEncoder();
		encoder.setOutputStream(baos);
		encoder.encodeStartDocument();
		encoder.encodeStartElement("", "r", null);
		encoder.encodeAttribute("", "k", null, new StringValue("a"));
		encoder.encodeCharacters(new StringValue("b"));
		encoder.encodeEndElement();
		encoder.encodeEndDocument();
		encoder.flush();

		EXIBodyDecoder decoder = factory.createEXIBodyDecoder();
		decoder.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
		assertEquals(EventType.START_DOCUMENT, decoder.next());
		decoder.decodeStartDocument();
		assertEquals(EventType.START_ELEMENT, decoder.next());
		assertEquals(new QName("", "r"), decoder.decodeStartElement()
				.getQName());
		assertEquals(EventType.ATTRIBUTE, decoder.next());
		assertEquals(new QName("", "k"), decoder.decodeAttribute().getQName());
		assertEquals("a", decoder.getAttributeValue().toString());
		assertEquals(EventType.CHARACTERS, decoder.next());
		assertEquals("b", decoder.decodeCharacters().toString());
		assertEquals(EventType.END_ELEMENT, decoder.next());
		decoder.decodeEndElement();
		assertEquals(EventType.END_DOCUMENT, decoder.next());
		decoder.decodeEndDocument();
	}

	/**
	 * Builds the grammars exificient-grammars derives from the schema above.
	 */
	private static SchemaInformedGrammars createGrammars() {
		QNameContext k = new QNameContext(0, 0, new QName("", "k"));
		QNameContext r = new QNameContext(0, 1, new QName("", "r"));
		QNameContext[] xsdNames = qNames(3, Constants.XML_SCHEMA_NS_URI,
				Constants.LOCAL_NAMES_XSD);
		GrammarUriContext[] uris = {
				new GrammarUriContext(0, "", new QNameContext[] { k, r }),
				new GrammarUriContext(1, Constants.XML_NS_URI, qNames(1,
						Constants.XML_NS_URI, Constants.LOCAL_NAMES_XML)),
				new GrammarUriContext(2,
						Constants.XML_SCHEMA_INSTANCE_NS_URI, qNames(2,
								Constants.XML_SCHEMA_INSTANCE_NS_URI,
								Constants.LOCAL_NAMES_XSI)),
				new GrammarUriContext(3, Constants.XML_SCHEMA_NS_URI, xsdNames) };
		Datatype string = new StringDatatype(
				xsdNames[Arrays.asList(Constants.LOCAL_NAMES_XSD).indexOf(
						"string")]);

		// r: AT(k) StartTag, CH EE
		SchemaInformedElement end = new SchemaInformedElement();
		end.addTerminalProduction(new EndElement());
		SchemaInformedStartTag content = new SchemaInformedStartTag(end);
		content.addProduction(new Characters(string), end);
		SchemaInformedFirstStartTag firstStartTag = new SchemaInformedFirstStartTag(
				end);
		firstStartTag.addProduction(new Attribute(k, string), content);
		StartElement seR = new StartElement(r);
		seR.setGrammar(firstStartTag);
		r.setGlobalStartElement(seR);

		DocEnd docEnd = new DocEnd("DocEnd");
		docEnd.addTerminalProduction(new EndDocument());
		SchemaInformedGrammar docContent = new SchemaInformedDocContent(
				"DocContent");
		docContent.addProduction(new StartElementGeneric(), docEnd);
		docContent.addProduction(seR, docEnd);
		Document document = new Document("Document");
		document.addProduction(new StartDocument(), docContent);

		int qNames = 2 + Constants.LOCAL_NAMES_XML.length
				+ Constants.LOCAL_NAMES_XSI.length + xsdNames.length;
		return new SchemaInformedGrammars(new GrammarContext(uris, qNames),
				document, null, null);
	}

	private static QNameContext[] qNames(int uriId, String uri,
			String[] localNames) {
		QNameContext[] qNames = new QNameContext[localNames.length];
		for (int i = 0; i < localNames.length; i++) {
			qNames[i] = new QNameContext(uriId, i,
					new QName(uri, localNames[i]));
		}
		return qNames;
	}
}
