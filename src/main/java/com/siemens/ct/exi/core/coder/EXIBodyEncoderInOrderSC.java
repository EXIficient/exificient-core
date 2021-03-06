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

package com.siemens.ct.exi.core.coder;

import java.io.IOException;

import javax.xml.namespace.QName;

import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.exceptions.ErrorHandler;
import com.siemens.ct.exi.core.grammars.event.EventType;
import com.siemens.ct.exi.core.values.Value;

/**
 * EXI encoder for bit or byte-aligned streams and possible SELF_CONTAINED
 * elements.
 * 
 * <p>
 * All productions of the form LeftHandSide : SC Fragment are evaluated as
 * follows:
 * </p>
 * <ol>
 * <li>Save the string table, grammars and any implementation-specific state
 * learned while processing this EXI Body.</li>
 * <li>Initialize the string table, grammars and any implementation-specific
 * state learned while processing this EXI Body to the state they held just
 * prior to processing this EXI Body.</li>
 * <li>Skip to the next byte-aligned boundary in the stream if it is not already
 * at such a boundary.</li>
 * <li>Let qname be the qname of the SE event immediately preceding this SC
 * event.</li>
 * <li>Let content be the sequence of events following this SC event that match
 * the grammar for element qname, up to and including the terminating EE event.</li>
 * <li>Evaluate the sequence of events (SD, SE(qname), content, ED) according to
 * the Fragment grammar.</li>
 * <li>Skip to the next byte-aligned boundary in the stream if it is not already
 * at such a boundary.</li>
 * <li>Restore the string table, grammars and implementation-specific state
 * learned while processing this EXI Body to that saved in step 1 above.</li>
 * </ol>
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public class EXIBodyEncoderInOrderSC extends EXIBodyEncoderInOrder {

	protected EXIBodyEncoderInOrderSC scEncoder;

	public EXIBodyEncoderInOrderSC(EXIFactory exiFactory) throws EXIException {
		super(exiFactory);
	}

	@Override
	public void initForEachRun() throws EXIException, IOException {
		super.initForEachRun();

		// clear possibly remaining encoder
		scEncoder = null;
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		if (scEncoder == null) {
			super.setErrorHandler(errorHandler);
		} else {
			scEncoder.setErrorHandler(errorHandler);
		}
	}

	@Override
	public void encodeStartDocument() throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeStartDocument();
		} else {
			scEncoder.encodeStartDocument();
		}
	}

	@Override
	public void encodeEndDocument() throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeEndDocument();
		} else {
			scEncoder.encodeEndDocument();
		}
	}

	protected void encodeEndSC() throws EXIException, IOException {
		// end SC fragment
		scEncoder.encodeEndDocument();
		// Skip to the next byte-aligned boundary in the stream if it is
		// not already at such a boundary
		this.channel.align();
		// indicate that SC portion is over
		scEncoder = null;
		super.popElement();

		// NOTE: NO outer EE
		// Spec says
		// "Evaluate the sequence of events (SD, SE(qname), content, ED) .."
		// e.g., "sc" is self-Contained element
		// Sequence: <sc>foo</sc>
		// --> SE(sc) --> SC --> SD --> SE(sc) --> CH --> EE --> ED
		// content == SE(sc) --> CH --> EE
	}

	@Override
	public void encodeStartElement(String uri, String localName, String prefix)
			throws EXIException, IOException {
		// QName qname;

		// business as usual
		if (scEncoder == null) {
			super.encodeStartElement(uri, localName, prefix);
			QName qname = getElementContext().qnameContext.getQName();

			// start SC fragment ?
			if (exiFactory.isSelfContainedElement(qname)) {
				int ec2 = fidelityOptions.get2ndLevelEventCode(
						EventType.SELF_CONTAINED, getCurrentGrammar());
				encode2ndLevelEventCode(ec2);

				// Skip to the next byte-aligned boundary in the stream if it is
				// not already at such a boundary
				this.channel.align();

				// infor
				if (exiFactory.getSelfContainedHandler() != null) {
					exiFactory.getSelfContainedHandler().scElement(uri,
							localName, this.channel);
				}

				// start SC element
				this.encodeStartSC(uri, localName, prefix);
			}
		} else {
			scEncoder.encodeStartElement(uri, localName, prefix);
			// qname = scEncoder.elementContext.eqname.getQName();
		}
	}

	protected void encodeStartSC(String uri, String localName, String prefix)
			throws EXIException, IOException {
		// SC Factory & Encoder
		EXIFactory scEXIFactory = exiFactory.clone();
		scEXIFactory.setFragment(true);
		scEncoder = (EXIBodyEncoderInOrderSC) scEXIFactory
				.createEXIBodyEncoder();
		scEncoder.channel = this.channel;
		scEncoder.setErrorHandler(this.errorHandler);

		// Evaluate the sequence of events (SD, SE(qname), content, ED)
		// according to the Fragment grammar
		scEncoder.encodeStartDocument();
		// NO SC again
		scEncoder.encodeStartElementNoSC(uri, localName, prefix);
		// from now on events are forwarded to the scEncoder
		if (preservePrefix) {
			// encode NS inner declaration for SE
			scEncoder.encodeNamespaceDeclaration(uri, prefix);
		}
	}

	protected void encodeStartElementNoSC(String uri, String localName,
			String prefix) throws EXIException, IOException {
		super.encodeStartElement(uri, localName, prefix);
	}

	@Override
	public void encodeEndElement() throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeEndElement();
		} else {
			// fetch qname before EE
			QName qname = scEncoder.getElementContext().qnameContext.getQName();
			// EE
			scEncoder.encodeEndElement();
			// if (getElementContextQName().equals(qname)
			if (getElementContext().qnameContext.getQName().equals(qname)
					&& scEncoder.getCurrentGrammar().getProduction(
							EventType.END_DOCUMENT) != null) {
				this.encodeEndSC();
			}
		}
	}

	@Override
	public void encodeAttribute(String uri, String localName, String prefix,
			Value value) throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeAttribute(uri, localName, prefix, value);
		} else {
			scEncoder.encodeAttribute(uri, localName, prefix, value);
		}
	}

	@Override
	public void encodeAttribute(QName at, Value value) throws EXIException,
			IOException {
		if (scEncoder == null) {
			super.encodeAttribute(at, value);
		} else {
			scEncoder.encodeAttribute(at, value);
		}
	}

	@Override
	public void encodeNamespaceDeclaration(String uri, String prefix)
			throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeNamespaceDeclaration(uri, prefix);
		} else {
			scEncoder.encodeNamespaceDeclaration(uri, prefix);
		}
	}

	@Override
	public void encodeAttributeXsiNil(Value nil, String pfx)
			throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeAttributeXsiNil(nil, pfx);
		} else {
			scEncoder.encodeAttributeXsiNil(nil, pfx);
		}
	}

	@Override
	public void encodeAttributeXsiType(Value type, String pfx)
			throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeAttributeXsiType(type, pfx);
		} else {
			scEncoder.encodeAttributeXsiType(type, pfx);
		}
	}

	@Override
	public void encodeCharacters(Value chars) throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeCharacters(chars);
		} else {
			scEncoder.encodeCharacters(chars);
		}
	}

	@Override
	public void encodeDocType(String name, String publicID, String systemID,
			String text) throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeDocType(name, publicID, systemID, text);
		} else {
			scEncoder.encodeDocType(name, publicID, systemID, text);
		}
	}

	@Override
	public void encodeEntityReference(String name) throws EXIException,
			IOException {
		if (scEncoder == null) {
			super.encodeEntityReference(name);
		} else {
			scEncoder.encodeEntityReference(name);
		}
	}

	@Override
	public void encodeComment(char[] ch, int start, int length)
			throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeComment(ch, start, length);
		} else {
			scEncoder.encodeComment(ch, start, length);
		}
	}

	@Override
	public void encodeProcessingInstruction(String target, String data)
			throws EXIException, IOException {
		if (scEncoder == null) {
			super.encodeProcessingInstruction(target, data);
		} else {
			scEncoder.encodeProcessingInstruction(target, data);
		}
	}
}
