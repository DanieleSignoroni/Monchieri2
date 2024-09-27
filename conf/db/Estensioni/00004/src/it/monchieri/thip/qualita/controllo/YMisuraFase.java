/*
 * @(#)YMisuraFase.java
 */

/**
 * null
 *
 * <br></br><b>Copyright (C) : Thera SpA</b>
 * @author Wizard 27/09/2024 at 16:10:13
 */
/*
 * Revisions:
 * Date          Owner      Description
 * 27/09/2024    Wizard     Codice generato da Wizard
 *
 */
package it.monchieri.thip.qualita.controllo;
import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import it.thera.thip.qualita.controllo.*;
import com.thera.thermfw.common.*;
import it.thera.thip.base.azienda.Azienda;

public class YMisuraFase extends MisuraFase {

  
  /**
   * Attributo iIdCodificaFaseMame
   */
  protected Short iIdCodificaFaseMame;

  
  /**
   * YMisuraFase
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 27/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public YMisuraFase() {
    setIdAzienda(Azienda.getAziendaCorrente());
  }

  /**
   * Valorizza l'attributo. 
   * @param idCodificaFaseMame
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 27/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public void setIdCodificaFaseMame(Short idCodificaFaseMame) {
    this.iIdCodificaFaseMame = idCodificaFaseMame;
    setDirty();
  }

  /**
   * Restituisce l'attributo. 
   * @return Short
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 27/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public Short getIdCodificaFaseMame() {
    return iIdCodificaFaseMame;
  }

  /**
   * setEqual
   * @param obj
   * @throws CopyException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 27/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public void setEqual(Copyable obj) throws CopyException {
    super.setEqual(obj);
  }

}

