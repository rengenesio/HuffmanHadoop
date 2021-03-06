package br.ufrj.ppgi.huffmanhadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class EncoderConfiguration extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		// When implementing tool
		Configuration conf = this.getConf();
		conf.set("file_in", args[0]);
		conf.set("file_cb", args[2]);

		// Create job
		Job job = Job.getInstance(conf, "huffmanEncoder");
		job.setJarByClass(EncoderConfiguration.class);

		// Setup MapReduce job do not specify the number of Reducer
		job.setMapperClass(EncoderMap.class);
		job.setReducerClass(EncoderReduce.class);

		// Input
		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setInputFormatClass(ByteInputFormat.class);

		// Specify key / value
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(BytesWritableEncoder.class);

		// Output
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setOutputFormatClass(EncoderOutputFormat.class);
		job.setNumReduceTasks(3);

		// Execute job and return status
		return job.waitForCompletion(true) ? 0 : 1;
	}
}