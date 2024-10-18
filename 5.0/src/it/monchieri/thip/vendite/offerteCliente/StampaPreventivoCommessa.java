package it.monchieri.thip.vendite.offerteCliente;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.AvailableReport;
import com.thera.thermfw.batch.CrystalReportsInterface;
import com.thera.thermfw.batch.PrintingToolInterface;
import com.thera.thermfw.persist.Column;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.Proxy;
import com.thera.thermfw.security.Authorizable;
import com.thera.thermfw.security.Entity;
import com.thera.thermfw.security.Task;

import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.articolo.ArticoloCliente;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.cliente.AssoggettamentoIVA;
import it.thera.thip.base.cliente.Spesa;
import it.thera.thip.base.commessa.PreventivoCommessaTestata;
import it.thera.thip.base.comuniVenAcq.ImportiDocumentoOrdineUtil;
import it.thera.thip.base.comuniVenAcq.TipoRiga;
import it.thera.thip.base.documenti.StatoAttivita;
import it.thera.thip.cs.DescrizioneEstesa;
import it.thera.thip.cs.DescrizioneInLingua;
import it.thera.thip.cs.ThipElaboratePrintRunnable;
import it.thera.thip.vendite.generaleVE.CausaleOffertaCliente;
import it.thera.thip.vendite.generaleVE.PersDatiVen;
import it.thera.thip.vendite.offerteCliente.ImportiRigaOffertaVendita;
import it.thera.thip.vendite.offerteCliente.OffertaCliente;
import it.thera.thip.vendite.offerteCliente.OffertaClienteRiga;
import it.thera.thip.vendite.offerteCliente.OffertaClienteRigaPrm;
import it.thera.thip.vendite.offerteCliente.ReportOffertaClienteRiga;
import it.thera.thip.vendite.offerteCliente.ReportOffertaClienteTestata;
import it.thera.thip.vendite.ordineVE.ReportConfermaOrdVenTM;

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

public class StampaPreventivoCommessa extends ThipElaboratePrintRunnable implements Authorizable {

	protected static final String LINGUA_DEFAULT = SystemParam.getFrameworkLocaleString();

	@SuppressWarnings("rawtypes")
	protected static final Class TESTATA_REPORT = it.thera.thip.vendite.offerteCliente.ReportOffertaClienteTestata.class;

	@SuppressWarnings("rawtypes")
	protected static final Class RIGA_REPORT = it.thera.thip.vendite.offerteCliente.ReportOffertaClienteRiga.class;

	protected ReportOffertaClienteTestata reportOffertaClienteTestata = (ReportOffertaClienteTestata) Factory.createObject(TESTATA_REPORT);

	protected ReportOffertaClienteRiga reportOffertaClienteRiga = (ReportOffertaClienteRiga) Factory.createObject(RIGA_REPORT);

	public static Task task = null;
	public static Entity entity = null;

	protected List<OffertaClienteRigaPrm> iListaRigheSelezionate = new ArrayList<OffertaClienteRigaPrm>();

	protected Proxy iOffertaCliente = new Proxy(OffertaCliente.class);

	protected String iIdAzienda;

	protected boolean iStampaInLingua;

	protected AvailableReport availableReport;

	protected String iChiaveOfferta;

	protected String iChiaviSelezionati;

	protected int contatoreTestate = 0;
	protected int contatoreRighe = 0;
	protected int contatoreRigheGlobali = 0;

	protected String lingua;

	public String getIdAzienda() {
		return iIdAzienda;
	}

	public void setIdAzienda (String idAzienda){
		iIdAzienda = idAzienda;
		setIdAziendaInternal(idAzienda);
	}

	public void setIdAziendaInternal (String idAzienda){
		if (iOffertaCliente != null) {
			String ordKey = iOffertaCliente.getKey();
			iOffertaCliente.setKey(KeyHelper.replaceTokenObjectKey(ordKey, 1, idAzienda));
		}
	}

	public boolean isStampaInLingua() {
		return iStampaInLingua;
	}

	public void setStampaInLingua(boolean iStampaInLingua) {
		this.iStampaInLingua = iStampaInLingua;
	}

	public String getChiaveOfferta() {
		return iChiaveOfferta;
	}

	public void setChiaveOfferta(String iChiaveOfferta) {
		this.iChiaveOfferta = iChiaveOfferta;
	}

	public String getChiaviSelezionati() {
		return iChiaviSelezionati;
	}

	public void setChiaviSelezionati(String iChiaviSelezionati) {
		this.iChiaviSelezionati = iChiaviSelezionati;
	}

	public StampaPreventivoCommessa() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	@Override
	public boolean createReport() {
		setOffertaClienteKey(getChiaveOfferta());
		try {
			if (!initAvailableReport()) {
				return false;
			}
			if (entity == null) {
				entity = Entity.elementWithKey("YStampaPrevComm", Entity.NO_LOCK);
				String taskKey = KeyHelper.buildObjectKey(new String[] {"YStampaPrevComm", getTaskId()});
				task = Task.elementWithKey(taskKey, Entity.NO_LOCK);
			}
			initListaRigheKeys();
			if(iListaRigheSelezionate.size() > 0) {
				int rc1 = fillTestata(true);
				if (rc1 < ErrorCodes.NO_ROWS_UPDATED) {
					return false;
				}
				ConnectionManager.commit();
			}else {
				output.println(" ** Non e' presente nessuna riga offerta da stampare **");
				return false;
			}
		}catch (Exception ex) {
			return false;
		}
		return true;
	}

	protected boolean initAvailableReport (){
		availableReport = createNewReport(getReportId());
		setPrintToolInterface( (com.thera.thermfw.batch.PrintingToolInterface) com.thera.thermfw.persist.Factory.createObject(com.thera.thermfw.batch.CrystalReportsInterface.class));

		try {
			this.setPrintToolInterface( (PrintingToolInterface) Factory.createObject(CrystalReportsInterface.class));

			printToolInterface.fillUserParams(getParameters());
			printToolInterface.fillEnum(getEnums());
			reportOffertaClienteTestata = (ReportOffertaClienteTestata) Factory.createObject(TESTATA_REPORT);
			String s = printToolInterface.generateDefaultWhereCondition(availableReport, ReportConfermaOrdVenTM.TABLE_NAME);
			availableReport.setWhereCondition(s);
			int rc = availableReport.save();
			if (rc >= ErrorCodes.OK) {
				ConnectionManager.commit();
			} else {
				ConnectionManager.rollback();
				output.println("Fallito il salvataggio dell'availableReport");
				return false;
			}

		}
		catch (java.sql.SQLException ex) {
			ex.printStackTrace(com.thera.thermfw.base.Trace.excStream);
			return false;
		}
		return true;
	}

	private int fillTestata(boolean check) throws SQLException {
		ReportOffertaClienteTestata report = caricaDatiTestata(check);
		return salvaDatiTestata(report);
	}

