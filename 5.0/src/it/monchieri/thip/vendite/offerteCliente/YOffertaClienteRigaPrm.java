package it.monchieri.thip.vendite.offerteCliente;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.ad.ClassADCollectionManager;
import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.common.NumeratorException;
import com.thera.thermfw.persist.Column;
import com.thera.thermfw.persist.ConnectionDescriptor;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.Proxy;
import com.thera.thermfw.persist.SQLServerJTDSNoUnicodeDatabase;
import com.thera.thermfw.web.SessionEnvironment;

import it.mame.thip.qualita.controllo.YNormeQualita;
import it.monchieri.thip.base.fm.PrevTes;
import it.monchieri.thip.base.fm.utils.PrevTesRsIterator;
import it.monchieri.thip.base.produzione.TipoCollaudoMP;
import it.monchieri.thip.base.produzione.TipoLavorazioneMP;
import it.monchieri.thip.base.produzione.TrattTermMp;
import it.monchieri.thip.base.produzione.YLivelloAccettabilitaUT;
import it.monchieri.thip.base.produzione.generale.DatiLavorazione;
import it.monchieri.thip.stain.InterfacciaStain;
import it.monchieri.thip.stain.StainWriteStatusCode;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.articolo.ClasseA;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.commessa.Commessa;
import it.thera.thip.base.commessa.PreventivoCommessaRiga;
import it.thera.thip.base.commessa.PreventivoCommessaTestata;
import it.thera.thip.base.commessa.PreventivoCommessaTestataTM;
import it.thera.thip.base.commessa.PreventivoCommessaVoce;
import it.thera.thip.base.comuniVenAcq.DocumentoOrdineRiga;
import it.thera.thip.base.comuniVenAcq.DocumentoOrdineTestata;
import it.thera.thip.base.comuniVenAcq.SpecificheCopiaDocumento;
import it.thera.thip.base.dipendente.Dipendente;
import it.thera.thip.base.documenti.StatoAvanzamento;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.base.profilo.UtenteAzienda;
import it.thera.thip.base.risorse.Risorsa;
import it.thera.thip.base.risorse.RisorsaPO;
import it.thera.thip.base.risorse.RisorsaProxy;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.cs.ThipException;
import it.thera.thip.vendite.offerteCliente.OffertaCliente;
import it.thera.thip.vendite.offerteCliente.OffertaClienteRiga;
import it.thera.thip.vendite.offerteCliente.OffertaClienteRigaPrm;
import it.thera.thip.vendite.proposteEvasione.CreaMessaggioErrore;

/**
 * <h1>Softre Solutions</h1> <br>
 * 
 * @author Daniele Signoroni 27/05/2024 <br>
 *         <br>
 *         <b>71537 DSSOF3 27/05/2024</b>
 *         <p>
 *         Prima stesura.<br>
 * 
 *         </p>
 * <b>71603	DSSOF3	02/08/2024</b>
 * <p>
 * Aggiunta nuovi campi.<br>
 * </p>
 */

public class YOffertaClienteRigaPrm extends OffertaClienteRigaPrm {

	protected String iItemCliente;

	protected String iAcciaioCliente;

	protected String iDisegno;

	protected String iRevisioneDisegno;

	protected String iSpecCliente;

	protected String iSpecCliRev;

	protected String iSpecCliente2;

	protected String iPartNumber;

	protected String iMatricolaCliente;

	protected String iProject;

	protected String iPlant;

	protected String iEquipmentType;

	protected String iMiscellanous;

	protected char iTipoImballo = '0';

	protected char iPenale = '0';

	protected BigDecimal iMaxIngombroA;

	protected BigDecimal iMaxIngombroB;

	protected BigDecimal iMaxIngombroC;

	protected BigDecimal iPesoGrezzo;

	protected BigDecimal iPesoFinito;

	protected String iLinkPreventivo;

	protected String iPRDisegno;

	protected Integer iMinutiRisorsa;

	protected Proxy iGradofinitura = new Proxy(it.thera.thip.base.articolo.ClasseA.class);

	protected Proxy iPreventivista = new Proxy(it.thera.thip.base.dipendente.Dipendente.class);

	protected Proxy iLivelloaccetabilitaut = new Proxy(it.monchieri.thip.base.produzione.YLivelloAccettabilitaUT.class);

	protected Proxy iTipocollaudo = new Proxy(it.monchieri.thip.base.produzione.TipoCollaudoMP.class);

	protected Proxy iTipotrattermico = new Proxy(it.monchieri.thip.base.produzione.TrattTermMp.class);

	protected Proxy iTipolavorazione = new Proxy(it.monchieri.thip.base.produzione.TipoLavorazioneMP.class);

	protected Proxy iNormaQualita = new Proxy(YNormeQualita.class);

	protected Proxy iAcciaioFM = new Proxy(Articolo.class);

	protected Proxy iRisorsa = new RisorsaProxy();

	protected DatiLavorazione iDatiLavorazione;

	protected Date iDataFucinatura;

	protected String iChiaveRigaInCopia;

	protected BigDecimal iMargineIniziale;

	protected BigDecimal iMargineCorrente;

	public BigDecimal getMargineIniziale() {
		return iMargineIniziale;
	}

	public void setMargineIniziale(BigDecimal iMargineIniziale) {
		this.iMargineIniziale = iMargineIniziale;
		setDirty();
	}

	public BigDecimal getMargineCorrente() {
		return iMargineCorrente;
	}

	public void setMargineCorrente(BigDecimal iMargineCorrente) {
		this.iMargineCorrente = iMargineCorrente;
		setDirty();
	}

	public String getChiaveRigaInCopia() {
		return iChiaveRigaInCopia;
	}

	public void setChiaveRigaInCopia(String iChiaveRigaInCopia) {
		this.iChiaveRigaInCopia = iChiaveRigaInCopia;
	}

	public Date getDataFucinatura() {
		return iDataFucinatura;
	}

	public void setDataFucinatura(Date iDataFucinatura) {
		this.iDataFucinatura = iDataFucinatura;
		setDirty();
	}

	public DatiLavorazione getDatiLavorazione() {
		return iDatiLavorazione;
	}

	public void setDatiLavorazione(DatiLavorazione datiLavorazione) {
		this.iDatiLavorazione = datiLavorazione;
		setDirty();
	}

