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
import java.util.List;

import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.strings.ExtendedStringEncoderImpl;
import com.siemens.ct.exi.core.datatype.strings.StringDecoder;
import com.siemens.ct.exi.core.datatype.strings.StringDecoderImpl;
import com.siemens.ct.exi.core.datatype.strings.StringEncoder;
import com.siemens.ct.exi.core.datatype.strings.StringEncoderImpl;
import com.siemens.ct.exi.core.io.channel.DecoderChannel;
import com.siemens.ct.exi.core.io.channel.EncoderChannel;
import com.siemens.ct.exi.core.types.BuiltInType;
import com.siemens.ct.exi.core.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0-SNAPSHOT
 */

public class ExtendedStringDatatype extends AbstractDatatype {

//	protected final boolean isDerivedByUnion;

	protected String lastValue;
	
	protected List<String> sharedStrings;
	protected EnumDatatype grammarStrings;

	public ExtendedStringDatatype(QNameContext schemaType) {
		// default whiteSpace facet for string is preserve
		this(schemaType, WhiteSpace.preserve);
	}
	
	public ExtendedStringDatatype(QNameContext schemaType, WhiteSpace whiteSpace) {
//		this(schemaType, false);
		super(BuiltInType.EXTENDED_STRING, schemaType);
		
		/* default whiteSpace facet for string is preserve */
		this.whiteSpace = whiteSpace;
	}
	
	public void setSharedStrings(List<String> sharedStrings) {
		this.sharedStrings = sharedStrings;
	}
	
	public void setGrammarStrings(EnumDatatype grammarStrings) {
		this.grammarStrings = grammarStrings;
	}
	
	public EnumDatatype getGrammarStrings() {
		return this.grammarStrings;
	}

	public DatatypeID getDatatypeID() {
		return DatatypeID.exi_estring;
	}

//	public ExtendedStringDatatype(QNameContext schemaType, boolean isDerivedByUnion) {
//		super(BuiltInType.STRING, schemaType);
//		this.isDerivedByUnion = isDerivedByUnion;
//	}
//
//	public boolean isDerivedByUnion() {
//		return isDerivedByUnion;
//	}

	public boolean isValid(Value value) {
		lastValue = value.toString();
		return true;
	}

	public void writeValue(QNameContext qnContext, EncoderChannel valueChannel,
			StringEncoder stringEncoder) throws IOException {
		ExtendedStringEncoderImpl ese = new ExtendedStringEncoderImpl((StringEncoderImpl) stringEncoder);
		ese.setGrammarStrings(this.grammarStrings);
		
		ese.writeValue(qnContext, valueChannel, this.lastValue);
	}

//	public Value readValue(QNameContext qnContext, DecoderChannel valueChannel,
//			StringDecoder stringDecoder) throws IOException {
//		ExtendedStringDecoderImpl ese = new ExtendedStringDecoderImpl((StringDecoderImpl) stringDecoder);
//		ese.setGrammarStrings(this.grammarStrings);
//		
//		return ese.readValue(qnContext, valueChannel);
//	}
}

