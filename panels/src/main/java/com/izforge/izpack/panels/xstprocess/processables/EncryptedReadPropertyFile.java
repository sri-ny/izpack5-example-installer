package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.util.XstPropertyFileNormalizer;
import java.io.File;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.configuration2.PropertiesConfiguration;

public class EncryptedReadPropertyFile extends ReadPropertyFile
{
  String encryptionKey;
  
  public EncryptedReadPropertyFile(String fileName, HashMap<String, String> keyVariables, String encryptionKey)
  {
    super(fileName, keyVariables);
    this.encryptionKey = encryptionKey;
  }
  
  public boolean run()
  {
    PropertiesConfiguration propConfig = decryptFile(this.variables, this.fileName, this.encryptionKey);
    
    if (propConfig == null) {
      return false;
    }
    
    setInstallerVariables(propConfig, this.variables);
    return true;
  }
  
  public PropertiesConfiguration decryptFile(Variables variables, String fileName, String encryptionKey)
  {
    try
    {
      fileName = variables.replace(fileName);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    

    File propertyFileFile = new File(fileName);
    if (!propertyFileFile.exists()) {
      this.logger.severe("File set to read properties from does not exist: " + fileName);
      return null;
    }
    
    encryptionKey = variables.replace(encryptionKey);
    

    try
    {
      byte[] encBytes = Files.readAllBytes(Paths.get(fileName, new String[0]));
      

      byte[] key = encryptionKey.getBytes("UTF-8");
      

      MessageDigest sha = MessageDigest.getInstance("SHA-1");
      key = sha.digest(key);
      key = Arrays.copyOf(key, 16);
      
      SecretKey myDesKey = new SecretKeySpec(key, "AES");
      Cipher desCipher = Cipher.getInstance("AES");
      


      desCipher.init(2, myDesKey);
      

      byte[] textDecry = desCipher.doFinal(encBytes);
      String unencFile = new String(textDecry, "UTF-8");
      

      PropertiesConfiguration propConfig = new PropertiesConfiguration();
      StringReader stringReader = new StringReader(unencFile);
      
      propConfig.read(stringReader);
      

      PropertiesConfiguration propConfigNew = new PropertiesConfiguration();
      
      Iterator iter = propConfig.getKeys();
      while (iter.hasNext()) {
        String propkey = (String)iter.next();
        String propval = (String)propConfig.getProperty(propkey);
        
        System.out.println("Key before " + propkey + "   -   Value before: " + propval);
        propval = XstPropertyFileNormalizer.normalizeString(propval);
        System.out.println("Key after " + propkey + "   -   Value after: " + propval);
        
        propConfigNew.addProperty(propkey, propval);
        
        System.out.println("has next: " + iter.hasNext());
      }
      

      return propConfigNew;
    }
    catch (Exception ex) {
      System.out.println(ex); }
    return null;
  }
  

  public String getProcessableName()
  {
    return "Read Encrypted Property File";
  }
}
