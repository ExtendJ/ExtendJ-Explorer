package astgen;

import jastaddad.*;

import java.util.*;

import org.jastadd.jastaddj.JastAddJVersion;

import AST.*;

class ASTGen extends Frontend {
	public static void main(String args[]) {
		compile(args);
	}

	public static boolean compile(String args[]) {
		return new ASTGen().process(
				args,
				new BytecodeParser(),
				new JavaParser() {
					@Override
					public CompilationUnit parse(java.io.InputStream is, String fileName)
							throws java.io.IOException, beaver.Parser.Exception {
						return new parser.JavaParser().parse(is, fileName);
					}
				}
				);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void processErrors(Collection errors, CompilationUnit unit) {
		super.processErrors(errors, unit);
	}

	@Override
	protected void processNoErrors(CompilationUnit unit) {
		new JastAddAd(unit);
	}

	@Override
	protected String name() {
		return "ASTGen";
	}

	@Override
	protected String version() {
		return JastAddJVersion.getVersion();
	}
}
