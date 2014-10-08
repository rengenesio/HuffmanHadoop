package br.ufrj.ppgi.huffmanhadoop.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BinaryComparable;
import org.apache.hadoop.io.WritableComparable;

import br.ufrj.ppgi.huffmanhadoop.Codification;
import br.ufrj.ppgi.huffmanhadoop.Defines;

public class BytesWritableEncoder extends BinaryComparable implements WritableComparable<BinaryComparable> {
	private static final byte[] EMPTY_BYTES = {};

	private int index, bytes;
	private int bits;
	private byte[] b;

	public BytesWritableEncoder() {
		this(EMPTY_BYTES, 0, 0);
	}

	public BytesWritableEncoder(int capacity) {
		this(EMPTY_BYTES, 0, 0);
		this.setCapacity(capacity);
	}

	public BytesWritableEncoder(byte[] b, int bytes, int bits) {
		this.b = b;
		this.bytes = bytes;
		this.bits = bits;
		this.index = bits / 8;
	}

	public byte[] copyBytes() {
		byte[] result = new byte[bytes];
		System.arraycopy(b, 0, result, 0, bytes);
		return result;
	}

	@Override
	public byte[] getBytes() {
		return b;
	}

	@Override
	public int getLength() {
		return bytes;
	}

	public int getBits() {
		return bits;
	}

	public void setSize(int size) {
		if (size > b.length) {
			setCapacity(size * 3 / 2);
		}
		this.bytes = size;
	}

	public void setBits(int bits) {
		this.bits = bits;
	}

	public void setCapacity(int new_cap) {
		if (new_cap > b.length) {
			byte[] new_data = null;
			try {
				new_data = new byte[new_cap];
			} catch (Exception e) {
				e.printStackTrace();
				System.err
						.println("BytesWritableHuffman.setCapacity()\n-----------\nNew size error: "
								+ new_cap);
			}
			System.arraycopy(this.b, 0, new_data, 0, this.bytes);
			this.b = new_data;
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		setSize(0);
		setSize(in.readInt());
		setBits(in.readInt());
		in.readFully(b, 0, bytes);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(bytes);
		out.writeInt(bits);
		out.write(b, 0, bytes);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object right_obj) {
		if (right_obj instanceof BytesWritableEncoder)
			return super.equals(right_obj);
		return false;
	}

	@Override
	public String toString() {
		String s = new String();
		for (int i = 0; i < bytes; i++) {
			s += Integer.toHexString((0xFF & b[i]) >> 4);
			s += Integer.toHexString(0xF & b[i]);
			s += " ";
		}

		s += "--> bits: " + this.bits + "(" + this.bytes + " bytes)";
		return s;
	}

	public void addBit(boolean s) {
		int pos = Defines.BYTE_BIT - (this.bits % Defines.BYTE_BIT) - 1;
		if (s)
			b[this.index] |= 1 << pos;
		else
			b[this.index] &= ~(1 << pos);

		if (++this.bits % 8 == 1)
			this.bytes++;
		if (pos == 0)
			this.index++;
	}

	public boolean getBit(int pos) {
		int bit = b[pos / 8] & (1 << Defines.BYTE_BIT - (pos % 8) - 1);
		if (bit > 0)
			return true;

		return false;
	}

	public void addBytesWritable(BytesWritableEncoder bw) {
		if (this.b.length < this.getLength() + bw.getLength())
			this.setCapacity(this.getLength() + bw.getLength());
		for (int i = 0; i < bw.getBits(); i++)
			this.addBit(bw.getBit(i));
	}

	public void addCode(Codification c) {
		if (this.b.length < this.getLength() + (c.size / 8) + 1)
			this.setCapacity(this.getLength() + (c.size / 8) + 1);
		for (short i = 0; i < c.size; i++) {
			if (c.code.charAt(i) == '0')
				this.addBit(false);
			else
				this.addBit(true);
		}
	}
}