package it.mame.thip.qualita.controllo.web;

import com.thera.thermfw.web.servlet.*;
import com.thera.thermfw.web.*;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import it.mame.thip.qualita.controllo.CodificaCaratteristicaCollaudo;
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

public class RecuperaDatiCodificaCarattMame extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	public static final String ID_CODIFICA_CAR = "ID_CODIFICA_CAR";

	protected void processAction(ServletEnvironment se) throws java.lang.Exception {
		String codificaCar =  getStringParameter(se.getRequest(),ID_CODIFICA_CAR);
		if(codificaCar == null)
			return ;
		CodificaCaratteristicaCollaudo cCaratt = (CodificaCaratteristicaCollaudo)Factory.createObject(CodificaCaratteristicaCollaudo.class);
		//Fix 06424 AB
		//    cCaratt.setKey(KeyHelper.buildObjectKey(new Object[]{Azienda.getAziendaCorrente(),codificaCar}));
		com.thera.thermfw.type.PositiveIntegerType  type = new com.thera.thermfw.type.PositiveIntegerType(4);
		Object codice = type.stringToObject(type.unFormat(codificaCar));
		cCaratt.setKey(KeyHelper.buildObjectKey(new Object[]{Azienda.getAziendaCorrente(),codice}));
		//Fix 06424 AB end

		if(!cCaratt.retrieve())
			return;
		copiaDatiCodificaCarattMame(se,cCaratt);
	}

	/**
	 * copiaDatiCodificaCarattMame
	 * @param se ServletEnvironment
	 * @param caratt CodificaCaratteristicaCollaudo
	 * @param update boolean
	 * @throws Exception
	 */
	public void copiaDatiCodificaCarattMame(ServletEnvironment se,CodificaCaratteristicaCollaudo caratt) throws java.lang.Exception
	{
		if(caratt != null)
		{
			PrintWriter out = se.getResponse().getWriter();
			out.println("<script language=\"JavaScript1.2\">");
			//Fix AB 07416 begin
			//      out.println("parent.writeValueInCampo('SequenzaCaratteristica','" + caratt.getCodice() + "');");
			out.println("parent.writeValueInCampo('SequenzaCaratteristica','" + caratt.getCodice() + "',true);");
			//Fix AB 07416 end
			out.println("parent.executeCampoOnBlur('SequenzaCaratteristica');");
			out.println("parent.clearErrorDaCampo('SequenzaCaratteristica',true);");
			if(caratt.getDescrizione() != null )
			{
				//Fix AB 07416 begin
				//        out.println("parent.writeValueInCampo('Descrizione.Descrizione','" + caratt.getDescrizione().getDescrizione() +"');");
				//        out.println("parent.writeValueInCampo('Descrizione.Descrizione','" + caratt.getDescrizione().getDescrizione() +"',true);"); //70290 remmata
				out.println("parent.writeValueInCampo('DescrizioneCicloNLS.Descrizione','" + caratt.getDescrizione().getDescrizione() +"',true);");//70290

				//Fix AB 07416 end
				//        out.println("parent.executeCampoOnBlur('Descrizione.Descrizione');"); //70290 remmata
				out.println("parent.executeCampoOnBlur('DescrizioneCicloNLS.Descrizione');"); //70290

				//        out.println("parent.writeValueInCampo('Descrizione.DescrizioneRidotta','" + caratt.getDescrizione().getDescrizioneRidotta() +"');");
				//        out.println("parent.writeValueInCampo('Descrizione.DescrizioneRidotta','" + caratt.getDescrizione().getDescrizioneRidotta() +"',true);");//70290 remmata

				out.println("parent.writeValueInCampo('DescrizioneCicloNLS.DescrizioneRidotta','" + caratt.getDescrizione().getDescrizioneRidotta() +"',true);");//70290

				//Fix AB 07416 end
				//        out.println("parent.executeCampoOnBlur('Descrizione.DescrizioneRidotta');"); //70290 remmata
				out.println("parent.executeCampoOnBlur('DescrizioneCicloNLS.DescrizioneRidotta');"); //70290
			}
			out.println("</script>");
		}
	}

}