	public YOffertaClienteRigaPrm() {
		super();
		setTipoImballo('0');
		setPenale('0');
		setIdAzienda(Azienda.getAziendaCorrente());
		iDatiLavorazione = (DatiLavorazione) Factory.createObject(DatiLavorazione.class);
		iDatiLavorazione.setOwner(this);
		setTipoRisorsa(RisorsaPO.MACCHINE);
		setLivelloRisorsa(RisorsaPO.MATRICOLA);
	}

	public void setItemCliente(String itemCliente) {
		this.iItemCliente = itemCliente;
		setDirty();
	}

	public String getItemCliente() {
		return iItemCliente;
	}

	public void setAcciaioCliente(String acciaioCliente) {
		this.iAcciaioCliente = acciaioCliente;
		setDirty();
	}

	public String getAcciaioCliente() {
		return iAcciaioCliente;
	}

	public void setDisegno(String disegno) {
		this.iDisegno = disegno;
		setDirty();
	}

	public String getDisegno() {
		return iDisegno;
	}

	public void setRevisioneDisegno(String revisioneDisegno) {
		this.iRevisioneDisegno = revisioneDisegno;
		setDirty();
	}

	public String getRevisioneDisegno() {
		return iRevisioneDisegno;
	}

	public void setSpecCliente(String specCliente) {
		this.iSpecCliente = specCliente;
		setDirty();
	}

	public String getSpecCliente() {
		return iSpecCliente;
	}

	public void setSpecCliRev(String specCliRev) {
		this.iSpecCliRev = specCliRev;
		setDirty();
	}

	public String getSpecCliRev() {
		return iSpecCliRev;
	}

	public void setSpecCliente2(String specCliente2) {
		this.iSpecCliente2 = specCliente2;
		setDirty();
	}

	public String getSpecCliente2() {
		return iSpecCliente2;
	}

	public void setPartNumber(String partNumber) {
		this.iPartNumber = partNumber;
		setDirty();
	}

	public String getPartNumber() {
		return iPartNumber;
	}

	public void setMatricolaCliente(String matricolaCliente) {
		this.iMatricolaCliente = matricolaCliente;
		setDirty();
	}

	public String getMatricolaCliente() {
		return iMatricolaCliente;
	}

	public void setProject(String project) {
		this.iProject = project;
		setDirty();
	}

	public String getProject() {
		return iProject;
	}

	public void setPlant(String plant) {
		this.iPlant = plant;
		setDirty();
	}

	public String getPlant() {
		return iPlant;
	}

	public void setEquipmentType(String equipmentType) {
		this.iEquipmentType = equipmentType;
		setDirty();
	}

	public String getEquipmentType() {
		return iEquipmentType;
	}

	public void setMiscellanous(String miscellanous) {
		this.iMiscellanous = miscellanous;
		setDirty();
	}

	public String getMiscellanous() {
		return iMiscellanous;
	}

	public void setTipoImballo(char tipoImballo) {
		this.iTipoImballo = tipoImballo;
		setDirty();
	}

	public char getTipoImballo() {
		return iTipoImballo;
	}

	public void setPenale(char penale) {
		this.iPenale = penale;
		setDirty();
	}

	public char getPenale() {
		return iPenale;
	}

	public void setMaxIngombroA(BigDecimal maxIngombroA) {
		this.iMaxIngombroA = maxIngombroA;
		setDirty();
	}

	public BigDecimal getMaxIngombroA() {
		return iMaxIngombroA;
	}

	public void setMaxIngombroB(BigDecimal maxIngombroB) {
		this.iMaxIngombroB = maxIngombroB;
		setDirty();
	}

	public BigDecimal getMaxIngombroB() {
		return iMaxIngombroB;
	}

	public void setMaxIngombroC(BigDecimal maxIngombroC) {
		this.iMaxIngombroC = maxIngombroC;
		setDirty();
	}

	public BigDecimal getMaxIngombroC() {
		return iMaxIngombroC;
	}

	public void setPesoGrezzo(BigDecimal pesoGrezzo) {
		this.iPesoGrezzo = pesoGrezzo;
		setDirty();
	}

	public BigDecimal getPesoGrezzo() {
		return iPesoGrezzo;
	}

	public void setPesoFinito(BigDecimal pesoFinito) {
		this.iPesoFinito = pesoFinito;
		setDirty();
	}

	public BigDecimal getPesoFinito() {
		return iPesoFinito;
	}

	public void setLinkPreventivo(String linkPreventivo) {
		this.iLinkPreventivo = linkPreventivo;
		setDirty();
	}

	public String getLinkPreventivo() {
		return iLinkPreventivo;
	}

	public Integer getMinutiRisorsa() {
		return iMinutiRisorsa;
	}

	public void setMinutiRisorsa(Integer iMinutiRisorsa) {
		this.iMinutiRisorsa = iMinutiRisorsa;
		setDirty();
	}

