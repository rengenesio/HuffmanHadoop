package br.ufrj.ppgi.huffmanhadoop;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String in = new String(Defines.HDFS_SERVER + Defines.HDFS_PATH + args[0]);
		String out = new String(in);
		String cb = new String(in);
		out += ".dir/compressed";
		cb += ".dir/codification";

		new Encoder(in, out, cb);

		in += ".dir/decompressed";
		new Decoder(out, in, cb);
	}
}

/*******************************************

TODO

COMBINER CLASS!!!

********************************************/