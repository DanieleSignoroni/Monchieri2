package it.monchieri.thip.vendite.offerteCliente.web;

import java.io.IOException;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.ad.ClassADCollectionManager;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.security.Entity;
import com.thera.thermfw.security.Security;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.WebToolBarButton;

import it.thera.thip.base.comuniVenAcq.StatoEvasione;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.vendite.offerteCliente.OffertaClienteRigaPrm;
import it.thera.thip.vendite.offerteCliente.web.OffertaClienteRigaPrmFormActionAdapter;

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

public class YOffertaClienteRigaPrmFormActionAdapter extends OffertaClienteRigaPrmFormActionAdapter {

	private static final long serialVersionUID = 1L;

	protected static final String SBLOCCA_ARTICOLO = "SBLOCCA_ARTICOLO";
	protected static String IMG = "it/monchieri/thip/vendite/offerteCliente/img/sbloccaArticolo.png";
	protected static String RES = "it.monchieri.thip.vendite.offerteCliente.resources.YOffertaClienteRigaPrm";

	@Override
	public void modifyToolBar(WebToolBar toolBar) {
		WebToolBarButton sbloccaArticolo = new WebToolBarButton(SBLOCCA_ARTICOLO, "action_submit", "errorsFrame", "no",
				RES, SBLOCCA_ARTICOLO, IMG, SBLOCCA_ARTICOLO, "single", false);
		toolBar.addButton(sbloccaArticolo);
		super.modifyToolBar(toolBar);
	}

	@Override
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		String action = se.getRequest().getParameter(ACTION) != null ? se.getRequest().getParameter(ACTION) : "";
		if (action.equals(SBLOCCA_ARTICOLO)) {
			sbloccaArticolo(se);
		}else {
			super.otherActions(cadc, se);
		}
	}

	protected void sbloccaArticolo(ServletEnvironment se) throws ServletException, IOException {
		String key = se.getRequest().getParameter(KEY);
		String className = getStringParameter(se.getRequest(), CLASS_NAME);
		String thMode = getStringParameter(se.getRequest(), MODE);
		boolean isAutorizzato = isAutorizzatoTask(className, "SBLOCCA_ART");
		if(thMode.equals("UPDATE")) {
			if(isAutorizzato) {
				OffertaClienteRigaPrm off = getObject(key);
				if (off != null && off.getStato() == DatiComuniEstesi.VALIDO && off.getStatoEvasione() == StatoEvasione.INEVASO) {
					se.sendRequest(getServletContext(), "it/monchieri/thip/vendite/offerteCliente/YSbloccaArticoloOffertaClienteRigaPrm.jsp", false);
				}
			}else {
				se.addErrorMessage(new ErrorMessage("BAS0000020"));
			}
		}else {
			se.addErrorMessage(new ErrorMessage("BAS0000078","Non e' possibile sbloccare un articolo in modalita' "+thMode));
		}
		if(!se.isErrorListEmpity()) {
			se.sendRequest(getServletContext(), "com/thera/thermfw/common/ErrorListHandler.jsp", false);
		}
	}

	public static boolean isAutorizzatoTask(String className, String taskId) {
		try{
			ClassADCollection cadc = ClassADCollectionManager.collectionWithName(className);
			Class<?> classObj = Class.forName(cadc.getBOClassName());
			if (classObj != null) {
				Entity entityObj = Entity.findEntity(classObj);
				if (entityObj != null)
					return Security.validate(entityObj.getId(),taskId);
			}
		}catch(Exception ex1){
			ex1.printStackTrace(Trace.excStream);
		}
		return false;
	}
}