	public ReportOffertaClienteTestata caricaDatiTestata(boolean check) throws SQLException {
		contatoreRighe = 0;
		lingua = LINGUA_DEFAULT;
		if (isStampaInLingua() && getOffertaCliente().getLingua() != null) {
			lingua = getOffertaCliente().getLingua().getId();
		}
		setPropertiesLblLanguage(lingua);
		updateJobParameters();
		reportOffertaClienteTestata = (ReportOffertaClienteTestata) Factory.createObject(TESTATA_REPORT);
		//fillDatiCliente(getOffertaCliente());
		fillCausaleOffertaCliente(getOffertaCliente().getCausale());
		//fillCategoriaContabileCliente(getOffertaCliente().getCategoriaContabile());
		//fillDestinatario(getOffertaCliente());
		//fillDatiClienteFatturazione(getOffertaCliente());
		//fillBancaAziendale(getOffertaCliente());
		//fillBanca(getOffertaCliente());
		//fillDatiRif(getOffertaCliente());
		reportOffertaClienteTestata.setEmail(getOffertaCliente().getEmailOfferta());
		reportOffertaClienteTestata.setFax(getOffertaCliente().getFaxOfferta());

		//fillTotali(getOffertaCliente());

		reportOffertaClienteTestata.setAvailableReport(availableReport);
		reportOffertaClienteTestata.setStatoAvanzamento(getOffertaCliente().getStatoAvanzamento());
		reportOffertaClienteTestata.setStatoEvasione(getOffertaCliente().getStatoEvasione());
		reportOffertaClienteTestata.setSaldoManuale(getOffertaCliente().getSaldoManuale());
		reportOffertaClienteTestata.setIdCausaleChiusuraOffVen(getOffertaCliente().getIdCausaleChiusuraOffVen());
		reportOffertaClienteTestata.setIdAzienda(getOffertaCliente().getIdAzienda());
		reportOffertaClienteTestata.setIdAnnoDocumento(getOffertaCliente().getAnnoDocumento());
		reportOffertaClienteTestata.setIdNumeroDocumento(getOffertaCliente().getNumeroDocumento());
		reportOffertaClienteTestata.setDataDocumento(getOffertaCliente().getDataDocumento());
		reportOffertaClienteTestata.setNumeroOffertaFormattato(getOffertaCliente().getNumeroDocumentoFormattato());
		reportOffertaClienteTestata.setTipoIntestatarioOfferta(getOffertaCliente().getTipoIntestatarioOfferta());
		reportOffertaClienteTestata.setContatto(getOffertaCliente().getIdContatto());
		if (getOffertaCliente().getIdContatto() != null) {
			reportOffertaClienteTestata.setDesContatto(getOffertaCliente().getContatto().getAzRagioneSociale());
		}
		reportOffertaClienteTestata.setIdAnagrafico(getOffertaCliente().getIdAnagrafico());
		reportOffertaClienteTestata.setIdDivisione(getOffertaCliente().getIdDivisione());

		reportOffertaClienteTestata.setWfClassId(getOffertaCliente().getWfStatus().getWfClassId());
		reportOffertaClienteTestata.setWfId(getOffertaCliente().getWfStatus().getWfId());
		reportOffertaClienteTestata.setWfNodeId(getOffertaCliente().getWfStatus().getCurrentNodeId());
		reportOffertaClienteTestata.setWfSubNodeId(getOffertaCliente().getWfStatus().getCurrentSubNodeId());

		reportOffertaClienteTestata.setNota(getOffertaCliente().getNota());

		try {
			reportOffertaClienteTestata.caricaCommenti(getOffertaCliente(), entity, task, lingua);
		}
		catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}

		reportOffertaClienteTestata.setIdDocumentoMm(getOffertaCliente().getIdDocumentoMM());
		reportOffertaClienteTestata.setDataConsegnaRichiesta(getOffertaCliente().getDataConsegnaRichiesta());
		reportOffertaClienteTestata.setDataConsegnaConfermata(getOffertaCliente().getDataConsegnaConfermata());

		if (getOffertaCliente().getMagazzino() != null) {
			reportOffertaClienteTestata.setIdMagazzino(getOffertaCliente().getIdMagazzino());
			String desMagazzino = getOffertaCliente().getMagazzino().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getMagazzino().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desMagazzino = getOffertaCliente().getMagazzino().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesMagazzino(desMagazzino);
		}

		if (getOffertaCliente().getCommessa() != null) {
			reportOffertaClienteTestata.setIdCommessa(getOffertaCliente().getIdCommessa());
			String desCommessa = getOffertaCliente().getCommessa().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getCommessa().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desCommessa = getOffertaCliente().getCommessa().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesCommessa(desCommessa);
		}

		if (getOffertaCliente().getCentroCosto() != null) {
			reportOffertaClienteTestata.setIdCentroCosto(getOffertaCliente().getIdCentroCosto());
			String desCentroRicavo = getOffertaCliente().getCentroCosto().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getCentroCosto().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desCentroRicavo = getOffertaCliente().getCentroCosto().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesCentroRicavo(desCentroRicavo);
		}

		if (getOffertaCliente().getFornitore() != null) {
			reportOffertaClienteTestata.setIdFornitore(getOffertaCliente().getIdFornitore());
		}

		if (getOffertaCliente().getLingua() != null) {
			reportOffertaClienteTestata.setIdLingua(getOffertaCliente().getIdLingua());
			reportOffertaClienteTestata.setDesLingua(getOffertaCliente().getLingua().getDescription());
		}

		if (getOffertaCliente().getZona() != null) {
			reportOffertaClienteTestata.setIdZona(getOffertaCliente().getIdZona());
			String desZona = getOffertaCliente().getZona().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getZona().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desZona = getOffertaCliente().getZona().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesZona(desZona);
		}

		reportOffertaClienteTestata.setPriorita(getOffertaCliente().getPriorita());

		if (getOffertaCliente().getListinoPrezzi() != null) {
			reportOffertaClienteTestata.setIdListino(getOffertaCliente().getIdListinoPrezzi());
			String desListino = getOffertaCliente().getListinoPrezzi().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getListinoPrezzi().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desListino = getOffertaCliente().getListinoPrezzi().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesListino(desListino);
		}
		//reportOffertaClienteTestata.setOrdineContratto(getOffertaCliente().getOrdineContratto());

		if (getOffertaCliente().getValuta() != null) {
			reportOffertaClienteTestata.setIdValuta(getOffertaCliente().getIdValuta());
			String desValuta = getOffertaCliente().getValuta().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getValuta().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desValuta = getOffertaCliente().getValuta().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesValuta(desValuta);
		}

		if (getOffertaCliente().getCambio() != null) {
			reportOffertaClienteTestata.setFattoreCambi(getOffertaCliente().getCambio());

		}
		if (getOffertaCliente().getAssoggettamentoIVA() != null) {
			reportOffertaClienteTestata.setAssogIVA(getOffertaCliente().getIdAssogIva());

			String desAssogIVA = getOffertaCliente().getAssoggettamentoIVA().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getAssoggettamentoIVA().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desAssogIVA = getOffertaCliente().getAssoggettamentoIVA().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesAssogIVA(desAssogIVA);
		}

		if (getOffertaCliente().getModalitaPagamento() != null) {
			reportOffertaClienteTestata.setModPagamento(getOffertaCliente().getIdModPagamento());
			String desModPagamento = getOffertaCliente().getModalitaPagamento().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getModalitaPagamento().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desModPagamento = getOffertaCliente().getModalitaPagamento().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesModPagamento(desModPagamento);
		}

