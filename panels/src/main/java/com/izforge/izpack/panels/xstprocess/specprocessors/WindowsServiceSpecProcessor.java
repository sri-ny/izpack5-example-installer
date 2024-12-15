package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.processables.WindowsService;
import com.izforge.izpack.panels.xstprocess.processables.WindowsService.ServiceOperation;
import java.io.PrintStream;

public class WindowsServiceSpecProcessor extends SpecProcessor
{
    public boolean isApplicable(IXMLElement element)
    {
        return element.getName().equals("windowsservice");
    }

    public com.izforge.izpack.panels.xstprocess.Processable processElement(IXMLElement element)
    {
        String serviceName = element.getAttribute("servicename");
        String serviceOperation = element.getAttribute("operation");

        if ((serviceName == null) || (serviceName.length() == 0))
        {
            System.err.println("missing \"serviceName\" attribute for <windowsservice>");
            return null;
        }

        if ((serviceOperation == null) || (serviceOperation.length() == 0))
        {
            System.err.println("missing \"serviceOperation\" attribute for <windowsservice>");
            return null;
        }

        ServiceOperation serviceOpEnum = null;
        try {
            serviceOpEnum = ServiceOperation.valueOf(serviceOperation.toUpperCase());
        } catch (Exception e) {
            System.err.println("invalid service operation \"serviceOperation\" attribute for <windowsservice>: " + serviceOperation);
            return null;
        }

        return new WindowsService(serviceName, serviceOpEnum);
    }
}
