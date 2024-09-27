package it.mame.thip.qualita.controllo.web;

import com.thera.thermfw.web.servlet.*;
import com.thera.thermfw.web.*;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import it.mame.thip.qualita.controllo.CodificaFaseCollaudo ;
import it.thera.thip.base.azienda.Azienda;
import java.io.PrintWriter;

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

public class RecuperaDatiCodificaFaseMame extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	public static final String ID_CODIFICA_FASE = "ID_CODIFICA_FASE";

	protected void processAction(ServletEnvironment se) throws java.lang.Exception {
		String codificaFase =  getStringParameter(se.getRequest(),ID_CODIFICA_FASE);
		if(codificaFase == null)
			return ;
		CodificaFaseCollaudo cf = (CodificaFaseCollaudo)Factory.createObject(CodificaFaseCollaudo.class);
		//Fix 06424 AB Begin
		//     cf.setKey(KeyHelper.buildObjectKey(new Object[]{Azienda.getAziendaCorrente(),codificaFase}));
		com.thera.thermfw.type.PositiveIntegerType  type = new com.thera.thermfw.type.PositiveIntegerType(4);
		Object codice = type.stringToObject(type.unFormat(codificaFase));
		cf.setKey(KeyHelper.buildObjectKey(new Object[]{Azienda.getAziendaCorrente(),codice}));
		//Fix 06424 AB end
		if(!cf.retrieve())
			return;
		copiaDatiCodificaFaseMame(se,cf);
	}

	/**
	 * copiaDatiCodificaFaseMame
	 * @param se ServletEnvironment
	 * @param cf CodificaFaseCollaudo
	 * @throws Exception
	 */
	public void copiaDatiCodificaFaseMame(ServletEnvironment se,CodificaFaseCollaudo cf) throws java.lang.Exception
	{
		if(cf != null)
		{
			PrintWriter out = se.getResponse().getWriter();
			out.println("<script language=\"JavaScript1.2\">");
			out.println("parent.writeValueInCampo('SequenzaFase','" + cf.getCodice() + "');");
			out.println("parent.executeCampoOnBlur('SequenzaFase');");
			out.println("parent.clearErrorDaCampo('SequenzaFase',true);");
			if(cf.getDescrizione() != null )
			{
				out.println("parent.writeValueInCampo('Descrizione.Descrizione','" + cf.getDescrizione().getDescrizione() +"');");
				out.println("parent.executeCampoOnBlur('Descrizione.Descrizione');");
				out.println("parent.writeValueInCampo('Descrizione.DescrizioneRidotta','" + cf.getDescrizione().getDescrizioneRidotta() +"');");
				out.println("parent.executeCampoOnBlur('Descrizione.DescrizioneRidotta');");
			}
			out.println("</script>");
		}
	}
}
