package it.mame.thip.qualita.controllo;

import com.thera.thermfw.base.*;
import com.thera.thermfw.persist.Factory;

import it.monchieri.thip.acquisti.ordineAC.YOrdineAcquistoRigaPrm;
import it.thera.thip.base.qualita.*;
import it.thera.thip.qualita.controllo.*;
import com.thera.thermfw.persist.KeyHelper;

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

public class YGestoreOrdAcqAnChimica extends GestoreOrdAcqAnChimica {
	
	public YGestoreOrdAcqAnChimica (){
		super();
	}

	public CicloCollaudoTestataInfo findCicloCollaudo (RigaOrdineAcqConAnalisiChimica riga){
		//=== STANDARD - Begin
		CicloCollaudoTestataInfo ret = null;
		String idAzienda = riga.getIdAzienda();
		ret = doFindCicloCollaudoKey(idAzienda, getWhereOrdineRiga(riga), false);
		//=== STANDARD - End
		if (ret == null) {
			//Ricerca sulla norma qualità
			ret = doFindCicloCollaudoKey(idAzienda, riga, true);
		}
		//=== STANDARD - Begin
		/* Esclusa questa parte dello standard per evitare che recuperi cicli di collaudo non legati alla norma
        if (ret == null) {
            ret = doFindCicloCollaudoKey(idAzienda, getWhereOrdine(riga), true);
        }
        if (ret == null) {
            ret = doFindCicloCollaudoKey(idAzienda, getWhereArticoloCommessa(riga.getIdArticolo(), riga.getIdCommessa()), true);
        }
        if (ret == null) {
            ret = doFindCicloCollaudoKey(idAzienda, getWhereArticolo(riga.getIdArticolo()), true);
        }
        if (ret == null) {
            ret = doFindCicloCollaudoKey(idAzienda, getWhereArticolo(riga.getArticolo().getIdArticoloCicloColl()), true);
        }
		 */

		return ret;
		//=== STANDARD - End
	}

	protected CicloCollaudoTestataInfo doFindCicloCollaudoKey (String idAzienda, RigaOrdineAcqConAnalisiChimica riga, boolean newCiclo){
		CicloCollaudoTestataInfo ret = null;

		try {
			Object[] keyParts = {riga.getIdAzienda(), riga.getAnnoDocumento(), riga.getNumeroDocumento(), riga.getNumeroRigaDocumento()};
			String key = KeyHelper.buildObjectKey(keyParts);
			YOrdineAcquistoRigaPrm ordacqrig = (YOrdineAcquistoRigaPrm) Factory.createObject(YOrdineAcquistoRigaPrm.class);
			ordacqrig.setKey(key);
			ordacqrig.setDeepRetrieveEnabled(false);
			if (ordacqrig.retrieve()) {
				if (ordacqrig.getNormaqualita() != null && ordacqrig.getNormaqualita().getAnalisiChimica() != null) {
					ret = (CicloCollaudoTestataInfo) Factory.createObject(CicloCollaudoTestataInfo.class);
					ret.setKey(ordacqrig.getNormaqualita().getAnalisiChimica().getKey());
					ret.setNewCiclo(newCiclo);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}

		return ret;
	}
}
