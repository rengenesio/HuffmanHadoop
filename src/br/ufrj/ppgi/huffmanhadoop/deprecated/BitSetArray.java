package br.ufrj.ppgi.huffmanhadoop.deprecated;

import br.ufrj.ppgi.huffmanhadoop.Codification;
import br.ufrj.ppgi.huffmanhadoop.Defines;

public class BitSetArray {
	public byte[] b;
	public int bits;
	int index_byte;

	public BitSetArray(int capacity) {
		this.b = new byte[capacity];
		this.bits = 0;
		this.index_byte = 0;
	}
	
	public void addByte(byte b) {
		this.b[this.index_byte] = b;
		this.bits += 8;
		this.index_byte++;
	}
	
	public void addBit(boolean s) {
		int pos = Defines.BYTE_BIT - (this.bits % Defines.BYTE_BIT) - 1;
		if (s)
			b[this.index_byte] |= 1 << pos;
		else
			b[this.index_byte] &= ~(1 << pos);
		
		if((++this.bits % 8) == 0)
			this.index_byte++;
	}
	
	public String toString() {
		String s = new String();
		for(int i = 0 ; i < this.bits ; i++) {
			int j = Defines.BYTE_BIT - (i % 8) - 1;
			int bit = (b[i/8] & (1 << j)) >> j;
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