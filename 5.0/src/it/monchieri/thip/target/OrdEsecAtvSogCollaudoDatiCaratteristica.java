package it.monchieri.thip.target;

import java.math.BigDecimal;

public class OrdEsecAtvSogCollaudoDatiCaratteristica {

		protected Integer idSequenzaFase;
		protected Integer idSequenzaCar;
		protected String descrizione;
		protected String descrizioneRid;
		protected String idUnitaMisura;
		protected BigDecimal valoreNominale;
		protected boolean rilevaLimiteSuperiore;
		protected boolean rilevaLimiteInferiore;
		protected BigDecimal percentualeControllare;
		
		protected Integer limiteSuperioreToll;
		protected Integer limiteInferioreToll;
		
		public OrdEsecAtvSogCollaudoDatiCaratteristica() {
			
		}
		
		public Integer getLimiteSuperioreToll() {
			return limiteSuperioreToll;
		}

		public void setLimiteSuperioreToll(Integer limiteSuperioreToll) {
			this.limiteSuperioreToll = limiteSuperioreToll;
		}

		public Integer getLimiteInferioreToll() {
			return limiteInferioreToll;
		}



		public void setLimiteInferioreToll(Integer limiteInferioreToll) {
			this.limiteInferioreToll = limiteInferioreToll;
		}



		public Integer getIdSequenzaFase() {
			return idSequenzaFase;
		}
		public void setIdSequenzaFase(Integer idSequenzaFase) {
			this.idSequenzaFase = idSequenzaFase;
		}
		public Integer getIdSequenzaCar() {
			return idSequenzaCar;
		}
		public void setIdSequenzaCar(Integer idSequenzaCar) {
			this.idSequenzaCar = idSequenzaCar;
		}
		public String getDescrizione() {
			return descrizione;
		}
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		public String getDescrizioneRid() {
			return descrizioneRid;
		}
		public void setDescrizioneRid(String descrizioneRid) {
			this.descrizioneRid = descrizioneRid;
		}
		public String getIdUnitaMisura() {
			return idUnitaMisura;
		}
		public void setIdUnitaMisura(String idUnitaMisura) {
			this.idUnitaMisura = idUnitaMisura;
		}
		public BigDecimal getValoreNominale() {
			return valoreNominale;
		}
		public void setValoreNominale(BigDecimal valoreNominale) {
			this.valoreNominale = valoreNominale;
		}
		public boolean isRilevaLimiteSuperiore() {
			return rilevaLimiteSuperiore;
		}
		public void setRilevaLimiteSuperiore(boolean rilevaLimiteSuperiore) {
			this.rilevaLimiteSuperiore = rilevaLimiteSuperiore;
		}
		public boolean isRilevaLimiteInferiore() {
			return rilevaLimiteInferiore;
		}
		public void setRilevaLimiteInferiore(boolean rilevaLimiteInferiore) {
			this.rilevaLimiteInferiore = rilevaLimiteInferiore;
		}
		public BigDecimal getPercentualeControllare() {
			return percentualeControllare;
		}
		public void setPercentualeControllare(BigDecimal percentualeControllare) {
			this.percentualeControllare = percentualeControllare;
		}

	}