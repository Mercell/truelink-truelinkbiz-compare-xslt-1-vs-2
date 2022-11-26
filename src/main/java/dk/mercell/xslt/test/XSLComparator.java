package dk.mercell.xslt.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLComparator {

    public static void main(String[] args) throws Exception {
        XSLComparator c = new XSLComparator();
        c.run();
    }

    public void run() throws Exception {
        String xml = readResourcesFile("data.xml");
        String xslt10 = readResourcesFile("test10.xsl");
        System.out.println("Applying XSLT version 1.0\n");
        System.out.println(xslt10);
        System.out.println("to XML data:\n");
        System.out.println(xml);
        System.out.println("and then replace stylesheet version='1.0' with '2.0' and apply again.\n");

        String xslt20 = xslt10.replace("xsl:stylesheet version=\"1.0\"", "xsl:stylesheet version=\"2.0\"");
        System.out.println("XSLT 1.0 result:");
        System.out.println(transform(xml, xslt10));
        System.out.println("XSLT 2.0 result:");
        System.out.println(transform(xml, xslt20));

    }

    public String transform(String xml, String xsl) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream xslStream = new ByteArrayInputStream(xsl.getBytes(StandardCharsets.UTF_8));
        TransformerFactory tFactory = TransformerFactory.newInstance();
//        System.out.println("Transformer implementation: "+tFactory.getClass().getName());
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslStream));
        transformer.transform(new StreamSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))), new StreamResult(output));
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private String readResourcesFile(String fileName) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try (InputStream is = this.getClass().getResourceAsStream("/" + fileName)) {
            int count;
            while ((count = is.read(buffer)) > 0) {
                baos.write(buffer, 0, count);
            }
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

}
