package it.monchieri.thip.vendite.offerteCliente.web;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.base.ResourceLoader;
import com.thera.thermfw.web.WebJSTypeList;

import it.thera.thip.vendite.offerteCliente.web.OffertaClienteEstrattoFormModifier;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 18/10/2024
 * <br><br>
 * <b>71665	DSSOF3	18/10/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class YOffertaClienteEstrattoFormModifier extends OffertaClienteEstrattoFormModifier {

	public static final String RES_PERS = "it/monchieri/thip/vendite/offerteCliente/resources/YOffertaCliente";

	@Override
	public void writeHeadElements(JspWriter out) throws IOException {
		super.writeHeadElements(out);
		out.println(WebJSTypeList.getImportForJSLibrary("it/monchieri/thip/vendite/offerteCliente/YOffertaClienteEstrattoPers.js", getServletEnvironment().getRequest()));
	}

	@Override
	public void writePulsantiBarraAzioniPers(JspWriter out) throws IOException {
		super.writePulsantiBarraAzioniPers(out);
		out.println("<td nowrap=\"true\" height=\"0\">");
		out.println("<button name=\"thStampaPrevCommessa\" id=\"thStampaPrevCommessa\" onclick=\"stampaPreventivoDiCommessa()\" style=\"width:" + widthBtnBarraAzioniStandard + ";height:30px;\" type=\"button\" title=\"" + ResourceLoader.getString(RES_PERS, "StampaPrevCommessa") + "\">");
		out.println("<img border=\"0\" width=\"" + widthImgBarraAzioniStandard + "\" height=\"24px\" src=\"it/monchieri/thip/vendite/offerteCliente/img/GenericPrint.gif\" >");
		out.println("</button>");
		out.println("</td>");
	}

	@Override
	public void writeFormStartElements(JspWriter out) throws IOException {
		super.writeFormStartElements(out);
		out.println("<input type=\"text\" style=\"display:none;\" id=\"RigheSelezionate\" name=\"RigheSelezionate\">");
	      
	}
}
