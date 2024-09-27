package it.mame.thip.magazzino.generalemag;

import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.magazzino.generalemag.Lotto;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 27/09/2024
 * <br><br>
 * <b>71XXX	DSSOF3	27/09/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class YLotto extends Lotto {
	
	protected String iCodiceLottoCliente = null;
	
	protected String iNota; 
	
	protected boolean iNucleare;
	
	protected char iYStato;


	public YLotto (){
		super();
		setYStato(DatiComuniEstesi.VALIDO);
	}

	public void setCodiceLottoCliente (String codiceLottoCliente){
		iCodiceLottoCliente = codiceLottoCliente;
		setDirty();
	}

	public String getCodiceLottoCliente (){
		return iCodiceLottoCliente;
	}

	public void setNota (String nota){
		iNota = nota;
		setDirty();
	}

	public String getNota (){
		return iNota;
	}

	public boolean getNucleare (){
		return iNucleare;
	}

	public void setNucleare (boolean nucleare){
		iNucleare = nucleare;
		setDirty();
	}

	public void setYStato (char stato){
		iYStato = stato;
		setDirty();
	}

	public char getYStato (){
		return iYStato;
	}
}
