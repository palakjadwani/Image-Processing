package ImageProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class ReadFile
{
    public static void combineTextFiles(String fileName, PrintWriter textFile)
    {
        BufferedReader bufferedReader = null;
        try
        {
            String currentLine;
            bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((currentLine = bufferedReader.readLine()) != null)
            {
                textFile.println(currentLine);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (bufferedReader != null)
                    bufferedReader.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}