	public void setGradofinitura(ClasseA gradofinitura) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (gradofinitura != null) {
			idAzienda = KeyHelper.getTokenObjectKey(gradofinitura.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iGradofinitura.setObject(gradofinitura);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public ClasseA getGradofinitura() {
		return (ClasseA) iGradofinitura.getObject();
	}

	public void setGradofinituraKey(String key) {
		String oldObjectKey = getKey();
		iGradofinitura.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getGradofinituraKey() {
		return iGradofinitura.getKey();
	}

	public void setIdGradoFinitura(String idGradoFinitura) {
		String key = iGradofinitura.getKey();
		iGradofinitura.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idGradoFinitura));
		setDirty();
	}

	public String getIdGradoFinitura() {
		String key = iGradofinitura.getKey();
		String objIdGradoFinitura = KeyHelper.getTokenObjectKey(key, 2);
		return objIdGradoFinitura;
	}

	public void setPreventivista(Dipendente preventivista) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (preventivista != null) {
			idAzienda = KeyHelper.getTokenObjectKey(preventivista.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iPreventivista.setObject(preventivista);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public Dipendente getPreventivista() {
		return (Dipendente) iPreventivista.getObject();
	}

	public void setPreventivistaKey(String key) {
		String oldObjectKey = getKey();
		iPreventivista.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getPreventivistaKey() {
		return iPreventivista.getKey();
	}

	public void setIdPreventivista(String idPreventivista) {
		String key = iPreventivista.getKey();
		iPreventivista.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idPreventivista));
		setDirty();
	}

	public String getIdPreventivista() {
		String key = iPreventivista.getKey();
		String objIdPreventivista = KeyHelper.getTokenObjectKey(key, 2);
		return objIdPreventivista;
	}

	public void setLivelloaccetabilitaut(YLivelloAccettabilitaUT livelloaccetabilitaut) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (livelloaccetabilitaut != null) {
			idAzienda = KeyHelper.getTokenObjectKey(livelloaccetabilitaut.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iLivelloaccetabilitaut.setObject(livelloaccetabilitaut);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public YLivelloAccettabilitaUT getLivelloaccetabilitaut() {
		return (YLivelloAccettabilitaUT) iLivelloaccetabilitaut.getObject();
	}

	public void setLivelloaccetabilitautKey(String key) {
		String oldObjectKey = getKey();
		iLivelloaccetabilitaut.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getLivelloaccetabilitautKey() {
		return iLivelloaccetabilitaut.getKey();
	}

	public void setIdLivAccut(String idLivAccut) {
		String key = iLivelloaccetabilitaut.getKey();
		iLivelloaccetabilitaut.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idLivAccut));
		setDirty();
	}

	public String getIdLivAccut() {
		String key = iLivelloaccetabilitaut.getKey();
		String objIdLivAccut = KeyHelper.getTokenObjectKey(key, 2);
		return objIdLivAccut;
	}

	public void setTipocollaudo(TipoCollaudoMP tipocollaudo) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (tipocollaudo != null) {
			idAzienda = KeyHelper.getTokenObjectKey(tipocollaudo.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iTipocollaudo.setObject(tipocollaudo);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public TipoCollaudoMP getTipocollaudo() {
		return (TipoCollaudoMP) iTipocollaudo.getObject();
	}

	public void setTipocollaudoKey(String key) {
		String oldObjectKey = getKey();
		iTipocollaudo.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getTipocollaudoKey() {
		return iTipocollaudo.getKey();
	}

	public void setIdTypCollaudo(String idTypCollaudo) {
		String key = iTipocollaudo.getKey();
		iTipocollaudo.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idTypCollaudo));
		setDirty();
	}

	public String getIdTypCollaudo() {
		String key = iTipocollaudo.getKey();
		String objIdTypCollaudo = KeyHelper.getTokenObjectKey(key, 2);
		return objIdTypCollaudo;
	}

	public void setTipotrattermico(TrattTermMp tipotrattermico) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (tipotrattermico != null) {
			idAzienda = KeyHelper.getTokenObjectKey(tipotrattermico.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iTipotrattermico.setObject(tipotrattermico);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public TrattTermMp getTipotrattermico() {
		return (TrattTermMp) iTipotrattermico.getObject();
	}

	public void setTipotrattermicoKey(String key) {
		String oldObjectKey = getKey();
		iTipotrattermico.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getTipotrattermicoKey() {
		return iTipotrattermico.getKey();
	}

	public void setIdTypTrTerm(String idTypTrTerm) {
		String key = iTipotrattermico.getKey();
		iTipotrattermico.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idTypTrTerm));
		setDirty();
	}

	public String getIdTypTrTerm() {
		String key = iTipotrattermico.getKey();
		String objIdTypTrTerm = KeyHelper.getTokenObjectKey(key, 2);
		return objIdTypTrTerm;
	}

	public void setTipolavorazione(TipoLavorazioneMP tipolavorazione) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (tipolavorazione != null) {
			idAzienda = KeyHelper.getTokenObjectKey(tipolavorazione.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iTipolavorazione.setObject(tipolavorazione);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public TipoLavorazioneMP getTipolavorazione() {
		return (TipoLavorazioneMP) iTipolavorazione.getObject();
	}

	public void setTipolavorazioneKey(String key) {
		String oldObjectKey = getKey();
		iTipolavorazione.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getTipolavorazioneKey() {
		return iTipolavorazione.getKey();
	}

	public void setIdAzienda(String idAzienda) {
		super.setIdAzienda(idAzienda);

	}

	public void setIdTipoLavor(String idTipoLavor) {
		String key = iTipolavorazione.getKey();
		iTipolavorazione.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idTipoLavor));
		setDirty();
	}

	public String getIdTipoLavor() {
		String key = iTipolavorazione.getKey();
		String objIdTipoLavor = KeyHelper.getTokenObjectKey(key, 2);
		return objIdTipoLavor;
	}

	public String getPRDisegno() {
		return iPRDisegno;
	}

	public void setPRDisegno(String iPRDisegno) {
		this.iPRDisegno = iPRDisegno;
	}

	public void setNormaQualita(YNormeQualita tipolavorazione) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (tipolavorazione != null) {
			idAzienda = KeyHelper.getTokenObjectKey(tipolavorazione.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iNormaQualita.setObject(tipolavorazione);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public YNormeQualita getNormaQualita() {
		return (YNormeQualita) iNormaQualita.getObject();
	}

	public void setNormaQualitaKey(String key) {
		String oldObjectKey = getKey();
		iNormaQualita.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getNormaQualitaKey() {
		return iNormaQualita.getKey();
	}

	public void setIdNormaQualita(String idTipoLavor) {
		String key = iNormaQualita.getKey();
		iNormaQualita.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idTipoLavor));
		setDirty();
	}

	public String getIdNormaQualita() {
		String key = iNormaQualita.getKey();
		String objIdTipoLavor = KeyHelper.getTokenObjectKey(key, 2);
		return objIdTipoLavor;
	}

	/**
	 * 
	 */

	public void setAcciaioFM(Articolo tipolavorazione) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (tipolavorazione != null) {
			idAzienda = KeyHelper.getTokenObjectKey(tipolavorazione.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iAcciaioFM.setObject(tipolavorazione);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public Articolo getAcciaioFM() {
		return (Articolo) iAcciaioFM.getObject();
	}

	public void setAcciaioFMKey(String key) {
		String oldObjectKey = getKey();
		iAcciaioFM.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getAcciaioFMKey() {
		return iAcciaioFM.getKey();
	}

	public void setIdAcciaioFM(String idTipoLavor) {
		String key = iAcciaioFM.getKey();
		iAcciaioFM.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idTipoLavor));
		setDirty();
	}

	public String getIdAcciaioFM() {
		String key = iAcciaioFM.getKey();
		String objIdTipoLavor = KeyHelper.getTokenObjectKey(key, 2);
		return objIdTipoLavor;
	}

	public void setRisorsa(Risorsa risorsa) {
		String oldObjectKey = getKey();
		String idAzienda = null;
		if (risorsa != null) {
			idAzienda = KeyHelper.getTokenObjectKey(risorsa.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iRisorsa.setObject(risorsa);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public Risorsa getRisorsa() {
		return (Risorsa)iRisorsa.getObject();
	}

	public void setRisorsaKey(String key) {
		String oldObjectKey = getKey();
		iRisorsa.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getRisorsaKey() {
		return iRisorsa.getKey();
	}

	public void setTipoRisorsa(char tipoRisorsa) {
		String key = iRisorsa.getKey();
		Character tipoRisorsaTmp = new Character(tipoRisorsa);
		iRisorsa.setKey(KeyHelper.replaceTokenObjectKey(key , 2, tipoRisorsaTmp));
		setDirty();
	}

	public char getTipoRisorsa() {
		String key = iRisorsa.getKey();
		String objTipoRisorsa = KeyHelper.getTokenObjectKey(key,2);
		return KeyHelper.stringToChar(objTipoRisorsa);

	}

	public void setLivelloRisorsa(char livelloRisorsa) {
		String key = iRisorsa.getKey();
		Character livelloRisorsaTmp = new Character(livelloRisorsa);
		iRisorsa.setKey(KeyHelper.replaceTokenObjectKey(key , 3, livelloRisorsaTmp));
		setDirty();
	}

	public char getLivelloRisorsa() {
		String key = iRisorsa.getKey();
		String objLivelloRisorsa = KeyHelper.getTokenObjectKey(key,3);
		return KeyHelper.stringToChar(objLivelloRisorsa);

	}

	public void setIdRisorsa(String idRisorsa) {
		String key = iRisorsa.getKey();
		iRisorsa.setKey(KeyHelper.replaceTokenObjectKey(key , 4, idRisorsa));
		setDirty();
	}

	public String getIdRisorsa() {
		String key = iRisorsa.getKey();
		String objIdRisorsa = KeyHelper.getTokenObjectKey(key,4);
		return objIdRisorsa;
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YOffertaClienteRigaPrm yOffertaClienteRigaPrm = (YOffertaClienteRigaPrm) obj;
		iGradofinitura.setEqual(yOffertaClienteRigaPrm.iGradofinitura);
		iPreventivista.setEqual(yOffertaClienteRigaPrm.iPreventivista);
		iLivelloaccetabilitaut.setEqual(yOffertaClienteRigaPrm.iLivelloaccetabilitaut);
		iTipocollaudo.setEqual(yOffertaClienteRigaPrm.iTipocollaudo);
		iTipotrattermico.setEqual(yOffertaClienteRigaPrm.iTipotrattermico);
		iTipolavorazione.setEqual(yOffertaClienteRigaPrm.iTipolavorazione);
		iAcciaioFM.setEqual(yOffertaClienteRigaPrm.iAcciaioFM);
		iDatiLavorazione.setEqual(yOffertaClienteRigaPrm.iDatiLavorazione);
		iNormaQualita.setEqual(yOffertaClienteRigaPrm.iNormaQualita);
	}

	protected void setIdAziendaInternal(String idAzienda) {
		super.setIdAziendaInternal(idAzienda);
		if (iGradofinitura != null) {
			String key1 = iGradofinitura.getKey();
			iGradofinitura.setKey(KeyHelper.replaceTokenObjectKey(key1, 1, idAzienda));
		}
		if (iPreventivista != null) {
			String key2 = iPreventivista.getKey();
			iPreventivista.setKey(KeyHelper.replaceTokenObjectKey(key2, 1, idAzienda));
		}
		if (iLivelloaccetabilitaut != null) {
			String key3 = iLivelloaccetabilitaut.getKey();
			iLivelloaccetabilitaut.setKey(KeyHelper.replaceTokenObjectKey(key3, 1, idAzienda));
		}
		if (iTipocollaudo != null) {
			String key4 = iTipocollaudo.getKey();
			iTipocollaudo.setKey(KeyHelper.replaceTokenObjectKey(key4, 1, idAzienda));
		}
		if (iTipotrattermico != null) {
			String key5 = iTipotrattermico.getKey();
			iTipotrattermico.setKey(KeyHelper.replaceTokenObjectKey(key5, 1, idAzienda));
		}
		if (iTipolavorazione != null) {
			String key6 = iTipolavorazione.getKey();
			iTipolavorazione.setKey(KeyHelper.replaceTokenObjectKey(key6, 1, idAzienda));
		}
		if (iNormaQualita != null) {
			String key7 = iNormaQualita.getKey();
			iNormaQualita.setKey(KeyHelper.replaceTokenObjectKey(key7, 1, idAzienda));
		}
		if (iAcciaioFM != null) {
			String key8 = iAcciaioFM.getKey();
			iAcciaioFM.setKey(KeyHelper.replaceTokenObjectKey(key8, 1, idAzienda));
		}
		if (iRisorsa != null) {
			String key9 = iRisorsa.getKey();
			iRisorsa.setKey(KeyHelper.replaceTokenObjectKey(key9, 1, idAzienda));
		}
	}

	@Override
	public DocumentoOrdineRiga copiaRiga(DocumentoOrdineTestata docDest, SpecificheCopiaDocumento spec)
			throws CopyException {
		OffertaClienteRigaPrm riga = (OffertaClienteRigaPrm)(super.copiaRiga(docDest, spec));
		if (riga != null && riga instanceof YOffertaClienteRigaPrm) {
			if(spec.getTipoOperazione() == SpecificheCopiaDocumento.TOP_COPIA) {
				//se sono in copia normale allora devo riportare sulla riga l'articoo orignale
				((YOffertaClienteRigaPrm)riga).aggiornaArticoloAdOriginario();
				((YOffertaClienteRigaPrm)riga).setChiaveRigaInCopia(getKey());

				riga.setCommessa(null); //In copia devo sempre codificare una nuova commessa
			}
		}
		return riga;
	}

	@Override
	public int save() throws SQLException {
		boolean isOnDB = isOnDB();
		OffertaCliente testata = (OffertaCliente) getTestata();

		if(isOnDB) {
			String oldArticolo = getOldRiga().getIdArticolo();
			String currentArticolo = getIdArticolo();
			if(!oldArticolo.equals(currentArticolo)) {
				//Vuol dire che hanno lanciato la cambia articolo e quindi devo aggiornare l'articolo sul preventivo di commessa
				aggiornaArticoloPreventivoCommessa(currentArticolo);
			}
		}

		//Se sto copiando la riga ma non la testata vuol dire che e' copia normale quindi codifico la commessa in automatico
		if(!isOnDB && isInCopiaRiga && !testata.isInCopia()) {
			setCommessa(null);
		}

		if (!isOnDB) {
			creaCommessa();
			if(UtenteAzienda.getUtenteAziendaConnesso().getDipendente() != null)
				setPreventivista(UtenteAzienda.getUtenteAziendaConnesso().getDipendente());

			setTipoRisorsa(RisorsaPO.MACCHINE);
			setLivelloRisorsa(RisorsaPO.MATRICOLA);
		}

		//Se sto inserendo una riga nuova ma non da copia
		if(!isOnDB && !isInCopiaRiga && !testata.isInCopia()) {
			setProject(((YOffertaCliente)testata).getProject());
			setPlant(((YOffertaCliente)testata).getPlant());
			setEquipmentType(((YOffertaCliente)testata).getEquipmentType());
		}

		//Se sono in copia della sola riga allora nella chiave riga in copia metto quella attuale
		if(!isOnDB && isInCopiaRiga && !testata.isInCopia()) {
			aggiornaArticoloAdOriginario();

			setChiaveRigaInCopia(getKey());
		}

		int rc = super.save();

		//Se sono in copia della riga metto nella mappa della testata la chiave riga di copia
		if(!isOnDB && isInCopiaRiga && rc > 0) {
			((YOffertaCliente)getTestata()).getChiaviRigheInCopia().put(getKey(), getChiaveRigaInCopia());

			//Se sto copiando la singola riga ma non la testata allora copio anche il preventivatore tabella ext
			if(!testata.isInCopia())
				((YOffertaCliente)getTestata()).copiaRighePreventivoPthFM();

		}

		if(rc > ErrorCodes.NO_ROWS_UPDATED) {
			interfacciaStain(StainWriteStatusCode.INSERT);
		}

		return rc;
	}

	protected void aggiornaArticoloPreventivoCommessa(String currentArticolo) {
		PreventivoCommessaTestata preventivo = preventivoDaCommessa(getIdCommessa());
		if(preventivo != null) {

			PreventivoCommessaRiga riga0 = (PreventivoCommessaRiga) preventivo.getRighe().get(0);

			riga0.setIdArticolo(currentArticolo);

			try {
				riga0.save();
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}

	/**
	 * Serve per copiare un preventivo di commessa.<br>
	 * Il preventivo viene copiato dalla commessa della riga in essere.<br>
	 * @throws ThipException
	 */
	protected void copiaPreventivatore() throws ThipException {
		if(getIdCommessa() != null) {
			PreventivoCommessaTestata preventivo = preventivoDaCommessa(getIdCommessa());
			if(preventivo != null) {
				try {
					preventivo = copiaPreventivoDiComessaDaDefault(preventivo,false);
					int rc = preventivo.save();
					if(rc <= 0) {
						String msg = null;
						ErrorMessage em = CreaMessaggioErrore.daRcAErrorMessage(rc, (SQLException) preventivo.getException());
						if(em != null) {
							msg = em.getLongText();
						}else {
							msg = "Impossibile copiare il preventivo di commessa, origine ="+preventivo.getKey()+",rc ="+rc;
						}
						throw new ThipException(msg);
					}
				} catch (CopyException e) {
					e.printStackTrace(Trace.excStream);
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}else {
			throw new ThipException("Sulla riga offerta non e' presente una commessa, impossibile copiare il preventivo");
		}
	}

	@Override
	public int delete() throws SQLException {
		int rc = super.delete();
		if(rc > ErrorCodes.NO_ROWS_UPDATED) {
			interfacciaStain(StainWriteStatusCode.DELETE);
		}
		return rc;
	}

	protected void interfacciaStain(StainWriteStatusCode status) throws ThipException {
		InterfacciaStain intefaccia =  InterfacciaStain.getInterfacciaStain();
		if(intefaccia != null && intefaccia.getStato() == DatiComuniEstesi.VALIDO 
				&& intefaccia.getEsportaOffVen()) {

			JSONObject object = new JSONObject();
			object.put("riga", this);
			object.put("stainWriteStatusCode", status);

			try {
				JSONObject result = intefaccia.esportaOffertaVenditaRiga(Arrays.asList(object), true);
				if(Trace.isLogEnabled(Trace.US1))
					Trace.println(intefaccia.describeObjectResult((JSONArray) result.get("details"), getKey(), "dbo.Import_Oo_OfferteOdv"));
			} catch (NumeratorException e) {
				//Il numeratore ha ecceduto il limite, va cambiato o resettato
				throw new ThipException("Stain --> Exp offerta vendita riga, contattare l'amministratore di sistema : "+e.getMessage());
			}
		}
	}

	protected void creaCommessa() throws SQLException {
		if (getIdCommessa() == null && getStatoAvanzamento() == StatoAvanzamento.DEFINITIVO) {
			String numeroRiga = null;
			if (!isOnDB() && (getNumeroRigaDocumento() == null || getNumeroRigaDocumento() == 0)) {
				numeroRiga = new Integer(OffertaClienteRiga.getNumeroNuovaRiga(getTestata())).toString();
			} else {
				numeroRiga = getNumeroRigaDocumento().toString();
			}
			if (numeroRiga.length() == 1) {
				numeroRiga = "00" + numeroRiga;
			}
			if (numeroRiga.length() == 2) {
				numeroRiga = "0" + numeroRiga;
			}
			Commessa commessa = (Commessa) Factory.createObject(Commessa.class);
			commessa.setIdAzienda(getIdAzienda());
			//commessa.setIdCommessa(caricaIdCommessa(numeroRiga));
			commessa.setIdCommessa(YCodificaCommessaAutomatica.getInstance().recuperaIdCommessaOffertaRiga(getNumeroDocumento(), numeroRiga));
			commessa.getDescrizione().setDescrizione(caricaDescrizioneCommessa(numeroRiga));
			String descrRidtt = caricaDescrizioneRidottaCmm(numeroRiga);
			if (descrRidtt.length() > 15) {
				descrRidtt = descrRidtt.substring(0, 15);
			}
			commessa.getDescrizione().setDescrizioneRidotta(descrRidtt);
			commessa.setIdCliente(getIdCliente());
			commessa.setDataApertura(TimeUtils.getCurrentDate());
			commessa.setStatoAvanzamento(Commessa.STATO_AVANZAM__PROVVISORIA);
			commessa.setAggiornamentoSaldi(true);
			commessa.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);
			commessa.setCodificaCommessaCA(true);
			BODataCollector boDC = createDataCollector("Commessa");
			boDC.setBo(commessa);
			boDC.setAutoCommit(false);
			int rcsave = BODataCollector.ERROR;
			rcsave = boDC.save();
			if (rcsave != BODataCollector.ERROR) {
				setIdCommessa(commessa.getIdCommessa());
			} else {
				setIdCommessa(commessa.getIdCommessa());
				// riapriCostiCommessa(commessa.getIdCommessa());
			}
		}
	}

	protected String caricaDescrizioneRidottaCmm(String numeroRiga) {
		String result = getTestata().getNumeroDocumentoFormattato() + "/" + numeroRiga;
		return result;
	}

	protected String caricaIdCommessa(String numeroRiga) {
		numeroRiga = numeroRiga.substring(numeroRiga.length() - 3, numeroRiga.length());
		String anno = getAnnoDocumento().trim();
		anno = anno.substring(anno.length() - 2, anno.length());
		String numero = formatNumOrdinePerIdCommessa(getNumeroDocumento().trim(), 5);
		if (getTestata().getNumeratoreHandler().getNumeratore().isAmmessoVersionamento()
				&& getTestata().getNumeratoreHandler().getNumeratore().getPassoNumerazione() == 10) {
			numero = numero.substring(0, numero.length() - 1) + "A";
		}
		return anno.concat(numero).concat(numeroRiga);
	}

	public String formatNumOrdinePerIdCommessa(String num, int lunghezza) {
		String rc = "";

		for (int k = num.length() - 1; k >= 0; k--) {
			if (num.charAt(k) != ' ') {
				rc = num.charAt(k) + rc;
			} else {
				break;
			}
		}
		if (rc.length() > lunghezza) {
			rc = rc.substring(rc.length() - lunghezza, rc.length());
		} else if (rc.length() < lunghezza) {
			for (int k = rc.length() + 1; k <= lunghezza; k++) {
				rc = "0" + rc;
			}
		}
		return rc;
	}

	protected static BODataCollector createDataCollector(String classHdr) throws SQLException {
		try {
			BODataCollector bodc = null;
			ClassADCollection classDesc = ClassADCollectionManager.collectionWithName(classHdr);
			String collectorName = classDesc.getBODataCollector();
			if (collectorName != null) {
				bodc = (BODataCollector) Factory.createObject(collectorName);
				bodc.initialize(classDesc.getClassName(), true, PersistentObject.NO_LOCK);
			} else {
				bodc = new BODataCollector(classDesc.getClassName(), true, PersistentObject.NO_LOCK);
			}

			return bodc;
		} catch (Exception e) {
			e.printStackTrace(Trace.excStream);
			throw new ThipException(e.getMessage());
		}
	}

	protected String caricaDescrizioneCommessa(String numeroRiga) {
		String result = getNumOrdTitleNLS() + ": " + caricaDescrizioneRidottaCmm(numeroRiga);
		if (result.length() > 35) {
			result = result.substring(0, 35);
		}
		return result;
	}

	protected String getNumOrdTitleNLS() {
		ClassADCollection classADCollection = null;
		try {
			classADCollection = ClassADCollectionManager.collectionWithName("OrdineVenditaRigaPrm");
		} catch (NoSuchFieldException ex) {
			ex.printStackTrace();
		} catch (NoSuchElementException ex) {
			ex.printStackTrace();
		}

		return classADCollection.getAttribute("NumeroOrdine").getColumnTitleNLS();
	}

	/**
	 * Se sto eliminando la riga ordine devo aggiornare la riga offerta riportando l'articolo originario sulla riga.<br>
	 * @return rc della save della riga offerta
	 */
	public int aggiornaArticoloAdOriginario() {
		int rc = -1;
		try {
			Articolo articoloOriginale = (Articolo) Articolo.elementWithKey(Articolo.class, KeyHelper.buildObjectKey(new String[] {
					getIdAzienda(),
					getArticolo().getIdArticoloTecnico()
			}), PersistentObject.NO_LOCK);
			if(articoloOriginale != null) {
				this.cambiaArticolo(articoloOriginale, null, isNecessarioRecuperoDatiVenAcqOnSave());
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return rc;

	}

	/**
	 * <h1>Processa il preventivo di commessa della riga offerta</h1>
	 * <p>
	 * </p>
	 * @return lista di eventuali error message
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List processaPreventivoDiCommessa() {
		List errorMessages = new ArrayList();
		ErrorMessage em = null;

		String chiavePreventivoCommessaDefault = ParametroPsn.getValoreParametroPsn("pers.offerteCliente.PreventivoCommessa", "ChiavePreventivoDefault");

		if(chiavePreventivoCommessaDefault == null || chiavePreventivoCommessaDefault.isEmpty()) {
			em = new ErrorMessage("EDI0000114",new String[] {"pers.offerteCliente.PreventivoCommessa","ChiavePreventivoDefault"});
			errorMessages.add(em);
		}else {
			try {
				String[] parameters = chiavePreventivoCommessaDefault.split("/");
				String c = parameters[0] + KEY_SEPARATOR + parameters[1] + KEY_SEPARATOR + parameters[2];
				PreventivoCommessaTestata prevDefault = (PreventivoCommessaTestata) Factory.createObject(PreventivoCommessaTestata.class);
				prevDefault.setDeepRetrieveEnabled(true);
				prevDefault.setKey(c);
				boolean isOk = prevDefault.retrieve();
				if(!isOk) {
					em = new ErrorMessage("BAS0000004",new String[] {c});
					errorMessages.add(em);
				}else {
					PrevTes prevTes = recuperaPreventivoDaPreventivatore();
					if(prevTes != null) {
						PreventivoCommessaTestata preventivo = preventivoDaCommessa(getIdCommessa());
						if(preventivo == null) {
							try {
								preventivo = copiaPreventivoDiComessaDaDefault(prevDefault,true);
							} catch (CopyException e) {
								em = new ErrorMessage("BAS00000078","Impossibile copiare il preventivo di commessa "+e.getMessage());
								e.printStackTrace(Trace.excStream);
							}
						}
						int rc = 0;

						//Processo le voci e setto i parametri

						PreventivoCommessaRiga riga = (PreventivoCommessaRiga) preventivo.getRighe().get(0);

						riga.setQtaUmPrm(getQtaInUMPrmMag());

						Iterator iterVoci = riga.getRighe().iterator();
						while(iterVoci.hasNext()) {
							PreventivoCommessaVoce voce = (PreventivoCommessaVoce) iterVoci.next();

							//voce.setQtaPrvUmVen(BigDecimal.ONE);
							voce.setQtaPrvUmVen(getQtaInUMPrmMag());

							BigDecimal costo = BigDecimal.ZERO;
							Character mezzaSagoma = Character.valueOf(prevTes.getMezzaSagoma() != null ? prevTes.getMezzaSagoma().charAt(0) : 'N');
							Boolean isMezzaSagoma = (mezzaSagoma == Column.TRUE_CHAR);

							String descrizioneVoce = voce.getIdArticolo();
							if(descrizioneVoce.equals("THROUGHPUT")) {
								//BigDecimal minutiForgia = BigDecimal.valueOf(prevTes.getMinForgia());
								//BigDecimal totMinuti = minutiForgia.multiply(getQtaInUMPrmMag());
								//voce.setQtaPrvUmVen(totMinuti);
								//voce.setCostoRifer(prevTes.getCostoForgia());
								costo = prevTes.getTpTot();
							}else if(descrizioneVoce.equals("SBLUMATURA")) {
								//BigDecimal tot = BigDecimal.valueOf(prevTes.getPesoGrezzoConCF());
								//tot = tot.multiply(getQtaInUMPrmMag());
								//voce.setQtaPrvUmVen(tot);
								//voce.setCostoRifer(prevTes.getEuKgSblu() != null ? prevTes.getEuKgSblu() : BigDecimal.ZERO);
								costo = prevTes.getCostoSblu();
							}else if(descrizioneVoce.equals("MATERIA_PRIMA")) {
								//BigDecimal tot = BigDecimal.valueOf(prevTes.getPesoGrezzoConCF());
								//tot = tot.multiply(getQtaInUMPrmMag());
								//voce.setQtaPrvUmVen(tot);
								//voce.setCostoRifer(prevTes.getCostoMp() != null ? prevTes.getCostoMp() : BigDecimal.ZERO);
								costo = prevTes.getCostoMpTot();
							}else if(descrizioneVoce.equals("REC_MATERIA_PRIMA")) {
								//BigDecimal tot = prevTes.getQtaRecMp() != null ? prevTes.getQtaRecMp() : BigDecimal.ZERO;
								//tot = tot.multiply(getQtaInUMPrmMag());
								//voce.setQtaPrvUmVen(tot);
								//voce.setCostoRifer(prevTes.getEuKgRecMp() != null ? prevTes.getEuKgRecMp() : BigDecimal.ZERO);
								costo = prevTes.getValoreRecMp();
							}else if(descrizioneVoce.equals("LAVORAZIONI_EXT_LM1")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoLm1();
							}else if(descrizioneVoce.equals("LAVORAZIONI_EXT_LM2")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoLm2();
							}else if(descrizioneVoce.equals("LAVORAZIONI_EXT_LM3")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoLm3();
							}else if(descrizioneVoce.equals("LAVORAZIONI_EXT_LM4")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoLm4();
							}else if(descrizioneVoce.equals("SPESE_TR_ECC")) {
								//BigDecimal qta = getQtaInUMPrmMag();
								//if(prevTes.getPesoFinito() != null) {
								//	qta = qta.multiply(BigDecimal.valueOf(prevTes.getPesoFinito()));
								//}
								//voce.setQtaPrvUmVen(qta);
								costo = prevTes.getCostoTraspInt();
							}else if(descrizioneVoce.equals("SPESE_IMB")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoImbSpec();
							}else if(descrizioneVoce.equals("SPESE_PAR")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoParcTot();
							}else if(descrizioneVoce.equals("SPESE_TR")) {
								//BigDecimal qta = getQtaInUMPrmMag();
								//if(prevTes.getPesoFinito() != null) {
								//	qta = qta.multiply(BigDecimal.valueOf(prevTes.getPesoFinito()));
								//}
								//voce.setQtaPrvUmVen(qta);
								costo = prevTes.getCostoTraCliTot();
							}else if(descrizioneVoce.equals("SPEC_TAGLIO")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoSpecTgl();
							}else if(descrizioneVoce.equals("SPEC_FORGIA")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoSpecForg();
							}else if(descrizioneVoce.equals("SPEC_TT")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoSpecTt();
							}else if(descrizioneVoce.equals("SPEC_CLC")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getCostoSpecColLabCer();
							}else if(descrizioneVoce.equals("SPEC_ESTERNO")) {
								costo = prevTes.getCostoSpecEst();
							}else if(descrizioneVoce.equals("VALORE_MARGINE")) {
								costo = prevTes.getValoreMargComm();
							}else if(descrizioneVoce.equals("VALORE_COMMISS")) {
								costo = prevTes.getCommTot();
							}else if(descrizioneVoce.equals("DUTIES")) {
								//voce.setQtaPrvUmVen(getQtaInUMPrmMag());
								costo = prevTes.getDutiesTot();
							}

							if(costo == null)
								costo = BigDecimal.ZERO;

							/*
							 * Se e' una mezza sagoma allora il cliente chiede due pezzi ma FMO lo produce come uno
							 * quindi vado a dividere il costo totale per due
							 */

							if(isMezzaSagoma) {
								costo = costo.divide(new BigDecimal(2), RoundingMode.DOWN);
							}

							voce.setCostoRifer(costo);
						}

						riga.setCosTotale(prevTes.getTotCosti());
						riga.getTestata().setSalvoDaSola(true); //per evitare di far ricalcolare i valori
						rc = preventivo.save();
						if(rc < 0) {
							em = CreaMessaggioErrore.daRcAErrorMessage(rc, (SQLException) preventivo.getException());
							errorMessages.add(em);
						}

						setMaxIngombroA(BigDecimal.valueOf(prevTes.getMaxIngA() != null ? prevTes.getMaxIngA() : 0));
						setMaxIngombroB(BigDecimal.valueOf(prevTes.getMaxIngB() != null ? prevTes.getMaxIngB() : 0));
						setMaxIngombroC(BigDecimal.valueOf(prevTes.getMaxIngC() != null ? prevTes.getMaxIngC() : 0));

						setMinutiRisorsa(prevTes.getMinForgia());

						setLivelloRisorsa(RisorsaPO.MATRICOLA);
						setIdRisorsa(prevTes.getIdPressa());

						setPesoFinito(BigDecimal.valueOf(prevTes.getPesoFinito() != null ? prevTes.getPesoFinito() : 0));
						setPesoGrezzo(BigDecimal.valueOf(prevTes.getPesoGrezzoConCF() != null ? prevTes.getPesoGrezzoConCF() : 0));

						if(getMargineIniziale() == null || getMargineIniziale().compareTo(BigDecimal.ZERO) == 0) {
							setMargineIniziale(prevTes.getMargCommPer());
						}

						setMargineCorrente(prevTes.getMargCommPer());

						rc = save();
						if(rc < 0) {
							em = CreaMessaggioErrore.daRcAErrorMessage(rc, (SQLException) preventivo.getException());
							errorMessages.add(em);				
						}
					}else {
						em = new ErrorMessage("BAS0000078","Impossibile trovare il preventivo nella tabella del preventivatore");
						errorMessages.add(em);
					}
				}
			}catch (SQLException e) {
				em = new ErrorMessage("BAS0000036", String.valueOf(e.getErrorCode()) + ": "
						+ e.getLocalizedMessage());
				errorMessages.add(em);
				e.printStackTrace(Trace.excStream);
			}
		}
		return errorMessages;
	}

	/**
	 * 
	 * @param prevDefault
	 * @return
	 * @throws CopyException
	 */
	@SuppressWarnings("rawtypes")
	public PreventivoCommessaTestata copiaPreventivoDiComessaDaDefault(PreventivoCommessaTestata prevDefault, boolean azzeraCosti) throws CopyException {
		PreventivoCommessaTestata preventivo = (PreventivoCommessaTestata) Factory.createObject(PreventivoCommessaTestata.class);
		preventivo.setEqual(prevDefault);
		preventivo.setOnDB(false);
		preventivo.getNumeratoreHandler().setIdNumeratore("PREV_COMMESSA");
		preventivo.getNumeratoreHandler().setIdSerie("PC");

		/*
		 * Purtroppo Panthera non ha messo nella classe PreventivoCommessaRiga nel setEqual
		 * il settaggio delle PreventivoCommessaVoce, quindi lo devo fare io manualmente 
		 */

		Iterator prevRowIter = preventivo.getRighe().iterator();
		while(prevRowIter.hasNext()) {
			PreventivoCommessaRiga riga = (PreventivoCommessaRiga) prevRowIter.next();
			riga.setOnDB(false);
			riga.setRigaOff(getNumeroRigaDocumento());
			riga.completaBO();
			riga.setArticolo(getArticolo());
			riga.setCommessa(getCommessa());
			riga.setCommessaPrincipale(null);

			if(azzeraCosti) {
				riga.setMarkupArticolo(BigDecimal.ZERO);
				riga.setMarkupMacchina(BigDecimal.ZERO);
				riga.setMarkupUomo(BigDecimal.ZERO);
				riga.setQtaUmPrm(BigDecimal.ZERO);
			}

			Iterator iterVociRows = riga.getRighe().iterator();
			while(iterVociRows.hasNext()) {
				PreventivoCommessaVoce voce = (PreventivoCommessaVoce) iterVociRows.next();
				voce.setOnDB(false);

				if(azzeraCosti) {
					voce.setMarkup(BigDecimal.ZERO);
					voce.setPrezzo(BigDecimal.ZERO);
					voce.setPrezzoExtra(BigDecimal.ZERO);
					voce.setCostoRifer(BigDecimal.ZERO);
					voce.setCosTotale(BigDecimal.ZERO);
					voce.setQtaPrvUmPrm(BigDecimal.ZERO);
					voce.setQtaPrvUmSec(BigDecimal.ZERO);
					voce.setQtaPrvUmVen(BigDecimal.ZERO);
				}
			}
		}

		preventivo.setCliente(((YOffertaCliente)getTestata()).getCliente());
		preventivo.setOffertaCliente(((YOffertaCliente)getTestata()));
		preventivo.completaBO();
		preventivo.setCommessa(getCommessa());
		return preventivo;
	}

	public static ConnectionDescriptor getConnectionDescriptorPreventivatore() {
		Object[] info = SessionEnvironment.getDBInfoFromIniFile();
		SQLServerJTDSNoUnicodeDatabase infoDB = (SQLServerJTDSNoUnicodeDatabase) info[3];
		Database db = new SQLServerJTDSNoUnicodeDatabase(infoDB.getHost(), infoDB.getPort());
		ConnectionDescriptor connDescr = new ConnectionDescriptor("PTH_FM", "sa", "sa2014", db);
		return connDescr;
	}

	public PrevTes recuperaPreventivoDaPreventivatore() {
		PrevTes prevTes = null;
		PreparedStatement ps = null;
		PrevTesRsIterator prevTesIterator = null;
		ConnectionDescriptor connDescr = getConnectionDescriptorPreventivatore();
		try {
			connDescr.openConnection();

			String query = "SELECT * FROM FM_PREV.PREV_TES WHERE ID_AZIENDA_PR = ? AND ID_ANNO_PR = ? AND ID_NUMERO_PR = ? AND ID_RIGA_PR = ? ";
			ps = connDescr.getConnection().prepareStatement(query);
			ps.setString(1, Azienda.getAziendaCorrente());
			ps.setString(2, getAnnoDocumento());
			ps.setString(3, getNumeroDocumento());
			ps.setString(4, getNumeroRigaDocumento().toString());

			ResultSet rs = ps.executeQuery();
			prevTesIterator = new PrevTesRsIterator(rs);

			return prevTesIterator.hasNext() ? (PrevTes) prevTesIterator.next() : null;

		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(prevTesIterator != null) {
					prevTesIterator.closeCursor();
				}
				connDescr.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}

		return prevTes;
	}


	@SuppressWarnings("rawtypes")
	public static PreventivoCommessaTestata preventivoDaCommessa(String idCommessa) {
		PreventivoCommessaTestata preventivo = null;

		String where = " "+PreventivoCommessaTestataTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"' AND "+PreventivoCommessaTestataTM.R_COMMESSA+" = '"+idCommessa+"' ";

		try {
			Vector list = PreventivoCommessaTestata.retrieveList(PreventivoCommessaTestata.class, where, "", false);
			if(list.size() > 0) {
				preventivo = (PreventivoCommessaTestata) list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return preventivo;
	}

}