		reportOffertaClienteTestata.setDataInizioPag(getOffertaCliente().getDataInizioPagamento());
		reportOffertaClienteTestata.setSpesa1(getOffertaCliente().getIdSpesa1());
		reportOffertaClienteTestata.setSpesa2(getOffertaCliente().getIdSpesa2());
		reportOffertaClienteTestata.setScontoModalita(getOffertaCliente().getPrcScontoModalita());
		reportOffertaClienteTestata.setScontoCli(getOffertaCliente().getPrcScontoIntestatario());
		reportOffertaClienteTestata.setSconto(getOffertaCliente().getIdSconto());

		if (getOffertaCliente().getScontoTabellare() != null) {
			String desSconto = getOffertaCliente().getScontoTabellare().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getScontoTabellare().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desSconto = getOffertaCliente().getScontoTabellare().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesSconto(desSconto);
		}

		reportOffertaClienteTestata.setScontoFineFatt(getOffertaCliente().getPrcScontoFineFattura());

		if (getOffertaCliente().getAgente() != null) {
			reportOffertaClienteTestata.setAgente(getOffertaCliente().getIdAgente());
			reportOffertaClienteTestata.setDesAgente(getOffertaCliente().getAgente().getDescrizione().getDescrizione());
			reportOffertaClienteTestata.setPvgAgente(getOffertaCliente().getAgente().getPrcProvvigione());
		}

		if (getOffertaCliente().getSubagente() != null) {
			reportOffertaClienteTestata.setAgenteSub(getOffertaCliente().getIdSubAgente());
			reportOffertaClienteTestata.setDesAgenteSub(getOffertaCliente().getSubagente().getDescrizione().getDescrizione());
			reportOffertaClienteTestata.setPvgAgenteSub(getOffertaCliente().getSubagente().getPrcProvvigione());
		}

		if (getOffertaCliente().getModalitaSpedizione() != null) {
			reportOffertaClienteTestata.setModSpedizione(getOffertaCliente().getIdModSpedizione());
			String desModSpediz = getOffertaCliente().getModalitaSpedizione().getDescrizione().getDescrizione();
			if (desModSpediz != null && getOffertaCliente().getDescrModalitaSpedizione() != null &&
					!desModSpediz.equals(getOffertaCliente().getDescrModalitaSpedizione())) {
				desModSpediz = getOffertaCliente().getDescrModalitaSpedizione();
			}
			else {
				desModSpediz = getDescrizioneInLinguaCorrente(getOffertaCliente().getModalitaSpedizione().getDescrizione());
			}
			reportOffertaClienteTestata.setDesModSpediz(desModSpediz);
		}

		if (getOffertaCliente().getModalitaConsegna() != null) {
			reportOffertaClienteTestata.setModConsegna(getOffertaCliente().getIdModConsegna());
			String desModConsegna = getOffertaCliente().getModalitaConsegna().getDescrizione().getDescrizione();
			//...Se è stata indicata una descrizione apposita DIVERSA da quella standard utilizzo quella
			//...anziché quella in lingua.
			if (desModConsegna != null && getOffertaCliente().getDescrModalitaConsegna() != null &&
					!desModConsegna.equals(getOffertaCliente().getDescrModalitaConsegna())) {
				desModConsegna = getOffertaCliente().getDescrModalitaConsegna();
			}
			else {
				desModConsegna = getDescrizioneInLinguaCorrente(getOffertaCliente().getModalitaConsegna().getDescrizione());
			}
			reportOffertaClienteTestata.setDesModConsegna(desModConsegna);
		}

		if (getOffertaCliente().getAspettoEsteriore() != null) {
			reportOffertaClienteTestata.setAspettoEsn(getOffertaCliente().getIdAspettoEsn());
			String desAspettoEsn = getOffertaCliente().getAspettoEsteriore().getDescrizione().getDescrizione();
			//...Se è stata indicata una descrizione apposita DIVERSA da quella standard utilizzo quella
			//...anziché quella in lingua.
			if (desAspettoEsn != null && getOffertaCliente().getDescrAspettoEsteriore() != null &&
					!desAspettoEsn.equals(getOffertaCliente().getDescrAspettoEsteriore())) {
				desAspettoEsn = getOffertaCliente().getDescrAspettoEsteriore();
			}
			else {
				desAspettoEsn = getDescrizioneInLinguaCorrente(getOffertaCliente().getAspettoEsteriore().getDescrizione());
			}
			reportOffertaClienteTestata.setDesAspettoEsn(desAspettoEsn);
		}

		if (getOffertaCliente().getCausaleTrasporto() != null) {
			reportOffertaClienteTestata.setCauTrasporto(getOffertaCliente().getIdCauTrasporto());
			String desCauTrasporto = getOffertaCliente().getCausaleTrasporto().getDescrizione().getDescrizione();
			//...Se è stata indicata una descrizione apposita DIVERSA da quella standard utilizzo quella
			//...anziché quella in lingua.
			if (desCauTrasporto != null && getOffertaCliente().getDescrCausaleTrasporto() != null &&
					!desCauTrasporto.equals(getOffertaCliente().getDescrCausaleTrasporto())) {
				desCauTrasporto = getOffertaCliente().getDescrCausaleTrasporto();
			}
			else {
				desCauTrasporto = getDescrizioneInLinguaCorrente(getOffertaCliente().getCausaleTrasporto().getDescrizione());
			}
			reportOffertaClienteTestata.setDesCauTrasporto(desCauTrasporto);
		}

		if (getOffertaCliente().getVettore1() != null) {
			reportOffertaClienteTestata.setVettore1(getOffertaCliente().getIdVettore1());
			//...Se è stata indicata una descrizione apposita utilizzo quella
			if (getOffertaCliente().getDescrVettore1() != null && !getOffertaCliente().getDescrVettore1().equals("")) {
				reportOffertaClienteTestata.setDesVettore1(getOffertaCliente().getDescrVettore1());
			}
			else if (getOffertaCliente().getVettore1() != null) {
				reportOffertaClienteTestata.setDesVettore1(getOffertaCliente().getVettore1().getRagioneSociale());
			}
		}

		if (getOffertaCliente().getVettore2() != null) {
			reportOffertaClienteTestata.setVettore2(getOffertaCliente().getIdVettore2());
			//...Se è stata indicata una descrizione apposita utilizzo quella
			if (getOffertaCliente().getDescrVettore2() != null && !getOffertaCliente().getDescrVettore2().equals("")) {
				reportOffertaClienteTestata.setDesVettore2(getOffertaCliente().getDescrVettore2());
			}
			else if (getOffertaCliente().getVettore2() != null) {
				reportOffertaClienteTestata.setDesVettore2(getOffertaCliente().getVettore2().getRagioneSociale());
			}
		}

		if (getOffertaCliente().getVettore3() != null) {
			reportOffertaClienteTestata.setVettore3(getOffertaCliente().getIdVettore3());
			//...Se è stata indicata una descrizione apposita utilizzo quella
			if (getOffertaCliente().getDescrVettore3() != null && !getOffertaCliente().getDescrVettore3().equals("")) {
				reportOffertaClienteTestata.setDesVettore3(getOffertaCliente().getDescrVettore3());
			}
			else if (getOffertaCliente().getVettore3() != null) {
				reportOffertaClienteTestata.setDesVettore3(getOffertaCliente().getVettore3().getRagioneSociale());
			}
		}

