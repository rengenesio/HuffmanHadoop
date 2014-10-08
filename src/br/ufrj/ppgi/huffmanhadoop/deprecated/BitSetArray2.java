package br.ufrj.ppgi.huffmanhadoop.deprecated;

import br.ufrj.ppgi.huffmanhadoop.Codification;
import br.ufrj.ppgi.huffmanhadoop.Defines;

public class BitSetArray2 {
	public BitSet[] bs;
	public int bits;
	int index_byte;

	public BitSetArray2(int capacity) {
		this.bs = new BitSet[capacity];
		for(int i = 0 ; i < capacity ; i++)
			this.bs[i] = new BitSet();
		this.bits = 0;
		this.index_byte = 0;
	}
	
	public void addByte(byte b) {
		this.bs[this.index_byte].fromByte(b);
		this.bits += 8;
		this.index_byte++;
	}
	
	public void addBit(boolean s) {
		int pos = Defines.BYTE_BIT - (this.bits % Defines.BYTE_BIT) - 1;
		if (s)
			bs[this.index_byte].b |= 1 << pos;
		else
			bs[this.index_byte].b &= ~(1 << pos);
		
		if((++this.bits % 8) == 0)
			this.index_byte++;
	}
	
	public String toString() {
		String s = new String();
		for(int i = 0 ; i < this.bits ; i++) {
			int j = Defines.BYTE_BIT - (i % 8) - 1;
			int bit = (bs[i/8].b & (1 << j)) >> j;
			s += bit;
			if((i % 8) == 7)
				s += " ";
		}
		return s;
	}
		
	
	/*public boolean checkBit(int pos) {
		if(pos < 0) {
			System.out.println("erro!!!");
			System.exit(-1);
		}
		int bit = b[this.bytes] & (1 << Defines.BYTE_BIT - pos - 1);
		if (bit > 0)
			return true;

		return false;
	}*/
	
	public void addCode(Codification c) {
		for(short i = 0 ; i < c.size ; i++) {
			if(c.code.charAt(i) == '0')
				this.addBit(false);
			else
				this.addBit(true);
		}
	}
}