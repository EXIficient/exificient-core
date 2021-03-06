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

package com.siemens.ct.exi.core.util;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Richard.Kuntschke@siemens.com
 * 
 */

public class MethodsBag {

	// ///////////////////////////////////////////////////////
	//
	// C O D I N G _ L E N G T H Operations
	//
	// ///////////////////////////////////////////////////////
	private static final double log2div = Math.log(2.0);

	/**
	 * Returns the least number of 7 bit-blocks that is needed to represent the
	 * parameter n. Returns 1 if parameter n is 0.
	 * 
	 * @param n
	 *            integer value
	 * @return number of 7Bit blocks
	 */
	public static int numberOf7BitBlocksToRepresent(final int n) {
		assert (n >= 0);

		// 7 bits
		if (n < 128) {
			return 1;
		}
		// 14 bits
		else if (n < 16384) {
			return 2;
		}
		// 21 bits
		else if (n < 2097152) {
			return 3;
		}
		// 28 bits
		else if (n < 268435456) {
			return 4;
		}
		// 35 bits
		else {
			// int, 32 bits
			return 5;
		}
	}

	/**
	 * Returns the least number of 7 bit-blocks that is needed to represent the
	 * parameter l. Returns 1 if parameter l is 0.
	 * 
	 * @param l
	 *            long value
	 * @return number of 7Bit blocks
	 * 
	 */
	public static int numberOf7BitBlocksToRepresent(final long l) {
		if (l < 0xffffffff) {
			return numberOf7BitBlocksToRepresent((int) l);
		}
		// 35 bits
		else if (l < 0x800000000L) {
			return 5;
		}
		// 42 bits
		else if (l < 0x40000000000L) {
			return 6;
		}
		// 49 bits
		else if (l < 0x2000000000000L) {
			return 7;
		}
		// 56 bits
		else if (l < 0x100000000000000L) {
			return 8;
		}
		// 63 bits
		else if (l < 0x8000000000000000L) {
			return 9;
		}
		// 70 bits
		else {
			// long, 64 bits
			return 10;
		}
	}

	static final public int getCodingLength(final int characteristics) {
		assert (characteristics >= 0);

		switch (characteristics) {
		case 0:
		case 1:
			return 0;
		case 2:
			return 1;
		case 3:
		case 4:
			return 2;
		case 5:
		case 6:
		case 7:
		case 8:
			return 3;
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
			return 4;
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
		case 32:
			// 17 .. 32
			return 5;
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
		case 41:
		case 42:
		case 43:
		case 44:
		case 45:
		case 46:
		case 47:
		case 48:
		case 49:
		case 50:
		case 51:
		case 52:
		case 53:
		case 54:
		case 55:
		case 56:
		case 57:
		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
			// 33 .. 64
			return 6;
		default:
			assert (characteristics > 64);
			if (characteristics < 129) {
				// 65 .. 128
				return 7;
			} else if (characteristics < 257) {
				// 129 .. 256
				return 8;
			} else if (characteristics < 513) {
				// 257 .. 512
				return 9;
			} else if (characteristics < 1025) {
				// 513 .. 1024
				return 10;
			} else if (characteristics < 2049) {
				// 1025 .. 2048
				return 11;
			} else if (characteristics < 4097) {
				// 2049 .. 4096
				return 12;
			} else if (characteristics < 8193) {
				// 4097 .. 8192
				return 13;
			} else if (characteristics < 16385) {
				// 8193 .. 16384
				return 14;
			} else if (characteristics < 32769) {
				// 16385 .. 32768
				return 15;
			} else {
				return (int) Math.ceil(Math.log((double) (characteristics))
						/ log2div);
			}
		}
	}

	// ///////////////////////////////////////////////////////
	//
	// I N T E G E R & L O N G _ T O _ S T R I N G Operations
	//
	// ///////////////////////////////////////////////////////
	public final static char[] INTEGER_MIN_VALUE_CHARARRAY = "-2147483648"
			.toCharArray();
	public final static char[] LONG_MIN_VALUE_CHARARRAY = "-9223372036854775808"
			.toCharArray();

	final static char[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', };

	final static char[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0',
			'0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2',
			'2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3',
			'3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4',
			'4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
			'6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7',
			'7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8',
			'8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9',
			'9', };

	/**
	 * All possible chars for representing a number as a String
	 */
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
			99999999, 999999999, Integer.MAX_VALUE };

