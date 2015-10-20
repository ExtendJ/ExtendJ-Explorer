package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import lang.ast.Program;

/**
 * Tests code generation
 *
 * @Author Niklas Fors
 */
@RunWith(Parameterized.class)
public class TestCodeGeneration extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "testfiles/asm";

	/**
	 * Construct a new code generation test
	 * @param filename filename of test input file
	 */
	public TestCodeGeneration(String filename) {
		super(TEST_DIR, filename);
	}

	/**
	 * Run the code generation test
	 */
	@Test
	public void runTest() throws IOException, Exception {
		Program program = (Program) parse(inFile);

		assertEquals("[]", program.errors().toString());

		// Generate Assembly file
		File assemblyFile = getFileReplaceExtension(inFile, ".s");
		PrintStream out = new PrintStream(new FileOutputStream(assemblyFile));
		program.genCode(out);
		out.close();

		// Generate object file
		File objectFile = getFileReplaceExtension(inFile, ".o");
		ArrayList<String> cmdAs = new ArrayList<String>();
		cmdAs.add("as");
		cmdAs.add("--gstabs");
		cmdAs.add(assemblyFile.getAbsolutePath());
		cmdAs.add("-o");
		cmdAs.add(objectFile.getAbsolutePath());
		execute(cmdAs);

		// Link object file and generate executable file
		File execFile = getFileReplaceExtension(inFile, ".elf");
		ArrayList<String> cmdLd = new ArrayList<String>();
		cmdLd.add("ld");
		cmdLd.add(objectFile.getAbsolutePath());
		cmdLd.add("-o");
		cmdLd.add(execFile.getAbsolutePath());
		execute(cmdLd);

		// Run the executable file
		String output = execute(Arrays.asList(execFile.getAbsolutePath()));
		compareOutput(output, outFile, expectedFile);
	}

	private String execute(List<String> cmd) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(cmd);
		Process process = pb.start();
		process.getOutputStream().close();
		process.waitFor();

		String standardError = inputStreamToString(process.getErrorStream());
		assertEquals(
			"Standard error was not empty when running command '" + cmd.get(0) + "'",
			"", standardError);
		assertEquals(
			"Exit code was not zero (error occured) when running command '" + cmd.get(0) + "'",
			0, process.exitValue());

		return inputStreamToString(process.getInputStream());
	}
	
	private String inputStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return sb.toString();
	}

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
