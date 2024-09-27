package it.monchieri.thip.qualita.controllo;

import it.mame.thip.qualita.controllo.YCicloCollaudoCaratt;
import it.mame.thip.qualita.controllo.YCicloCollaudoFase;
import it.mame.thip.qualita.controllo.YMisuraCaracteriche;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.DocumentoCollaudoTestata;
import it.thera.thip.qualita.controllo.MisuraCaracteriche;
import it.thera.thip.qualita.controllo.MisuraFase;

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

public class YDocumentoCollaudoTestata extends DocumentoCollaudoTestata {

	/**
	 * <p>
	 * Settaggio della formula ripresa dalla caratteristica.<br>
	 * Verra' poi usata nei calcoli.
	 * </p>
	 * @param car
	 * @return
	 */
	@Override
	public MisuraCaracteriche prepareCaracteriche(CicloCollaudoCaratteristica car) {
		MisuraCaracteriche misura = super.prepareCaracteriche(car);
		if(misura instanceof YMisuraCaracteriche && car instanceof YCicloCollaudoCaratt) {
			((YMisuraCaracteriche)misura).setYFormula(((YCicloCollaudoCaratt)car).getYFormula());
		}
		return misura;
	}
	
	@Override
	public MisuraFase prepareFase(CicloCollaudoFase fase) {
		MisuraFase misura = super.prepareFase(fase);
		if(misura instanceof YMisuraFase && fase instanceof YCicloCollaudoFase) {
			((YMisuraFase)misura).setIdCodificaFaseMame(((YCicloCollaudoFase)fase).getIdCodificaFase());
		}
		return misura;
	}
}
