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

package com.siemens.ct.exi.core;

import java.util.HashMap;
import java.util.Map;

import com.siemens.ct.exi.core.exceptions.UnsupportedOption;

/**
 * Some applications may require EXI coding options shared via the EXI Header
 * (e.g., EXI Cookie). This class allows one to specify which coding options are
 * needed.
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 1.0.0-SNAPSHOT
 */

public class EncodingOptions {

	/** Include EXI Cookie */
	public static final String INCLUDE_COOKIE = "INCLUDE_COOKIE";

	/** Include EXI Options (or opposite of omitOptionsDocument in Canonical EXI) */
	public static final String INCLUDE_OPTIONS = "INCLUDE_OPTIONS";

	/** Include schemaID as part of EXI Options */
	public static final String INCLUDE_SCHEMA_ID = "INCLUDE_SCHEMA_ID";

	/** Encode entity references as ER event instead of trying to resolve them */
	public static final String RETAIN_ENTITY_REFERENCE = "KEEP_ENTITY_REFERENCES_UNRESOLVED";

	/** Include attribute "schemaLocation" and "noNamespaceSchemaLocation" */
	public static final String INCLUDE_XSI_SCHEMALOCATION = "INCLUDE_XSI_SCHEMALOCATION";

	/** Include Insignificant xsi:nil values e.g., xsi:nil="false" */
	public static final String INCLUDE_INSIGNIFICANT_XSI_NIL = "INCLUDE_INSIGNIFICANT_XSI_NIL";

	/**
	 * To indicate that the EXI profile is in use and advertising each parameter
	 * value (exi:p element)
	 */
	public static final String INCLUDE_PROFILE_VALUES = "INCLUDE_PROFILE_VALUES";

	/**
	 * The utcTime option is used to specify whether Date-Time values must be
	 * represented using Coordinated Universal Time (UTC, sometimes called
	 * "Greenwich Mean Time").
	 */
	public static final String UTC_TIME = "UTC_TIME";

	/**
	 * To indicate that the EXI stream should respect the Canonical EXI rules (see http://www.w3.org/TR/exi-c14n)
	 * @see INCLUDE_OPTIONS
	 * @see UTC_TIME
	 */
	public static final String CANONICAL_EXI = "http://www.w3.org/TR/exi-c14n";

	/**
	 * To set the deflate stream with a specified compression level.
	 * 
	 * @see java.util.zip.Deflater#setLevel(int)
	 */
	public static final String DEFLATE_COMPRESSION_VALUE = "DEFLATE_COMPRESSION_VALUE";

	/* contains options and according values */
	protected Map<String, Object> options;

	protected EncodingOptions() {
		options = new HashMap<String, Object>();
	}

	/**
	 * Creates encoding options using default options (NO Cookie, option or
	 * schemaID).
	 * 
	 * @return default encoding options
	 */
	public static EncodingOptions createDefault() {
		EncodingOptions ho = new EncodingOptions();
		return ho;
	}

	/**
	 * Enables given option.
	 * 
	 * <p>
	 * Note: Some options (e.g. INCLUDE_SCHEMA_ID) will only take effect if the
	 * EXI options document is set to encode options in general (see
	 * INCLUDE_OPTIONS).
	 * </p>
	 * 
	 * @param key
	 *            referring to a specific option
	 * 
	 * @throws UnsupportedOption
	 *             if option is not supported
	 */
	public void setOption(String key) throws UnsupportedOption {
		setOption(key, null);
	}

	/**
	 * Enables given option with value.
	 * 
	 * <p>
	 * Note: Some options (e.g. INCLUDE_SCHEMA_ID) will only take effect if the
	 * EXI options document is set to encode options in general (see
	 * INCLUDE_OPTIONS).
	 * </p>
	 * 
	 * @param key
	 *            referring to a specific option
	 * @param value
	 *            specific option value
	 * 
	 * @throws UnsupportedOption
	 *             if option is not supported
	 */
	public void setOption(String key, Object value) throws UnsupportedOption {
		if (key.equals(INCLUDE_COOKIE)) {
			options.put(key, null);
		} else if (key.equals(INCLUDE_OPTIONS)) {
			options.put(key, null);
		} else if (key.equals(INCLUDE_SCHEMA_ID)) {
			options.put(key, null);
		} else if (key.equals(RETAIN_ENTITY_REFERENCE)) {
			options.put(key, null);
		} else if (key.equals(INCLUDE_XSI_SCHEMALOCATION)) {
			options.put(key, null);
		} else if (key.equals(INCLUDE_INSIGNIFICANT_XSI_NIL)) {
			options.put(key, null);
		} else if (key.equals(INCLUDE_PROFILE_VALUES)) {
			options.put(key, null);
		} else if (key.equals(CANONICAL_EXI)) {
			options.put(key, null);
			// by default the Canonical EXI Option "omitOptionsDocument" is false
			// --> include options
			this.setOption(INCLUDE_OPTIONS);
		} else if (key.equals(UTC_TIME)) {
			options.put(key, null);
		} else if (key.equals(DEFLATE_COMPRESSION_VALUE)) {
			if (value != null && value instanceof Integer) {
				options.put(key, value);
			} else {
				throw new UnsupportedOption("EncodingOption '" + key
						+ "' requires value of type Integer");
			}
		} else {
			throw new UnsupportedOption("EncodingOption '" + key
					+ "' is unknown!");
		}
	}

	/**
	 * Disables given option.
	 * 
	 * @param key
	 *            referring to a specific option
	 * @return whether option was set
	 * 
	 */
	public boolean unsetOption(String key) {
		// we do have null values --> check for key
		boolean b = options.containsKey(key);
		options.remove(key);
		return b;
	}

	/**
	 * Informs whether the specified option is enabled.
	 * 
	 * @param key
	 *            feature
	 * @return whether option is turned on
	 */
	public boolean isOptionEnabled(String key) {
		return options.containsKey(key);
	}

	/**
	 * Returns the specified option value.
	 * 
	 * @param key
	 *            feature
	 * @return whether option is turned on
	 */
	public Object getOptionValue(String key) {
		return options.get(key);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EncodingOptions) {
			EncodingOptions other = (EncodingOptions) o;
			return options.equals(other.options);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return options.hashCode();
	}

	@Override
	public String toString() {
		return options.toString();
	}

}
