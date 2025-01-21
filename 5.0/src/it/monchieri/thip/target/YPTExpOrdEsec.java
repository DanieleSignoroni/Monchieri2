package it.monchieri.thip.target;

import java.sql.SQLException;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.PersistentObject;

import it.monchieri.thip.produzione.documento.YDocumentoProduzione;
import it.monchieri.thip.produzione.ordese.YOrdineEsecutivo;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 12/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    12/12/2024</b>
 * <p></p>
 */

public class YPTExpOrdEsec extends YPTExpOrdEsecPO {

	public String iChiaveOrdineEsecutivoPanthera = null;

	public String iChiaveDocumentoProduzionePanthera = null;

	protected YOrdineEsecutivo ordineEsecutivoPanthera = null;

	protected YDocumentoProduzione documentoProduzionePanthera = null;

	public YOrdineEsecutivo getOrdineEsecutivoPanthera() {
		if(ordineEsecutivoPanthera == null && iChiaveOrdineEsecutivoPanthera != null) {
			loadOrdineEsecutivoPanthera();
		}
		return ordineEsecutivoPanthera;
	}

	public void setOrdineEsecutivoPanthera(YOrdineEsecutivo ordineEsecutivoPanthera) {
		this.ordineEsecutivoPanthera = ordineEsecutivoPanthera;
	}

	public YDocumentoProduzione getDocumentoProduzionePanthera() {
		if(documentoProduzionePanthera == null && iChiaveDocumentoProduzionePanthera != null) {
			loadDocumentoProduzionePanthera();
		}
		return documentoProduzionePanthera;
	}

	public void setDocumentoProduzionePanthera(YDocumentoProduzione documentoProduzionePanthera) {
		this.documentoProduzionePanthera = documentoProduzionePanthera;
	}

	@Override
	public ErrorMessage checkDelete() {
		return null;
	}

	private void loadDocumentoProduzionePanthera() {
		try {
			setDocumentoProduzionePanthera((YDocumentoProduzione) YDocumentoProduzione.elementWithKey(YDocumentoProduzione.class, iChiaveDocumentoProduzionePanthera, PersistentObject.NO_LOCK));
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	private void loadOrdineEsecutivoPanthera() {
		try {
			setOrdineEsecutivoPanthera((YOrdineEsecutivo) YOrdineEsecutivo.elementWithKey(YOrdineEsecutivo.class, iChiaveOrdineEsecutivoPanthera, PersistentObject.NO_LOCK));
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
	}

}
