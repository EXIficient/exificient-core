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

/**
 * The alignment option is used to control the alignment of event codes and
 * content items.
 * 
 * <p>
 * The value is one of bit-packed, byte-alignment, pre-compression, or
 * compression.
 * </p>
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public enum CodingMode {
	/**
	 * Alignment option value bit-packed indicates that the the event codes and
	 * associated content are packed in bits without any paddings in-between.
	 */
	BIT_PACKED,

	/**
	 * Alignment option value byte-alignment indicates that the event codes and
	 * associated content are aligned on byte boundaries. While byte-alignment
	 * generally results in EXI streams of larger sizes compared with their
	 * bit-packed equivalents, byte-alignment may provide a help in some use
	 * cases that involve frequent copying of large arrays of scalar data
	 * directly out of the stream. It can also make it possible to work with
	 * data in-place and can make it easier to debug encoded data by allowing
	 * items on aligned boundaries to be easily located in the stream.
	 */
	BYTE_PACKED,

	/**
	 * Alignment option value pre-compression alignment indicates that all steps
	 * involved in compression are to be done with the exception of the final
	 * step of applying the DEFLATE algorithm.
	 */
	PRE_COMPRESSION,

	/**
	 * The compression option is used to increase compactness using additional
	 * computational resources (DEFLATE algorithm).
	 */
	COMPRESSION;
}
