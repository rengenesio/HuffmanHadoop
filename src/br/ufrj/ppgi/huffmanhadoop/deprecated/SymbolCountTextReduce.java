package br.ufrj.ppgi.huffmanhadoop.deprecated;

import java.io.IOException;

import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class SymbolCountTextReduce extends
		Reducer<ByteWritable, IntWritable, ByteWritable, IntWritable> {
	private IntWritable result = new IntWritable();

	public void reduce(ByteWritable key, Iterable<IntWritable> values,
			Context context) throws IOException, InterruptedException {

		int sum = 0;
		for (IntWritable val : values)
			sum += val.get();

		result.set(sum);
		context.write(key, result);
	}
}