		reportOffertaClienteTestata.setDataInizioTrp(getOffertaCliente().getDataInizioTrasporto());
		reportOffertaClienteTestata.setNumeroColli(getOffertaCliente().getNumeroColli());
		reportOffertaClienteTestata.setPesoLordo(getOffertaCliente().getPesoLordo());
		reportOffertaClienteTestata.setPesoNetto(getOffertaCliente().getPesoNetto());
		reportOffertaClienteTestata.setTipoEvasioneOrd(getOffertaCliente().getTipoEvasioneOrdine());
		reportOffertaClienteTestata.setPrcPdtResiduo(getOffertaCliente().getPrcPerditaResiduo());

		if (getOffertaCliente().getCadenzaConsegna() != null) {
			reportOffertaClienteTestata.setCadenzaCng(getOffertaCliente().getIdCadenzaCng());
			String desCadenzaCng = getOffertaCliente().getCadenzaConsegna().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getCadenzaConsegna().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desCadenzaCng = getOffertaCliente().getCadenzaConsegna().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesCadenzaCng(desCadenzaCng);
		}

		if (getOffertaCliente().getGiroConsegne() != null) {
			reportOffertaClienteTestata.setGiroConsegne(getOffertaCliente().getIdGiroConsegne());
			String desGiroConsegne = getOffertaCliente().getGiroConsegne().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getGiroConsegne().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desGiroConsegne = getOffertaCliente().getGiroConsegne().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesGiroConsegne(desGiroConsegne);
		}

		if (getOffertaCliente().getResponsabileVendite() != null) {
			reportOffertaClienteTestata.setResponVendite(getOffertaCliente().getIdResponsabileVendite());
			String desResponVendite = getOffertaCliente().getResponsabileVendite().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getResponsabileVendite().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desResponVendite = getOffertaCliente().getResponsabileVendite().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesResponVendite(desResponVendite);
		}

		if (getOffertaCliente().getCategoriaContabile() != null) {
			reportOffertaClienteTestata.setCatContCli(getOffertaCliente().getIdCategoriaContabile());
			String desCatContCli = getOffertaCliente().getCategoriaContabile().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
			if (isStampaInLingua() && getOffertaCliente().getCategoriaContabile().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
				desCatContCli = getOffertaCliente().getCategoriaContabile().getDescrizione().getHandler().getText("Descrizione", lingua);
			}
			reportOffertaClienteTestata.setDesCatContCli(desCatContCli);
		}

