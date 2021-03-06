package br.ufrj.ppgi.huffmanhadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import br.ufrj.ppgi.huffmanhadoop.Codification;
import br.ufrj.ppgi.huffmanhadoop.Defines;

public class Decoder {
	Codification[] codification;
	short symbols = 0;
	byte max_code = 0;
	Path in, out, cb;
	CodificationArrayElement[] treeArray;

	public Decoder(String file_in, String file_out, String file_cb)
			throws IOException {
		in = new Path(file_in);
		out = new Path(file_out);
		cb = new Path(file_cb);

		fileToCodification();
		codeToTreeArray();
		huffmanDecode();
	}

	public void fileToCodification() throws IOException {
		FileSystem fs = FileSystem.get(new Configuration());
		FSDataInputStream f = fs.open(cb);
	
		codification = new Codification[Defines.POWER_BITS_CODIFICATION];
		
		while (f.available() != 0) {
			byte symbol = (byte) f.read();
			byte size = (byte) f.read();
			byte[] code = new byte[(size & 0xFF)];

			f.read(code, 0, size & (0xFF));
			codification[symbols] = new Codification(symbol, size, new String(code));
			if ((size & 0xFF) > max_code)
				max_code = size;
			
			symbols++;
		}

		/*
		System.out.println("CODIFICATION: symbol (size) code"); 
		for(short i = 0 ; i < symbols ; i++)
			System.out.println(codification[i].toString());
		*/
	}

	public void codeToTreeArray() {
		treeArray = new CodificationArrayElement[(int) Math.pow(2,
				(max_code + 1))];

		for (short i = 0; i < symbols; i++) {
			int index = 0;
			for (char c : codification[i].code.toCharArray()) {
				index <<= 1;
				if (c == '0')
					index += 1;
				else
					index += 2;
			}
			treeArray[index] = new CodificationArrayElement(
					codification[i].symbol);
		}

		/*
		System.out.println("codeToTreeArray():");
		System.out.println("TREE_ARRAY:"); 
		for(int i = 0 ; i < Math.pow(2,(max_code + 1)) ; i++) 
			if(treeArray[i] != null)
				System.out.println("i: " + i + " -> " + treeArray[i].symbol);
		System.out.println("------------------------------");
		*/
	}

	public void huffmanDecode() throws IOException {
		byte[] buffer = new byte[1];
		BitSet bufferBits = new BitSet();
		int index = 0;

		FileSystem fs = FileSystem.get(new Configuration());
		FileStatus[] status = fs.listStatus(in);
		FSDataOutputStream fout = fs.create(out);

		for (byte i = 1; i < status.length; i++) {
		//for (byte i = 1; i < 2; i++) {
			FSDataInputStream fin = fs.open(status[i].getPath());
			while (fin.available() > 3) {
				fin.read(buffer, 0, 1);
				bufferBits.fromByte(buffer[0]);
				for (byte j = 0; j < Defines.BYTE_BIT; j++) {
					index <<= 1;
					if (bufferBits.cheackBit(j) == false)
						index += 1;
					else
						index += 2;

					if (treeArray[index] != null) {
						if (treeArray[index].symbol != Defines.EOF) {
							fout.write(treeArray[index].symbol);
						} else {
							index = 0;
							break;
						}
						index = 0;
					}
				}
			}
			fin.close();
		}
		fout.close();
	}
}