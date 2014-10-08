package br.ufrj.ppgi.huffmanhadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class SymbolCountMap extends
		Mapper<LongWritable, BytesWritable, ByteWritable, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private ByteWritable bw = new ByteWritable();

	public void map(LongWritable key, BytesWritable value, Context context)
			throws IOException, InterruptedException {
		byte b;
		for (int i = 0; i < value.getLength(); i++) {
			b = value.getBytes()[i];
			bw.set(b);
			context.write(bw, one);
		}
	}
}