		//		Hashtable iva = totali.getImponibileImpostaAssoggIva();
		//		List<String> listKeys = new ArrayList<String>( iva.keySet());//33301
		//		Collections.sort(listKeys, Collections.reverseOrder());//33301
		//		//Enumeration enm = iva.keys();
		//		Iterator  enm = listKeys.iterator();
		//		TotaliImportiOffertaVendita.DatiAssoggIva datiIVA;
		//		int size = iva.size();
		//		if (size > 0) {
		//			String desAssogIVA = null;//Fix 32749
		//			switch (size) {
		//			case 8:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				AssoggettamentoIVA assIVA = datiIVA.getAssoggIva();
		//				reportOffertaClienteTestata.setAssIVA08(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}
		//				//reportOffertaClienteTestata.setDesAssIVA08(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA08(desAssogIVA);
		//				//Fix 32749 Fine
		//				reportOffertaClienteTestata.setAlqIVA08(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr08(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA08(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA08(datiIVA.getImposta());
		//			case 7:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				assIVA = datiIVA.getAssoggIva();
		//				reportOffertaClienteTestata.setAssIVA07(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}				
		//				//reportOffertaClienteTestata.setDesAssIVA07(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA07(desAssogIVA);
		//				//Fix 32749 Fine
		//				reportOffertaClienteTestata.setAlqIVA07(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr07(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA07(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA07(datiIVA.getImposta());
		//			case 6:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				assIVA = datiIVA.getAssoggIva();
		//				reportOffertaClienteTestata.setAssIVA06(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}				
		//				//reportOffertaClienteTestata.setDesAssIVA06(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA06(desAssogIVA);
		//				//Fix 32749 Fine
		//				reportOffertaClienteTestata.setAlqIVA06(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr06(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA06(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA06(datiIVA.getImposta());
		//			case 5:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				assIVA = datiIVA.getAssoggIva();
		//				reportOffertaClienteTestata.setAssIVA05(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}				
		//				//reportOffertaClienteTestata.setDesAssIVA05(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA05(desAssogIVA);
		//				//Fix 32749 Fine					
		//				reportOffertaClienteTestata.setAlqIVA05(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr05(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA05(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA05(datiIVA.getImposta());
		//			case 4:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				assIVA = datiIVA.getAssoggIva();				
		//				reportOffertaClienteTestata.setAssIVA04(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}				
		//				//reportOffertaClienteTestata.setDesAssIVA04(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA04(desAssogIVA);
		//				//Fix 32749 Fine		
		//				reportOffertaClienteTestata.setAlqIVA04(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr04(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA04(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA04(datiIVA.getImposta());
		//			case 3:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				assIVA = datiIVA.getAssoggIva();
		//				reportOffertaClienteTestata.setAssIVA03(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}				
		//				//reportOffertaClienteTestata.setDesAssIVA03(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA03(desAssogIVA);
		//				//Fix 32749 Fine				
		//				reportOffertaClienteTestata.setAlqIVA03(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr03(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA03(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA03(datiIVA.getImposta());
		//			case 2:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				assIVA = datiIVA.getAssoggIva();
		//				reportOffertaClienteTestata.setAssIVA02(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}				
		//				//reportOffertaClienteTestata.setDesAssIVA02(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA02(desAssogIVA);
		//				//Fix 32749 Fine
		//				reportOffertaClienteTestata.setAlqIVA02(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr02(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA02(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA02(datiIVA.getImposta());
		//			case 1:
		//				//datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.nextElement());//33301
		//				datiIVA = (TotaliImportiOffertaVendita.DatiAssoggIva) iva.get(enm.next());//33301
		//				assIVA = datiIVA.getAssoggIva();
		//				reportOffertaClienteTestata.setAssIVA01(assIVA.getIdAssoggettamentoIVA());
		//				//Fix 32749 INIZIO
		//				desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  +  assIVA.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		//				if(isStampaInLingua())
		//				{
		//					String desAssInLing = assIVA.getDescrizione().getHandler().getText("Descrizione", lingua);
		//					if (desAssInLing != null)
		//						desAssogIVA = assIVA.getIdAssoggettamentoIVA() + " - "  + desAssInLing;
		//				}				
		//				//reportOffertaClienteTestata.setDesAssIVA01(datiIVA.getDescrAssoggIvaFmt());
		//				reportOffertaClienteTestata.setDesAssIVA01(desAssogIVA);
		//				//Fix 32749 Fine
		//				reportOffertaClienteTestata.setAlqIVA01(assIVA.getAliquotaIVA());
		//				reportOffertaClienteTestata.setAlqIVAStr01(datiIVA.getAliquotaIvaFmt());
		//				reportOffertaClienteTestata.setImponIVA01(datiIVA.getImponibile());
		//				reportOffertaClienteTestata.setImpostaIVA01(datiIVA.getImposta());
		//			}
		//		}
		reportOffertaClienteTestata.setFlagRisUte1(getOffertaCliente().getFlagRiservatoUtente1());
		reportOffertaClienteTestata.setFlagRisUte2(getOffertaCliente().getFlagRiservatoUtente2());
		reportOffertaClienteTestata.setFlagRisUte3(getOffertaCliente().getFlagRiservatoUtente3());
		reportOffertaClienteTestata.setFlagRisUte4(getOffertaCliente().getFlagRiservatoUtente4());
		reportOffertaClienteTestata.setFlagRisUte5(getOffertaCliente().getFlagRiservatoUtente5());
		reportOffertaClienteTestata.setStringaRisUte1(getOffertaCliente().getAlfanumRiservatoUtente1());
		reportOffertaClienteTestata.setStringaRisUte2(getOffertaCliente().getAlfanumRiservatoUtente2());
		reportOffertaClienteTestata.setNumRisUte1(new Integer(getOffertaCliente().getNumeroRiservatoUtente1()));
		reportOffertaClienteTestata.setNumRisUte2(new Integer(getOffertaCliente().getNumeroRiservatoUtente2()));
		/*reportOffertaClienteTestata.setStampaListaControllo(getOffertaCliente().getStampaListaControllo());
         reportOffertaClienteTestata.setStampaConfermaOrdine(getOffertaCliente().getStampaConfermaOrdine());
         reportOffertaClienteTestata.setEmailConferma(getOffertaCliente().getEmailConferma());
         reportOffertaClienteTestata.setFaxConferma(getOffertaCliente().getFaxConferma());*/
		reportOffertaClienteTestata.setRBatchJobId(new Integer(getBatchJob().getBatchJobId()));
		reportOffertaClienteTestata.setReportNumber(new Integer(availableReport.getReportNr()));
		reportOffertaClienteTestata.setRigaJobId(++contatoreTestate);
		reportOffertaClienteTestata.setTimestampAgg(getOffertaCliente().getDatiComuniEstesi().getTimestampAgg());
		reportOffertaClienteTestata.setTimestampCrz(getOffertaCliente().getDatiComuniEstesi().getTimestampCrz());
		reportOffertaClienteTestata.setIdUtenteAgg(getOffertaCliente().getDatiComuniEstesi().getIdUtenteAgg());
		reportOffertaClienteTestata.setIdUtenteCrz(getOffertaCliente().getDatiComuniEstesi().getIdUtenteCrz());
		reportOffertaClienteTestata.setStato(getOffertaCliente().getDatiComuniEstesi().getStato());
		//caricaAltriDatiTestata();//Fix 34035
		//fillDatiClienteDestSSD(getOffertaCliente());//Fix 41051
		return reportOffertaClienteTestata;
	}

	public String getDescrizioneInLinguaCorrente(DescrizioneInLingua descrInLingua) {
		return ImportiDocumentoOrdineUtil.getDescrizioneInLingua(descrInLingua, lingua);
	}

	@SuppressWarnings("rawtypes")
	public int salvaDatiTestata(ReportOffertaClienteTestata reportOffertaClienteTestata) throws SQLException {
		int rc = reportOffertaClienteTestata.save();
		if (rc >= ErrorCodes.OK) {
			List righePrimarie = iListaRigheSelezionate;
			int rc1 = fillRighePrimarie(righePrimarie);
			if (rc1 >= ErrorCodes.OK) {
				getOffertaCliente().setStampaConfermaOfferta(StatoAttivita.ESEGUITO);
				int rc2 = getOffertaCliente().salvaSoloTestata();
				if (rc2 < ErrorCodes.OK) {
					rc = rc2;
				}
				else {
					rc = rc + rc1 + rc2;
				}
			}
			else {
				rc = rc1;
			}
		}

		contatoreRigheGlobali += contatoreRighe;

		return rc;
	}

	private void fillCausaleOffertaCliente(CausaleOffertaCliente cauOffCli) {
		reportOffertaClienteTestata.setCauOffVen(cauOffCli.getIdCausaleOffertaCliente());
		String desCauOffCli = cauOffCli.getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
		if (isStampaInLingua() && cauOffCli.getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
			desCauOffCli = cauOffCli.getDescrizione().getHandler().getText("Descrizione", lingua);
		}
		reportOffertaClienteTestata.setDesCauOffVen(desCauOffCli);
		reportOffertaClienteTestata.setScorporoIVA(cauOffCli.isScorporoIVAAbilitato());
	}

	@SuppressWarnings("rawtypes")
	protected int fillRighePrimarie(List righePrimarie) throws SQLException {
		Iterator iterator = righePrimarie.iterator();
		int rc = 0;
		while (iterator.hasNext()) {
			OffertaClienteRigaPrm offertaClienteRiga = (OffertaClienteRigaPrm) iterator.next();
			if (offertaClienteRiga.getTipoRiga() != TipoRiga.SPESE_MOV_VALORE ||
					!offertaClienteRiga.isSpesaPercentuale()) {
				rc = fillRiga((OffertaClienteRiga) offertaClienteRiga, true);
			}
		}
		return rc;
	}

	protected int fillRiga(OffertaClienteRiga ir, boolean primaria) throws SQLException {
		int ris = 0;
		if (ir.getArticolo() != null) {
			ReportOffertaClienteRiga report = caricaDatiRiga(ir, primaria);
			if (report != null) {
				ris = salvaDatiRiga(report);
			}
		}
		return ris;
	}

	@SuppressWarnings({ "unused", "static-access" })
	public ReportOffertaClienteRiga caricaDatiRiga(OffertaClienteRiga ir, boolean primaria) throws SQLException {
		int rc = 0;
		reportOffertaClienteRiga = (ReportOffertaClienteRiga) Factory.createObject(RIGA_REPORT);
		ImportiRigaOffertaVendita iro = null;

		String rigaKey = ir.getKey();
		if (ir.getSpecializzazioneRiga() == ir.RIGA_PRIMARIA || ir.getSpecializzazioneRiga() == ir.RIGA_SECONDARIA_DA_FATTURARE) {
			//iro = (ImportiRigaOffertaVendita) totali.getImportiDaFatturare().get(rigaKey);

		}
		String riga;
		Articolo articolo = ir.getArticolo();
		if (articolo != null) {
			contatoreRighe++;
			reportOffertaClienteRiga.setBatchJobId(reportOffertaClienteTestata.getRBatchJobId());
			reportOffertaClienteRiga.setReportNr(reportOffertaClienteTestata.getReportNumber());
			reportOffertaClienteRiga.setRigaJobId(reportOffertaClienteTestata.getRigaJobId());
			reportOffertaClienteRiga.setDetRigaJob(new Integer(contatoreRighe));

			ArticoloCliente articoloCliente = (ArticoloCliente) ir.recuperaArticoloIntestatario();
			if (articoloCliente != null) {
				reportOffertaClienteRiga.setArticoloCliente(articoloCliente.getArticoloPerCliente());
				DescrizioneEstesa desEstesa = articoloCliente.getDescrizioneEst();
				if (desEstesa.getDescrizione() != null) {
					//reportOffertaClienteRiga.setDescrizioneArtClifor(ImportiDocumentoOrdineUtil.getDescrizioneInLingua(((DescrizioneInLingua)desEstesa), lingua));
					reportOffertaClienteRiga.setDescArticoloCliente(ImportiDocumentoOrdineUtil.getDescrizioneInLingua(((DescrizioneInLingua) desEstesa), lingua));
				}
			}

			String descrizioneArticoloEstesa = getDesExtArt(articoloCliente, articolo);
			//reportOffertaClienteRiga.setDesArtClifor(descrizioneArticoloEstesa);

			//Fix14727 Inizio RA
			//reportOffertaClienteRiga.setDesEstesaArticolo(descrizioneArticoloEstesa);
			String descInitRiga = (articoloCliente != null && articoloCliente.getDescrizioneEst() != null && !isEm(articoloCliente.getDescrizioneEst().getDescrizioneEstesa())) ? articoloCliente.getDescrizioneEst().getDescrizioneEstesa() : articolo.getDescrizioneArticoloNLS().getDescrizioneEstesa();

			boolean recuparaDescExtDaArt = getRecuparaDescExtDaArt();//34589

			if (PersDatiVen.getCurrentPersDatiVen().getGestioneDescExtArticolo() == PersDatiVen.NON_GESTITA)
				reportOffertaClienteRiga.setDesEstesaArticolo(descrizioneArticoloEstesa);
			else if(!isStampaInLingua())
			{
				// if (!isEm(ir.getDescrizioneExtArticolo()))//34589
				if (!isEm(ir.getDescrizioneExtArticolo()) || !recuparaDescExtDaArt)//34589
					reportOffertaClienteRiga.setDesEstesaArticolo(ir.getDescrizioneExtArticolo());
				else
					reportOffertaClienteRiga.setDesEstesaArticolo(descInitRiga);
			}
			else //if (isEm(ir.getDescrizioneExtArticolo()) || ir.getDescrizioneExtArticolo().equals(descInitRiga))//34589
				if ((isEm(ir.getDescrizioneExtArticolo()))&& recuparaDescExtDaArt || (ir.getDescrizioneExtArticolo()!=null && ir.getDescrizioneExtArticolo().equals(descInitRiga)))//34589
					//Fix 19744 inizio
					//reportOffertaClienteRiga.setDesEstesaArticolo(descrizioneArticoloEstesa);
					reportOffertaClienteRiga.setDesEstesaArticolo(ImportiDocumentoOrdineUtil.getDesExtSoloInLingua(articoloCliente!=null ? articoloCliente.getDescrizioneEst() : null, articolo.getDescrizioneArticoloNLS(), (lingua == null ? LINGUA_DEFAULT : lingua), false));
			//Fix 19744 fine
				else
					reportOffertaClienteRiga.setDesEstesaArticolo(ir.getDescrizioneExtArticolo());

			//Fix14727 Fine RA

			if (ir.getTipoRiga() != TipoRiga.SPESE_MOV_VALORE) {
				String des1 = ir.getArticolo().getDescrizioneArticoloNLS().getDescrizione();
				String des2 = ir.getDescrizioneArticolo();
				if ((des1 != null && des2 != null && des1.equals(des2)) || des2 == null || des2.equals("")) {
					String desArticolo = ir.getArticolo().getDescrizioneArticoloNLS().getHandler().getText("Descrizione", LINGUA_DEFAULT);
					if (isStampaInLingua() && ir.getArticolo().getDescrizioneArticoloNLS().getHandler().getText("Descrizione", lingua) != null) {
						desArticolo = ir.getArticolo().getDescrizioneArticoloNLS().getHandler().getText("Descrizione", lingua);
					}
					reportOffertaClienteRiga.setDesArticolo(desArticolo);
				}
				else {
					reportOffertaClienteRiga.setDesArticolo(des2);
				}
			}
			else {
				//righe di tipo spesa: recupero la descrizione in lingua della spesa
				Spesa spesa = ir.getSpesa();
				if (spesa != null) {
					String desSpesa = spesa.getDescrizione().getDescrizione(); //descr.default in lingua aziendale
					String desSpesaSuRiga = ir.getDescrizioneArticolo(); // descriz.ev.riscritta sulla riga
					if ((desSpesa != null && desSpesaSuRiga != null && desSpesa.equals(desSpesaSuRiga)) // la descriz.di riga non è stata modificata
							|| desSpesaSuRiga == null || desSpesaSuRiga.equals("")) {
						reportOffertaClienteRiga.setDesArticolo(getDescrizioneInLinguaCorrente(spesa.getDescrizione()));
					}
					else {
						reportOffertaClienteRiga.setDesArticolo(desSpesaSuRiga);
					}
				}
			}
			reportOffertaClienteRiga.setTipoRigaReport(ReportOffertaClienteRiga.TIPO_RIGA_REPORT_STANDARD);
			reportOffertaClienteRiga.setStatoAvanzamento(ir.getStatoAvanzamento());
			reportOffertaClienteRiga.setStatoEvasione(ir.getStatoEvasione());
			reportOffertaClienteRiga.setSaldoManuale(ir.isSaldoManuale() ? Column.TRUE_CHAR : Column.FALSE_CHAR);
			reportOffertaClienteRiga.setIdAzienda(ir.getIdAzienda());
			reportOffertaClienteRiga.setIdAnnoDocumento(ir.getAnnoDocumento());
			reportOffertaClienteRiga.setIdNumeroOff(ir.getNumeroDocumento());
			reportOffertaClienteRiga.setIdRigaOff(ir.getNumeroRigaDocumento());
			reportOffertaClienteRiga.setIdDetRigaOff(ir.getDettaglioRigaDocumento());
			reportOffertaClienteRiga.setSequenzaRiga(ir.getSequenzaRiga());
			reportOffertaClienteRiga.setSplRiga(ir.getSpecializzazioneRiga());
			reportOffertaClienteRiga.setRigaClg(ir.getIdRigaCollegata());
			reportOffertaClienteRiga.setDetRigaClg(ir.getIdDettaglioRigaCollegata());
			reportOffertaClienteRiga.setAnnoBozza(getOffertaCliente().getAnnoBozza());
			reportOffertaClienteRiga.setNumeroBozza(getOffertaCliente().getNumeroBozza());

			if (ir.getDettaglioRigaDocumento().equals(new Integer(0))) {
				//Fix 15643 inizio
				/*CatalEsternoRicerca catalEsn = ((OffertaClienteRigaPrm) ir).getCatalogoEsterno();
				if (catalEsn != null) {
					reportOffertaClienteRiga.setArticoloForCatalogoEst(catalEsn.getIdArticoloFor());
					reportOffertaClienteRiga.setFornitoreCatalogoEst(catalEsn.getRFornitore());
				}*/
				reportOffertaClienteRiga.setArticoloForCatalogoEst(ir.getIdArtFornCatalogo());
				reportOffertaClienteRiga.setFornitoreCatalogoEst(ir.getIdFornitoreCatalogo());
				//Fix 15643 fine
			}
			if (ir.getCausaleRiga() != null) {
				reportOffertaClienteRiga.setIdCausaleRigaOffertaVen(ir.getIdCauRig());
				String desCauRigOrdven = ir.getCausaleRiga().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getCausaleRiga().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desCauRigOrdven = ir.getCausaleRiga().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesCauRigaOffertaVen(desCauRigOrdven);
			}
			reportOffertaClienteRiga.setTipoRiga(ir.getTipoRiga());
			reportOffertaClienteRiga.setMagazzino(ir.getIdMagazzino());
			reportOffertaClienteRiga.setArticolo(ir.getIdArticolo());
			reportOffertaClienteRiga.setVersioneSal(ir.getIdVersioneSal());
			if (ir.getConfigurazione() != null) {
				reportOffertaClienteRiga.setConfigurazione(ir.getIdConfigurazione());
				String desConfigurazione = ir.getConfigurazione().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getConfigurazione().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desConfigurazione = ir.getConfigurazione().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesConfigurazione(desConfigurazione);
				reportOffertaClienteRiga.setCodConfig(ir.getConfigurazione().getIdEsternoConfig());
				//RA FIX 13292 inizio
				//reportOffertaClienteRiga.setSintesiCfg(getSintesiConfigurazione(ir.getConfigurazione(), primaria));
				//RA FIX 13292 fine
			}
			reportOffertaClienteRiga.setVersioneRcs(ir.getIdVersioneRcs());
			reportOffertaClienteRiga.setNota(ir.getNota());
			try {
				reportOffertaClienteRiga.caricaCommenti(ir, entity, task, lingua);
			}
			catch (Exception e) {
				e.printStackTrace(Trace.excStream);
			}
			reportOffertaClienteRiga.setSpesa(ir.getIdSpesa());
			reportOffertaClienteRiga.setImpPrcSpesa(ir.getImportoPercentualeSpesa());
			reportOffertaClienteRiga.setSpesaPrc(ir.isSpesaPercentuale());
			reportOffertaClienteRiga.setUmVen(ir.getIdUMRif());
			if (ir.getIdUMPrm() != null) {
				reportOffertaClienteRiga.setUmPrm(ir.getIdUMPrm());
				reportOffertaClienteRiga.setQtaOffertaUmPrm(ir.getQtaInUMPrmMag());
			}
			reportOffertaClienteRiga.setUmSec(ir.getIdUMSec());
			reportOffertaClienteRiga.setQtaOffertaUmVen(ir.getQtaInUMRif());
			reportOffertaClienteRiga.setQtaOffertaUmPrm(ir.getQtaInUMPrmMag());
			reportOffertaClienteRiga.setQtaOffertaUmSec(ir.getQtaInUMSecMag());
			reportOffertaClienteRiga.setCoeffImpiegoCmp(ir.getCoefficienteImpiego());
			reportOffertaClienteRiga.setBloccoRicalcoloQtaComp(ir.isBloccoRicalcoloQtaComp());
			reportOffertaClienteRiga.setDataConsegRcs(ir.getDataConsegnaRichiesta());
			reportOffertaClienteRiga.setDataConsegCfm(ir.getDataConsegnaConfermata());
			if (ir.getListinoPrezzi() != null) {
				reportOffertaClienteRiga.setListino(ir.getIdListinoPrezzi());
				String desListino = ir.getListinoPrezzi().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getListinoPrezzi().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desListino = ir.getListinoPrezzi().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesListino(desListino);
			}

			BigDecimal prezzo = ir.getPrezzo();
			BigDecimal prezzoExtra = ir.getPrezzoExtra();
			if (PersDatiVen.getCurrentPersDatiVen().isGestionePrezzoExtra()) {
				if (prezzoExtra != null && prezzo != null) {
					prezzo = prezzo.add(prezzoExtra);
				}
			}
			reportOffertaClienteRiga.setPrezzo(prezzo);
			reportOffertaClienteRiga.setPrezzoExtra(prezzoExtra);
			reportOffertaClienteRiga.setUmPrezzo(ir.getRiferimentoUMPrezzo());
			reportOffertaClienteRiga.setTpPrezzo(ir.getTipoPrezzo());
			//MG FIX 11258 inizio
			reportOffertaClienteRiga.setPrezzoConcordato(ir.getPrezzoConcordato());
			//MG FIX 11258 fine

			if (ir.getAssoggettamentoIVA() != null) {
				reportOffertaClienteRiga.setAssogIva(ir.getIdAssogIVA());
				AssoggettamentoIVA iva = ir.getAssoggettamentoIVA();
				if (getOffertaCliente().getAssoggettamentoIVA() != null && getOffertaCliente().getAssoggettamentoIVA().getTipoIVA() != AssoggettamentoIVA.SOGGETTO_A_CALCOLO_IVA) {
					if (ir.getAssoggettamentoIVA().getTipoIVA() == AssoggettamentoIVA.SOGGETTO_A_CALCOLO_IVA) {
						iva = getOffertaCliente().getAssoggettamentoIVA();
					}
				}
				reportOffertaClienteRiga.setAliqivaStr(ImportiDocumentoOrdineUtil.formattaAliquotaIva(iva));
			}
			if (ir.getResponsabileVendite() != null) {
				reportOffertaClienteRiga.setResponVendite(ir.getIdResponsabileVendite());
				String desResponVendite = ir.getResponsabileVendite().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getResponsabileVendite().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desResponVendite = ir.getResponsabileVendite().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesResponVendite(desResponVendite);
			}
			reportOffertaClienteRiga.setScontoArt1(ir.getScontoArticolo1());
			reportOffertaClienteRiga.setScontoArt2(ir.getScontoArticolo2());
			reportOffertaClienteRiga.setMaggiorazione(ir.getMaggiorazione());
			if (ir.getSconto() != null) {
				reportOffertaClienteRiga.setSconto(ir.getIdSconto());
				String desSconto = ir.getSconto().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getSconto().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desSconto = ir.getSconto().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesSconto(desSconto);
			}
			reportOffertaClienteRiga.setScontoCli(ir.getPrcScontoIntestatario());
			reportOffertaClienteRiga.setScontoMod(ir.getPrcScontoModalita());
			if (ir.getScontoModalita() != null) {
				reportOffertaClienteRiga.setRScontoMod(ir.getIdScontoMod());
				String desScontoMod = ir.getScontoModalita().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getScontoModalita().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desScontoMod = ir.getScontoModalita().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesScontoMod(desScontoMod);
			}
			if (iro != null) {
				reportOffertaClienteRiga.setScontiStr(iro.getStringaSconti());
				//Fix10617 --inizio
				//reportOffertaClienteRiga.setImportoRiga(iro.getValoreOrdinatoAlLordoScontoFF());
				//reportOffertaClienteRiga.setValoreOffertoTotaleRiga(iro.getValoreTotaleOrdinato());
				reportOffertaClienteRiga.setImportoRiga(iro.getValoreOffertoAlLordoScontoFF());
				reportOffertaClienteRiga.setValoreOffertoTotaleRiga(iro.getValoreTotaleOfferto());
				//Fix10617 --fine
			}

			if (ir.getAgente() != null) {
				reportOffertaClienteRiga.setAgente(ir.getIdAgente());
				reportOffertaClienteRiga.setDesAgente(ir.getAgente().getDescrizione().getDescrizione());
				reportOffertaClienteRiga.setPvg1Agente(ir.getProvvigione1Agente());
				reportOffertaClienteRiga.setPvg2Agente(ir.getProvvigione2Agente());
			}
			if (ir.getSubagente() != null) {
				reportOffertaClienteRiga.setAgenteSub(ir.getIdSubagente());
				reportOffertaClienteRiga.setDesAgenteSub(ir.getSubagente().getDescrizione().getDescrizione());
				reportOffertaClienteRiga.setPvg1SubAgente(ir.getProvvigione1Subagente());
				reportOffertaClienteRiga.setPvg2SubAgente(ir.getProvvigione2Subagente());
			}
			if (ir.getCommessa() != null) {
				reportOffertaClienteRiga.setCommessa(ir.getIdCommessa());
				String desCommessa = ir.getCommessa().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getCommessa().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desCommessa = ir.getCommessa().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesCommessa(desCommessa);
			}
			if (ir.getCentroCosto() != null) {
				reportOffertaClienteRiga.setCentroCosto(ir.getIdCentroCosto());
				String desCentroCosto = ir.getCentroCosto().getDescrizione().getHandler().getText("Descrizione", LINGUA_DEFAULT);
				if (isStampaInLingua() && ir.getCentroCosto().getDescrizione().getHandler().getText("Descrizione", lingua) != null) {
					desCentroCosto = ir.getCentroCosto().getDescrizione().getHandler().getText("Descrizione", lingua);
				}
				reportOffertaClienteRiga.setDesCentroCosto(desCentroCosto);
			}
			if (ir.getFornitore() != null) {
				reportOffertaClienteRiga.setFornitore(ir.getIdFornitore());
				reportOffertaClienteRiga.setDesFornitore(ir.getFornitore().getRagioneSociale());
			}
			reportOffertaClienteRiga.setPriorita(ir.getPriorita());
			reportOffertaClienteRiga.setGrpConsegna(ir.getGruppoConsegna());
			reportOffertaClienteRiga.setRigaNonFrazio(ir.isRigaNonFrazionabile());
			reportOffertaClienteRiga.setPrcPdtResiduo(ir.getPrcPerditaResiduo());
			reportOffertaClienteRiga.setFlagRisUte1(ir.getFlagRiservatoUtente1());
			reportOffertaClienteRiga.setFlagRisUte2(ir.getFlagRiservatoUtente2());
			reportOffertaClienteRiga.setFlagRisUte3(ir.getFlagRiservatoUtente3());
			reportOffertaClienteRiga.setFlagRisUte4(ir.getFlagRiservatoUtente4());
			reportOffertaClienteRiga.setFlagRisUte5(ir.getFlagRiservatoUtente5());

			reportOffertaClienteRiga.setStato(ir.getDatiComuniEstesi().getStato());
			reportOffertaClienteRiga.setUtenteAgg(ir.getDatiComuniEstesi().getIdUtenteAgg());
			reportOffertaClienteRiga.setUtenteCrz(ir.getDatiComuniEstesi().getIdUtenteCrz());
			reportOffertaClienteRiga.setTimestamp(ir.getDatiComuniEstesi().getTimestamp());
			reportOffertaClienteRiga.setTimestampAgg(ir.getDatiComuniEstesi().getTimestampAgg());

			reportOffertaClienteRiga.setStringaRisUte1(ir.getAlfanumRiservatoUtente1());
			reportOffertaClienteRiga.setStringaRisUte2(ir.getAlfanumRiservatoUtente2());
			reportOffertaClienteRiga.setNumRisUte1(new Integer(ir.getNumeroRiservatoUtente1()));
			reportOffertaClienteRiga.setNumRisUte2(new Integer(ir.getNumeroRiservatoUtente1()));
			//    fillDatiRifRiga(ir);

		}
		else {
			contatoreRighe++;
			return null;
		}
		//caricaAltriDatiRiga(ir);//Fix 34035	
		return reportOffertaClienteRiga;
	}

	public int salvaDatiRiga(ReportOffertaClienteRiga reportOffertaClienteRiga) throws SQLException {
		int rc = reportOffertaClienteRiga.save();
		return rc;
	}

	protected String getDesExtArt(ArticoloCliente artCli, Articolo articolo) {
		return ImportiDocumentoOrdineUtil.getDesExtSoloInLingua(artCli != null ? artCli.getDescrizioneEst() : null,
				articolo.getDescrizioneArticoloNLS(), lingua);
	}

	public boolean getRecuparaDescExtDaArt(){
		return PersDatiVen.getRecuparaDescExtDaArt();
	}

	public boolean isEm(String tmp){
		return (tmp != null && !tmp.equals("")) ? false :true;
	}

	@SuppressWarnings("unchecked")
	public void initListaRigheKeys (){
		String righeSelezionate = getChiaviSelezionati();
		if(righeSelezionate != null && !righeSelezionate.isEmpty()) {
			String[] keys = righeSelezionate.split(",");
			for(String key : keys) {
				try {
					iListaRigheSelezionate.add((OffertaClienteRigaPrm) OffertaClienteRigaPrm.elementWithKey(OffertaClienteRigaPrm.class, key, PersistentObject.NO_LOCK));
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}else {
			iListaRigheSelezionate.addAll(getOffertaCliente().getRighe());
		}

		for (Iterator<OffertaClienteRigaPrm> iterator = iListaRigheSelezionate.iterator(); iterator.hasNext();) {
			OffertaClienteRigaPrm riga = (OffertaClienteRigaPrm) iterator.next();

			PreventivoCommessaTestata preventivo = YOffertaClienteRigaPrm.preventivoDaCommessa(riga.getIdCommessa());

			if(preventivo == null) {
				iterator.remove();
			}

		}
	}

	public OffertaCliente getOffertaCliente (){
		return (OffertaCliente) iOffertaCliente.getObject();
	}

	public void setOffertaCliente (OffertaCliente OffertaCliente){
		iOffertaCliente.setObject(OffertaCliente);
		//String key = iOffertaCliente.getKey();
		//String idNumeroOrdine = KeyHelper.getTokenObjectKey(key, 3);
	}

	public void setIdAnnoOffertaCliente (String idAnnoOrdine){
		String key1 = iOffertaCliente.getKey();
		iOffertaCliente.setKey(KeyHelper.replaceTokenObjectKey(key1, 2, idAnnoOrdine));
	}

	public String getIdAnnoOffertaCliente (){
		return KeyHelper.getTokenObjectKey(iOffertaCliente.getKey(), 2);
	}

	public void setIdNumeroOffertaCliente (String idNumeroOrdine){
		String key1 = iOffertaCliente.getKey();
		iOffertaCliente.setKey(KeyHelper.replaceTokenObjectKey(key1, 3, idNumeroOrdine));
	}

	public String getIdNumeroOffertaCliente (){
		return KeyHelper.getTokenObjectKey(iOffertaCliente.getKey(), 3);
	}

	public String getOffertaClienteKey (){
		return iOffertaCliente.getKey();
	}

	public void setOffertaClienteKey (String key){
		iOffertaCliente.setKey(key);
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YStampaPrevComm";
	}


}
