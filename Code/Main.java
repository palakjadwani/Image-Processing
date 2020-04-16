package ImageProcessing;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.List;
import java.util.Scanner;


/*------------------------------------------------------------------------------------------------------------
	FLOW:

	#1 Input folder directory
	#2 Iterate through all PDF files in a the folder
		#2.1 Go into command prompt
		#2.2 Convert PDF pdfPageCount -> Images
		#2.3 Iterate through all page images of a file
		     #2.3.1 Starting process
			        #2.3.1.1 Command to convert in tesseract for command line
			        #2.3.1.2 Read from output text file and print in console

	------------------------------------------------------------------------------------------------------------*/


public class Main
{
    public static void main(String[] args) throws IOException
    {
        
        //Input folder directory
        System.out.println("Enter directory path: ");
        Scanner input = new Scanner(System.in);
        String theInput = input.nextLine();
        if (input != null)
        {
            // close the Scanner once finished with it
            input.close();
        }
        String folderPath = theInput+"\\";
        
        convert(folderPath);        
    }

    private static void convert(String folderPath) throws IOException {
        
        File[] files = new File(folderPath).listFiles();
        
        //Iterate through all PDF files in a the folder
        for (File file : files)
        {
            //baseName= file name without .PDF extension
            String fileName = file.getName();
            String pdfFile = folderPath + fileName;
            String baseName= pdfFile.substring(0, pdfFile.lastIndexOf("."));
            String tesseract_install_path="C:\\Program Files (x86)\\Tesseract-OCR\\tesseract";

            //Navigate into command prompt
            String[] command = { "cmd", };
            
            //Consolidated final text file
            PrintWriter textFile = new PrintWriter(baseName + ".txt");

            int pdfPageCount = pdfToImg(pdfFile);

            for(int pg=1; pg<=pdfPageCount; pg++)
            {
                //Starting process
                Process p;
                try
                {
                    p = Runtime.getRuntime().exec(command);
                    new Thread(new Sync(p.getErrorStream(), System.err)).start();
                    new Thread(new Sync(p.getInputStream(),  System.out)).start();
                    PrintWriter stdin = new PrintWriter(p.getOutputStream());

                    File pageFile = new File(baseName+"-"+pg+".txt");
                    //Command to convert PDF  to Image(.png) in tesseract for command line
                    stdin.println("\""+tesseract_install_path+"\" \""+baseName+"-"+pg+".png"+"\" \""+baseName+"-"+pg+"\" -l eng");
                    stdin.close();

                    p.waitFor();

                    //Read from output text file and print in console
                    ReadFile.combineTextFiles(baseName+"-"+pg+".txt", textFile);

                    //Delete individual page text file
                    pageFile.delete();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            textFile.close();
        }
    }
    
    private static int pdfToImg(String pdfFile) throws IOException {
        
        PDDocument document = PDDocument.loadNonSeq(new File(pdfFile), null);
        List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
        String baseName= pdfFile.substring(0, pdfFile.lastIndexOf("."));

        int pageCount = 0;
        for (PDPage pdPage : pdPages)
        {
            ++pageCount;
            BufferedImage img = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
            ImageIOUtil.writeImage(img, baseName+"-"+pageCount+".png", 300);
        }
        document.close();
        return pageCount;
    }
}
