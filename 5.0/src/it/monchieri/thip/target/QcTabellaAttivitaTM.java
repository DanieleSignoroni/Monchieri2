package it.monchieri.thip.target;

import java.sql.SQLException;
import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.TableManager;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 23/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    23/12/2024</b>
 * <p></p>
 */

public class QcTabellaAttivitaTM extends TableManager {

	public static final String NOME_TABELLA              = "NOME_TABELLA";
	public static final String ID_AZIENDA                = "ID_AZIENDA";
	public static final String ID_ATTIVITA               = "ID_ATTIVITA";
	public static final String ID_SEQUENZA_FASE          = "ID_SEQUENZA_FASE";
	public static final String SEQ_VISUALIZZ             = "SEQ_VISUALIZZ";
	public static final String MISURE_OBBL               = "MISURE_OBBL";
	public static final String VAL_NOM_PROPOSTO          = "VAL_NOM_PROPOSTO";
	public static final String FASE_OBBL                 = "FASE_OBBL";
	public static final String FORZA_SOG_COLLAUDO        = "FORZA_SOG_COLLAUDO";
	public static final String FLAG_GENERATO_CERTIFICATO = "FLAG_GENERATO_CERTIFICATO";
	public static final String CMTR_WHITNESS             = "CMTR_WHITNESS";
	public static final String ID_SEQUENZA_FASE_TT       = "ID_SEQUENZA_FASE_TT";

	public static final String TABLE_NAME = SystemParam.getSchema("dbo") + "QC_TABELLA_ATTIVITA";
	private static final String CLASS_NAME = QcTabellaAttivita.class.getName();

	private static TableManager cInstance;

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(QcTabellaAttivitaTM.class);
		}
		return cInstance;
	}

	public QcTabellaAttivitaTM() throws SQLException {
		super();
	}

	@Override
	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	@Override
	protected void initializeRelation() throws SQLException {
		super.initializeRelation();

		addAttribute("NomeTabella",             NOME_TABELLA);
		addAttribute("IdAzienda",               ID_AZIENDA);
		addAttribute("IdAttivita",              ID_ATTIVITA);
		addAttribute("IdSequenzaFase",          ID_SEQUENZA_FASE);
		addAttribute("SeqVisualizz",            SEQ_VISUALIZZ);
		addAttribute("MisureObbl",              MISURE_OBBL);
		addAttribute("ValNomProposto",          VAL_NOM_PROPOSTO);
		addAttribute("FaseObbl",                FASE_OBBL);
		addAttribute("ForzaSogCollaudo",        FORZA_SOG_COLLAUDO);
		addAttribute("FlagGeneratoCertificato", FLAG_GENERATO_CERTIFICATO);
		addAttribute("CmtrWhitness",            CMTR_WHITNESS);
		addAttribute("IdSequenzaFaseTt",        ID_SEQUENZA_FASE_TT);

		// Primary Key
		setKeys(NOME_TABELLA + ", " + ID_AZIENDA + ", " + ID_ATTIVITA);

		// If you have a concurrency/timestamp column, specify here:
		// setTimestampColumn("<columnName>");
	}

	private void init() throws SQLException {
		configure();
	}
}
