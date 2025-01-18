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

public class OrdEsecAtvSogCollaudoTM extends TableManager {

	public static final String ID_AZIENDA        = "ID_AZIENDA";
	public static final String DATA_ORDINE       = "DATA_ORDINE";
	public static final String ORD_CLI_RIGA      = "ORD_CLI_RIGA";
	public static final String ID_ORIGINALE      = "ID_ORIGINALE";
	public static final String ID_PROGRESSIVO    = "ID_PROGRESSIVO";
	public static final String ARTICOLO          = "R_ARTICOLO";
	public static final String COMMESSA          = "R_COMMESSA";
	public static final String CLIENTE           = "R_CLIENTE";
	public static final String DESCRIZIONE       = "DESCRIZIONE";
	public static final String DESCR_RIDOTTA     = "DESCR_RIDOTTA";
	public static final String ATTIVITA          = "R_ATTIVITA";
	public static final String R_CENTRO_LAVORO   = "R_CENTRO_LAVORO";
	public static final String  NUMERO_ORD_FMT = "NUMERO_ORD_FMT";
	public static final String QTA_CTR           = "QTA_CTR";
	public static final String PERC_CTR          = "PERC_CTR";
	public static final String NOME_TABELLA      = "NOME_TABELLA";
	public static final String ID_ATTIVITA       = "ID_ATTIVITA";
	public static final String ID_SEQUENZA_FASE  = "ID_SEQUENZA_FASE";
	public static final String SEQ_VISUALIZZ     = "SEQ_VISUALIZZ";
	public static final String MISURE_OBBL       = "MISURE_OBBL";
	public static final String VAL_NOM_PROPOSTO  = "VAL_NOM_PROPOSTO";
	public static final String FASE_OBBL         = "FASE_OBBL";
	public static final String CMTR_WHITNESS     = "CMTR_WHITNESS";

	public static final String ORP_EFF_DOC_ID = "ORP_EFF_DOC_ID";

	public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "ORD_ESEC_ATV_SOG_COLLAUDO_20";
	private static final String CLASS_NAME = OrdEsecAtvSogCollaudo.class.getName();

	private static TableManager cInstance;

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(OrdEsecAtvSogCollaudoTM.class);
		}
		return cInstance;
	}

	public OrdEsecAtvSogCollaudoTM() throws SQLException {
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

		addAttribute("IdAzienda",         ID_AZIENDA);
		addAttribute("DataOrdine",        DATA_ORDINE);
		addAttribute("OrdCliRiga",        ORD_CLI_RIGA);
		addAttribute("IdOriginale",       ID_ORIGINALE);
		addAttribute("IdProgressivo",     ID_PROGRESSIVO);
		addAttribute("Articolo",          ARTICOLO);
		addAttribute("Commessa",          COMMESSA);
		addAttribute("Cliente",           CLIENTE);
		addAttribute("Descrizione",       DESCRIZIONE);
		addAttribute("DescrRidotta",      DESCR_RIDOTTA);
		addAttribute("Attivita",          ATTIVITA);
		addAttribute("RCentroLavoro",     R_CENTRO_LAVORO);
		addAttribute("QtaCtr",            QTA_CTR);
		addAttribute("PercCtr",           PERC_CTR);
		addAttribute("NomeTabella",       NOME_TABELLA);
		addAttribute("IdAttivita",        ID_ATTIVITA);
		addAttribute("IdSequenzaFase",    ID_SEQUENZA_FASE);
		addAttribute("SeqVisualizz",      SEQ_VISUALIZZ);
		addAttribute("MisureObbl",        MISURE_OBBL);
		addAttribute("ValNomProposto",    VAL_NOM_PROPOSTO);
		addAttribute("FaseObbl",          FASE_OBBL);
		addAttribute("CmtrWhitness",      CMTR_WHITNESS);
		addAttribute("NumeroOrdFmt",NUMERO_ORD_FMT);
		//addAttribute("IdNumeroOrp", ORP_EFF_DOC_ID);

		// If this table has a primary key, uncomment or modify accordingly:
		// setKeys(ID_AZIENDA + ", " + ID_ORIGINALE + ", ...);

		// If there is a concurrency/timestamp column, specify it here:
		// setTimestampColumn(<columnName>);
	}

	private void init() throws SQLException {
		configure();
	}
}
