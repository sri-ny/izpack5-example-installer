package com.izforge.izpack.panels.xstprocess.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class XstPropertyFileNormalizer
{
  public static String normalizeString(String string)
  {
    char[] charArray = string.toCharArray();
    StringBuilder normalizedString = new StringBuilder();
    
    for (int i = 0; i < charArray.length; i++)
    {

      if (i + 1 == charArray.length) {
        normalizedString.append(charArray[i]);
      } else if ((charArray[i] == '\\') && ((charArray[(i + 1)] == '=') || (charArray[(i + 1)] == ':'))) {
        normalizedString.append(charArray[(++i)]);
      } else {
        normalizedString.append(charArray[i]);
      }
    }
    
    return normalizedString.toString();
  }
  
  public static void normalizeFile(String fileName) throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    
    StringBuilder sb = new StringBuilder();
    
    String line = "";
    while ((line = br.readLine()) != null) {
      sb.append(normalizeString(line));
      sb.append('\n');
    }
    
    br.close();
    

    new File(fileName).delete();
    BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
    bw.write(sb.toString());
    bw.close();
  }
}

