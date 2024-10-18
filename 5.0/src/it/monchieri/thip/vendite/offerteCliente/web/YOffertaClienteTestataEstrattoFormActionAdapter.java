package it.monchieri.thip.vendite.offerteCliente.web;

import java.io.IOException;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.web.ServletEnvironment;

import it.thera.thip.vendite.offerteCliente.web.OffertaClienteTestataEstrattoFormActionAdapter;

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

public class YOffertaClienteTestataEstrattoFormActionAdapter extends OffertaClienteTestataEstrattoFormActionAdapter {

	private static final long serialVersionUID = 1L;

	public static final String STAMPA_PREVENTIVO_COMMESSA = "STAMPA_PREVENTIVO_COMMESSA";

	@Override
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		String azione = getAzione(se);
		if(azione.equals(STAMPA_PREVENTIVO_COMMESSA)) {
			stampaPreventivoDiCommessa(se);
		}else {
			super.otherActions(cadc, se);
		}

	}

	protected void stampaPreventivoDiCommessa(ServletEnvironment se) throws ServletException, IOException {
		String righeSelezionate = getStringParameter(se.getRequest(), "RigheSelezionate");
		String thKey = getStringParameter(se.getRequest(), KEY);
		
		se.getRequest().setAttribute("RigheSelezionate", righeSelezionate);
		se.getRequest().setAttribute("ChiaveOfferta", thKey);
		
		se.sendRequest(getServletContext(),"it/monchieri/thip/vendite/offerteCliente/StampaPreventivoCommessa.jsp", true);
	}
}
