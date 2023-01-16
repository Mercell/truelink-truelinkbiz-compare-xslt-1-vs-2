package dk.mercell.xslt.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.security.NoTypePermission;

public class SchematronComparator {

	public static void main(String[] args) throws Exception {
		SchematronComparator c = new SchematronComparator();
		c.run();
	}
	
	public void run() throws Exception {
		compare("1.12.3_xalan", "1.13.1.eba0ace");
		compare("1.12.3_xalan", "1.13.1.d5ee2f1");
	}

	public void compare(String schOldCode, String schNewCode) throws Exception {
		System.out.println("\n----- Compare " + schOldCode + " vs " + schNewCode + " -----");
		
		File[] examples = new File("examples", schOldCode + "_vs_" + schNewCode).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});

		Transformer schOld = buildTransformer(schOldCode);
		Transformer schNew = buildTransformer(schNewCode);

		XStream validationResultParser = newXStream(new Class<?>[] { SchematronResult.class, SchematronError.class }, SchematronResult.class);

		for (int i = 0; i < examples.length; i++) {
			File example = examples[i];

			System.out.println("\nValidate " + example.getName());

			System.out.println("");

			validate(schOld, schOldCode, example, validationResultParser);
			System.out.println("");
			validate(schNew, schNewCode, example, validationResultParser);
		}
	}

	private Transformer buildTransformer(String schCode) throws Exception {
		if (schCode.contains("xalan")) {
			return new org.apache.xalan.processor.TransformerFactoryImpl().newTransformer(new StreamSource(getSchematronInputStream(schCode)));
		}
		return TransformerFactory.newInstance().newTransformer(new StreamSource(getSchematronInputStream(schCode)));
	}

	private XStream newXStream(Class<?>[] classes, Class<?> root) {
		XStream xStream = new XStream();
		secure(xStream, classes);
		xStream.ignoreUnknownElements();
		xStream.processAnnotations(root);
		return xStream;
	}

	private void secure(XStream xstream, Class<?>[] classes) {
		xstream.addPermission(NoTypePermission.NONE);
		xstream.allowTypeHierarchy(String.class);

		xstream.allowTypes(classes);
	}

	private void validate(Transformer sch, String schCode, File example, XStream validationResultParser) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		sch.transform(new StreamSource(new FileInputStream(example)), new StreamResult(output));
		String schResult = new String(output.toByteArray(), StandardCharsets.UTF_8);

		SchematronResult res = (SchematronResult) validationResultParser.fromXML(schResult);

		System.out.println(res.print(example.getName(), schCode));
	}

	private FileInputStream getSchematronInputStream(String schCode) throws FileNotFoundException {
		return new FileInputStream("schematron/OIOUBL_Invoice_Schematron_" + schCode + ".xsl");
	}

	@XStreamAlias("Schematron")
	private class SchematronResult {

		@XStreamAlias("Information")
		private String information;

		@XStreamAlias("Error")
		@XStreamImplicit()
		private List<SchematronError> errors;

		public boolean isOk() {
			if (errors != null) {
				return errors.isEmpty();
			}
			return true;
		}

		public String print(String documentFileName, String schCode) {
			StringBuilder sb = new StringBuilder();
			sb.append(information);
			sb.append("\n");
			if (isOk()) {
				sb.append("Document is valid.");
				if (documentFileName.contains("should_fail_in_" + schCode)) {
					if (information.contains("Version " + schCode)) {
						sb.append(" BUT SHOULD HAVE FAILED !!!");
					}
				}
			} else {
				sb.append("Document is not valid.\n");
				sb.append("Errors:");
				for (int i = 0; i < errors.size(); i++) {
					sb.append("\n");
					sb.append(errors.get(i).description);
				}
			}
			return sb.toString();
		}

	}

	private class SchematronError {
		@XStreamAlias("Description")
		private String description;
	}

}
