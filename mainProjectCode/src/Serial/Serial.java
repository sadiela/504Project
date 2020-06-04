package Serial;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Serial implements Serializable {

    String text = "";

    public Serial(String S) {
        text = S;
    }

    public int compress(String path) {
        // plain text for comparison

        try (PrintWriter out = new PrintWriter("plain_text.txt")) {
            out.println(text);
        } catch (IOException ex) {
            System.out.println("IOException is caught, while printing string.txt");
        }
        // compression
        try {
            FileOutputStream fos = new FileOutputStream(path);
            GZIPOutputStream gis = new GZIPOutputStream(fos);
            gis.write(text.getBytes());

            //close resources
            gis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file_tmp = new File(path);
        return (int) file_tmp.length();
    }

    public String decompress(String path) {
        String decompressedString = "";
        String line;
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
//                System.out.println(child);
                try {
                    FileInputStream fis = new FileInputStream(child);
                    GZIPInputStream gis = new GZIPInputStream(fis);
                    BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
                    while ((line = bf.readLine()) != null) {
                        decompressedString += line;
                    }
                    //close resources
                    gis.close();
                    gis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }

        try (PrintWriter out = new PrintWriter("decompressed_text.txt")) {
            out.println(decompressedString);
        } catch (IOException ex) {
            System.out.println("IOException is caught, while printing decompressed text.txt");
        }
        return decompressedString;
    }
}

