package it.monchieri.thip.dtsx.web;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.web.WebFormModifier;

import it.monchieri.thip.dtsx.YDtsxRmOdaDDTRunner;
import it.monchieri.thip.dtsx.YTimestampElaborazDtsx;
import it.thera.thip.base.azienda.Azienda;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 11/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    11/12/2024</b>
 * <p></p>
 */

public class YDtsxRmOdaDDTRunnerFormModifier extends WebFormModifier {

	@Override
	public void writeHeadElements(JspWriter out) throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		java.sql.Date dataPartenzaDefault = new java.sql.Date(calendar.getTimeInMillis());
		Timestamp timestampDefault = TimeUtils.getTimestamp(dataPartenzaDefault);
		YDtsxRmOdaDDTRunner bo = (YDtsxRmOdaDDTRunner) getBODataCollector().getBo();
		try {
			YTimestampElaborazDtsx elabAnalisiQcAcciaieria = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
			elabAnalisiQcAcciaieria.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"QC_ANALISI_ACCIAIERIA"}));
			boolean onDB = elabAnalisiQcAcciaieria.retrieve();
			if(onDB) {
				bo.setDataEstrazioneAnalisiAcc(TimeUtils.getDate(elabAnalisiQcAcciaieria.getDatiComuni().getTimestampAgg()));
			}else {
				bo.setDataEstrazioneAnalisiAcc(TimeUtils.getDate(timestampDefault));
			}
			YTimestampElaborazDtsx elabAnalisiQcRm = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
			elabAnalisiQcRm.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"QC_ANALISI_RM"}));
			onDB = elabAnalisiQcRm.retrieve();
			if(onDB) {
				bo.setDataEstrazioneAnalisiRm(TimeUtils.getDate(elabAnalisiQcRm.getDatiComuni().getTimestampAgg()));
			}else {
				bo.setDataEstrazioneAnalisiRm(TimeUtils.getDate(timestampDefault));
			}
			YTimestampElaborazDtsx elabAnalisiOrdFOR = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
			elabAnalisiOrdFOR.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"YPT_ORD_FOR"}));
			onDB = elabAnalisiOrdFOR.retrieve();
			if(onDB) {
				bo.setDataEstrazioneOrdFor(TimeUtils.getDate(elabAnalisiOrdFOR.getDatiComuni().getTimestampAgg()));
			}else {
				bo.setDataEstrazioneOrdFor(TimeUtils.getDate(timestampDefault));
			}
			YTimestampElaborazDtsx elabAnalisiDdtFOR = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
			elabAnalisiDdtFOR.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"YPT_DDT_FOR"}));
			onDB = elabAnalisiDdtFOR.retrieve();
			if(onDB) {
				bo.setDataEstrazioneDdtFor(TimeUtils.getDate(elabAnalisiDdtFOR.getDatiComuni().getTimestampAgg()));
			}else {
				bo.setDataEstrazioneDdtFor(TimeUtils.getDate(timestampDefault));
			}
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}

		getBODataCollector().setOnBORecursive();
	}

	@Override
	public void writeBodyStartElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeFormStartElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeFormEndElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeBodyEndElements(JspWriter out) throws IOException {

	}

}
