package br.ufrj.ppgi.huffmanhadoop.deprecated;

import java.io.IOException;

import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SymbolCountTextMap extends
		Mapper<LongWritable, Text, ByteWritable, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private ByteWritable b = new ByteWritable();

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		System.out.println("Map:");
		System.out.println(value.toString());
		for (char c : value.toString().toCharArray()) {
			b.set((byte) c);
			context.write(b, one);
		}
		b.set((byte) '\n');
		context.write(b, one);
	}
}