	// Requires positive x
	final static int stringSize(int x) {
		for (int i = 0;; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}

	// Requires positive x
	final static int stringSize(long x) {
		long p = 10;
		for (int i = 1; i < 19; i++) {
			if (x < p)
				return i;
			p = 10 * p;
		}
		return 19;
	}

	public static final int getStringSize(int i) {
		return (i < 0) ? stringSize(-i) + 1 : stringSize(i);
	}

	public static final int getStringSize(long l) {
		if (l == Long.MIN_VALUE) {
			// -9223372036854775808
			return 20;
		} else {
			return (l < 0) ? stringSize(-l) + 1 : stringSize(l);
		}
	}

	/**
	 * Places characters representing the integer i into the character array
	 * buf. The characters are placed into the buffer backwards starting with
	 * the least significant digit at the specified index (exclusive), and
	 * working backwards from there.
	 * 
	 * Will fail if i == Integer.MIN_VALUE
	 * 
	 * @param i
	 *            integer
	 * @param index
	 *            index
	 * @param buf
	 *            character buffer
	 */
	public final static void itos(int i, int index, char[] buf) {
		assert (!(i == Integer.MIN_VALUE));

		int q, r;
		char sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		// Generate two digits per iteration
		while (i >= 65536) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = i - ((q << 6) + (q << 5) + (q << 2));
			i = q;
			buf[--index] = DigitOnes[r];
			buf[--index] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i <= 65536, i);
		for (;;) {
			q = (i * 52429) >>> (16 + 3);
			r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
			buf[--index] = digits[r];
			i = q;
			if (i == 0)
				break;
		}
		if (sign != 0) {
			buf[--index] = sign;
		}
	}

	/**
	 * Places characters representing the integer i into the character array
	 * buf. The characters are placed into the buffer backwards starting with
	 * the least significant digit at the specified index (exclusive), and
	 * working backwards from there.
	 * 
	 * @param i
	 *            integer
	 * @param index
	 *            index
	 * @param buf
	 *            character buffer
	 */
	public final static void itos(long i, int index, char[] buf) {
		if (i == Long.MIN_VALUE) {
			// -9223372036854775808
			buf[--index] = '8';
			buf[--index] = '0';
			buf[--index] = '8';
			buf[--index] = '5';
			buf[--index] = '7';
			buf[--index] = '7';
			buf[--index] = '4';
			buf[--index] = '5';
			buf[--index] = '8';
			buf[--index] = '6';
			buf[--index] = '3';
			buf[--index] = '0';
			buf[--index] = '2';
			buf[--index] = '7';
			buf[--index] = '3';
			buf[--index] = '3';
			buf[--index] = '2';
			buf[--index] = '2';
			buf[--index] = '9';
			buf[--index] = '-';
			return;
		}
		assert (!(i == Long.MIN_VALUE));

		long q;
		int r;
		char sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		// Get 2 digits/iteration using longs until quotient fits into an int
		while (i > Integer.MAX_VALUE) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
			i = q;
			buf[--index] = DigitOnes[r];
			buf[--index] = DigitTens[r];
		}

		// Get 2 digits/iteration using ints
		int q2;
		int i2 = (int) i;
		while (i2 >= 65536) {
			q2 = i2 / 100;
			// really: r = i2 - (q * 100);
			r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
			i2 = q2;
			buf[--index] = DigitOnes[r];
			buf[--index] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i2 <= 65536, i2);
		for (;;) {
			q2 = (i2 * 52429) >>> (16 + 3);
			r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
			buf[--index] = digits[r];
			i2 = q2;
			if (i2 == 0)
				break;
		}
		if (sign != 0) {
			buf[--index] = sign;
		}
	}

	/**
	 * Places characters representing the integer i into the character array buf
	 * in reverse order.
	 * 
	 * Will fail if i &lt; 0 (zero)
	 * 
	 * @param i
	 *            integer
	 * @param index
	 *            index
	 * @param buf
	 *            character buffer
	 * 
	 * @return number of written chars
	 */
	public final static int itosReverse(int i, int index, char[] buf) {
		assert (i >= 0);
		int q, r;
		int posChar = index;

		// Generate two digits per iteration
		while (i >= 65536) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = i - ((q << 6) + (q << 5) + (q << 2));
			i = q;
			buf[posChar++] = DigitOnes[r];
			buf[posChar++] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i <= 65536, i);
		for (;;) {
			q = (i * 52429) >>> (16 + 3);
			r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
			buf[posChar++] = digits[r];
			i = q;
			if (i == 0)
				break;
		}

		return (posChar - index); // number of written chars
	}

	/**
	 * Places characters representing the integer i into the character array buf
	 * in reverse order.
	 * 
	 * Will fail if i &lt; 0 (zero)
	 * 
	 * @param i
	 *            integer
	 * @param index
	 *            index
	 * @param buf
	 *            character buffer
	 */
	public final static void itosReverse(long i, int index, char[] buf) {
		assert (i >= 0);
		long q;
		int r;

		// Get 2 digits/iteration using longs until quotient fits into an int
		while (i > Integer.MAX_VALUE) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
			i = q;
			buf[index++] = DigitOnes[r];
			buf[index++] = DigitTens[r];
		}

		// Get 2 digits/iteration using ints
		int q2;
		int i2 = (int) i;
		while (i2 >= 65536) {
			q2 = i2 / 100;
			// really: r = i2 - (q * 100);
			r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
			i2 = q2;
			buf[index++] = DigitOnes[r];
			buf[index++] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i2 <= 65536, i2);
		for (;;) {
			q2 = (i2 * 52429) >>> (16 + 3);
			r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
			buf[index++] = digits[r];
			i2 = q2;
			if (i2 == 0)
				break;
		}
	}

}
