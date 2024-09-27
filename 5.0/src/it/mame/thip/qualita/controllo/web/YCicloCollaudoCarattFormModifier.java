package it.mame.thip.qualita.controllo.web;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.web.WebJSTypeList;

import it.thera.thip.qualita.controllo.web.CicloCollaudoCarattFormModifier;

public class YCicloCollaudoCarattFormModifier extends CicloCollaudoCarattFormModifier {
	
	public void writeBodyEndElements(JspWriter out) throws java.io.IOException{
		super.writeBodyEndElements(out);
		out.println(WebJSTypeList.getImportForJSLibrary("it/mame/thip/qualita/controllo/CicloCollaudoCarattMame.js", getServletEnvironment().getRequest()));
	}

}
