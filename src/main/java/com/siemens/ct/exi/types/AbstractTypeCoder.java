/*
 * Copyright (c) 2007-2016 Siemens AG
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

package com.siemens.ct.exi.types;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import com.siemens.ct.exi.Constants;
import com.siemens.ct.exi.context.QNameContext;
import com.siemens.ct.exi.datatype.BinaryBase64Datatype;
import com.siemens.ct.exi.datatype.BinaryHexDatatype;
import com.siemens.ct.exi.datatype.BooleanDatatype;
import com.siemens.ct.exi.datatype.Datatype;
import com.siemens.ct.exi.datatype.DatetimeDatatype;
import com.siemens.ct.exi.datatype.DecimalDatatype;
import com.siemens.ct.exi.datatype.EnumerationDatatype;
import com.siemens.ct.exi.datatype.FloatDatatype;
import com.siemens.ct.exi.datatype.IntegerDatatype;
import com.siemens.ct.exi.datatype.ListDatatype;
import com.siemens.ct.exi.datatype.StringDatatype;
import com.siemens.ct.exi.exceptions.EXIException;
import com.siemens.ct.exi.util.xml.QNameUtilities;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.9.7-SNAPSHOT
 */

public abstract class AbstractTypeCoder implements TypeCoder {

	// DTR maps
	protected final QName[] dtrMapTypes;
	protected final QName[] dtrMapRepresentations;
	protected final Map<QName, Datatype> dtrMapRepresentationsDatatype;
	protected Map<QName, Datatype> dtrMap;
	protected final boolean dtrMapInUse;

	public AbstractTypeCoder() throws EXIException {
		this(null, null, null);
	}

	public AbstractTypeCoder(QName[] dtrMapTypes, QName[] dtrMapRepresentations, Map<QName, Datatype> dtrMapRepresentationsDatatype)
			throws EXIException {
		this.dtrMapTypes = dtrMapTypes;
		this.dtrMapRepresentations = dtrMapRepresentations;
		this.dtrMapRepresentationsDatatype = dtrMapRepresentationsDatatype;

		if (dtrMapTypes == null) {
			dtrMapInUse = false;
		} else {
			dtrMapInUse = true;

			dtrMap = new HashMap<QName, Datatype>();
			assert (dtrMapTypes.length == dtrMapRepresentations.length);
			this.initDtrMaps();
		}
	}

