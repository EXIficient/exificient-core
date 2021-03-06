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

import javax.xml.namespace.QName;

import com.siemens.ct.exi.core.Constants;
import com.siemens.ct.exi.core.context.QNameContext;
import com.siemens.ct.exi.core.datatype.Datatype;
import com.siemens.ct.exi.core.datatype.StringDatatype;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public class BuiltIn {

	/*
	 * Binary
	 */
	public static final QName XSD_BASE64BINARY = new QName(
			Constants.XML_SCHEMA_NS_URI, "base64Binary");
	public static final QName XSD_HEXBINARY = new QName(
			Constants.XML_SCHEMA_NS_URI, "hexBinary");
	/*
	 * Boolean
	 */
	public static final QName XSD_BOOLEAN = new QName(
			Constants.XML_SCHEMA_NS_URI, "boolean");
	/*
	 * Date-Time
	 */
	public static final QName XSD_DATETIME = new QName(
			Constants.XML_SCHEMA_NS_URI, "dateTime");
	public static final QName XSD_TIME = new QName(Constants.XML_SCHEMA_NS_URI,
			"time");
	public static final QName XSD_DATE = new QName(Constants.XML_SCHEMA_NS_URI,
			"date");
	public static final QName XSD_GYEARMONTH = new QName(
			Constants.XML_SCHEMA_NS_URI, "gYearMonth");
	public static final QName XSD_GYEAR = new QName(
			Constants.XML_SCHEMA_NS_URI, "gYear");
	public static final QName XSD_GMONTHDAY = new QName(
			Constants.XML_SCHEMA_NS_URI, "gMonthDay");
	public static final QName XSD_GDAY = new QName(Constants.XML_SCHEMA_NS_URI,
			"gDay");
	public static final QName XSD_GMONTH = new QName(
			Constants.XML_SCHEMA_NS_URI, "gMonth");

	/*
	 * Decimal
	 */
	public static final QName XSD_DECIMAL = new QName(
			Constants.XML_SCHEMA_NS_URI, "decimal");
	/*
	 * Float
	 */
	public static final QName XSD_FLOAT = new QName(
			Constants.XML_SCHEMA_NS_URI, "float");
	public static final QName XSD_DOUBLE = new QName(
			Constants.XML_SCHEMA_NS_URI, "double");
	/*
	 * Integer
	 */
	public static final QName XSD_INTEGER = new QName(
			Constants.XML_SCHEMA_NS_URI, "integer");
	public static final QName XSD_NON_NEGATIVE_INTEGER = new QName(
			Constants.XML_SCHEMA_NS_URI, "nonNegativeInteger");
	/*
	 * String
	 */
	public static final QName XSD_STRING = new QName(
			Constants.XML_SCHEMA_NS_URI, "string");
	//
	public static final QName XSD_ANY_SIMPLE_TYPE = new QName(
			Constants.XML_SCHEMA_NS_URI, "anySimpleType");

	/*
	 * Misc
	 */
	public static final QName XSD_QNAME = new QName(
			Constants.XML_SCHEMA_NS_URI, "QName");
	public static final QName XSD_NOTATION = new QName(
			Constants.XML_SCHEMA_NS_URI, "Notation");

	/*
	 * default QName / BuiltInType / Datatype
	 */
	public static final QName DEFAULT_VALUE_NAME = XSD_STRING;

	private static final Datatype DEFAULT_DATATYPE = new StringDatatype(
			new QNameContext(-1, -1, new QName("")));

	public static Datatype getDefaultDatatype() {
		return DEFAULT_DATATYPE;
	}
}
