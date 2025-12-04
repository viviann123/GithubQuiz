import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.nio.charset.Charset;

public class GitHubQuiz
{
	private static final Charset TEXT_ENCODING = Charset.forName("Windows-1252");

	    public static void main(String[] args) throws Exception {

	        String input1 = "Book1.txt";
	        String input2 = "Book2.txt";
	        String output1 = "output1.txt";
	        String output2 = "output2.txt";

	        long start = System.nanoTime();

	        ExecutorService executor = Executors.newFixedThreadPool(2);

	        Callable<Void> task1 = () -> { processFile(input1, output1); return null; };
	        Callable<Void> task2 = () -> { processFile(input2, output2); return null; };

	        Future<Void> f1 = executor.submit(task1);
	        Future<Void> f2 = executor.submit(task2);

	        f1.get();
	        f2.get();

	        executor.shutdown();

	        long end = System.nanoTime();
	        System.out.println("Total time: " + (end - start) / 1_000_000 + " ms");
	    }

	    private static void processFile(String inputPath, String outputPath) {
	        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputPath), TEXT_ENCODING);
	             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath), TEXT_ENCODING)) {

	            String line;
	            while ((line = reader.readLine()) != null) {
	                String replacedLine = fastReplaceWholeWordThe(line);
	                writer.write(replacedLine);
	                writer.newLine();
	            }

	        } catch (IOException e) {
	            throw new RuntimeException("Error processing file: " + inputPath, e);
	        }
	    }

	    private static String fastReplaceWholeWordThe(String line) {
	        String lower = line.toLowerCase();
	        int idx = 0;
	        int found;
	        StringBuilder sb = null;

	        while ((found = lower.indexOf("the", idx)) != -1) {
	            boolean leftOk = found == 0 || !Character.isLetter(lower.charAt(found - 1));
	            boolean rightOk = found + 3 == lower.length() || !Character.isLetter(lower.charAt(found + 3));

	            if (leftOk && rightOk) {
	                if (sb == null) {
	                    sb = new StringBuilder(line.length());
	                }
	                sb.append(line, idx, found);
	                sb.append("and");
	                idx = found + 3;
	            } else {
	                if (sb != null) {
	                    sb.append(line, idx, found + 1);
	                }
	                idx = found + 1;
	            }
	        }

	        if (sb == null) {
	            return line;
	        } else {
	            sb.append(line, idx, line.length());
	            return sb.toString();
	        }
    }

}