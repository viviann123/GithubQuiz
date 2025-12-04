import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitHubQuiz
{
	public static void main (String []args)
	{
		//Vivian Ng and Pragna Buddharaju - 12/4/25
		//Every time the code detects the word "the" it replaces it with the word "and"
       	double time1 = processFile("Book1.txt", "output1.txt");
        double time2 = processFile("Book2.txt", "output2.txt");

	           double totalTime = time1 + time2;

	           System.out.println("Total combined time: " + totalTime + " ms");
	       }

	       private static double processFile(String inputFile, String outputFile) {
	           try {
	               long startTime = System.nanoTime();

	               String content = new String(Files.readAllBytes(Paths.get(inputFile)));

	               StringBuilder result = new StringBuilder();
	               String target = "the";
	               String replacement = "and";

	               for (int i = 0; i < content.length();) {
	                   if (i + target.length() <= content.length() &&
	                       content.substring(i, i + target.length()).equals(target)) {
	                       result.append(replacement);
	                       i += target.length();
	                   } else {
	                       result.append(content.charAt(i));
	                       i++;
	                   }
	               }

	               Files.write(Paths.get(outputFile), result.toString().getBytes());

	               long endTime = System.nanoTime();
	               double elapsedMs = (endTime - startTime) / 1_000_000.0;

	               System.out.println("Processed: " + inputFile);
	               System.out.println("Time taken: " + elapsedMs + " ms\n");

	               return elapsedMs;

	           } catch (IOException e) {
	               e.printStackTrace();
	               return 0;
	           }
    }
}