	private void initDtrMaps() throws EXIException {
		assert (dtrMapInUse);

		// binary
		dtrMap.put(
				BuiltIn.XSD_BASE64BINARY,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_BASE64BINARY));
		dtrMap.put(
				BuiltIn.XSD_HEXBINARY,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_HEXBINARY));
		// boolean
		dtrMap.put(
				BuiltIn.XSD_BOOLEAN,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_BOOLEAN));
		// date-times
		dtrMap.put(
				BuiltIn.XSD_DATETIME,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_DATETIME));
		dtrMap.put(
				BuiltIn.XSD_TIME,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_TIME));
		dtrMap.put(
				BuiltIn.XSD_DATE,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_DATE));
		dtrMap.put(
				BuiltIn.XSD_GYEARMONTH,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_GYEARMONTH));
		dtrMap.put(
				BuiltIn.XSD_GYEAR,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_GYEAR));
		dtrMap.put(
				BuiltIn.XSD_GMONTHDAY,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_GMONTHDAY));
		dtrMap.put(
				BuiltIn.XSD_GDAY,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_GDAY));
		dtrMap.put(
				BuiltIn.XSD_GMONTH,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_GMONTH));
		// decimal
		dtrMap.put(
				BuiltIn.XSD_DECIMAL,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_DECIMAL));
		// float
		dtrMap.put(
				BuiltIn.XSD_FLOAT,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_DOUBLE));
		dtrMap.put(
				BuiltIn.XSD_DOUBLE,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_DOUBLE));
		// integer
		dtrMap.put(
				BuiltIn.XSD_INTEGER,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_INTEGER));
		// string
		dtrMap.put(
				BuiltIn.XSD_STRING,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_STRING));
		dtrMap.put(
				BuiltIn.XSD_ANY_SIMPLE_TYPE,
				getDatatypeRepresentation(Constants.W3C_EXI_NS_URI,
						Constants.W3C_EXI_LN_STRING));
		// all types derived by union are done differently

		for (int i = 0; i < dtrMapTypes.length; i++) {
			QName dtrMapRepr = dtrMapRepresentations[i];
			Datatype representation = getDatatypeRepresentation(
					dtrMapRepr.getNamespaceURI(), dtrMapRepr.getLocalPart());
			QName type = dtrMapTypes[i];
			dtrMap.put(type, representation);
		}
	}

	protected Datatype getDtrDatatype(final Datatype datatype) {
		assert (dtrMapInUse);

		Datatype dtrDatatype = null;
		if (datatype == BuiltIn.DEFAULT_DATATYPE) {
			// e.g., untyped values are encoded always as String
			dtrDatatype = datatype;
		} else {
			// check mappings
			QName schemaType = datatype.getSchemaType().getQName();
			
			// unions
			if ( dtrDatatype == null &&
					datatype.getBuiltInType() == BuiltInType.STRING
					&& ((StringDatatype) datatype).isDerivedByUnion()) {
				
				if (dtrMap.containsKey(schemaType)) {
					// direct DTR mapping
					dtrDatatype = dtrMap.get(schemaType);
				} else {
					Datatype baseDatatype = datatype.getBaseDatatype();
					QName schemBaseType = baseDatatype.getSchemaType().getQName();
					if(baseDatatype.getBuiltInType() == BuiltInType.STRING && ((StringDatatype) baseDatatype).isDerivedByUnion() &&  dtrMap.containsKey(schemBaseType)) {
						dtrDatatype = dtrMap.get(schemBaseType);
					} else {
						dtrDatatype = datatype;
					}
				}
				
				
//				dtrDatatype = datatype;
//				// union ancestors of interest
//				// Datatype dtBase = qncSchemaType.getSimpleBaseDatatype();
//				Datatype dtBase = datatype.getBaseDatatype();
//
//				if (dtBase != null
//						&& dtBase.getBuiltInType() == BuiltInType.STRING
//						&& ((StringDatatype) dtBase).isDerivedByUnion()) {
//					// check again
//					dtrDatatype = null;
//				}
			}
			// lists
			if ( dtrDatatype == null && 
					datatype.getBuiltInType() == BuiltInType.LIST) {
				
				if (dtrMap.containsKey(schemaType)) {
					// direct DTR mapping
					dtrDatatype = dtrMap.get(schemaType);
				} else {
					ListDatatype ldt = (ListDatatype) datatype;
					Datatype datatypeList = ldt.getListDatatype();
					Datatype dtrDatatypeList = getDtrDatatype(datatypeList);
					if(datatypeList.getBuiltInType() == dtrDatatypeList.getBuiltInType()) {
						dtrDatatype = datatype;
					} else {
						// update DTR for list datatype
						dtrDatatype = new ListDatatype(dtrDatatypeList, ldt.getSchemaType());
					}
				}
				
//				dtrDatatype = datatype;
//				// list ancestors of interest
//				// Datatype dtBase = qncSchemaType.getSimpleBaseDatatype();
//				Datatype dtBase = datatype.getBaseDatatype();
//				if (dtBase != null
//						&& dtBase.getBuiltInType() == BuiltInType.LIST) {
//					// check again
//					dtrDatatype = null;
//				}
			}
			// enums
			if ( dtrDatatype == null && 
					datatype.getBuiltInType() == BuiltInType.ENUMERATION) {
				
				if (dtrMap.containsKey(schemaType)) {
					// direct DTR mapping
					dtrDatatype = dtrMap.get(schemaType);
				} else {
					Datatype baseDatatype = datatype.getBaseDatatype();
					QName schemBaseType = baseDatatype.getSchemaType().getQName();
					if(baseDatatype.getBuiltInType() == BuiltInType.ENUMERATION &&  dtrMap.containsKey(schemBaseType)) {
						dtrDatatype = dtrMap.get(schemBaseType);
					} else {
						dtrDatatype = datatype;
					}
					
//					Datatype dtrDatatypeBase = getDtrDatatype(datatype.getBaseDatatype());
//					dtrDatatype = dtrDatatypeBase;
//					dtrMap.put(schemaType, dtrDatatype);
					
//					EnumerationDatatype edt = (EnumerationDatatype) datatype;
//					Datatype datatypeEnum =  edt.getEnumValueDatatype();
//					Datatype dtrDatatypeEnum = getDtrDatatype(datatypeEnum);
//					if(datatypeEnum.getBuiltInType() == dtrDatatypeEnum.getBuiltInType()) {
//						dtrDatatype = datatype;
//					} else {
//						// update DTR for list datatype
//						dtrDatatype = dtrDatatypeEnum;
//					}
				}
				
//				dtrDatatype = datatype;
//				// only ancestor types that have enums are of interest
//				// Datatype dtBase = qncSchemaType.getSimpleBaseDatatype();
//				Datatype dtBase = datatype.getBaseDatatype();
//				if (dtBase != null
//						&& dtBase.getBuiltInType() == BuiltInType.ENUMERATION) {
//					// check again
//					dtrDatatype = null;
//				}
			}
			
			if (dtrDatatype == null) {
				// QNameContext qncSchemaType = datatype.getSchemaType();
				
				dtrDatatype = dtrMap.get(schemaType);
				
				if (dtrDatatype == null) {
					// no mapping yet
					// dtrDatatype = updateDtrDatatype(qncSchemaType);
					dtrDatatype = updateDtrDatatype(datatype);
//					// special integer handling
//					if (dtrDatatype.getBuiltInType() == BuiltInType.INTEGER
//							&& (datatype.getBuiltInType() == BuiltInType.NBIT_UNSIGNED_INTEGER || datatype
//									.getBuiltInType() == BuiltInType.UNSIGNED_INTEGER)) {
//						dtrDatatype = datatype;
//					}
					// dtrMap.put(qncSchemaType.getQName(), dtrDatatype);
				}
			}

			

		}

//		// list item types
//		assert (dtrDatatype != null);
//		if (dtrDatatype.getBuiltInType() == BuiltInType.LIST) {
//			Datatype prev = dtrDatatype;
//			ListDatatype ldt = (ListDatatype) dtrDatatype;
//			Datatype dtList = ldt.getListDatatype();
//			dtrDatatype = this.getDtrDatatype(dtList);
//			if (dtrDatatype != dtList) {
//				// update item codec
//				dtrDatatype = new ListDatatype(dtrDatatype, ldt.getSchemaType());
//			} else {
//				dtrDatatype = prev;
//			}
//
//		}

		return dtrDatatype;
	}

	
	// protected Datatype updateDtrDatatype(QNameContext qncSchemaType) {
	protected Datatype updateDtrDatatype(Datatype datatype) {
	// protected Datatype updateDtrDatatype(QNameContext qncSchemaType) {
		assert (dtrMapInUse);
		
//		// QNameContext qncSchemaType = datatype.getSchemaType();
//		QNameContext qncBase =  qncSchemaType.getSimpleBaseType();
//		assert(qncSchemaType != null);
//		
//		
////		// special integer handling
////		if (dtrDatatype.getBuiltInType() == BuiltInType.INTEGER
////				&& (datatype.getBuiltInType() == BuiltInType.NBIT_UNSIGNED_INTEGER || datatype
////						.getBuiltInType() == BuiltInType.UNSIGNED_INTEGER)) {
////			dtrDatatype = datatype;
////		}
//		
//		Datatype dt = dtrMap.get(qncBase.getQName());
//		
//		if (dt == null) {
//			// dt = updateDtrDatatype(simpleBaseType);
//			// dt = updateDtrDatatype(qncSchemaType.getSimpleBaseDatatype());
//			dt = updateDtrDatatype(qncBase); //baseDatatype);
////			dtrMap.put(qncBase.getQName(), dt);
//		} else {
//			// save new mapping in map
////			dtrMap.put(qncSchemaType.getQName(), dt);
//		}
//		
//		return dt;
		
		
		
		Datatype baseDatatype = datatype.getBaseDatatype();
		// QNameContext qncSchemaType = datatype.getSchemaType();
		// QNameContext simpleBaseType = qncSchemaType.getSimpleBaseDatatype().getSchemaType();
		QNameContext simpleBaseType = baseDatatype.getSchemaType();
		
		
		Datatype dtrDatatype = dtrMap.get(simpleBaseType.getQName());
		
		if (dtrDatatype == null) {
			// dt = updateDtrDatatype(simpleBaseType);
			// dt = updateDtrDatatype(qncSchemaType.getSimpleBaseDatatype());
			dtrDatatype = updateDtrDatatype(baseDatatype);
		}
		
		// special integer handling
		if ((dtrDatatype.getBuiltInType() == BuiltInType.INTEGER || dtrDatatype.getBuiltInType() == BuiltInType.UNSIGNED_INTEGER)
				&& (datatype.getBuiltInType() == BuiltInType.NBIT_UNSIGNED_INTEGER || datatype
						.getBuiltInType() == BuiltInType.UNSIGNED_INTEGER)) {
			dtrDatatype = datatype;
		}
		
		// save new mapping in map
		dtrMap.put(datatype.getSchemaType().getQName(), dtrDatatype);
		


		return dtrDatatype;
	}

	protected Datatype getDatatypeRepresentation(String reprUri,
			String reprLocalPart) throws EXIException {
		assert (dtrMapInUse);

		try {
			// find datatype for given representation
			Datatype datatype = null;
			if (Constants.W3C_EXI_NS_URI.equals(reprUri)) {
				// EXI built-in datatypes
				// see http://www.w3.org/TR/exi/#builtInEXITypes
				if ("base64Binary".equals(reprLocalPart)) {
					datatype = new BinaryBase64Datatype(null);
				} else if ("hexBinary".equals(reprLocalPart)) {
					datatype = new BinaryHexDatatype(null);
				} else if ("boolean".equals(reprLocalPart)) {
					datatype = new BooleanDatatype(null);
				} else if ("dateTime".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.dateTime, null);
				} else if ("time".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.time, null);
				} else if ("date".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.date, null);
				} else if ("gYearMonth".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.gYearMonth,
							null);
				} else if ("gYear".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.gYear, null);
				} else if ("gMonthDay".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.gMonthDay,
							null);
				} else if ("gDay".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.gDay, null);
				} else if ("gMonth".equals(reprLocalPart)) {
					datatype = new DatetimeDatatype(DateTimeType.gMonth, null);
				} else if ("decimal".equals(reprLocalPart)) {
					datatype = new DecimalDatatype(null);
				} else if ("double".equals(reprLocalPart)) {
					datatype = new FloatDatatype(null);
				} else if ("integer".equals(reprLocalPart)) {
					datatype = new IntegerDatatype(null);
				} else if ("string".equals(reprLocalPart)) {
					datatype = new StringDatatype(null);
				} else {
					throw new EXIException(
							"[EXI] Unsupported datatype representation: {"
									+ reprUri + "}" + reprLocalPart);
				}
			} else {
				// try to load datatype
				QName qn = new QName(reprUri, reprLocalPart);
				if(this.dtrMapRepresentationsDatatype != null) {
					datatype = this.dtrMapRepresentationsDatatype.get(qn);
				}
				if(datatype == null) {
					// final try: load class with this qname
					String className = QNameUtilities.getClassName(qn);
					@SuppressWarnings("rawtypes")
					Class c = Class.forName(className);
					Object o = c.newInstance();
					if (o instanceof Datatype) {
						datatype = (Datatype) o;
					} else {
						throw new Exception("[EXI] no Datatype instance");
					}
				}
			}

			return datatype;
		} catch (Exception e) {
			throw new EXIException(e);
		}
	}
}
