package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import properties.ReadProperties;

public class GenerateMeanFile {
	static ReadProperties properties = new ReadProperties();
	
	public static void main(String[] args) throws FileNotFoundException {
	String[] names = {"rosenbrock", "rastrigin", "sphere"};
	String[] algorithms = {"pso"};
	//String[] weight = {"0.8", "1.0", "0.9-0.4", "clerc"};
	for(int i=0; i<names.length; i++){
		for (int j=0; j<algorithms.length; j++){
//			for (int k=0; k<weight.length; k++){
				String foldPath = "D:\\mestrado\\comparacao\\" +algorithms[j] + "\\" + names[i];
				writeMeanFile(foldPath);
			}
		}
	//}
	
	}
	public static void writeMeanFile(String foldPath) throws FileNotFoundException{
		File file = new File(foldPath,"mean.csv");
		PrintWriter pw = new PrintWriter(file);
		double[] mean = readFilesFromPath(foldPath);
		for (int i=0; i<mean.length; i++){
			System.out.println(i);
			pw.append(String.valueOf(mean[i]));
			pw.append("\n");
			
		}
		pw.close();
		
	}
	public static double[] readFilesFromPath(String foldPath) throws FileNotFoundException{
		double[] fileInfo = new double[properties.getIteration()];
		File file = new File(foldPath);
		File[] fileList = file.listFiles();
		for (int i=0; i<fileList.length; i++){
			int j=0;
		
			Scanner scanner = new Scanner(fileList[i]);
			while(scanner.hasNext()){
				String line = scanner.next();
					if (j<10000){
					if (i==0){
						
					fileInfo[j] = fileInfo[j] + Double.parseDouble(line);
					} else{
						fileInfo[j] = ((fileInfo[j]+ Double.parseDouble(line))/2);
					}
				}
				j++;
			}
			
			scanner.close();
	}
		System.out.println(fileInfo.length);
		return fileInfo;

	}
}
