package it.monchieri.thip.vendite.offerteCliente.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.ad.ClassAD;
import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.ad.ClassADCollectionManager;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.base.Utils;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.web.WebElement;
import com.thera.thermfw.web.WebFormModifier;

import it.monchieri.thip.vendite.offerteCliente.StampaPreventivoCommessa;
import it.monchieri.thip.vendite.offerteCliente.YOffertaClienteRigaPrm;
import it.thera.thip.base.commessa.PreventivoCommessaTestata;
import it.thera.thip.vendite.offerteCliente.OffertaCliente;
import it.thera.thip.vendite.offerteCliente.OffertaClienteRigaPrm;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRiga;

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

public class StampaPreventivoCommessaModifier extends WebFormModifier {

	String TD_Prefix = "<TD align=\"left\" class=\"cell\">";
	String TD_End = "</TD>";
	String TR_Prefix = "<TR>";
	String TR_Grid_RowPrefix = "<TR class=\"grid_row\">";
	String TR_End = "</TR>";
	String TABLE_Prefix = "<TABLE align=\"top\"  width=\"100%\" class=\"header_table\">";
	String TABLE_End = "</TABLE>";
	String TBODY_Prefix = "<tbody class=\"body_table\">";
	String TBODY_END = "</tbody>";

	@Override
	public void writeHeadElements(JspWriter out) throws IOException {

		StampaPreventivoCommessa bo = (StampaPreventivoCommessa) getBODataCollector().getBo();

		String chiaveOfferta = (String) getServletEnvironment().getRequest().getAttribute("ChiaveOfferta");
		String righeSelezionate = (String) getServletEnvironment().getRequest().getAttribute("RigheSelezionate");

		bo.setChiaveOfferta(chiaveOfferta);
		bo.setChiaviSelezionati(righeSelezionate);

		getBODataCollector().setBo(bo);

		getBODataCollector().setOnBORecursive();

	}

	/**
	 * isRigaPrimaria
	 * @param riga OrdineVenditaRigaPrmMame
	 * @return boolean
	 */
	protected boolean isRigaPrimaria(OrdineVenditaRiga riga)
	{
		if(riga == null)
			return false;
		return Utils.compare(riga.getDettaglioRigaDocumento(), new Integer(0) ) == 0;
	}
	/**
	 *
	 * @param key String
	 * @return OrdineVenditaMame
	 */
	protected OffertaCliente getOffertaCliente(String key)
	{
		if(key == null || key.trim().length() == 0)
			return null;
		OffertaCliente ordine = (OffertaCliente)Factory.createObject(OffertaCliente.class);
		ordine.setKey(key);
		try {
			if(!ordine.retrieve())
				return null;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return ordine;
	}

	@SuppressWarnings("rawtypes")
	public void visualizzaLista(JspWriter out) throws java.io.IOException {
		StampaPreventivoCommessa object = (StampaPreventivoCommessa) getBODataCollector().getBo();
		out.println("<script language='JavaScript1.2'>");
		out.println("ListaTR.style.display='';");
		Vector list = getListaRighe(object);
		out.println("ListaTD.innerHTML ='" + getHTMLForListaToShow(list) + "';");
		out.println("</script>");
	}


	/**
	 *
	 * @param object StampaOrdVenRigheMame
	 * @return Vector
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getListaRighe(StampaPreventivoCommessa object)
	{
		if(object == null ) return null;
		Vector ret = new Vector();
		String chiaviSel = object.getChiaviSelezionati();
		if(chiaviSel.isEmpty()) {
			ret.addAll(getOffertaCliente(object.getChiaveOfferta()).getRighe());
		}else {

		}
		if(object.getChiaviSelezionati() != null && !object.getChiaviSelezionati().isEmpty()){
			String[] rows = chiaviSel.split(",");
			for(String key : rows) {
				OffertaClienteRigaPrm riga =  getRigaOffertaCliente(key);
				if(riga != null)
					ret.add(riga);
			}
		}
		
		for (Iterator<OffertaClienteRigaPrm> iterator = ret.iterator(); iterator.hasNext();) {
			OffertaClienteRigaPrm riga = (OffertaClienteRigaPrm) iterator.next();

			PreventivoCommessaTestata preventivo = YOffertaClienteRigaPrm.preventivoDaCommessa(riga.getIdCommessa());

			//Escludere le righe che non sono parte di un preventivo
			if(preventivo == null) {
				iterator.remove();
			}

		}
		
		return ret;
	}

	/**
	 *
	 * @param key String
	 * @return OrdineVenditaRigaPrmMame
	 */
	public OffertaClienteRigaPrm getRigaOffertaCliente(String key) {
		OffertaClienteRigaPrm riga = (OffertaClienteRigaPrm) Factory.createObject(OffertaClienteRigaPrm.class);
		riga.setKey(key);
		try {
			if (riga.retrieve())
				return riga;
			else return null;
		}
		catch (java.sql.SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String getColonneTitleNls(ClassADCollection cadc, String classADName) {
		ClassAD cad = cadc.getAttribute(classADName);
		if (cad != null)
			return cad.getColumnTitleNLS();
		else return "";
	}

	protected ClassADCollection getClassADCollection(String className) {
		ClassADCollection cadc = null;
		try {
			cadc = ClassADCollectionManager.collectionWithName(className);
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace(Trace.excStream);
		}
		return cadc;
	}

	@SuppressWarnings("rawtypes")
	public String getHTMLForListaToShow(Vector list) {
		ClassADCollection cadc = getClassADCollection("OffertaClienteRigaPrm");
		String header = "<thead><tr>" +
				"<TH class=\"header\" title=\"Sequenza\">" + getColonneTitleNls(cadc, "SequenzaRiga") + " </TH> " +
				"<TH class=\"header\" title=\"Descrizione\">" + getColonneTitleNls(cadc, "DescrizioneArticolo") + " </TH> " +
				"<TH class=\"header\" title=\"Data\">" + getColonneTitleNls(cadc, "DataConsegnaConfermata") + "</TH>" + "</tr></thead>";
		String tmp = "";
		for (int i = 0; i < list.size(); i++) {
			OffertaClienteRigaPrm riga =  (OffertaClienteRigaPrm)list.get(i);
			if (riga != null) {
				tmp += TR_Grid_RowPrefix + TD_Prefix + riga.getSequenzaRiga() + TD_End;
				String descr = "";
				if(riga.getDescrizioneArticolo() != null && riga.getDescrizioneArticolo().trim().length() >0  )
					descr =riga.getDescrizioneArticolo() ;
				else
					descr = riga.getArticolo().getDescrizioneArticoloNLS().getDescrizione();

				tmp += TD_Prefix + WebElement.formatStringForHTML(descr) + TD_End;
				tmp += TD_Prefix + WebElement.formatStringForHTML(formatDate( riga.getDataConsegnaConfermata())) + TD_End + TR_End;
			}
		}
		String retStr = "";
		if (tmp.trim().length() > 0)
			retStr =   TABLE_Prefix + header + TBODY_Prefix + tmp + TBODY_END + TABLE_End ;
		return retStr;
	}
	/**
	 * formatDate
	 * @param date Date
	 * @return String
	 */
	protected String formatDate(java.sql.Date date)
	{
		if(date == null ) return null;
		com.thera.thermfw.type.DateType dt = new com.thera.thermfw.type.DateType();
		return dt.objectToString(date);
	}

	@Override
	public void writeBodyStartElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeFormStartElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeFormEndElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeBodyEndElements(JspWriter out) throws IOException {
		visualizzaLista(out);
	}

}
