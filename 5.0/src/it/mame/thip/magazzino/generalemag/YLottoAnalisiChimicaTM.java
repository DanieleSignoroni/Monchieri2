package it.mame.thip.magazzino.generalemag;

import com.thera.thermfw.persist.*;
import java.sql.*;
import com.thera.thermfw.base.*;

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

public class YLottoAnalisiChimicaTM extends TableManager {

	public static final String ID_AZIENDA = "ID_AZIENDA";

	public static final String ID_ARTICOLO = "ID_ARTICOLO";

	public static final String ID_LOTTO = "ID_LOTTO";

	public static final String ID_FASE_SGQ = "ID_FASE_SGQ";

	public static final String ID_CARAT_SGQ = "ID_CARAT_SGQ";

	public static final String ID_PROGR_PEZZO = "ID_PROGR_PEZZO";

	public static final String DSC_FASE_SGQ = "DSC_FASE_SGQ";

	public static final String DSC_CARAT_SGQ = "DSC_CARAT_SGQ";

	public static final String DSC_CARAT_SGQ2 = "DSC_CARAT_SGQ2";

	public static final String ID_UNITA_MISURA = "ID_UNITA_MISURA";

	public static final String LIM_INF_TOL = "LIM_INF_TOL";

	public static final String LIM_SUP_TOL = "LIM_SUP_TOL";

	public static final String VALORE_NOMINALE = "VALORE_NOMINALE";

	public static final String VAL_RILEVATO1 = "VAL_RILEVATO1";

	public static final String ESITO1 = "ESITO1";

	public static final String VAL_RILEVATO2 = "VAL_RILEVATO2";

	public static final String ESITO2 = "ESITO2";

	public static final String ANNOTAZIONI = "ANNOTAZIONI";

	public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "YLOTTI_AC";

	private static TableManager cInstance;

	private static final String CLASS_NAME = it.mame.thip.magazzino.generalemag.YLottoAnalisiChimica.class.getName();

	public synchronized static TableManager getInstance () throws SQLException
	{
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(YLottoAnalisiChimicaTM.class);
		}
		return cInstance;
	}

	public YLottoAnalisiChimicaTM () throws SQLException
	{
		super();
	}

	protected void initialize () throws SQLException
	{
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation () throws SQLException
	{
		super.initializeRelation();
		addAttribute("IdFaseSgq", ID_FASE_SGQ);
		addAttribute("IdCaratSgq", ID_CARAT_SGQ);
		addAttribute("IdProgrPezzo", ID_PROGR_PEZZO);
		addAttribute("DscFaseSgq", DSC_FASE_SGQ);
		addAttribute("DscCaratSgq", DSC_CARAT_SGQ);
		addAttribute("DscCaratSgq2", DSC_CARAT_SGQ2);
		addAttribute("IdUnitaMisura", ID_UNITA_MISURA);
		addAttribute("LimInfTol", LIM_INF_TOL);
		addAttribute("LimSupTol", LIM_SUP_TOL);
		addAttribute("ValoreNominale", VALORE_NOMINALE);
		addAttribute("ValRilevato1", VAL_RILEVATO1);
		addAttribute("Esito1", ESITO1);
		addAttribute("ValRilevato2", VAL_RILEVATO2);
		addAttribute("Esito2", ESITO2);
		addAttribute("Annotazioni", ANNOTAZIONI);
		addAttribute("IdAzienda", ID_AZIENDA);
		addAttribute("IdArticolo", ID_ARTICOLO);
		addAttribute("IdLotto", ID_LOTTO);

		setKeys(ID_AZIENDA + "," + ID_ARTICOLO + "," + ID_LOTTO + "," + ID_FASE_SGQ + "," + ID_CARAT_SGQ + "," + ID_PROGR_PEZZO);
	}

	private void init () throws SQLException
	{
		configure(ID_FASE_SGQ + ", " + ID_CARAT_SGQ + ", " + ID_PROGR_PEZZO + ", " + DSC_FASE_SGQ
				+ ", " + DSC_CARAT_SGQ + ", " + DSC_CARAT_SGQ2 + ", " + ID_UNITA_MISURA + ", " + LIM_INF_TOL
				+ ", " + LIM_SUP_TOL + ", " + VALORE_NOMINALE + ", " + VAL_RILEVATO1 + ", " + ESITO1
				+ ", " + VAL_RILEVATO2 + ", " + ESITO2 + ", " + ANNOTAZIONI + ", " + ID_AZIENDA
				+ ", " + ID_ARTICOLO + ", " + ID_LOTTO);
	}

}
