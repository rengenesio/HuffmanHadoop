package br.ufrj.ppgi.huffmanhadoop.deprecated;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
//import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

import br.ufrj.ppgi.huffmanhadoop.Defines;

public class SymbolCountTextConfiguration extends Configured implements Tool {
	
	public int run(String[] args) throws Exception {
		String file_in = Defines.HDFS_SERVER + Defines.HDFS_PATH + args[0];
				
		// When implementing tool
		Configuration conf = this.getConf();
		
		// Create job
		Job job = Job.getInstance(conf, "huffmanSymbolCount");
		job.setJarByClass(SymbolCountTextConfiguration.class);
		
		// Setup MapReduce job do not specify the number of Reducer
		job.setMapperClass(SymbolCountTextMap.class);
		job.setReducerClass(SymbolCountTextReduce.class);
		
		// Input
		FileInputFormat.addInputPath(job, new Path(file_in));
		job.setInputFormatClass(TextInputFormat.class);

		// Specify key / value
		job.setMapOutputKeyClass(ByteWritable.class);
		job.setMapOutputValueClass(IntWritable.class);

		// Output
		FileOutputFormat.setOutputPath(job, new Path(file_in + ".output"));
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// Execute job and return status
		return job.waitForCompletion(true) ? 0 : 1;
	}
}