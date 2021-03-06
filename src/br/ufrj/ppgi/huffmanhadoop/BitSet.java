package br.ufrj.ppgi.huffmanhadoop;

import br.ufrj.ppgi.huffmanhadoop.Defines;

public class BitSet {
	byte b;

	public BitSet() {
		b = 0;
	}

	public void setBit(int pos, boolean s) {
		pos = Defines.BYTE_BIT - pos - 1;
		if (s)
			b |= 1 << pos;
		else
			b &= ~(1 << pos);
	}

	public boolean cheackBit(int pos) {
		if (pos < 0) {
			System.err.println("Error!");
			System.exit(-1);
		}
		int bit = b & (1 << Defines.BYTE_BIT - pos - 1);
		if (bit > 0)
			return true;

		return false;
	}

	public void fromByte(byte b) {
		this.b = b;
	}

	public String toString() {
		String s = new String();
		for (int i = Defines.BYTE_BIT - 1; i >= 0; i--) {
			int bit = (b & (1 << i)) >> i;
			s += bit;
		}

		return s;
	}
}