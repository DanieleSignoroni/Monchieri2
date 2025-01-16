package it.monchieri.thip.target;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.thera.thermfw.persist.Factory;

import it.thera.thip.cs.ResultSetIterator;

public class OrdEsecAtvSogCollaudoDatiCaratteristicaRsIterator extends ResultSetIterator {

	public OrdEsecAtvSogCollaudoDatiCaratteristicaRsIterator(ResultSet rs) {
		super(rs);
	}

	@Override
	protected Object createObject() throws SQLException {
		OrdEsecAtvSogCollaudoDatiCaratteristica carat = (OrdEsecAtvSogCollaudoDatiCaratteristica) Factory.createObject(OrdEsecAtvSogCollaudoDatiCaratteristica.class);
		carat.setIdSequenzaFase(cursor.getInt("ID_SEQUENZA_FASE"));
		carat.setIdSequenzaCar(cursor.getInt("ID_SEQUENZA_CAR"));
		carat.setDescrizione(cursor.getString("DESCRIZIONE"));
		carat.setDescrizioneRid(cursor.getString("DESCR_RIDOTTA"));
		String idUnitaMisura = cursor.getString("R_UNITA_MISURA") != null ? cursor.getString("R_UNITA_MISURA").trim() : null;
		if(idUnitaMisura != null && idUnitaMisura.equals("?C")) {
			idUnitaMisura = "°C";
		}else if(idUnitaMisura != null && idUnitaMisura.equals("H")) {
			idUnitaMisura = "h";
		}else if(idUnitaMisura != null && idUnitaMisura.equals("KG")) {
			idUnitaMisura = "kg";
		}else if(idUnitaMisura != null && idUnitaMisura.equals("NN")) {
			idUnitaMisura = "nr";
		}else if(idUnitaMisura != null && idUnitaMisura.equals("MM")) {
			idUnitaMisura = "mm";
		}
		carat.setIdUnitaMisura(idUnitaMisura);
		carat.setValoreNominale(cursor.getBigDecimal("VALORE_NOMINALE"));
		carat.setRilevaLimiteInferiore(cursor.getString("RIL_LIM_INF_TOL").equals("Y") ? true : false);
		carat.setRilevaLimiteSuperiore(cursor.getString("RIL_LIM_SUP_TOL").equals("Y") ? true : false);
		carat.setPercentualeControllare(cursor.getBigDecimal("PERC_CTR"));
		
		carat.setLimiteInferioreToll(cursor.getInt("LIM_INF_TOL"));
		carat.setLimiteSuperioreToll(cursor.getInt("LIM_SUP_TOL"));
		return carat;
	}

}
