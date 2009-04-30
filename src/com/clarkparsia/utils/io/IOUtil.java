package com.clarkparsia.utils.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * Title: IOUtil<br/>
 * Description: Collection of utility methods for basic IO tasks.<br/>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com> <br/>
 * Created: Mar 27, 2009 9:10:15 AM <br/>
 *
 * @author Michael Grove <mike@clarkparsia.com>
 */
public class IOUtil {
    /**
     * The system dependent end-of-line defined here for convenience
     */
    public static String ENDL = System.getProperty( "line.separator" );

    /**
     * Given a path to a file on the local disk, return the contents of that file as a String.<br><br>
     *
     * @param theFileName     Fully qualified file name to a file on the local disk
     * @return Contents of the file as a String
     * @throws java.io.IOException if there are problems opening or reading from the file
     * @throws java.io.FileNotFoundException if the file cannot be found
     */
    public static String getFileAsString(String theFileName) throws java.io.IOException {
        return readStringFromReader(new BufferedReader(new InputStreamReader(new FileInputStream(theFileName))));
    }

    /**
     * Read the contents of the stream into a string
     * @param theStream the stream to read from
     * @return the contents of the input stream
     * @throws IOException thrown if there is an error while reading from the stream
     */
    public static String readStringFromStream(InputStream theStream) throws IOException {
        return readStringFromReader(new BufferedReader(new InputStreamReader(theStream)));
    }

    /**
     * Read the contents of the specified reader return them as a String
     * @param theReader the reader to read from
     * @return String the string contained in the reader
     * @throws java.io.IOException if there is an error reading
     */
    public static String readStringFromReader(Reader theReader) throws IOException {
        BufferedReader aReader = new BufferedReader(theReader);

        StringBuffer theFile = new StringBuffer();
        String line = aReader.readLine();

        while (line != null) {
            theFile.append(line);
            line = aReader.readLine();
            
            if (line != null) {
                theFile.append(ENDL);
            }
        }

        aReader.close();

        return theFile.toString();
    }

    /**
     * Returns the specified URL as a string.  This function will read the contents at the specified URl and return the results.
     * @param theURL URL the URL to read from
     * @return String the String found at the URL
     * @throws IOException if there is an error reading
     */
    public static String getURLAsString(URL theURL) throws IOException {
        if (theURL == null) {
            return null;
        }

        Reader reader = new BufferedReader(new InputStreamReader(theURL.openStream()));
        return readStringFromReader(reader);
    }

    /**
     * Write the specifed string to the given file name
     * @param theSave String the string to save
     * @param theFileName String the file to save the string to
     * @throws IOException if there is an error while writing
     */
    public static void writeStringToFile(String theSave, String theFileName) throws IOException {
        writeStringToStream(theSave, new FileOutputStream(theFileName));
    }

    /**
     * Write the specified string to the output stream.  The output stream will be closed when writing is complete.
     * @param theString the string to write to the stream
     * @param theOutput the stream to write to
     * @throws IOException thrown if there is an error while writing
     */
    public static void writeStringToStream(String theString, OutputStream theOutput) throws IOException {
        writeStringToWriter(theString, new OutputStreamWriter(theOutput));
    }

    /**
     * Write the specified string out to the writer.  The writer is closed when finished.
     * @param theString the string to write
     * @param theWriter the writer to write to
     * @throws IOException thrown if there is an error while writing
     */
    public static void writeStringToWriter(String theString, Writer theWriter) throws IOException {
        BufferedWriter aWriter = new BufferedWriter(theWriter);

        StringTokenizer st = new StringTokenizer(theString,"\n",true);
        String s;
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            if (s.equals("\n"))
                aWriter.newLine();
            else aWriter.write(s);
        }

        aWriter.flush();
        aWriter.close();
    }

    public static void transfer(InputStream theInputStream, OutputStream theOutputStream) throws IOException {
        transfer(new InputStreamReader(theInputStream), new OutputStreamWriter(theOutputStream));
    }

    public static void transfer(Reader theReader, Writer theWriter) throws IOException {
		char[] aCharBuffer = new char[2048];
        int aBytesRead = 0;

		while ((aBytesRead = theReader.read(aCharBuffer)) != -1) {
			theWriter.write(aCharBuffer, 0, aBytesRead);
		}

        theWriter.flush();
    }
}