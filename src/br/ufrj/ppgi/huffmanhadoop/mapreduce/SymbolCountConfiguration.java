package br.ufrj.ppgi.huffmanhadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class SymbolCountConfiguration extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		// When implementing tool
		Configuration conf = this.getConf();

		// Create job
		Job job = Job.getInstance(conf, "huffmanSymbolCount");
		job.setJarByClass(SymbolCountConfiguration.class);

		// Setup MapReduce job do not specify the number of Reducer
		job.setMapperClass(SymbolCountMap.class);
		job.setCombinerClass(SymbolCountReduce.class);
		job.setReducerClass(SymbolCountReduce.class);

		// Input
		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setInputFormatClass(ByteInputFormat.class);

		// Specify key / value
		job.setMapOutputKeyClass(ByteWritable.class);
		job.setMapOutputValueClass(IntWritable.class);

		// Output
		FileOutputFormat.setOutputPath(job, new Path(args[0] + ".dir/symbolcount"));
		job.setOutputFormatClass(SymbolCountOutputFormat.class);

		// Execute job and return status
		return job.waitForCompletion(true) ? 0 : 1;
	}
}