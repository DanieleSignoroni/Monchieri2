package it.mame.thip.qualita.controllo.web;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.web.WebJSTypeList;

import it.thera.thip.qualita.controllo.web.CicloCollaudoFaseFormModifier;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 26/09/2024
 * <br><br>
 * <b>71XXX	DSSOF3	26/09/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class YCicloCollaudoFaseFormModifier extends CicloCollaudoFaseFormModifier {

	public void writeBodyEndElements(JspWriter out) throws java.io.IOException{
		super.writeBodyEndElements(out);
		out.println(WebJSTypeList.getImportForJSLibrary("it/mame/thip/qualita/controllo/CicloCollaudoFaseMame.js", getServletEnvironment().getRequest()));
	}

}
