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

import it.monchieri.thip.dtsx.YDtsxPtExpOrdEsecRunner;
import it.monchieri.thip.dtsx.YTimestampElaborazDtsx;
import it.thera.thip.base.azienda.Azienda;

public class YDtsxPtExpOrdEsecRunnerFormModifier extends WebFormModifier {

	@Override
	public void writeHeadElements(JspWriter out) throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		java.sql.Date dataPartenzaDefault = new java.sql.Date(calendar.getTimeInMillis());
		Timestamp timestampDefault = TimeUtils.getTimestamp(dataPartenzaDefault);
		YDtsxPtExpOrdEsecRunner bo = (YDtsxPtExpOrdEsecRunner) getBODataCollector().getBo();
		try {
			YTimestampElaborazDtsx elabAnalisiQcAcciaieria = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
			elabAnalisiQcAcciaieria.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"YPT_EXP_ORD_ESEC"}));
			boolean onDB = elabAnalisiQcAcciaieria.retrieve();
			if(onDB) {
				bo.setDataEstrazioneOrdEsec(TimeUtils.getDate(elabAnalisiQcAcciaieria.getDatiComuni().getTimestampAgg()));
			}else {
				bo.setDataEstrazioneOrdEsec(TimeUtils.getDate(timestampDefault));
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
